import { Minus, Plus, ShoppingBag } from 'lucide-react-native';
import { useState } from 'react';
import { Pressable, ScrollView, View } from 'react-native';
import Animated, { FadeInDown, FadeOutDown, useReducedMotion } from 'react-native-reanimated';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { AltSayfa } from '@/components/alt-sayfa';
import { Buton } from '@/components/buton';
import { useKatman } from '@/components/katman';
import { KonseptBaslik } from '@/components/konsept-baslik';
import { Metin } from '@/components/metin';
import { OrnekEtiket } from '@/components/ornek-etiket';
import { RESTORAN, tl } from '@/data/ornek';
import { haptik } from '@/lib/haptik';
import { useKalici } from '@/lib/kalici';
import { useTema, type Palette } from '@/theme/tokens';

const TUM_URUNLER = RESTORAN.kategoriler.flatMap((k) => k.urunler);

export default function RestoranEkrani() {
  const t = useTema('restoran');
  const { toast } = useKatman();
  const alt = useSafeAreaInsets().bottom;
  const azHareket = useReducedMotion();
  const [kategori, setKategori] = useState(RESTORAN.kategoriler[0].id);
  const [sepet, setSepet] = useKalici<Record<string, number>>('demo:restoran', {});
  const [sepetAcik, setSepetAcik] = useState(false);

  const satirlar = TUM_URUNLER.filter((u) => (sepet[u.id] ?? 0) > 0).map((u) => ({ ...u, adet: sepet[u.id] }));
  const adet = satirlar.reduce((a, s) => a + s.adet, 0);
  const toplam = satirlar.reduce((a, s) => a + s.adet * s.fiyat, 0);
  const mesaj = [`Merhaba, ${RESTORAN.ad} için sipariş:`, ...satirlar.map((s) => `• ${s.adet} × ${s.ad}`), `Toplam: ${tl(toplam)}`].join('\n');

  const degistir = (id: string, fark: number) => {
    haptik.hafif();
    setSepet((s) => {
      const yeni = Math.max(0, (s[id] ?? 0) + fark);
      const { [id]: _, ...kalan } = s;
      return yeni > 0 ? { ...kalan, [id]: yeni } : kalan;
    });
  };

  const urunler = RESTORAN.kategoriler.find((k) => k.id === kategori)?.urunler ?? [];

  return (
    <View className="flex-1" style={{ backgroundColor: t.bg }}>
      <KonseptBaslik baslik="Menü" tema={t} />
      <ScrollView contentInsetAdjustmentBehavior="automatic" contentContainerClassName="gap-6 px-5 pt-4" contentContainerStyle={{ paddingBottom: alt + 120 }}>
        <View className="gap-3">
          <OrnekEtiket tema={t} />
          <Metin tur="h2" renk={t.fg} accessibilityRole="header">
            {RESTORAN.ad}
          </Metin>
        </View>

        <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginHorizontal: -20 }} contentContainerStyle={{ paddingHorizontal: 20, gap: 8 }} accessibilityRole="tablist">
          {RESTORAN.kategoriler.map((k) => {
            const secili = k.id === kategori;
            return (
              <Pressable
                key={k.id}
                accessibilityRole="tab"
                accessibilityState={{ selected: secili }}
                onPress={() => {
                  haptik.secim();
                  setKategori(k.id);
                }}
              >
                {({ pressed }) => (
                  <View className="h-11 justify-center rounded-full px-5" style={{ backgroundColor: secili ? t.fg : pressed ? t.border : t.surface }}>
                    <Metin tur="body" className="font-medium" renk={secili ? t.bg : t.fg}>
                      {k.ad}
                    </Metin>
                  </View>
                )}
              </Pressable>
            );
          })}
        </ScrollView>

        <View>
          {urunler.map((u, i) => (
            <UrunSatiri key={u.id} ad={u.ad} aciklama={u.aciklama} fiyat={u.fiyat} adet={sepet[u.id] ?? 0} tema={t} ayrac={i > 0} degistir={(f) => degistir(u.id, f)} />
          ))}
        </View>
        <Metin tur="small" renk={t.muted}>
          Fiyatlar örnektir. Sizin uygulamanızda menünüz ve fiyatlarınız yer alır.
        </Metin>
      </ScrollView>

      {adet > 0 ? (
        <Animated.View entering={azHareket ? undefined : FadeInDown.duration(220)} exiting={azHareket ? undefined : FadeOutDown.duration(180)} className="absolute left-0 right-0 px-5" style={{ bottom: alt + 12 }}>
          <Pressable accessibilityRole="button" accessibilityLabel={`Sepeti aç, ${adet} ürün, ${tl(toplam)}`} onPress={() => setSepetAcik(true)}>
            {({ pressed }) => (
              <View className="h-[56px] flex-row items-center gap-3 rounded-full px-5" style={{ backgroundColor: pressed ? t.accentPressed : t.accent, borderCurve: 'continuous' }}>
                <ShoppingBag size={20} strokeWidth={1.75} color={t.accentFg} />
                <Metin tur="body" className="flex-1 font-medium" renk={t.accentFg}>
                  Sepeti gör · {adet} ürün
                </Metin>
                <Metin tur="body" className="font-medium" renk={t.accentFg} style={{ fontVariant: ['tabular-nums'] }}>
                  {tl(toplam)}
                </Metin>
              </View>
            )}
          </Pressable>
        </Animated.View>
      ) : null}

      <AltSayfa
        acik={sepetAcik}
        kapat={() => setSepetAcik(false)}
        tema={t}
        baslik="Sepetiniz"
        altKisim={
          adet > 0 ? (
            <Buton
              tema={t}
              baslik="WhatsApp’tan sipariş ver"
              onPress={() => {
                setSepetAcik(false);
                setSepet({});
                toast('Demo: gerçek uygulamada bu mesaj işletmenin WhatsApp’ına gider.', 'basari');
              }}
            />
          ) : (
            <Buton tema={t} tur="ikincil" baslik="Menüye dön" onPress={() => setSepetAcik(false)} />
          )
        }
      >
        {adet > 0 ? (
          <View className="gap-4">
            {satirlar.map((s) => (
              <View key={s.id} className="flex-row items-center gap-3">
                <Metin tur="body" className="flex-1" renk={t.fg}>
                  {s.adet} × {s.ad}
                </Metin>
                <Metin tur="body" renk={t.muted} style={{ fontVariant: ['tabular-nums'] }}>
                  {tl(s.adet * s.fiyat)}
                </Metin>
              </View>
            ))}
            <View className="flex-row justify-between pt-3" style={{ borderTopWidth: 1, borderColor: t.border }}>
              <Metin tur="h3" renk={t.fg}>
                Toplam
              </Metin>
              <Metin tur="h3" renk={t.fg} style={{ fontVariant: ['tabular-nums'] }}>
                {tl(toplam)}
              </Metin>
            </View>
            <View className="gap-1 rounded-md p-3" style={{ backgroundColor: t.surface }}>
              <Metin tur="small" renk={t.muted}>
                Gidecek mesaj
              </Metin>
              <Metin tur="small" renk={t.fg} selectable>
                {mesaj}
              </Metin>
            </View>
          </View>
        ) : (
          <Metin tur="body" renk={t.muted}>
            Sepetiniz boş. Menüden ürün ekleyin.
          </Metin>
        )}
      </AltSayfa>
    </View>
  );
}

function UrunSatiri({ ad, aciklama, fiyat, adet, tema, ayrac, degistir }: { ad: string; aciklama: string; fiyat: number; adet: number; tema: Palette; ayrac: boolean; degistir: (fark: number) => void }) {
  return (
    <View className="min-h-[76px] flex-row items-center gap-3 py-3" style={ayrac ? { borderTopWidth: 1, borderColor: tema.border } : undefined}>
      <View className="flex-1 gap-0.5">
        <Metin tur="body" className="font-medium" renk={tema.fg}>
          {ad}
        </Metin>
        <Metin tur="small" renk={tema.muted}>
          {aciklama}
        </Metin>
        <Metin tur="small" renk={tema.fg} style={{ fontVariant: ['tabular-nums'] }}>
          {tl(fiyat)}
        </Metin>
      </View>
      {adet > 0 ? (
        <View className="flex-row items-center gap-1 rounded-full" style={{ backgroundColor: tema.surface }}>
          <AdetButonu tema={tema} etiket={`${ad} azalt`} onPress={() => degistir(-1)} ikon={<Minus size={18} strokeWidth={1.75} color={tema.fg} />} />
          <Metin tur="body" className="w-6 text-center font-medium" renk={tema.fg} style={{ fontVariant: ['tabular-nums'] }}>
            {adet}
          </Metin>
          <AdetButonu tema={tema} etiket={`${ad} artır`} onPress={() => degistir(1)} ikon={<Plus size={18} strokeWidth={1.75} color={tema.fg} />} />
        </View>
      ) : (
        <Pressable accessibilityRole="button" accessibilityLabel={`${ad} sepete ekle`} onPress={() => degistir(1)}>
          {({ pressed }) => (
            <View className="h-11 flex-row items-center gap-1 rounded-full px-4" style={{ backgroundColor: pressed ? tema.accentPressed : tema.accent }}>
              <Plus size={16} strokeWidth={1.75} color={tema.accentFg} />
              <Metin tur="small" renk={tema.accentFg}>
                Ekle
              </Metin>
            </View>
          )}
        </Pressable>
      )}
    </View>
  );
}

function AdetButonu({ tema, etiket, onPress, ikon }: { tema: Palette; etiket: string; onPress: () => void; ikon: React.ReactNode }) {
  return (
    <Pressable accessibilityRole="button" accessibilityLabel={etiket} onPress={onPress} hitSlop={4}>
      {({ pressed }) => (
        <View className="h-11 w-11 items-center justify-center rounded-full" style={{ backgroundColor: pressed ? tema.border : 'transparent' }}>
          {ikon}
        </View>
      )}
    </Pressable>
  );
}
