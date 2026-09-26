import AsyncStorage from "@react-native-async-storage/async-storage";

const PREFIX = "etymology_cache:";
const TTL_MS = 1000 * 60 * 60 * 24 * 7; // 1 hafta - aynı kelimeyi tekrar sorunca yapay zekaya tekrar ödeme yapılmasın

export async function getCached(word) {
  try {
    const raw = await AsyncStorage.getItem(PREFIX + word.toLocaleLowerCase("tr-TR"));
    if (!raw) return null;

    const entry = JSON.parse(raw);
    if (Date.now() > entry.expiresAt) {
      await AsyncStorage.removeItem(PREFIX + word.toLocaleLowerCase("tr-TR"));
      return null;
    }
    return entry.value;
  } catch {
    return null;
  }
}

export async function setCached(word, value) {
  try {
    const entry = { value, expiresAt: Date.now() + TTL_MS };
    await AsyncStorage.setItem(
      PREFIX + word.toLocaleLowerCase("tr-TR"),
      JSON.stringify(entry)
    );
  } catch {
    // önbelleme başarısız olursa sessizce geç, uygulama yine çalışır
  }
}
