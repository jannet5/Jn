"""Gerçek uvicorn sunucusuna karşı tarayıcı kabul testi (masaüstü + mobil).

Kullanım: python scripts/e2e.py http://127.0.0.1:8765 samples/ out/
"""
import sys
from pathlib import Path

from playwright.sync_api import expect, sync_playwright

base, samples, out = sys.argv[1], Path(sys.argv[2]), Path(sys.argv[3])
out.mkdir(parents=True, exist_ok=True)

with sync_playwright() as p:
    import os
    exe = os.environ.get("CHROMIUM_PATH")  # sabit Chromium varsa onu kullan
    browser = p.chromium.launch(executable_path=exe) if exe else p.chromium.launch()

    # 1) Masaüstü, Türkçe arayüz: Türk öğrenci PDF paylaşır
    ctx = browser.new_context(viewport={"width": 1280, "height": 860}, locale="tr-TR")
    page = ctx.new_page()
    page.goto(base)
    expect(page.locator("#qLeft")).to_have_text("8")  # sunucu MEDPDF_MONTHLY_FREE=8 ile açılır
    expect(page.locator("#askBtn")).to_contain_text("1 kredi")
    page.click("[data-tab=lib]")
    page.set_input_files("#file", str(samples / "acil-dahiliye-ozet-tr.pdf"))
    page.fill("#subject", "Acil Tıp")
    page.check("#rights")
    page.click("#upBtn")
    expect(page.locator("#upMsg")).to_contain_text("Eklendi", timeout=60000)
    expect(page.locator("#docs")).to_contain_text("Acil ve Dahiliye Özet Notları")
    page.screenshot(path=str(out / "1-masaustu-kutuphane-tr.png"), full_page=True)
    assert page.locator("#quota").bounding_box()["x"] > 800, "kota paneli sağda olmalı"
    ctx.close()

    # 2) Mobil (iPhone boyutu), İspanyolca arayüz: başka kişi, giriş yok
    m = browser.new_context(**p.devices["iPhone 13"], locale="es-ES")
    mp = m.new_page()
    mp.goto(base)
    expect(mp.locator("#miniMeter")).to_be_visible()
    expect(mp.locator("#miniText")).to_contain_text("8")
    mp.fill("#q", "¿Cuál es el tratamiento inicial del infarto agudo de miocardio?")
    mp.click("#askBtn")
    ans = mp.locator(".ans").first
    expect(ans).to_contain_text("aspirina", timeout=60000)
    expect(ans).to_contain_text("Idioma de la fuente: Türkçe")
    expect(ans).to_contain_text("Idioma de la respuesta: Español")
    expect(ans).to_contain_text("controles automáticos superficiales")
    expect(ans).not_to_contain_text("✓")
    expect(ans.locator("details.orig").first).to_be_attached()  # Türkçe orijinal açılabilir
    expect(mp.locator("#miniText")).to_contain_text("7")
    expect(mp.locator("#threadHint")).to_contain_text("1 pregunta")
    mp.screenshot(path=str(out / "2-mobil-ispanyolca-soru.png"), full_page=True)
    # Kaynakta olmayan soru → açıkça "cevap yok"
    mp.fill("#q", "¿Cuál es el tratamiento de la crisis asmática?")
    mp.click("#askBtn")
    expect(mp.locator(".ans").first).to_contain_text("No inventamos respuestas", timeout=60000)
    mp.locator(".ans").first.screenshot(path=str(out / "2b-mobil-cevap-yok.png"))
    # Hakkın çoğunu aynı konuda kullan → uyarı seviyesine geç, panel aç
    for _ in range(5):
        mp.fill("#q", "¿Qué dosis de adrenalina se usa en la anafilaxia?")
        mp.click("#askBtn")
        expect(mp.locator("#askBtn")).to_be_enabled(timeout=60000)
    expect(mp.locator("#miniText")).to_contain_text("1")
    mp.click("#miniMeter")
    expect(mp.locator("#qLevel")).to_contain_text("75 %")
    mp.screenshot(path=str(out / "3-mobil-kota-uyari.png"))
    # Son kredi + açık konu → esneme payıyla devam, yarıda kesilmez
    for _ in range(2):
        mp.fill("#q", "¿Qué dosis de adrenalina se usa en la anafilaxia?")
        mp.click("#askBtn")
        expect(mp.locator("#askBtn")).to_be_enabled(timeout=60000)
    expect(mp.locator("#qGrace")).to_contain_text("5 / 6")
    expect(mp.locator("#qLevel")).to_contain_text("margen")
    mp.evaluate("window.scrollTo(0,0)"); mp.screenshot(path=str(out / "4-mobil-is-bitirme-payi.png"))
    assert mp.evaluate("document.documentElement.scrollWidth <= window.innerWidth"), "yatay taşma var"
    box = mp.locator("#lang").bounding_box()
    assert box["x"] + box["width"] <= mp.viewport_size["width"], "dil seçici ekran dışında"
    assert mp.locator("#aiWrap").is_hidden() and mp.locator("#donate").is_hidden()
    m.close()

    # 3) Masaüstü İngilizce, karanlık tema
    d = browser.new_context(viewport={"width": 1280, "height": 860}, locale="en-US", color_scheme="dark")
    dp = d.new_page()
    dp.goto(base)
    dp.fill("#q", "What is the target HbA1c in type 2 diabetes?")
    dp.click("#askBtn")
    expect(dp.locator(".ans").first).to_contain_text("7%", timeout=60000)
    expect(dp.locator(".ans").first).to_contain_text("Answer language: English")
    dp.screenshot(path=str(out / "5-masaustu-ingilizce-karanlik.png"), full_page=True)
    d.close()
    browser.close()
print("E2E OK")
