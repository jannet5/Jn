const $ = (sel) => document.querySelector(sel);

async function api(method, path, body) {
  const res = await fetch(path, {
    method,
    headers: body ? { "Content-Type": "application/json" } : undefined,
    body: body ? JSON.stringify(body) : undefined,
  });
  if (!res.ok) throw new Error(`${method} ${path} -> ${res.status}`);
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

function fmtBytes(n) {
  if (n === null || n === undefined) return "-";
  const units = ["B", "KB", "MB", "GB", "TB"];
  let v = Math.abs(n), i = 0;
  while (v >= 1024 && i < units.length - 1) { v /= 1024; i++; }
  return `${n < 0 ? "-" : ""}${v.toFixed(1)} ${units[i]}`;
}

// ---- Pairing ---------------------------------------------------------

let pairPollTimer = null;

$("#begin-pair-btn").addEventListener("click", async () => {
  const r = await api("POST", "/admin/pair/begin");
  $("#pair-result").hidden = false;
  $("#pair-qr").src = `data:image/png;base64,${r.qrPngBase64}`;
  $("#pair-pin").textContent = r.pin;
  $("#pair-expiry").textContent = r.expiresInSeconds;
  if (pairPollTimer) clearInterval(pairPollTimer);
  pairPollTimer = setInterval(loadPending, 2000);
});

async function loadPending() {
  const pending = await api("GET", "/admin/pair/pending");
  const section = $("#pending-section");
  const list = $("#pending-list");
  section.hidden = pending.length === 0;
  list.innerHTML = pending.length
    ? pending.map(p => `
        <div class="row">
          <div>
            <div>${escapeHtml(p.deviceName)}</div>
            <div class="meta">Kod: ${p.pairingId.slice(0, 8)}…</div>
          </div>
          <div>
            <button data-approve="${p.pairingId}">Onayla</button>
            <button class="danger" data-reject="${p.pairingId}">Reddet</button>
          </div>
        </div>`).join("")
    : "";

  list.querySelectorAll("[data-approve]").forEach(btn =>
    btn.addEventListener("click", async () => { await api("POST", `/admin/pair/approve/${btn.dataset.approve}`); loadAll(); }));
  list.querySelectorAll("[data-reject]").forEach(btn =>
    btn.addEventListener("click", async () => { await api("POST", `/admin/pair/reject/${btn.dataset.reject}`); loadAll(); }));
}

// ---- Devices ---------------------------------------------------------

async function loadDevices() {
  const devices = await api("GET", "/admin/devices");
  $("#devices-list").innerHTML = devices.length
    ? devices.map(d => `
        <div class="row">
          <div>
            <div>${escapeHtml(d.name)}</div>
            <div class="meta">Eşleşme: ${new Date(d.createdAt).toLocaleString("tr-TR")}</div>
          </div>
          <div>
            <span class="badge ${d.revoked ? "danger" : "ok"}">${d.revoked ? "İptal edildi" : "Aktif"}</span>
            ${!d.revoked ? `<button class="danger" data-revoke="${d.id}">Eşleşmeyi kaldır</button>` : ""}
          </div>
        </div>`).join("")
    : `<div class="empty">Henüz eşleşmiş cihaz yok.</div>`;

  $("#devices-list").querySelectorAll("[data-revoke]").forEach(btn =>
    btn.addEventListener("click", async () => { await api("POST", `/admin/devices/${btn.dataset.revoke}/revoke`); loadDevices(); }));
}

// ---- Allowlist ---------------------------------------------------------

async function loadAllowlist() {
  const apps = await api("GET", "/admin/allowlist");
  $("#allowlist-list").innerHTML = apps.length
    ? apps.map(a => `
        <div class="row">
          <div>
            <div>${escapeHtml(a.displayName)}</div>
            <div class="meta">${escapeHtml(a.path)}</div>
          </div>
          <button class="danger" data-remove="${a.id}">Kaldır</button>
        </div>`).join("")
    : `<div class="empty">Liste boş — telefon henüz hiçbir uygulama başlatamaz.</div>`;

  $("#allowlist-list").querySelectorAll("[data-remove]").forEach(btn =>
    btn.addEventListener("click", async () => { await api("DELETE", `/admin/allowlist/${btn.dataset.remove}`); loadAllowlist(); }));
}

$("#allowlist-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  await api("POST", "/admin/allowlist", {
    displayName: $("#allowlist-name").value.trim(),
    path: $("#allowlist-path").value.trim(),
  });
  $("#allowlist-name").value = "";
  $("#allowlist-path").value = "";
  loadAllowlist();
});

// ---- Watched roots ---------------------------------------------------------

async function loadRoots() {
  const roots = await api("GET", "/admin/watched-roots");
  $("#roots-list").innerHTML = roots.length
    ? roots.map(r => `
        <div class="row">
          <div>${escapeHtml(r.path)}</div>
          <label><input type="checkbox" data-toggle="${escapeHtml(r.path)}" ${r.enabled ? "checked" : ""} /> İzleniyor</label>
        </div>`).join("")
    : `<div class="empty">İzlenecek sürücü bulunamadı.</div>`;

  $("#roots-list").querySelectorAll("[data-toggle]").forEach(cb =>
    cb.addEventListener("change", async () => {
      await api("POST", "/admin/watched-roots/toggle", { path: cb.dataset.toggle, enabled: cb.checked });
    }));
}

// ---- Audit ---------------------------------------------------------

async function loadAudit() {
  const entries = await api("GET", "/admin/audit");
  $("#audit-list").innerHTML = entries.length
    ? entries.slice(0, 30).map(e => `
        <div class="row">
          <div>
            <div>${escapeHtml(e.action)} ${e.target ? `— ${escapeHtml(e.target)}` : ""}</div>
            <div class="meta">${escapeHtml(e.deviceName)} · ${new Date(e.occurredAt).toLocaleString("tr-TR")}</div>
          </div>
          <span class="badge ${e.success ? "ok" : "danger"}">${e.success ? "Başarılı" : escapeHtml(e.reason || "Başarısız")}</span>
        </div>`).join("")
    : `<div class="empty">Henüz bir işlem yapılmadı.</div>`;
}

function escapeHtml(s) {
  return String(s ?? "").replace(/[&<>"']/g, c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
}

function loadAll() {
  loadPending();
  loadDevices();
  loadAllowlist();
  loadRoots();
  loadAudit();
}

loadAll();
setInterval(loadAll, 5000);
