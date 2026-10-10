import AsyncStorage from '@react-native-async-storage/async-storage';
import { useEffect, useState } from 'react';

// Yerel kalıcı durum (backend yok). Yükleme bitene kadar `hazir` false.
export function useKalici<T>(anahtar: string, baslangic: T) {
  const [deger, setDeger] = useState<T>(baslangic);
  const [hazir, setHazir] = useState(false);

  useEffect(() => {
    let iptal = false;
    AsyncStorage.getItem(anahtar)
      .then((ham) => {
        if (!iptal && ham != null) setDeger(JSON.parse(ham) as T);
      })
      .catch(() => {})
      .finally(() => !iptal && setHazir(true));
    return () => {
      iptal = true;
    };
  }, [anahtar]);

  useEffect(() => {
    if (hazir) AsyncStorage.setItem(anahtar, JSON.stringify(deger)).catch(() => {});
  }, [anahtar, deger, hazir]);

  return [deger, setDeger, hazir] as const;
}

export const DEMO_ANAHTARLARI = ['demo:kafe', 'demo:berber', 'demo:restoran'];

export async function demoyuSifirla() {
  await AsyncStorage.multiRemove(DEMO_ANAHTARLARI);
}
