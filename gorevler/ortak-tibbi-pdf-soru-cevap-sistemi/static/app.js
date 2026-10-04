"use strict";
const T = {
  tr: {ansLang:"Cevap dili",nearest:"En yakın bölümler (cevabı içermiyor)",trVerified:"Yerel çeviri — sayı, birim, olumsuzluk ve karşılaştırma denetlendi",showOrig:"Orijinal (kaynak dil)",noAnswerSrc:"Bu sorunun cevabı ortak kütüphanedeki kaynaklarda bulunamadı. Uydurma cevap vermiyoruz.",missingTerms:"Kaynakta bulunmayan terimler",unverifiedHead:"Çevirisi doğrulanamayan cümle (cevaba eklenmedi) — orijinali:",trUnverifiedAll:"Kaynak bulundu ama çeviri doğrulanamadı; aşağıda orijinal metni görüyorsun.",trError:"Yerel çeviri şu an kullanılamıyor; kaynak dildeki alıntı gösteriliyor.",trUnsupported:"Bu dil çifti için yerel çeviri yok; kaynak dildeki alıntı gösteriliyor.",tabAsk:"Sor",tabLib:"Kütüphane",askLabel:"Sorunu istediğin dilde yaz",allDocs:"Tüm ortak kütüphane",useAi:"Yapay zekâ ile kendi dilimde özetle",newThread:"Yeni konu",
    disclaimer:"Eğitim amaçlıdır; klinik karar veya tıbbi tavsiye değildir. Her yanıtı kaynak sayfasından doğrulayın.",upTitle:"PDF paylaş — kütüphaneye katkı ücretsiz",
    titlePh:"Başlık (isteğe bağlı)",subjectPh:"Ders / konu (ör. Kardiyoloji)",rights:"Bu PDF'i paylaşma hakkım var (kendi notum, açık lisans ya da izinli).",upload:"Yükle",
    searchPh:"Kütüphanede ara",quotaTitle:"Kullanım hakkın",nextCost:"Sıradaki soru",bonus:"Destek kredisi",grace:"İş bitirme payı",reset:"Yenilenme",howTitle:"Bu kota nasıl çalışır?",
    how:"Herkese her ay eşit ücretsiz hak verilir; kart veya giriş istenmez. Hakkın bitmek üzereyken önceden uyarırız. Başladığın bir konu hakkın bitse bile \"iş bitirme payı\" ile tamamlanır — yarıda kesilmezsin. Çok kullanırsan gönüllü destekle ek kredi alabilirsin.",
    donate:"Projeye destek ol",codePh:"Destek kodu",redeem:"Ekle",history:"Son hareketler",ask:"Sor",credit:"kredi",of:"/ {n} bu ay",
    lvl_ok:"Rahat kullanabilirsin.",lvl_warn:"Aylık hakkının %75'ini kullandın — planlı ilerle.",lvl_low:"Hakkın bitmek üzere. Başladığın konu yine de tamamlanır.",
    lvl_grace:"Aylık hakkın bitti. Açık konunu iş bitirme payıyla tamamlayabilirsin; yeni konu için yenilenmeyi bekle ya da destek kodu ekle.",lvl_blocked:"Bu ayın hakkı ve iş bitirme payı bitti. {d} tarihinde yenilenir; istersen destek kodu ekleyebilirsin.",
    thread:"Açık konu: {n} soru. Bu konu hakkın bitse bile yarıda kesilmez.",noAnswer:"Kütüphanede bu soruya yeterince yakın bir bölüm bulunamadı. Soruyu farklı ifade et ya da ilgili PDF'i paylaş.",
    srcLang:"Kaynak dili",qLang:"Soru dili",page:"s.",sources:"Kaynak bölümler",match:"eşleşme",lowConf:"düşük güven",excerpt:"Kaynaktan alıntı (orijinal dilde)",
    pages:"sayfa",report:"Bildir",reportAsk:"Neden? copyright / wrong / spam / other",reported:"Bildirildi, teşekkürler.",uploading:"Yükleniyor ve dizinleniyor…",
    uploaded:"Eklendi: {t} ({p} sayfa). Herkes artık bu PDF'ten soru sorabilir.",dup:"Bu PDF zaten kütüphanede: {t}",added:"+{n} destek kredisi eklendi.",empty:"Kütüphane boş — ilk PDF'i sen paylaş.",
    hk_ask:"soru",hk_ask_ai:"YZ soru",hk_upload:"yükleme",hk_free:"",hk_bonus:"destek",hk_grace:"pay",cost:"{n} kredi"},
  en: {ansLang:"Answer language",nearest:"Closest passages (do not contain the answer)",trVerified:"Local translation — numbers, units, negation and comparisons checked",showOrig:"Original (source language)",noAnswerSrc:"The shared library sources do not contain an answer to this question. We do not make one up.",missingTerms:"Terms not found in sources",unverifiedHead:"Sentence whose translation could not be verified (left out of the answer) — original:",trUnverifiedAll:"A source was found but its translation could not be verified; the original text is shown below.",trError:"Local translation is unavailable right now; showing the source-language excerpt.",trUnsupported:"No local translation for this language pair; showing the source-language excerpt.",tabAsk:"Ask",tabLib:"Library",askLabel:"Ask in any language",allDocs:"Whole shared library",useAi:"Summarise in my language with AI",newThread:"New topic",
    disclaimer:"For education only; not clinical decision support or medical advice. Verify every answer on its source page.",upTitle:"Share a PDF — contributing is free",
    titlePh:"Title (optional)",subjectPh:"Course / subject (e.g. Cardiology)",rights:"I have the right to share this PDF (my own notes, open licence or permission).",upload:"Upload",
    searchPh:"Search library",quotaTitle:"Your allowance",nextCost:"Next question",bonus:"Supporter credit",grace:"Finish-up buffer",reset:"Resets",howTitle:"How does this quota work?",
    how:"Everyone gets the same free monthly allowance; no card, no login. We warn you early. A topic you started is never cut off mid-way — the finish-up buffer lets you complete it. Heavy users can add credit by supporting the project.",
    donate:"Support the project",codePh:"Supporter code",redeem:"Add",history:"Recent activity",ask:"Ask",credit:"credit",of:"/ {n} this month",
    lvl_ok:"Plenty left.",lvl_warn:"You've used 75% of this month — plan ahead.",lvl_low:"Almost out. A topic you started will still finish.",
    lvl_grace:"Monthly allowance used. Finish your open topic with the buffer; for new topics wait for the reset or add a supporter code.",lvl_blocked:"This month's allowance and buffer are used. Resets on {d}; you can add a supporter code.",
    thread:"Open topic: {n} question(s). It won't be cut off even if your allowance runs out.",noAnswer:"No sufficiently close passage in the library. Rephrase, or share the relevant PDF.",
    srcLang:"Source language",qLang:"Question language",page:"p.",sources:"Source passages",match:"match",lowConf:"low confidence",excerpt:"Excerpt from source (original language)",
    pages:"pages",report:"Report",reportAsk:"Why? copyright / wrong / spam / other",reported:"Reported, thank you.",uploading:"Uploading and indexing…",
    uploaded:"Added: {t} ({p} pages). Everyone can now ask questions from it.",dup:"Already in the library: {t}",added:"+{n} supporter credits added.",empty:"Library is empty — share the first PDF.",
    hk_ask:"question",hk_ask_ai:"AI question",hk_upload:"upload",hk_free:"",hk_bonus:"supporter",hk_grace:"buffer",cost:"{n} credit(s)"},
  es: {ansLang:"Idioma de la respuesta",nearest:"Pasajes más cercanos (no contienen la respuesta)",trVerified:"Traducción local — números, unidades, negación y comparaciones verificados",showOrig:"Original (idioma de la fuente)",noAnswerSrc:"Las fuentes de la biblioteca compartida no contienen la respuesta a esta pregunta. No inventamos respuestas.",missingTerms:"Términos que no aparecen en la fuente",unverifiedHead:"Frase cuya traducción no se pudo verificar (no incluida en la respuesta) — original:",trUnverifiedAll:"Se encontró una fuente, pero su traducción no se pudo verificar; abajo ves el texto original.",trError:"La traducción local no está disponible ahora; se muestra el extracto en el idioma de la fuente.",trUnsupported:"No hay traducción local para este par de idiomas; se muestra el extracto en el idioma de la fuente.",tabAsk:"Preguntar",tabLib:"Biblioteca",askLabel:"Pregunta en cualquier idioma",allDocs:"Toda la biblioteca compartida",useAi:"Resumir en mi idioma con IA",newThread:"Nuevo tema",
    disclaimer:"Solo con fines educativos; no es consejo médico. Verifica cada respuesta en su página de origen.",upTitle:"Comparte un PDF — aportar es gratis",
    titlePh:"Título (opcional)",subjectPh:"Asignatura / tema (p. ej. Cardiología)",rights:"Tengo derecho a compartir este PDF (apuntes propios, licencia abierta o permiso).",upload:"Subir",
    searchPh:"Buscar en la biblioteca",quotaTitle:"Tu cuota",nextCost:"Próxima pregunta",bonus:"Crédito de apoyo",grace:"Margen para terminar",reset:"Se renueva",howTitle:"¿Cómo funciona la cuota?",
    how:"Todos reciben la misma cuota mensual gratuita; sin tarjeta ni registro. Avisamos con antelación. Un tema iniciado nunca se corta a mitad: el margen para terminar te deja completarlo. Si usas mucho, puedes apoyar el proyecto y añadir créditos.",
    donate:"Apoyar el proyecto",codePh:"Código de apoyo",redeem:"Añadir",history:"Actividad reciente",ask:"Preguntar",credit:"crédito",of:"/ {n} este mes",
    lvl_ok:"Te queda de sobra.",lvl_warn:"Has usado el 75 % del mes — planifica.",lvl_low:"Casi sin cuota. El tema iniciado se completará igualmente.",
    lvl_grace:"Cuota mensual agotada. Termina tu tema abierto con el margen; para temas nuevos espera la renovación o añade un código.",lvl_blocked:"Cuota y margen agotados. Se renueva el {d}; puedes añadir un código de apoyo.",
    thread:"Tema abierto: {n} pregunta(s). No se cortará aunque se agote tu cuota.",noAnswer:"No se encontró un pasaje suficientemente cercano. Reformula o comparte el PDF correspondiente.",
    srcLang:"Idioma de la fuente",qLang:"Idioma de la pregunta",page:"p.",sources:"Pasajes de origen",match:"coincidencia",lowConf:"confianza baja",excerpt:"Extracto de la fuente (idioma original)",
    pages:"páginas",report:"Reportar",reportAsk:"¿Motivo? copyright / wrong / spam / other",reported:"Reportado, gracias.",uploading:"Subiendo e indexando…",
    uploaded:"Añadido: {t} ({p} páginas). Ya todos pueden preguntar sobre él.",dup:"Ya está en la biblioteca: {t}",added:"+{n} créditos de apoyo añadidos.",empty:"Biblioteca vacía — comparte el primer PDF.",
    hk_ask:"pregunta",hk_ask_ai:"pregunta IA",hk_upload:"subida",hk_free:"",hk_bonus:"apoyo",hk_grace:"margen",cost:"{n} crédito(s)"},
};
const LN = {tr:"Türkçe",en:"English",es:"Español",de:"Deutsch",fr:"Français",pt:"Português",it:"Italiano",ar:"العربية",ru:"Русский",unknown:"?"};
const $ = (s) => document.querySelector(s);
let lang = (localStorage.getItem("lang") || navigator.language || "tr").slice(0, 2);
if (!T[lang]) lang = "en";
const t = (k, v = {}) => (T[lang][k] ?? T.en[k] ?? k).replace(/\{(\w+)\}/g, (_, x) => v[x] ?? "");
const esc = (s) => String(s ?? "").replace(/[&<>"']/g, (c) => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[c]));
let Q = null, threadId = null, threadCount = 0;

function toast(msg) { const el = $("#toast"); el.textContent = msg; el.classList.add("on"); clearTimeout(toast.h); toast.h = setTimeout(() => el.classList.remove("on"), 3500); }

async function api(path, opts = {}) {
  opts.headers = {...(opts.headers || {}), "X-Device-Id": localStorage.getItem("device") || ""};
  let r = await fetch(path, opts);
  if (r.status === 401 && !opts._retry) { await ensureDevice(true); return api(path, {...opts, _retry: true}); }
  const body = await r.json().catch(() => ({}));
  if (body.quota) renderQuota(body.quota);
  if (!r.ok) { const e = new Error(body.detail || body.error || r.statusText); e.status = r.status; e.body = body; throw e; }
  return body;
}

async function ensureDevice(force) {
  if (!force && localStorage.getItem("device")) return;
  const r = await fetch("/api/device", {method: "POST"});
  const b = await r.json();
  if (!r.ok) { toast(b.detail || "error"); return; }
  localStorage.setItem("device", b.device_id);
  renderQuota(b.quota);
}

function applyI18n() {
  document.documentElement.lang = lang; $("#lang").value = lang;
  document.querySelectorAll("[data-i18n]").forEach((el) => (el.textContent = t(el.dataset.i18n)));
  document.querySelectorAll("[data-i18n-ph]").forEach((el) => (el.placeholder = t(el.dataset.i18nPh)));
  if (Q) renderQuota(Q);
  renderThread();
}

function questionCost() { return Q ? ($("#useAi").checked ? Q.costs.question_ai : Q.costs.question) : 1; }

function renderQuota(q) {
  Q = q;
  const pct = q.free_total ? Math.round((q.free_left / q.free_total) * 100) : 0;
  $("#quota").dataset.level = q.level; $("#miniMeter").dataset.level = q.level;
  $("#qLeft").textContent = q.free_left + q.bonus_left;
  $("#qOf").textContent = t("of", {n: q.free_total + (q.bonus_left ? " + " + q.bonus_left : "")});
  $("#qBar").style.width = pct + "%"; $("#miniBar").style.width = pct + "%";
  $("#miniText").textContent = (q.free_left + q.bonus_left) + " " + t("credit");
  const d = new Date(q.reset_at).toLocaleDateString(lang);
  $("#qLevel").textContent = t("lvl_" + q.level, {d});
  $("#qCost").textContent = t("cost", {n: questionCost()});
  $("#qBonus").textContent = q.bonus_left;
  $("#qGrace").textContent = q.grace_left + " / " + q.grace_total;
  $("#qReset").textContent = d;
  $("#aiWrap").hidden = !q.ai_enabled;
  const don = $("#donate"); if (q.donate_url) { don.href = q.donate_url; don.hidden = false; } else don.hidden = true;
  $("#qHist").innerHTML = (q.history || []).map((h) => {
    const [k, s] = h.kind.split(":");
    return `<li><span>${esc(t("hk_" + k))} ${s !== "free" ? "· " + esc(t("hk_" + s)) : ""}</span><span>−${h.cost} · ${new Date(h.created_at).toLocaleTimeString(lang, {hour: "2-digit", minute: "2-digit"})}</span></li>`;
  }).join("");
  $("#askBtn").textContent = `${t("ask")} · ${t("cost", {n: questionCost()})}`;
}

function renderThread() { $("#threadHint").textContent = threadId ? t("thread", {n: threadCount}) : ""; }

function renderAnswer(question, res) {
  const a = res.answer, box = document.createElement("article");
  box.className = "ans" + (a.confident ? "" : " low") + (a.no_answer ? " none" : "");
  let body;
  if (a.mode === "translated" && a.no_answer) {
    body = `<p class="a">${esc(t("noAnswerSrc"))}</p>` + (a.missing_terms?.length ? `<p class="hint">${esc(t("missingTerms"))}: ${esc(a.missing_terms.join(", "))}…</p>` : "");
  } else if (a.mode === "translated") {
    body = (a.answer ? `<p class="hint">✓ ${esc(t("trVerified"))}</p>` + a.sentences.map((x) => `<p class="a tr-s">${esc(x.text)} <sup class="cite">[${esc(x.title)}, ${t("page")}${x.page}]</sup></p><details class="orig"><summary>${esc(t("showOrig"))}</summary><p>${esc(x.source_text)}</p></details>`).join("") : `<p class="a">${esc(t("trUnverifiedAll"))}</p>`)
      + (a.unverified?.length ? `<div class="unv"><p class="hint">⚠ ${esc(t("unverifiedHead"))}</p>${a.unverified.map((x) => `<p class="orig-p">${esc(x.source_text)} <sup class="cite">[${t("page")}${x.page}]</sup></p>`).join("")}</div>` : "");
  } else if (!a.answer) body = `<p class="a">${esc(t("noAnswer"))}</p>`;
  else if (a.mode === "ai") body = `<p class="a">${esc(a.answer)}</p>`;
  else body = (a.translation_error ? `<p class="hint warn-t">⚠ ${esc(t(a.translation_error === "unsupported_pair" ? "trUnsupported" : "trError"))}</p>` : "") + `<p class="hint">${esc(t("excerpt"))} — ${esc(a.source.title)}, ${t("page")}${a.source.page}</p><p class="a">${esc(a.answer)}</p>`;
  const badges = [`${t("qLang")}: ${LN[a.question_language] || a.question_language}`];
  const srcL = a.source_language || (a.mode !== "translated" && a.answer_language);
  if (srcL) badges.push(`${t("srcLang")}: ${LN[srcL] || srcL}`);
  if (a.mode === "translated" && a.answer) badges.push(`${t("ansLang")}: ${LN[a.answer_language] || a.answer_language}`);
  if (!a.confident && a.answer) badges.push(t("lowConf"));
  badges.push("−" + t("cost", {n: res.cost}));
  const srcs = res.hits.map((h, i) => `<details class="src"${i === 0 ? " open" : ""}><summary>[${i + 1}] ${esc(h.title)} · ${t("page")}${h.page} · ${LN[h.language] || h.language} · ${t("match")} ${(h.score * 100).toFixed(0)}%</summary>
    <p>${(h.highlights || []).map((s) => `<span class="hl">${esc(s)}</span>`).join(" … ") || esc(h.text.slice(0, 400))}</p></details>`).join("");
  box.innerHTML = `<div class="q">${esc(question)}</div>${body}<div class="badges">${badges.map((b) => `<span class="badge">${esc(b)}</span>`).join("")}</div>${srcs ? `<div><b style="font-size:13px">${esc(t(a.no_answer ? "nearest" : "sources"))}</b>${srcs}</div>` : ""}`;
  $("#answers").prepend(box);
}

async function loadDocs() {
  const q = $("#libSearch").value.trim();
  const docs = await api("/api/documents?q=" + encodeURIComponent(q));
  $("#docs").innerHTML = docs.length ? docs.map((d) => `<li><div class="t"><b>${esc(d.title)}</b><div class="meta">${esc(d.language_name)} · ${d.pages} ${t("pages")}${d.subject ? " · " + esc(d.subject) : ""}</div></div><button data-report="${d.id}">${t("report")}</button></li>`).join("") : `<li class="meta">${esc(t("empty"))}</li>`;
  if (!q) {
    const sel = $("#scope"), cur = sel.value;
    sel.innerHTML = `<option value="">${esc(t("allDocs"))}</option>` + docs.map((d) => `<option value="${d.id}">${esc(d.title)} (${esc(d.language_name)})</option>`).join("");
    sel.value = cur;
  }
}

$("#askForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const question = $("#q").value.trim(); if (question.length < 3) return;
  const btn = $("#askBtn"); btn.disabled = true;
  try {
    const scope = $("#scope").value;
    const res = await api("/api/ask", {method: "POST", headers: {"Content-Type": "application/json"},
      body: JSON.stringify({question, thread_id: threadId, doc_ids: scope ? [Number(scope)] : null, use_ai: $("#useAi").checked, ui_language: lang})});
    if (res.thread_id !== threadId) { threadId = res.thread_id; threadCount = 0; }
    threadCount++; renderThread(); renderAnswer(question, res); $("#q").value = "";
  } catch (err) {
    if (err.status === 402) { toast(t("lvl_" + (err.body.quota?.level || "blocked"), {d: new Date(err.body.quota.reset_at).toLocaleDateString(lang)})); $("#quota").classList.remove("collapsed"); $("#quota").scrollIntoView({behavior: "smooth"}); }
    else toast(err.message);
  } finally { btn.disabled = false; }
});
$("#newThread").addEventListener("click", () => { threadId = null; threadCount = 0; renderThread(); $("#q").focus(); });
$("#useAi").addEventListener("change", () => Q && renderQuota(Q));

$("#upForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const f = $("#file").files[0]; if (!f) return;
  const fd = new FormData(); fd.append("file", f); fd.append("title", $("#title").value); fd.append("subject", $("#subject").value); fd.append("rights", $("#rights").checked);
  $("#upBtn").disabled = true; $("#upMsg").textContent = t("uploading");
  try {
    const r = await api("/api/documents", {method: "POST", body: fd});
    $("#upMsg").textContent = r.duplicate ? t("dup", {t: r.title}) : t("uploaded", {t: r.title, p: r.pages});
    e.target.reset(); loadDocs();
  } catch (err) { $("#upMsg").textContent = err.message; } finally { $("#upBtn").disabled = false; }
});

$("#docs").addEventListener("click", async (e) => {
  const id = e.target.dataset.report; if (!id) return;
  const reason = (prompt(t("reportAsk"), "wrong") || "").trim().toLowerCase(); if (!reason) return;
  try { await api(`/api/documents/${id}/report`, {method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify({reason})}); toast(t("reported")); loadDocs(); }
  catch (err) { toast(err.message); }
});
let st; $("#libSearch").addEventListener("input", () => { clearTimeout(st); st = setTimeout(loadDocs, 250); });

$("#redeemForm").addEventListener("submit", async (e) => {
  e.preventDefault(); const fd = new FormData(); fd.append("code", $("#code").value);
  try { const r = await api("/api/redeem", {method: "POST", body: fd}); toast(t("added", {n: r.added})); $("#code").value = ""; } catch (err) { toast(err.message); }
});

document.querySelectorAll(".tabs button").forEach((b) => b.addEventListener("click", () => {
  document.querySelectorAll(".tabs button").forEach((x) => x.classList.toggle("on", x === b));
  $("#tab-ask").hidden = b.dataset.tab !== "ask"; $("#tab-lib").hidden = b.dataset.tab !== "lib";
}));
$("#miniMeter").addEventListener("click", () => { $("#quota").classList.toggle("collapsed"); });
$("#lang").addEventListener("change", (e) => { lang = e.target.value; localStorage.setItem("lang", lang); applyI18n(); loadDocs(); });

(async () => {
  if (matchMedia("(max-width:860px)").matches) $("#quota").classList.add("collapsed");
  applyI18n();
  await ensureDevice(false);
  try { renderQuota(await api("/api/quota")); } catch {}
  loadDocs();
  if ("serviceWorker" in navigator) navigator.serviceWorker.register("/sw.js").catch(() => {});
})();
