package com.jn.melodizil.data.store // Kalıcı depolama katmanı

import android.content.Context // Bağlam
import androidx.datastore.core.DataStore // DataStore
import androidx.datastore.preferences.core.Preferences // Tercihler
import androidx.datastore.preferences.core.booleanPreferencesKey // Boolean anahtar
import androidx.datastore.preferences.core.edit // Düzenleme
import androidx.datastore.preferences.core.stringPreferencesKey // String anahtar
import androidx.datastore.preferences.preferencesDataStore // Uzantı
import kotlinx.coroutines.flow.Flow // Akış
import kotlinx.coroutines.flow.map // Dönüştürme
import kotlinx.serialization.Serializable // JSON
import kotlinx.serialization.builtins.ListSerializer // Liste serileştirici
import kotlinx.serialization.json.Json // JSON motoru

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "melodizil_prefs") // Tek DataStore örneği

/** Tema seçimi. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

/** Kullanıcının kaydettiği bir zil sesi. */
@Serializable
data class SavedRingtone(
    val id: String, // Benzersiz kimlik
    val title: String, // Şarkı başlığı
    val instrumentId: String, // Enstrüman kimliği
    val kind: String, // RingtoneKind adı
    val uri: String, // MediaStore Uri
    val createdAtMillis: Long, // Oluşturma zamanı
    val lengthSec: Int, // Uzunluk
    val sourceVideoId: String?, // Kaynak video (yerel dosyaysa null)
    val thumbnailUrl: String?, // Kapak
)

/** Tercihler ve kayıtlı ziller (küçük veri; DataStore + JSON yeterli). */
class PrefsStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true } // Eski kayıtlarla uyum
    private val KEY_THEME = stringPreferencesKey("theme") // Tema anahtarı
    private val KEY_ONBOARDED = booleanPreferencesKey("onboarded") // Tanıtım görüldü mü
    private val KEY_LIBRARY = stringPreferencesKey("library") // Kayıtlı ziller JSON
    private val KEY_LAST_INSTRUMENT = stringPreferencesKey("last_instrument") // Son seçilen enstrüman

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { p -> p[KEY_THEME]?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() } ?: ThemeMode.SYSTEM } // Tema akışı
    val onboarded: Flow<Boolean> = context.dataStore.data.map { it[KEY_ONBOARDED] ?: false } // Tanıtım akışı
    val lastInstrument: Flow<String?> = context.dataStore.data.map { it[KEY_LAST_INSTRUMENT] } // Son enstrüman
    val library: Flow<List<SavedRingtone>> = context.dataStore.data.map { p -> // Kütüphane akışı
        p[KEY_LIBRARY]?.let { runCatching { json.decodeFromString(ListSerializer(SavedRingtone.serializer()), it) }.getOrDefault(emptyList()) } ?: emptyList() // JSON çöz
    }

    suspend fun setTheme(mode: ThemeMode) { context.dataStore.edit { it[KEY_THEME] = mode.name } } // Tema kaydet
    suspend fun setOnboarded() { context.dataStore.edit { it[KEY_ONBOARDED] = true } } // Tanıtım görüldü
    suspend fun setLastInstrument(id: String) { context.dataStore.edit { it[KEY_LAST_INSTRUMENT] = id } } // Son enstrüman

    suspend fun addToLibrary(item: SavedRingtone) { // Kütüphaneye ekle
        context.dataStore.edit { p -> // Düzenle
            val current = p[KEY_LIBRARY]?.let { runCatching { json.decodeFromString(ListSerializer(SavedRingtone.serializer()), it) }.getOrDefault(emptyList()) } ?: emptyList() // Mevcut
            p[KEY_LIBRARY] = json.encodeToString(ListSerializer(SavedRingtone.serializer()), listOf(item) + current) // En yeni başa
        }
    }

    suspend fun removeFromLibrary(id: String) { // Kütüphaneden çıkar
        context.dataStore.edit { p -> // Düzenle
            val current = p[KEY_LIBRARY]?.let { runCatching { json.decodeFromString(ListSerializer(SavedRingtone.serializer()), it) }.getOrDefault(emptyList()) } ?: emptyList() // Mevcut
            p[KEY_LIBRARY] = json.encodeToString(ListSerializer(SavedRingtone.serializer()), current.filterNot { it.id == id }) // Süz
        }
    }
}
