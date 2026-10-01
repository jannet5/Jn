package com.jn.yazikart.data // veri katmanı paketi

import android.content.res.AssetManager // uygulama içindeki dosyalara (assets) erişim
import android.graphics.Typeface // Android yazı tipi nesnesi

// Tek bir yazı tipinin bilgisi: adı, normal dosyası ve (varsa) kalın dosyası
data class FontInfo(
    val name: String, // ekranda görünen ad
    val regularPath: String, // normal kalınlıktaki dosyanın yolu
    val boldPath: String?, // kalın dosyanın yolu (yoksa null, o zaman yapay kalınlık kullanılır)
)

// Uygulamadaki 50 yazı tipinin listesi ve yüklenmiş halleri
object FontCatalog {
    // Dünyada en çok kullanılan (Google Fonts kullanım sırasına göre) 50 yazı tipi.
    // Hepsi ücretsiz/açık lisanslı ve hepsi Türkçe karakterleri (ç ğ ı İ ö ş ü) destekliyor.
    val fonts: List<FontInfo> = listOf(
        FontInfo("Roboto", "fonts/roboto.ttf", "fonts/roboto_bold.ttf"), // 1. yazı tipi
        FontInfo("Open Sans", "fonts/opensans.ttf", "fonts/opensans_bold.ttf"), // 2. yazı tipi
        FontInfo("Noto Sans", "fonts/notosans.ttf", "fonts/notosans_bold.ttf"), // 3. yazı tipi
        FontInfo("Montserrat", "fonts/montserrat.ttf", "fonts/montserrat_bold.ttf"), // 4. yazı tipi
        FontInfo("Inter", "fonts/inter.ttf", "fonts/inter_bold.ttf"), // 5. yazı tipi
        FontInfo("Poppins", "fonts/poppins.ttf", "fonts/poppins_bold.ttf"), // 6. yazı tipi
        FontInfo("Roboto Condensed", "fonts/robotocondensed.ttf", "fonts/robotocondensed_bold.ttf"), // 7. yazı tipi
        FontInfo("Oswald", "fonts/oswald.ttf", "fonts/oswald_bold.ttf"), // 8. yazı tipi
        FontInfo("Raleway", "fonts/raleway.ttf", "fonts/raleway_bold.ttf"), // 9. yazı tipi
        FontInfo("Nunito", "fonts/nunito.ttf", "fonts/nunito_bold.ttf"), // 10. yazı tipi
        FontInfo("Rubik", "fonts/rubik.ttf", "fonts/rubik_bold.ttf"), // 11. yazı tipi
        FontInfo("Playfair Display", "fonts/playfairdisplay.ttf", "fonts/playfairdisplay_bold.ttf"), // 12. yazı tipi
        FontInfo("Ubuntu", "fonts/ubuntu.ttf", "fonts/ubuntu_bold.ttf"), // 13. yazı tipi
        FontInfo("Roboto Mono", "fonts/robotomono.ttf", "fonts/robotomono_bold.ttf"), // 14. yazı tipi
        FontInfo("Nunito Sans", "fonts/nunitosans.ttf", "fonts/nunitosans_bold.ttf"), // 15. yazı tipi
        FontInfo("Roboto Slab", "fonts/robotoslab.ttf", "fonts/robotoslab_bold.ttf"), // 16. yazı tipi
        FontInfo("Merriweather", "fonts/merriweather.ttf", "fonts/merriweather_bold.ttf"), // 17. yazı tipi
        FontInfo("Work Sans", "fonts/worksans.ttf", "fonts/worksans_bold.ttf"), // 18. yazı tipi
        FontInfo("PT Sans", "fonts/ptsans.ttf", "fonts/ptsans_bold.ttf"), // 19. yazı tipi
        FontInfo("Lora", "fonts/lora.ttf", "fonts/lora_bold.ttf"), // 20. yazı tipi
        FontInfo("Fira Sans", "fonts/firasans.ttf", "fonts/firasans_bold.ttf"), // 21. yazı tipi
        FontInfo("Quicksand", "fonts/quicksand.ttf", "fonts/quicksand_bold.ttf"), // 22. yazı tipi
        FontInfo("Mulish", "fonts/mulish.ttf", "fonts/mulish_bold.ttf"), // 23. yazı tipi
        FontInfo("Barlow", "fonts/barlow.ttf", "fonts/barlow_bold.ttf"), // 24. yazı tipi
        FontInfo("DM Sans", "fonts/dmsans.ttf", "fonts/dmsans_bold.ttf"), // 25. yazı tipi
        FontInfo("Titillium Web", "fonts/titilliumweb.ttf", "fonts/titilliumweb_bold.ttf"), // 26. yazı tipi
        FontInfo("IBM Plex Sans", "fonts/ibmplexsans.ttf", "fonts/ibmplexsans_bold.ttf"), // 27. yazı tipi
        FontInfo("Bebas Neue", "fonts/bebasneue.ttf", null), // 28. yazı tipi
        FontInfo("Manrope", "fonts/manrope.ttf", "fonts/manrope_bold.ttf"), // 29. yazı tipi
        FontInfo("Karla", "fonts/karla.ttf", "fonts/karla_bold.ttf"), // 30. yazı tipi
        FontInfo("Libre Baskerville", "fonts/librebaskerville.ttf", "fonts/librebaskerville_bold.ttf"), // 31. yazı tipi
        FontInfo("Josefin Sans", "fonts/josefinsans.ttf", "fonts/josefinsans_bold.ttf"), // 32. yazı tipi
        FontInfo("Source Sans 3", "fonts/sourcesans3.ttf", "fonts/sourcesans3_bold.ttf"), // 33. yazı tipi
        FontInfo("PT Serif", "fonts/ptserif.ttf", "fonts/ptserif_bold.ttf"), // 34. yazı tipi
        FontInfo("Dancing Script", "fonts/dancingscript.ttf", "fonts/dancingscript_bold.ttf"), // 35. yazı tipi
        FontInfo("Arimo (Arial benzeri)", "fonts/arimo.ttf", "fonts/arimo_bold.ttf"), // 36. yazı tipi
        FontInfo("Tinos (Times New Roman benzeri)", "fonts/tinos.ttf", "fonts/tinos_bold.ttf"), // 37. yazı tipi
        FontInfo("Anton", "fonts/anton.ttf", null), // 38. yazı tipi
        FontInfo("Pacifico", "fonts/pacifico.ttf", null), // 39. yazı tipi
        FontInfo("Lobster", "fonts/lobster.ttf", null), // 40. yazı tipi
        FontInfo("Caveat", "fonts/caveat.ttf", "fonts/caveat_bold.ttf"), // 41. yazı tipi
        FontInfo("EB Garamond", "fonts/ebgaramond.ttf", "fonts/ebgaramond_bold.ttf"), // 42. yazı tipi
        FontInfo("Abril Fatface", "fonts/abrilfatface.ttf", null), // 43. yazı tipi
        FontInfo("Bitter", "fonts/bitter.ttf", "fonts/bitter_bold.ttf"), // 44. yazı tipi
        FontInfo("Comfortaa", "fonts/comfortaa.ttf", "fonts/comfortaa_bold.ttf"), // 45. yazı tipi
        FontInfo("Great Vibes", "fonts/greatvibes.ttf", null), // 46. yazı tipi
        FontInfo("Cormorant Garamond", "fonts/cormorantgaramond.ttf", "fonts/cormorantgaramond_bold.ttf"), // 47. yazı tipi
        FontInfo("Space Grotesk", "fonts/spacegrotesk.ttf", "fonts/spacegrotesk_bold.ttf"), // 48. yazı tipi
        FontInfo("Shadows Into Light", "fonts/shadowsintolight.ttf", null), // 49. yazı tipi
        FontInfo("Alfa Slab One", "fonts/alfaslabone.ttf", null), // 50. yazı tipi
    )

    private val cache = HashMap<String, Typeface>() // aynı dosyayı tekrar tekrar açmamak için önbellek

    // Seçilen yazı tipini (normal ya da kalın) Typeface olarak döndürür
    fun typeface(assets: AssetManager, index: Int, bold: Boolean): Typeface {
        val info = fonts[index.coerceIn(0, fonts.lastIndex)] // index sınır dışındaysa düzeltiliyor
        val path = if (bold && info.boldPath != null) info.boldPath else info.regularPath // kalın istendiyse ve varsa kalın dosya
        return synchronized(cache) { // önbelleğe aynı anda tek iş parçacığı erişsin
            cache.getOrPut(path) { Typeface.createFromAsset(assets, path) } // yoksa dosyadan yükleniyor
        }
    }

    // Kalın istendiği halde kalın dosyası yoksa yapay kalınlık gerekir mi?
    fun needsFakeBold(index: Int, bold: Boolean): Boolean =
        bold && fonts[index.coerceIn(0, fonts.lastIndex)].boldPath == null // kalın dosya yoksa true
}
