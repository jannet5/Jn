/* ViralForge tarayıcı yakalama — kullanıcının kendi oturumunda, kendi tıklamasıyla çalışır.
 * Açık olan Instagram gönderi sayfasından: https URL, kapak/fotoğraf, açıklama, beğeni/yorum, ses etiketi.
 * İki dosya indirir: vf-capture-<kod>.json ve vf-capture-<kod>.jpg  →  python -m viralforge import-meta <İndirilenler>
 * Yer imi (bookmarklet) sürümü için bookmarklet.txt dosyasına bakın.
 */
(async () => {
  const m = location.pathname.match(/\/(p|reel|reels|tv)\/([A-Za-z0-9_-]{5,64})/);
  if (!m) { alert("ViralForge: bu bir Instagram gönderi sayfası değil."); return; }
  const kind = m[1] === "p" ? "p" : "reel";
  const code = m[2];
  const meta = (p) => (document.querySelector(`meta[property="${p}"]`) || {}).content || null;
  const audioLinks = [...document.querySelectorAll('a[href*="/reels/audio/"], a[href*="/audio/"]')];
  const audioText = audioLinks.map((a) => a.innerText.trim()).filter(Boolean)[0] || null;
  let audio = { status: "unknown" };
  if (audioText) {
    const parts = audioText.split("·").map((s) => s.trim());
    audio = {
      status: "known",
      artist: parts.length > 1 ? parts[0] : null,
      title: parts.length > 1 ? parts.slice(1).join(" · ") : parts[0],
      audio_url: audioLinks[0].href,
      original: /original audio|orijinal ses/i.test(audioText),
    };
  } else if (kind === "p") {
    audio = { status: "none" }; // fotoğraf gönderisinde müzik etiketi görünmüyor
  }
  const record = {
    url: `https://www.instagram.com/${kind}/${code}/`,
    shortcode: code,
    og_description: meta("og:description"),
    og_image: meta("og:image"),
    audio,
    captured_at: new Date().toISOString(),
    capture_tool: "viralforge-capture.js",
  };
  const save = (blob, name) => {
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob); a.download = name; document.body.appendChild(a); a.click(); a.remove();
  };
  save(new Blob([JSON.stringify(record, null, 2)], { type: "application/json" }), `vf-capture-${code}.json`);
  if (record.og_image) {
    try {
      const r = await fetch(record.og_image);
      save(await r.blob(), `vf-capture-${code}.jpg`);
    } catch (e) {
      window.open(record.og_image, "_blank"); // CORS engellerse: açılan görseli vf-capture-<kod>.jpg olarak kaydedin
    }
  }
})();
