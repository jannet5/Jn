import {
  WebGLRenderer, Scene, PerspectiveCamera, Group, Mesh, BufferGeometry,
  Float32BufferAttribute, ShaderMaterial, CanvasTexture, SRGBColorSpace,
  LinearMipmapLinearFilter, LinearFilter, DoubleSide, Raycaster, Vector2, Color,
} from 'three';
import { CONFIG } from './config.js';
import { drawArtwork } from './artwork.js';

const vertexShader = /* glsl */ `
  varying vec2 vUv;
  varying float vDepth;
  void main() {
    vUv = uv;
    vec4 mv = modelViewMatrix * vec4(position, 1.0);
    vDepth = -mv.z;
    gl_Position = projectionMatrix * mv;
  }
`;

const fragmentShader = /* glsl */ `
  uniform sampler2D map;
  uniform float uHover;
  uniform float uNear;
  uniform float uFar;
  uniform vec3 uFog;
  varying vec2 vUv;
  varying float vDepth;
  void main() {
    vec2 uv = vUv;
    if (!gl_FrontFacing) uv.x = 1.0 - uv.x;
    vec4 tex = texture2D(map, uv);
    vec3 col = tex.rgb;
    // arka yüz: içeriden görünen karolar koyulaşır
    if (!gl_FrontFacing) col *= 0.28;
    // kenar vinyeti
    vec2 e = smoothstep(vec2(0.0), vec2(0.06), uv) * smoothstep(vec2(0.0), vec2(0.06), 1.0 - uv);
    col *= mix(0.55, 1.0, e.x * e.y);
    // hover: hafif aydınlanma
    col = mix(col, col * 1.18 + 0.03, uHover);
    // derinlik sisi
    float f = smoothstep(uNear, uFar, vDepth);
    col = mix(col, uFog, f * 0.85);
    gl_FragColor = vec4(col, 1.0);
    #include <colorspace_fragment>
  }
`;

function curvedTile(radius, arc, height, segments) {
  const pos = [];
  const uv = [];
  const idx = [];
  for (let i = 0; i <= segments; i++) {
    const t = i / segments;
    const th = -arc / 2 + arc * t;
    const x = Math.sin(th) * radius;
    const z = Math.cos(th) * radius;
    pos.push(x, height / 2, z, x, -height / 2, z);
    uv.push(t, 1, t, 0);
  }
  for (let i = 0; i < segments; i++) {
    const a = i * 2;
    idx.push(a, a + 1, a + 2, a + 1, a + 3, a + 2);
  }
  const g = new BufferGeometry();
  g.setAttribute('position', new Float32BufferAttribute(pos, 3));
  g.setAttribute('uv', new Float32BufferAttribute(uv, 2));
  g.setIndex(idx);
  g.computeBoundingSphere();
  return g;
}

/**
 * Spiral galeriyi kurar. WebGL yoksa hata fırlatır; çağıran yedek görünüme geçer.
 * @param {HTMLCanvasElement} canvas
 * @param {HTMLElement} host  boyutu alınan kapsayıcı
 */
export function createGallery(canvas, host) {
  const renderer = new WebGLRenderer({ canvas, antialias: true, alpha: false, powerPreference: 'high-performance' });
  renderer.outputColorSpace = SRGBColorSpace;
  const fogColor = new Color('#0d0c0b');
  renderer.setClearColor(fogColor, 1);

  const scene = new Scene();
  const camera = new PerspectiveCamera(38, 1, 0.1, 100);
  camera.position.set(0, 0, CONFIG.cameraZ);

  const spiral = new Group();
  scene.add(spiral);

  const maxAniso = renderer.capabilities.getMaxAnisotropy();
  const textures = Array.from({ length: CONFIG.totalImages }, (_, i) => {
    const tex = new CanvasTexture(drawArtwork(i, { width: 512, height: 640 }));
    tex.colorSpace = SRGBColorSpace;
    tex.anisotropy = Math.min(8, maxAniso);
    tex.minFilter = LinearMipmapLinearFilter;
    tex.magFilter = LinearFilter;
    return tex;
  });

  const total = CONFIG.tilesPerRevolution * CONFIG.revolutions;
  const step = (Math.PI * 2) / CONFIG.tilesPerRevolution;
  const arc = step - CONFIG.tileGap;
  const tileH = 2 * CONFIG.startRadius * Math.sin(arc / 2) * CONFIG.tileHeightRatio;
  const spiralHeight = (total - 1) * CONFIG.spiralGap;
  const tiles = [];

  for (let i = 0; i < total; i++) {
    const p = i / (total - 1);
    const radius = CONFIG.startRadius + (CONFIG.endRadius - CONFIG.startRadius) * p;
    const material = new ShaderMaterial({
      vertexShader,
      fragmentShader,
      side: DoubleSide,
      uniforms: {
        map: { value: textures[i % textures.length] },
        uHover: { value: 0 },
        uNear: { value: CONFIG.cameraZ - 2 },
        uFar: { value: CONFIG.cameraZ + CONFIG.startRadius + 3 },
        uFog: { value: new Color('#0d0c0b').convertSRGBToLinear() },
      },
    });
    const mesh = new Mesh(curvedTile(radius, arc, tileH, CONFIG.tileSegments), material);
    mesh.rotation.y = i * step;
    mesh.position.y = spiralHeight / 2 - i * CONFIG.spiralGap;
    mesh.userData = { index: i % textures.length, hover: 0 };
    spiral.add(mesh);
    tiles.push(mesh);
  }

  const raycaster = new Raycaster();
  const ndc = new Vector2(10, 10);
  const state = {
    spin: 0,            // birikmiş dönüş (radyan)
    spinVelocity: 0,
    mouseX: 0, mouseY: 0,
    rotCurrent: 0, tiltX: 0, tiltZ: 0,
    progress: 0,        // 0..1 kahraman alanında ilerleme
    camY: spiralHeight * 0.3,
    hovered: -1,
    pointerInside: false,
    frames: 0,
    reducedMotion: false,
  };

  function resize() {
    const w = host.clientWidth;
    const h = host.clientHeight;
    renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, CONFIG.maxPixelRatio));
    renderer.setSize(w, h, false);
    camera.aspect = w / h;
    // Dar ekranlarda spiral kadraja sığsın diye kamera geri çekilir.
    camera.position.z = CONFIG.cameraZ * (camera.aspect < 0.8 ? 1.45 : camera.aspect < 1.2 ? 1.15 : 1);
    camera.updateProjectionMatrix();
  }
  resize();

  function setPointer(clientX, clientY) {
    const rect = canvas.getBoundingClientRect();
    const x = (clientX - rect.left) / rect.width;
    const y = (clientY - rect.top) / rect.height;
    state.pointerInside = x >= 0 && x <= 1 && y >= 0 && y <= 1;
    state.mouseX = Math.max(-1, Math.min(1, x * 2 - 1));
    state.mouseY = Math.max(-1, Math.min(1, y * 2 - 1));
    ndc.set(state.mouseX, -state.mouseY);
  }

  function clearPointer() {
    state.pointerInside = false;
    ndc.set(10, 10);
  }

  /** Kaydırma hızı (px/kare) — ne kadar hızlı kaydırılırsa o kadar hızlı döner. */
  function addScrollVelocity(v) {
    state.spinVelocity += v * CONFIG.scrollSpin;
    state.spinVelocity = Math.max(-CONFIG.maxSpin, Math.min(CONFIG.maxSpin, state.spinVelocity));
  }

  function setProgress(p) {
    state.progress = Math.max(0, Math.min(1, p));
  }

  // Kare hızından bağımsız yumuşatma: 60 fps'te verilen katsayı, diğer hızlarda eşdeğeri.
  const ease = (base, f) => 1 - Math.pow(1 - base, f);

  /** @param {number} dtMs önceki kareden bu yana geçen süre */
  function update(dtMs = 16.67) {
    const f = Math.min(dtMs, 100) / 16.67; // 60 fps = 1
    const k = state.reducedMotion ? 0.25 : 1;
    state.spin += (CONFIG.baseRotationSpeed * k + state.spinVelocity) * f;
    state.spinVelocity *= Math.pow(CONFIG.spinDecay, f);
    if (Math.abs(state.spinVelocity) < 1e-5) state.spinVelocity = 0;

    const s = ease(CONFIG.mouseSmoothing, f);
    state.rotCurrent += (state.mouseX * CONFIG.mouseRotate - state.rotCurrent) * s;
    state.tiltX += (state.mouseY * CONFIG.mouseTilt - state.tiltX) * s;
    state.tiltZ += (-state.mouseX * CONFIG.mouseTilt * 0.4 - state.tiltZ) * s;

    spiral.rotation.set(state.tiltX, state.spin + state.rotCurrent, state.tiltZ);

    const targetY = spiralHeight * 0.3 - state.progress * spiralHeight * CONFIG.cameraTravel;
    state.camY += (targetY - state.camY) * ease(CONFIG.cameraSmoothing, f);
    camera.position.y = state.camY;
    camera.lookAt(0, state.camY - 0.4, 0);

    // hover: imlecin altındaki en yakın karo
    let hit = -1;
    if (state.pointerInside) {
      raycaster.setFromCamera(ndc, camera);
      const hits = raycaster.intersectObjects(tiles, false);
      if (hits.length) hit = tiles.indexOf(hits[0].object);
    }
    state.hovered = hit;
    for (let i = 0; i < tiles.length; i++) {
      const m = tiles[i];
      const target = i === hit ? 1 : 0;
      m.userData.hover += (target - m.userData.hover) * ease(CONFIG.hoverSmoothing, f);
      const sc = 1 - CONFIG.hoverPush * m.userData.hover;
      m.scale.set(sc, 1 - 0.04 * m.userData.hover, sc);
      m.material.uniforms.uHover.value = m.userData.hover;
    }

    renderer.render(scene, camera);
    state.frames++;
  }

  function hoveredWork() {
    return state.hovered >= 0 ? tiles[state.hovered].userData.index : -1;
  }

  function dispose() {
    tiles.forEach((m) => { m.geometry.dispose(); m.material.dispose(); });
    textures.forEach((t) => t.dispose());
    renderer.dispose();
  }

  return { state, resize, setPointer, clearPointer, addScrollVelocity, setProgress, update, hoveredWork, dispose, tileCount: total };
}
