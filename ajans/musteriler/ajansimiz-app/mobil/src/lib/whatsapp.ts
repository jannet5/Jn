import { Linking } from 'react-native';

export function whatsappLinki(numara: string, mesaj: string) {
  return `https://wa.me/${numara}?text=${encodeURIComponent(mesaj)}`;
}

export async function whatsappAc(numara: string, mesaj: string): Promise<boolean> {
  if (!numara) return false;
  try {
    await Linking.openURL(whatsappLinki(numara, mesaj));
    return true;
  } catch {
    return false;
  }
}
