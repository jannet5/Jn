package com.jn.melodizil.ui // Arayüz durumu

import android.app.Application // Uygulama
import android.content.Intent // Paylaşım
import android.net.Uri // Uri
import androidx.core.content.FileProvider // Dosya paylaşımı
import androidx.lifecycle.AndroidViewModel // ViewModel
import androidx.lifecycle.viewModelScope // Kapsam
import com.jn.melodizil.MelodiZilApp // Bağımlılıklar
import com.jn.melodizil.core.Instrument // Enstrüman
import com.jn.melodizil.core.SegmentChooser // Bölüm seçici
import com.jn.melodizil.core.Synth // Sentez
import com.jn.melodizil.core.WavWriter // WAV
import com.jn.melodizil.data.audio.PcmPlayer // Önizleme
import com.jn.melodizil.data.store.RingtoneKind // Zil türü
import com.jn.melodizil.data.store.SavedRingtone // Kayıt
import com.jn.melodizil.data.store.ThemeMode // Tema
import com.jn.melodizil.data.youtube.SourceException // Hatalar
import com.jn.melodizil.domain.AnalyzedTrack // Analiz sonucu
import com.jn.melodizil.domain.PipelineProgress // İlerleme
import kotlinx.coroutines.Dispatchers // İş parçacığı
import kotlinx.coroutines.Job // İş
import kotlinx.coroutines.flow.MutableStateFlow // Durum
import kotlinx.coroutines.flow.SharingStarted // Paylaşım
import kotlinx.coroutines.flow.StateFlow // Akış
import kotlinx.coroutines.flow.first // İlk değer
import kotlinx.coroutines.flow.stateIn // Durum akışı
import kotlinx.coroutines.launch // Başlat
import kotlinx.coroutines.withContext // Bağlam
import java.io.File // Dosya
import java.util.UUID // Kimlik

/** İşlem ekranının durumu. */
sealed interface ProcessState {
    data object Idle : ProcessState // Boşta
    data class Running(val progress: PipelineProgress, val sourceLabel: String) : ProcessState // Çalışıyor
    data class Failed(val message: String, val canRetry: Boolean) : ProcessState // Hata
    data class Done(val track: AnalyzedTrack) : ProcessState // Bitti
}

/** Sonuç ekranının durumu. */
data class ResultState(
    val track: AnalyzedTrack? = null, // Analiz
    val instrument: Instrument = Instrument.PIYANO, // Seçili tını
    val lengthSec: Int = 30, // Zil uzunluğu
    val startSec: Float = 0f, // Pencere başlangıcı
    val rendering: Boolean = false, // Sentez sürüyor
    val pcm: FloatArray? = null, // Son sentez (önizleme ve kayıt için)
    val saving: Boolean = false, // Kaydediliyor
    val lastSavedUri: Uri? = null, // Son kayıt
    val message: String? = null, // Snackbar mesajı
    val needsWriteSettings: Boolean = false, // Varsayılan yapmak için izin gerekiyor
)

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val deps = app as MelodiZilApp // Bağımlılıklar
    private val synth = Synth(RENDER_RATE) // Sentezleyici
    val player = PcmPlayer(viewModelScope) // Önizleme oynatıcı

    val themeMode: StateFlow<ThemeMode> = deps.prefs.themeMode.stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM) // Tema
    val onboarded: StateFlow<Boolean?> = deps.prefs.onboarded.stateIn(viewModelScope, SharingStarted.Eagerly, null) // Tanıtım (null = yükleniyor)
    val library: StateFlow<List<SavedRingtone>> = deps.prefs.library.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList()) // Kütüphane

    private val _process = MutableStateFlow<ProcessState>(ProcessState.Idle) // İşlem
    val process: StateFlow<ProcessState> = _process // Dışa
    private val _result = MutableStateFlow(ResultState()) // Sonuç
    val result: StateFlow<ResultState> = _result // Dışa
    private var job: Job? = null // Aktif işlem
    private var lastInput: (() -> Unit)? = null // Tekrar denemek için son eylem
    private var pendingDefault: Pair<Uri, RingtoneKind>? = null // İzin sonrası uygulanacak varsayılan

    // -------------------------------------------------------------- işlem

    fun startFromLink(link: String) { lastInput = { startFromLink(link) }; run("YouTube") { deps.pipeline.fromYouTube(link, it) } } // Bağlantıdan
    fun startFromFile(uri: Uri) { lastInput = { startFromFile(uri) }; run("Dosya") { deps.pipeline.fromLocalFile(uri, it) } } // Dosyadan
    fun retry() { lastInput?.invoke() } // Tekrar dene

    private fun run(label: String, block: suspend ((PipelineProgress) -> Unit) -> AnalyzedTrack) {
        job?.cancel() // Önceki iptal
        player.stop() // Ses dursun
        _process.value = ProcessState.Running(PipelineProgress(com.jn.melodizil.domain.Step.RESOLVE, 0f), label) // Başladı
        job = viewModelScope.launch { // Arka plan
            try {
                val track = block { p -> _process.value = ProcessState.Running(p, label) } // Çalıştır
                val length = 30 // Varsayılan uzunluk
                val start = SegmentChooser.bestStart(track.melody, length.toFloat()) // En iyi bölüm
                val inst = deps.prefs.lastInstrument.first()?.let { Instrument.fromId(it) } ?: Instrument.PIYANO // Son seçilen enstrüman ya da piyano
                _result.value = ResultState(track = track, instrument = inst, lengthSec = length, startSec = start) // Sonuç durumu
                _process.value = ProcessState.Done(track) // Bitti
                render() // İlk sentez
            } catch (e: kotlinx.coroutines.CancellationException) { _process.value = ProcessState.Idle } // İptal
            catch (e: SourceException.TooLong) { _process.value = ProcessState.Failed("Bu video ${e.durationSec / 60} dakika. Yalnızca 10 dakikanın altındaki videolar desteklenir.", false) } // Uzun
            catch (e: SourceException) { _process.value = ProcessState.Failed(e.message ?: "Bir sorun oluştu", true) } // Kaynak hatası
            catch (e: Exception) { _process.value = ProcessState.Failed("Beklenmeyen hata: ${e.javaClass.simpleName}", true) } // Diğer
        }
    }

    fun cancel() { job?.cancel(); job = null; _process.value = ProcessState.Idle } // İptal
    fun resetProcess() { if (_process.value !is ProcessState.Running) _process.value = ProcessState.Idle } // Ekrandan çıkışta

    // -------------------------------------------------------------- sonuç

    fun selectInstrument(i: Instrument) { _result.value = _result.value.copy(instrument = i); viewModelScope.launch { deps.prefs.setLastInstrument(i.id) }; render() } // Tını
    fun setLength(sec: Int) { // Uzunluk
        val s = _result.value; val track = s.track ?: return // Durum
        val maxStart = (track.melody.durationSec - sec).coerceAtLeast(0f) // Yeni üst sınır
        _result.value = s.copy(lengthSec = sec, startSec = s.startSec.coerceAtMost(maxStart)); render() // Uygula
    }
    fun setStart(sec: Float) { val s = _result.value; _result.value = s.copy(startSec = sec) } // Kaydırma sırasında
    fun commitStart() { render() } // Kaydırma bitti
    fun autoStart() { val s = _result.value; val t = s.track ?: return; _result.value = s.copy(startSec = SegmentChooser.bestStart(t.melody, s.lengthSec.toFloat())); render() } // Otomatik

    private var renderJob: Job? = null // Sentez işi
    private fun render() {
        val s = _result.value; val track = s.track ?: return // Durum
        renderJob?.cancel(); player.stop() // Önceki iptal
        _result.value = s.copy(rendering = true, pcm = null) // Yükleniyor
        renderJob = viewModelScope.launch(Dispatchers.Default) { // CPU işi
            val pcm = synth.render(track.melody, s.instrument, s.startSec, s.lengthSec.toFloat()) // Sentez
            _result.value = _result.value.copy(rendering = false, pcm = pcm) // Hazır
        }
    }

    fun togglePlay() { // Oynat/durdur
        if (player.isPlaying.value) { player.stop(); return } // Durdur
        _result.value.pcm?.let { player.play(it, RENDER_RATE) } // Çal
    }

    fun save(kind: RingtoneKind, setDefault: Boolean) { // Kaydet
        val s = _result.value; val track = s.track ?: return; val pcm = s.pcm ?: return // Durum
        _result.value = s.copy(saving = true) // Kaydediliyor
        viewModelScope.launch { // Arka plan
            try {
                val name = "${track.title.take(40)} - ${instrumentName(s.instrument)}" // Dosya adı
                val uri = withContext(Dispatchers.IO) { deps.ringtones.save(name, WavWriter.toWav(pcm, RENDER_RATE), kind) } // MediaStore
                deps.prefs.addToLibrary(SavedRingtone(UUID.randomUUID().toString(), track.title, s.instrument.id, kind.name, uri.toString(), System.currentTimeMillis(), s.lengthSec, track.sourceVideoId, track.thumbnailUrl)) // Kütüphane
                var msg = "Kaydedildi" // Mesaj
                var needs = false // İzin gerekir mi
                if (setDefault) { // Varsayılan istenmiş
                    if (deps.ringtones.setAsDefault(uri, kind)) msg = "Kaydedildi ve ${kindLabel(kind)} olarak ayarlandı" // Başarılı
                    else { pendingDefault = uri to kind; needs = true } // İzin gerekiyor
                }
                _result.value = _result.value.copy(saving = false, lastSavedUri = uri, message = msg, needsWriteSettings = needs) // Sonuç
            } catch (e: Exception) { _result.value = _result.value.copy(saving = false, message = "Kaydedilemedi: ${e.message ?: e.javaClass.simpleName}") } // Hata
        }
    }

    /** Kullanıcı izin ekranından döndükten sonra çağrılır; bekleyen varsayılan uygulanır. */
    fun applyPendingDefault(): Boolean {
        val (uri, kind) = pendingDefault ?: return false // Bekleyen yok
        if (!deps.ringtones.canWriteSettings()) return false // İzin hâlâ yok
        val ok = deps.ringtones.setAsDefault(uri, kind); pendingDefault = null // Uygula
        _result.value = _result.value.copy(needsWriteSettings = false, message = if (ok) "${kindLabel(kind)} olarak ayarlandı" else "Ayarlanamadı") // Mesaj
        return ok // Sonuç
    }
    fun writeSettingsIntent(): Intent = deps.ringtones.writeSettingsIntent() // İzin ekranı
    fun canWriteSettings(): Boolean = deps.ringtones.canWriteSettings() // İzin var mı
    fun dismissPermission() { pendingDefault = null; _result.value = _result.value.copy(needsWriteSettings = false) } // Vazgeç
    fun consumeMessage() { _result.value = _result.value.copy(message = null) } // Snackbar gösterildi
    fun showMessage(text: String) { _result.value = _result.value.copy(message = text) } // Dışarıdan mesaj

    /** Mevcut sentezi geçici WAV olarak yazar ve paylaşım Intent'i döndürür. */
    suspend fun shareIntent(): Intent? = withContext(Dispatchers.IO) {
        val s = _result.value; val pcm = s.pcm ?: return@withContext null; val track = s.track ?: return@withContext null // Durum
        val dir = File(getApplication<Application>().cacheDir, "paylasim").apply { mkdirs() } // Klasör
        val file = File(dir, "${track.title.replace(Regex("[^\\p{L}\\p{N} _-]"), "").take(40).ifBlank { "melodi" }}.wav") // Dosya
        file.writeBytes(WavWriter.toWav(pcm, RENDER_RATE)) // Yaz
        val uri = FileProvider.getUriForFile(getApplication(), "${getApplication<Application>().packageName}.files", file) // Uri
        Intent(Intent.ACTION_SEND).setType("audio/wav").putExtra(Intent.EXTRA_STREAM, uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Intent
    }

    // -------------------------------------------------------------- kütüphane

    fun setSavedAsDefault(item: SavedRingtone, kind: RingtoneKind): Boolean { // Kütüphaneden varsayılan yap
        val uri = Uri.parse(item.uri) // Uri
        if (!deps.ringtones.exists(uri)) { _result.value = _result.value.copy(message = "Dosya bulunamadı, silinmiş olabilir"); return true } // Yok
        if (deps.ringtones.setAsDefault(uri, kind)) { _result.value = _result.value.copy(message = "${kindLabel(kind)} olarak ayarlandı"); return true } // Başarılı
        pendingDefault = uri to kind; return false // İzin gerekir
    }
    fun deleteSaved(item: SavedRingtone) { viewModelScope.launch { deps.ringtones.delete(Uri.parse(item.uri)); deps.prefs.removeFromLibrary(item.id); _result.value = _result.value.copy(message = "Silindi") } } // Sil
    fun shareSaved(item: SavedRingtone): Intent = Intent(Intent.ACTION_SEND).setType("audio/wav").putExtra(Intent.EXTRA_STREAM, Uri.parse(item.uri)).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Paylaş

    // -------------------------------------------------------------- ayarlar

    fun setTheme(mode: ThemeMode) { viewModelScope.launch { deps.prefs.setTheme(mode) } } // Tema
    fun finishOnboarding() { viewModelScope.launch { deps.prefs.setOnboarded() } } // Tanıtım bitti

    override fun onCleared() { player.stop(); super.onCleared() } // Temizlik

    companion object {
        const val RENDER_RATE = 44100 // Sentez örnekleme hızı
        fun instrumentName(i: Instrument): String = when (i) { // Türkçe adlar
            Instrument.PIYANO -> "Piyano"; Instrument.MUZIK_KUTUSU -> "Müzik Kutusu"; Instrument.SEKIZ_BIT -> "8-Bit"; Instrument.MARIMBA -> "Marimba"
            Instrument.FLUT -> "Flüt"; Instrument.CAN -> "Çan"; Instrument.GITAR -> "Gitar"; Instrument.SYNTH -> "Synth"
        }
        fun kindLabel(k: RingtoneKind): String = when (k) { RingtoneKind.RINGTONE -> "zil sesi"; RingtoneKind.NOTIFICATION -> "bildirim sesi"; RingtoneKind.ALARM -> "alarm sesi" } // Tür adları
    }
}
