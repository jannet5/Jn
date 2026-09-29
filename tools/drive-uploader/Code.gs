/**
 * CepGözcü Drive yükleyicisi — Google Apps Script web uygulaması.
 *
 * Claude oturumlarının büyük dosyaları (APK vb.) Drive'a atabilmesi için tek amaçlı,
 * yalnızca-yazma bir uç nokta. Dosya okuma, listeleme veya silme işlemi SUNMAZ.
 * Her istek, Script Properties'te saklanan gizli anahtarla doğrulanır; anahtar koda
 * gömülü değildir. Kurulum: aynı klasördeki README.md.
 */

var MAX_BYTES = 45 * 1024 * 1024; // Apps Script POST sınırı ~50 MB; base64 şişmesi için pay bırakıldı
var FOLDER_NAME = 'Claude Yüklemeleri';

/** Kurulumda BİR KEZ çalıştırın: rastgele gizli anahtar üretir ve günlüğe yazar. */
function setupSecret() {
  var props = PropertiesService.getScriptProperties();
  var secret = Utilities.getUuid().replace(/-/g, '') + Utilities.getUuid().replace(/-/g, '');
  props.setProperty('UPLOAD_SECRET', secret);
  Logger.log('Gizli anahtar (DRIVE_UPLOAD_SECRET): ' + secret);
}

/** Anahtarın ele geçtiğinden şüphelenirseniz çalıştırın; eski anahtar anında geçersiz olur. */
function rotateSecret() {
  setupSecret();
}

function doPost(e) {
  try {
    var expected = PropertiesService.getScriptProperties().getProperty('UPLOAD_SECRET');
    if (!expected) return json_({ ok: false, error: 'not_configured' });

    var req = JSON.parse(e.postData.contents);
    if (!req || typeof req.secret !== 'string' || !safeEquals_(req.secret, expected)) {
      return json_({ ok: false, error: 'unauthorized' });
    }
    if (typeof req.name !== 'string' || !req.name.trim() || typeof req.dataBase64 !== 'string') {
      return json_({ ok: false, error: 'bad_request' });
    }

    var bytes = Utilities.base64Decode(req.dataBase64);
    if (bytes.length > MAX_BYTES) return json_({ ok: false, error: 'too_large' });

    var mime = typeof req.mimeType === 'string' && req.mimeType ? req.mimeType : 'application/octet-stream';
    var blob = Utilities.newBlob(bytes, mime, req.name.trim());
    var file = uploadFolder_().createFile(blob);

    return json_({
      ok: true,
      id: file.getId(),
      name: file.getName(),
      size: file.getSize(),
      md5: hex_(Utilities.computeDigest(Utilities.DigestAlgorithm.MD5, bytes)),
      url: file.getUrl(),
    });
  } catch (err) {
    return json_({ ok: false, error: 'server_error' });
  }
}

function uploadFolder_() {
  var props = PropertiesService.getScriptProperties();
  var id = props.getProperty('FOLDER_ID');
  if (id) {
    try { return DriveApp.getFolderById(id); } catch (ignored) { /* klasör silinmiş; yeniden oluştur */ }
  }
  var folder = DriveApp.createFolder(FOLDER_NAME);
  props.setProperty('FOLDER_ID', folder.getId());
  return folder;
}

function safeEquals_(a, b) {
  if (a.length !== b.length) return false;
  var diff = 0;
  for (var i = 0; i < a.length; i++) diff |= a.charCodeAt(i) ^ b.charCodeAt(i);
  return diff === 0;
}

function hex_(bytes) {
  return bytes.map(function (b) { return ('0' + (b & 0xff).toString(16)).slice(-2); }).join('');
}

function json_(obj) {
  return ContentService.createTextOutput(JSON.stringify(obj)).setMimeType(ContentService.MimeType.JSON);
}
