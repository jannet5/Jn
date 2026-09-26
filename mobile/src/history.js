import AsyncStorage from "@react-native-async-storage/async-storage";

const KEY = "etymology_history";
const MAX_ITEMS = 20;

export async function loadHistory() {
  try {
    const raw = await AsyncStorage.getItem(KEY);
    return raw ? JSON.parse(raw) : [];
  } catch {
    return [];
  }
}

export async function addToHistory(word) {
  const current = await loadHistory();
  const withoutDupe = current.filter(
    (w) => w.toLocaleLowerCase("tr-TR") !== word.toLocaleLowerCase("tr-TR")
  );
  const updated = [word, ...withoutDupe].slice(0, MAX_ITEMS);
  await AsyncStorage.setItem(KEY, JSON.stringify(updated));
  return updated;
}
