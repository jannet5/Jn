import { createContext, use, useCallback, useEffect, useId, useMemo, useRef, useState, type ReactNode } from 'react';
import { StyleSheet, View } from 'react-native';
import Animated, { FadeInDown, FadeOutDown, useReducedMotion } from 'react-native-reanimated';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { Metin } from '@/components/metin';
import { haptik } from '@/lib/haptik';
import { useTema } from '@/theme/tokens';

// Toast + portal: sheet'ler ekranın değil kökün üstünde açılır (header dahil her şeyi örter).
type ToastTon = 'bilgi' | 'basari' | 'uyari';
type Eylemler = {
  toast: (metin: string, ton?: ToastTon) => void;
  bagla: (id: string, dugum: ReactNode) => void;
  coz: (id: string) => void;
};

const EylemBaglami = createContext<Eylemler | null>(null);

export function useKatman() {
  const e = use(EylemBaglami);
  if (!e) throw new Error('KatmanSaglayici eksik');
  return e;
}

export function KatmanSaglayici({ children }: { children: ReactNode }) {
  const [dugumler, setDugumler] = useState<Record<string, ReactNode>>({});
  const [toastDurum, setToastDurum] = useState<{ id: number; metin: string; ton: ToastTon } | null>(null);
  const zamanlayici = useRef<ReturnType<typeof setTimeout> | null>(null);

  const toast = useCallback((metin: string, ton: ToastTon = 'bilgi') => {
    if (zamanlayici.current) clearTimeout(zamanlayici.current);
    if (ton === 'basari') haptik.basari();
    if (ton === 'uyari') haptik.uyari();
    setToastDurum({ id: Date.now(), metin, ton });
    zamanlayici.current = setTimeout(() => setToastDurum(null), 2400);
  }, []);
  const bagla = useCallback((id: string, dugum: ReactNode) => setDugumler((d) => ({ ...d, [id]: dugum })), []);
  const coz = useCallback(
    (id: string) =>
      setDugumler((d) => {
        const { [id]: _, ...kalan } = d;
        return kalan;
      }),
    [],
  );
  const eylemler = useMemo(() => ({ toast, bagla, coz }), [toast, bagla, coz]);

  return (
    <EylemBaglami value={eylemler}>
      {children}
      {Object.entries(dugumler).map(([id, d]) => (
        <View key={id} style={StyleSheet.absoluteFill} pointerEvents="box-none">
          {d}
        </View>
      ))}
      {toastDurum ? <ToastGorunum key={toastDurum.id} metin={toastDurum.metin} ton={toastDurum.ton} /> : null}
    </EylemBaglami>
  );
}

function ToastGorunum({ metin, ton }: { metin: string; ton: ToastTon }) {
  const t = useTema();
  const alt = useSafeAreaInsets().bottom;
  const azHareket = useReducedMotion();
  const nokta = ton === 'basari' ? t.success : ton === 'uyari' ? t.accent : t.muted;
  return (
    <View style={[StyleSheet.absoluteFill, { justifyContent: 'flex-end', paddingBottom: alt + 16 }]} pointerEvents="box-none">
      <Animated.View
        entering={azHareket ? undefined : FadeInDown.duration(220)}
        exiting={azHareket ? undefined : FadeOutDown.duration(180)}
        accessibilityLiveRegion="polite"
        accessibilityRole="alert"
        className="mx-5 flex-row items-center gap-3 rounded-md px-4 py-3"
        style={{ backgroundColor: t.scheme === 'dark' ? '#EEF2EF' : '#0E1512', borderCurve: 'continuous' }}
      >
        <View className="h-2 w-2 rounded-full" style={{ backgroundColor: nokta }} />
        <Metin tur="body" className="flex-1" renk={t.scheme === 'dark' ? '#0E1512' : '#FFFFFF'}>
          {metin}
        </Metin>
      </Animated.View>
    </View>
  );
}

/** Ekran içinden kökteki katmana düğüm bağlar. */
export function Portal({ children }: { children: ReactNode }) {
  const { bagla, coz } = useKatman();
  const id = useId();
  useEffect(() => {
    bagla(id, children);
  });
  useEffect(() => () => coz(id), [coz, id]);
  return null;
}
