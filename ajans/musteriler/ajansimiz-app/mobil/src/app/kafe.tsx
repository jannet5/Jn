import { Coffee, Gift, RotateCcw } from 'lucide-react-native';
import { useEffect, useRef, useState } from 'react';
import { ScrollView, View } from 'react-native';
import Animated, {
  useAnimatedStyle,
  useReducedMotion,
  useSharedValue,
  withDelay,
  withSequence,
  withSpring,
  withTiming,
  ZoomIn,
} from 'react-native-reanimated';

import { OnaySayfasi } from '@/components/alt-sayfa';
import { Buton } from '@/components/buton';
import { useKatman } from '@/components/katman';
import { KonseptBaslik } from '@/components/konsept-baslik';
import { Metin } from '@/components/metin';
import { OrnekEtiket } from '@/components/ornek-etiket';
import { KAFE } from '@/data/ornek';
import { haptik } from '@/lib/haptik';
import { useKalici } from '@/lib/kalici';
import { useTema, type Palette } from '@/theme/tokens';

export default function KafeEkrani() {
  const t = useTema('kafe');
  const { toast } = useKatman();
  const [durum, setDurum, hazir] = useKalici('demo:kafe', { damga: 0, hediye: 0 });
  const [sifirlaAcik, setSifirlaAcik] = useState(false);
  const [dalga, setDalga] = useState(0);
  const dolu = durum.damga >= KAFE.hedef;
  const kalan = KAFE.hedef - durum.damga;

  const damgaEkle = () => {
    if (dolu) return;
    const yeni = durum.damga + 1;
    setDurum({ ...durum, damga: yeni });
    if (yeni === KAFE.hedef) {
      setDalga((d) => d + 1);
      toast('Tebrikler! 10. kahve bizden.', 'basari');
    } else {
      haptik.orta();
    }
  };

  const hediyeyiKullan = () => {
    setDurum({ damga: 0, hediye: durum.hediye + 1 });
    toast('Afiyet olsun. Kart yeniden başladı.', 'basari');
  };

  return (
    <ScrollView contentInsetAdjustmentBehavior="automatic" style={{ backgroundColor: t.bg }} contentContainerClassName="gap-8 px-5 pb-12 pt-4">
      <KonseptBaslik baslik="Sadakat kartı" tema={t} />
      <View className="gap-3">
        <OrnekEtiket tema={t} />
        <Metin tur="h2" renk={t.fg} accessibilityRole="header">
          {KAFE.ad}
        </Metin>
      </View>

      <View className="gap-6 rounded-lg p-5" style={{ backgroundColor: t.surface, borderCurve: 'continuous' }}>
        <View className="flex-row items-end justify-between">
          <View className="gap-1">
            <Metin tur="small" renk={t.muted}>
              Damgalarınız
            </Metin>
            <Metin tur="display" renk={t.fg} style={{ fontVariant: ['tabular-nums'] }} accessibilityLabel={`${durum.damga} / ${KAFE.hedef} damga`}>
              {hazir ? durum.damga : '–'}
              <Metin tur="h3" renk={t.muted}>
                {' '}/ {KAFE.hedef}
              </Metin>
            </Metin>
          </View>
          {durum.hediye > 0 ? (
            <Metin tur="small" renk={t.muted}>
              Alınan hediye: {durum.hediye}
            </Metin>
          ) : null}
        </View>

        <View className="flex-row flex-wrap justify-between gap-y-3" accessibilityRole="progressbar" accessibilityValue={{ min: 0, max: KAFE.hedef, now: durum.damga }}>
          {Array.from({ length: KAFE.hedef }, (_, i) => (
            <Damga key={i} sira={i} dolu={i < durum.damga} son={i === KAFE.hedef - 1} tema={t} dalga={dalga} />
          ))}
        </View>

        {dolu ? (
          <Animated.View entering={ZoomIn.springify().damping(14)} className="flex-row items-center gap-3 rounded-md p-4" style={{ backgroundColor: t.accent }}>
            <Gift size={24} strokeWidth={1.75} color={t.accentFg} />
            <View className="flex-1">
              <Metin tur="h3" renk={t.accentFg}>
                Bedava kahve hazır
              </Metin>
              <Metin tur="small" renk={t.accentFg}>
                Kasada bu ekranı gösterin.
              </Metin>
            </View>
          </Animated.View>
        ) : (
          <Metin tur="body" renk={t.muted}>
            {durum.damga === 0 ? 'İlk kahvenizde ilk damga. 10. kahve bizden.' : kalan === 1 ? 'Bir damga daha, sonraki kahve bizden.' : `Hediye kahveye ${kalan} damga kaldı.`}
          </Metin>
        )}
      </View>

      <View className="gap-3">
        {dolu ? (
          <Buton tema={t} baslik="Hediyeyi kullan" onPress={hediyeyiKullan} ikon={<Gift size={18} strokeWidth={1.75} color={t.accentFg} />} />
        ) : (
          <Buton tema={t} baslik="Damga ekle" onPress={damgaEkle} devreDisi={!hazir} erisimIpucu="Kasada kahve alınca bir damga eklenir" ikon={<Coffee size={18} strokeWidth={1.75} color={t.accentFg} />} />
        )}
        <Buton tema={t} tur="ikincil" baslik="Kartı sıfırla" devreDisi={durum.damga === 0} onPress={() => setSifirlaAcik(true)} ikon={<RotateCcw size={18} strokeWidth={1.75} color={t.fg} />} />
        <Metin tur="small" renk={t.muted} className="pt-2">
          Gerçek uygulamada damgayı kasadaki QR ekler; burada denemek için butonla ekleniyor.
        </Metin>
      </View>

      <OnaySayfasi
        acik={sifirlaAcik}
        kapat={() => setSifirlaAcik(false)}
        tema={t}
        baslik="Kart sıfırlansın mı?"
        aciklama={`${durum.damga} damga silinecek. Bu demo içindir, gerçek kartta müşteri kendi kartını silemez.`}
        onayMetni="Sıfırla"
        onayla={() => {
          setDurum({ damga: 0, hediye: durum.hediye });
          toast('Kart sıfırlandı.');
        }}
      />
    </ScrollView>
  );
}

function Damga({ sira, dolu, son, tema, dalga }: { sira: number; dolu: boolean; son: boolean; tema: Palette; dalga: number }) {
  const azHareket = useReducedMotion();
  const olcek = useSharedValue(1);
  const y = useSharedValue(0);
  const onceki = useRef(dolu);

  // Yeni basılan damga: pop
  useEffect(() => {
    if (dolu && !onceki.current && !azHareket) olcek.set(withSequence(withTiming(1.25, { duration: 120 }), withSpring(1, { damping: 10, stiffness: 260 })));
    onceki.current = dolu;
  }, [dolu, azHareket, olcek]);

  // 10. damga: dalga gibi zıplama
  useEffect(() => {
    if (dalga === 0 || azHareket) return;
    y.set(withDelay(sira * 45, withSequence(withTiming(-12, { duration: 140 }), withSpring(0, { damping: 8, stiffness: 240 }))));
  }, [dalga, sira, azHareket, y]);

  const stil = useAnimatedStyle(() => ({ transform: [{ scale: olcek.get() }, { translateY: y.get() }] }));
  return (
    <Animated.View
      style={[stil, { width: '18%', aspectRatio: 1, backgroundColor: dolu ? tema.accent : 'transparent', borderColor: dolu ? tema.accent : tema.border, borderWidth: 2, borderStyle: dolu ? 'solid' : 'dashed' }]}
      className="items-center justify-center rounded-full"
    >
      {son ? <Gift size={20} strokeWidth={1.75} color={dolu ? tema.accentFg : tema.muted} /> : dolu ? <Coffee size={20} strokeWidth={1.75} color={tema.accentFg} /> : null}
    </Animated.View>
  );
}
