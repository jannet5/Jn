import { Link, type Href } from 'expo-router';
import { CalendarClock, ChevronRight, Coffee, MessageCircle, UtensilsCrossed, type LucideIcon } from 'lucide-react-native';
import { Pressable, ScrollView, View } from 'react-native';

import { Metin } from '@/components/metin';
import { temalar, useTema } from '@/theme/tokens';

type Konsept = { href: Href; tema: 'kafe' | 'berber' | 'restoran'; sektor: string; baslik: string; ozet: string; Ikon: LucideIcon };

const KONSEPTLER: Konsept[] = [
  { href: '/kafe', tema: 'kafe', sektor: 'Kafe', baslik: 'Dijital sadakat kartı', ozet: 'Kâğıt kart kaybolmaz. 10. kahve hediye, her damgada telefon titrer.', Ikon: Coffee },
  { href: '/berber', tema: 'berber', sektor: 'Berber · kuaför', baslik: 'Randevu', ozet: 'Gün ve saati müşteri seçer, telefon trafiği azalır.', Ikon: CalendarClock },
  { href: '/restoran', tema: 'restoran', sektor: 'Restoran', baslik: 'Menü ve WhatsApp sipariş', ozet: 'Menüden seçer, sepet hazır mesaj olarak WhatsApp’ınıza düşer.', Ikon: UtensilsCrossed },
];

export default function AnaEkran() {
  const t = useTema();
  return (
    <ScrollView contentInsetAdjustmentBehavior="automatic" style={{ backgroundColor: t.bg }} contentContainerClassName="gap-10 px-5 pb-12 pt-4">
      <View className="gap-3">
        <Metin tur="display" renk={t.fg} accessibilityRole="header">
          İşletmenizin uygulaması <Metin tur="display" renk={t.accent}>böyle</Metin> olur.
        </Metin>
        <Metin tur="lead" renk={t.muted}>
          Üç örnek konsept. Dokunun, deneyin; sizinki kendi renkleriniz ve menünüzle gelir.
        </Metin>
      </View>

      <View className="gap-3">
        {KONSEPTLER.map((k, i) => (
          <KonseptSatiri key={k.tema} k={k} buyuk={i === 0} />
        ))}
      </View>

      <Link href="/iletisim" asChild>
        <Pressable accessibilityRole="link">
          {({ pressed }) => (
            <View className="flex-row items-center gap-4 rounded-lg p-5" style={{ backgroundColor: pressed ? t.border : t.surface, borderCurve: 'continuous' }}>
              <View className="h-11 w-11 items-center justify-center rounded-full" style={{ backgroundColor: t.accent }}>
                <MessageCircle size={20} strokeWidth={1.75} color={t.accentFg} />
              </View>
              <View className="flex-1 gap-1">
                <Metin tur="h3" renk={t.fg}>
                  Bize ulaşın
                </Metin>
                <Metin tur="small" renk={t.muted}>
                  Ücretsiz dijital check-up, WhatsApp’tan
                </Metin>
              </View>
              <ChevronRight size={20} strokeWidth={1.75} color={t.muted} />
            </View>
          )}
        </Pressable>
      </Link>
    </ScrollView>
  );
}

function KonseptSatiri({ k, buyuk }: { k: Konsept; buyuk: boolean }) {
  const t = useTema();
  const kt = temalar[k.tema][t.scheme];
  return (
    <Link href={k.href} asChild>
      <Pressable accessibilityRole="link" accessibilityLabel={`${k.sektor} örnek konsepti: ${k.baslik}`}>
        {({ pressed }) => (
          <View
            className={`gap-4 rounded-lg p-5 ${buyuk ? 'pb-6' : ''}`}
            style={{ backgroundColor: kt.surface, borderCurve: 'continuous', opacity: pressed ? 0.85 : 1 }}
          >
            <View className="flex-row items-center justify-between">
              <View className="flex-row items-center gap-2">
                <View className="h-2 w-2 rounded-full" style={{ backgroundColor: kt.accent }} />
                <Metin tur="small" renk={kt.muted}>
                  {k.sektor} · örnek konsept
                </Metin>
              </View>
              <View className="h-10 w-10 items-center justify-center rounded-full" style={{ backgroundColor: kt.accent }}>
                <k.Ikon size={20} strokeWidth={1.75} color={kt.accentFg} />
              </View>
            </View>
            <View className="gap-1">
              <Metin tur={buyuk ? 'h2' : 'h3'} renk={kt.fg}>
                {k.baslik}
              </Metin>
              <Metin tur="body" renk={kt.muted}>
                {k.ozet}
              </Metin>
            </View>
            {buyuk ? <DamgaOnizleme renk={kt.accent} bos={kt.border} /> : null}
          </View>
        )}
      </Pressable>
    </Link>
  );
}

function DamgaOnizleme({ renk, bos }: { renk: string; bos: string }) {
  return (
    <View className="flex-row gap-1.5" accessibilityElementsHidden importantForAccessibility="no-hide-descendants">
      {Array.from({ length: 10 }, (_, i) => (
        <View key={i} className="h-5 flex-1 rounded-full" style={{ backgroundColor: i < 7 ? renk : bos }} />
      ))}
    </View>
  );
}
