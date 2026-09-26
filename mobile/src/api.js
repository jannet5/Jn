const API_URL = process.env.EXPO_PUBLIC_API_URL;

export async function fetchEtymology(word) {
  if (!API_URL) {
    throw new Error(
      "Sunucu adresi ayarlanmamış. mobile/.env dosyasında EXPO_PUBLIC_API_URL değerini kontrol et."
    );
  }

  const url = `${API_URL.replace(/\/$/, "")}/api/etymology/${encodeURIComponent(word.trim())}`;
  const res = await fetch(url);
  const data = await res.json();

  if (!res.ok) {
    throw new Error(data.error || "Bilinmeyen bir hata oluştu.");
  }

  return data;
}
