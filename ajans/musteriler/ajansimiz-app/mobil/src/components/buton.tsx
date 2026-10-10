import type { ReactNode } from 'react';
import { Pressable, View } from 'react-native';
import Animated, { useAnimatedStyle, useSharedValue, withSpring } from 'react-native-reanimated';

import { Metin } from '@/components/metin';
import { haptik } from '@/lib/haptik';
import type { Palette } from '@/theme/tokens';

type Props = {
  baslik: string;
  onPress: () => void;
  tema: Palette;
  tur?: 'birincil' | 'ikincil';
  devreDisi?: boolean;
  ikon?: ReactNode;
  erisimIpucu?: string;
};

const AnimatedPressable = Animated.createAnimatedComponent(Pressable);

export function Buton({ baslik, onPress, tema, tur = 'birincil', devreDisi, ikon, erisimIpucu }: Props) {
  const olcek = useSharedValue(1);
  const stil = useAnimatedStyle(() => ({ transform: [{ scale: olcek.get() }] }));
  const birincil = tur === 'birincil';

  return (
    <AnimatedPressable
      accessibilityRole="button"
      accessibilityState={{ disabled: !!devreDisi }}
      accessibilityHint={erisimIpucu}
      disabled={devreDisi}
      onPressIn={() => {
        olcek.set(withSpring(0.97, { damping: 18, stiffness: 400 }));
      }}
      onPressOut={() => {
        olcek.set(withSpring(1, { damping: 16, stiffness: 300 }));
      }}
      onPress={() => {
        haptik.hafif();
        onPress();
      }}
      style={stil}
    >
      {({ pressed }) => (
        <View
          className="h-[52px] flex-row items-center justify-center gap-2 rounded-full px-6"
          style={{
            borderCurve: 'continuous',
            opacity: devreDisi ? 0.4 : 1,
            backgroundColor: birincil ? (pressed ? tema.accentPressed : tema.accent) : pressed ? tema.surface : 'transparent',
            borderWidth: birincil ? 0 : 1,
            borderColor: tema.fg,
          }}
        >
          {ikon}
          <Metin tur="body" className="font-medium" renk={birincil ? tema.accentFg : tema.fg}>
            {baslik}
          </Metin>
        </View>
      )}
    </AnimatedPressable>
  );
}
