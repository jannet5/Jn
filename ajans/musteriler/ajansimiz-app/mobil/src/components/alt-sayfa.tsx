import { useEffect, type ReactNode } from 'react';
import { Pressable, StyleSheet, View } from 'react-native';
import { Gesture, GestureDetector } from 'react-native-gesture-handler';
import Animated, {
  Easing,
  runOnJS,
  useAnimatedStyle,
  useReducedMotion,
  useSharedValue,
  withSpring,
  withTiming,
} from 'react-native-reanimated';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { Buton } from '@/components/buton';
import { Portal } from '@/components/katman';
import { Metin } from '@/components/metin';
import { sheetShadow, type Palette } from '@/theme/tokens';

type Props = {
  acik: boolean;
  kapat: () => void;
  tema: Palette;
  baslik: string;
  children?: ReactNode;
  /** Birincil buton sheet açılır açılmaz görünür olmalı (mobil.md D-009) */
  altKisim?: ReactNode;
};

const KAPALI = 1200;

/** Kendi BottomSheet'imiz: tutamaç, aşağı sürükleyince kapanır, arka plana dokununca kapanır. */
export function AltSayfa({ acik, kapat, tema, baslik, children, altKisim }: Props) {
  const alt = useSafeAreaInsets().bottom;
  const azHareket = useReducedMotion();
  const y = useSharedValue(KAPALI);

  useEffect(() => {
    if (azHareket) y.set(acik ? 0 : KAPALI);
    else y.set(withTiming(acik ? 0 : KAPALI, { duration: acik ? 260 : 220, easing: Easing.out(Easing.cubic) }));
  }, [acik, azHareket, y]);

  const surukle = Gesture.Pan()
    .onChange((e) => {
      y.set(Math.max(0, y.get() + e.changeY));
    })
    .onEnd((e) => {
      if (y.get() > 120 || e.velocityY > 800) runOnJS(kapat)();
      else y.set(withSpring(0, { damping: 20, stiffness: 220 }));
    });

  const panelStil = useAnimatedStyle(() => ({ transform: [{ translateY: y.get() }] }));
  const perdeStil = useAnimatedStyle(() => ({ opacity: Math.max(0, 1 - y.get() / 400) * 0.45 }));

  return (
    <Portal>
      <View style={StyleSheet.absoluteFill} pointerEvents={acik ? 'auto' : 'none'}>
        <Animated.View style={[StyleSheet.absoluteFill, { backgroundColor: '#000000' }, perdeStil]}>
          <Pressable style={StyleSheet.absoluteFill} onPress={kapat} accessibilityLabel="Kapat" accessibilityRole="button" />
        </Animated.View>
        <Animated.View
          accessibilityViewIsModal
          className="absolute bottom-0 left-0 right-0 rounded-t-lg"
          style={[{ backgroundColor: tema.bg, boxShadow: sheetShadow, borderCurve: 'continuous', paddingBottom: alt + 16, maxHeight: '88%' }, panelStil]}
        >
          <GestureDetector gesture={surukle}>
            <View className="items-center pb-2 pt-3">
              <View className="h-[5px] w-10 rounded-full" style={{ backgroundColor: tema.border }} />
              <Metin tur="h3" renk={tema.fg} className="mt-4 self-stretch px-5" accessibilityRole="header">
                {baslik}
              </Metin>
            </View>
          </GestureDetector>
          {children ? <View className="px-5 pt-2">{children}</View> : null}
          {altKisim ? <View className="gap-3 px-5 pt-5">{altKisim}</View> : null}
        </Animated.View>
      </View>
    </Portal>
  );
}

/** Alert.alert yerine: geri alınamaz işlem onayı. */
export function OnaySayfasi({
  acik,
  kapat,
  tema,
  baslik,
  aciklama,
  onayMetni,
  onayla,
}: {
  acik: boolean;
  kapat: () => void;
  tema: Palette;
  baslik: string;
  aciklama: string;
  onayMetni: string;
  onayla: () => void;
}) {
  return (
    <AltSayfa
      acik={acik}
      kapat={kapat}
      tema={tema}
      baslik={baslik}
      altKisim={
        <>
          <Buton
            tema={tema}
            baslik={onayMetni}
            onPress={() => {
              onayla();
              kapat();
            }}
          />
          <Buton tema={tema} tur="ikincil" baslik="Vazgeç" onPress={kapat} />
        </>
      }
    >
      <Metin tur="body" renk={tema.muted}>
        {aciklama}
      </Metin>
    </AltSayfa>
  );
}
