import * as SecureStore from "expo-secure-store";

const KEY = "anthropic_api_key";

export async function getApiKey() {
  try {
    return await SecureStore.getItemAsync(KEY);
  } catch {
    return null;
  }
}

export async function setApiKey(value) {
  const trimmed = value.trim();
  if (trimmed) {
    await SecureStore.setItemAsync(KEY, trimmed);
  } else {
    await SecureStore.deleteItemAsync(KEY);
  }
}
