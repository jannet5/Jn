import { Stack } from 'expo-router';

import type { Palette } from '@/theme/tokens';

/** Konsept ekranı native stack header'ını kendi mini temasına boyar (elle header yok). */
export function KonseptBaslik({ baslik, tema }: { baslik: string; tema: Palette }) {
  return (
    <Stack.Screen
      options={{
        title: baslik,
        headerStyle: { backgroundColor: tema.bg },
        headerTintColor: tema.fg,
        contentStyle: { backgroundColor: tema.bg },
      }}
    />
  );
}
