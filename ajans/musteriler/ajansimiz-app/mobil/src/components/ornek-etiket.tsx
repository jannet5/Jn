import { View } from 'react-native';

import { Metin } from '@/components/metin';
import type { Palette } from '@/theme/tokens';

// Kanıt kuralı: her konsept görünür "Örnek konsept" etiketi taşır.
export function OrnekEtiket({ tema, metin = 'Örnek konsept' }: { tema: Palette; metin?: string }) {
  return (
    <View className="flex-row items-center gap-2 self-start rounded-full px-3 py-1" style={{ backgroundColor: tema.surface }}>
      <View className="h-2 w-2 rounded-full" style={{ backgroundColor: tema.accent }} />
      <Metin tur="etiket" renk={tema.muted}>
        {metin}
      </Metin>
    </View>
  );
}
