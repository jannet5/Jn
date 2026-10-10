import { Bell, CalendarCheck, Scissors } from 'lucide-react-native';
import { useMemo, useState } from 'react';
import { Pressable, ScrollView, Switch, View } from 'react-native';
import Animated, { FadeIn, LinearTransition } from 'react-native-reanimated';

import { AltSayfa, OnaySayfasi } from '@/components/alt-sayfa';
import { Buton } from '@/components/buton';
import { useKatman } from '@/components/katman';
import { KonseptBaslik } from '@/components/konsept-baslik';
import { Metin } from '@/components/metin';
import { OrnekEtiket } from '@/components/ornek-etiket';
import { BERBER, tl } from '@/data/ornek';
import { haptik } from '@/lib/haptik';
import { useKalici } from '@/lib/kalici';
import { useTema, type Palette } from '@/theme/tokens';

type Randevu = { gun: string; saat: string; hizmet: string; hatirlat: boolean };

const GUN_KISA = ['Paz', 'Pzt', 'Sal', 'Çar', 'Per', 'Cum', 'Cmt'];
const AYLAR = ['Oca', 'Şub', 'Mar', 'Nis', 'May', 'Haz', 'Tem', 'Ağu', 'Eyl', 'Eki', 'Kas', 'Ara'];

function gunler() {
  const bugun = new Date();
  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(bugun.getFullYear(), bugun.getMonth(), bugun.getDate() + i);
    return { anahtar: d.toISOString().slice(0, 10), kisa: i === 0 ? 'Bugün' : GUN_KISA[d.getDay()], gun: d.getDate(), uzun: `${d.getDate()} ${AYLAR[d.getMonth()]} ${GUN_KISA[d.getDay()]}`, sira: i };
  });
}

// Demo: her gün farklı birkaç saat dolu görünsün (deterministik).
const doluMu = (gunSira: number, saatSira: number) => (gunSira * 3 + saatSira * 5) % 7 === 0;

export default function BerberEkrani() {
  const t = useTema('berber');
  const { toast } = useKatman();
  const gunListesi = useMemo(() => gunler(), []);
  const [randevu, setRandevu, hazir] = useKalici<Randevu | null>('demo:berber', null);
  const [gun, setGun] = useState(gunListesi[0]);
  const [saat, setSaat] = useState<string | null>(null);
  const [hizmetId, setHizmetId] = useState(BERBER.hizmetler[0].id);
  const [onayAcik, setOnayAcik] = useState(false);
  const [iptalAcik, setIptalAcik] = useState(false);
  const hizmet = BERBER.hizmetler.find((h) => h.id === hizmetId) ?? BERBER.hizmetler[0];

  const onayla = () => {
    if (!saat) return;
    setRandevu({ gun: gun.uzun, saat, hizmet: hizmet.ad, hatirlat: true });
    setOnayAcik(false);
    setSaat(null);
    toast(`Randevunuz alındı: ${gun.uzun}, ${saat}`, 'basari');
  };

  return (
    <ScrollView contentInsetAdjustmentBehavior="automatic" style={{ backgroundColor: t.bg }} contentContainerClassName="gap-8 px-5 pb-12 pt-4">
      <KonseptBaslik baslik="Randevu" tema={t} />
      <View className="gap-3">
        <OrnekEtiket tema={t} />
        <Metin tur="h2" renk={t.fg} accessibilityRole="header">
          {BERBER.ad}
        </Metin>
      </View>

      {hazir && randevu ? (
        <Animated.View entering={FadeIn.duration(220)} layout={LinearTransition} className="gap-4 rounded-lg p-5" style={{ backgroundColor: t.surface, borderCurve: 'continuous' }}>
          <View className="flex-row items-center gap-3">
            <CalendarCheck size={22} strokeWidth={1.75} color={t.fg} />
            <Metin tur="h3" renk={t.fg} className="flex-1">
              {randevu.gun} · {randevu.saat}
            </Metin>
          </View>
          <Metin tur="body" renk={t.muted}>
            {randevu.hizmet}
          </Metin>
          <View className="flex-row items-center justify-between gap-3">
            <View className="flex-1 flex-row items-center gap-2">
              <Bell size={18} strokeWidth={1.75} color={t.muted} />
              <Metin tur="body" renk={t.fg}>
                1 saat önce hatırlat
              </Metin>
            </View>
            <Switch
              accessibilityLabel="1 saat önce hatırlat"
              value={randevu.hatirlat}
              trackColor={{ true: t.accent, false: t.border }}
              thumbColor={t.bg}
              onValueChange={(v) => {
                setRandevu({ ...randevu, hatirlat: v });
                toast(v ? 'Hatırlatma açık: 1 saat önce bildirim gelir.' : 'Hatırlatma kapatıldı.');
              }}
            />
          </View>
          <Buton tema={t} tur="ikincil" baslik="Randevuyu iptal et" onPress={() => setIptalAcik(true)} />
        </Animated.View>
      ) : null}

      <Bolum baslik="Hizmet" tema={t}>
        <View className="gap-2">
          {BERBER.hizmetler.map((h) => {
            const secili = h.id === hizmetId;
            return (
              <Pressable
                key={h.id}
                accessibilityRole="radio"
                accessibilityState={{ selected: secili }}
                onPress={() => {
                  haptik.secim();
                  setHizmetId(h.id);
                }}
              >
                {({ pressed }) => (
                  <View
                    className="min-h-[56px] flex-row items-center gap-3 rounded-md px-4"
                    style={{ backgroundColor: secili ? t.accent : pressed ? t.border : t.surface, borderCurve: 'continuous' }}
                  >
                    <Scissors size={18} strokeWidth={1.75} color={secili ? t.accentFg : t.muted} />
                    <Metin tur="body" className="flex-1 font-medium" renk={secili ? t.accentFg : t.fg}>
                      {h.ad}
                    </Metin>
                    <Metin tur="small" renk={secili ? t.accentFg : t.muted}>
                      {h.sure} dk · {tl(h.fiyat)}
                    </Metin>
                  </View>
                )}
              </Pressable>
            );
          })}
        </View>
      </Bolum>

      <Bolum baslik="Gün" tema={t}>
        <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerClassName="gap-2" style={{ marginHorizontal: -20 }} contentContainerStyle={{ paddingHorizontal: 20 }}>
          {gunListesi.map((g) => {
            const secili = g.anahtar === gun.anahtar;
            return (
              <Pressable
                key={g.anahtar}
                accessibilityRole="radio"
                accessibilityState={{ selected: secili }}
                accessibilityLabel={g.uzun}
                onPress={() => {
                  haptik.secim();
                  setGun(g);
                  setSaat(null);
                }}
              >
                {({ pressed }) => (
                  <View className="h-[72px] w-[60px] items-center justify-center gap-1 rounded-md" style={{ backgroundColor: secili ? t.accent : pressed ? t.border : t.surface, borderCurve: 'continuous' }}>
                    <Metin tur="small" renk={secili ? t.accentFg : t.muted}>
                      {g.kisa}
                    </Metin>
                    <Metin tur="h3" renk={secili ? t.accentFg : t.fg} style={{ fontVariant: ['tabular-nums'] }}>
                      {g.gun}
                    </Metin>
                  </View>
                )}
              </Pressable>
            );
          })}
        </ScrollView>
      </Bolum>

      <Bolum baslik="Saat" tema={t}>
        <View className="flex-row flex-wrap gap-2">
          {BERBER.saatler.map((s, i) => {
            const dolu = doluMu(gun.sira, i);
            const secili = s === saat;
            return (
              <Pressable
                key={s}
                disabled={dolu}
                accessibilityRole="radio"
                accessibilityState={{ selected: secili, disabled: dolu }}
                accessibilityLabel={dolu ? `${s} dolu` : s}
                onPress={() => {
                  haptik.secim();
                  setSaat(s);
                }}
                style={{ width: '23%' }}
              >
                {({ pressed }) => (
                  <View className="h-11 items-center justify-center rounded-sm" style={{ backgroundColor: secili ? t.accent : pressed ? t.border : t.surface, opacity: dolu ? 0.4 : 1 }}>
                    <Metin tur="small" renk={secili ? t.accentFg : t.fg} style={{ fontVariant: ['tabular-nums'], textDecorationLine: dolu ? 'line-through' : 'none' }}>
                      {s}
                    </Metin>
                  </View>
                )}
              </Pressable>
            );
          })}
        </View>
      </Bolum>

      <View className="gap-2">
        <Buton tema={t} baslik={saat ? `${gun.kisa} ${saat} için randevu al` : 'Randevu al'} devreDisi={!saat} onPress={() => setOnayAcik(true)} />
        {!saat ? (
          <Metin tur="small" renk={t.muted} className="text-center">
            Önce boş bir saat seçin.
          </Metin>
        ) : null}
      </View>

      <AltSayfa
        acik={onayAcik}
        kapat={() => setOnayAcik(false)}
        tema={t}
        baslik="Randevuyu onaylayın"
        altKisim={
          <>
            <Buton tema={t} baslik="Onayla" onPress={onayla} />
            <Buton tema={t} tur="ikincil" baslik="Değiştir" onPress={() => setOnayAcik(false)} />
          </>
        }
      >
        <View className="gap-3 rounded-md p-4" style={{ backgroundColor: t.surface }}>
          <Satir etiket="Gün" deger={gun.uzun} tema={t} />
          <Satir etiket="Saat" deger={saat ?? '–'} tema={t} />
          <Satir etiket="Hizmet" deger={`${hizmet.ad} · ${hizmet.sure} dk`} tema={t} />
          <Satir etiket="Ücret" deger={tl(hizmet.fiyat)} tema={t} />
        </View>
      </AltSayfa>

      <OnaySayfasi
        acik={iptalAcik}
        kapat={() => setIptalAcik(false)}
        tema={t}
        baslik="Randevu iptal edilsin mi?"
        aciklama={randevu ? `${randevu.gun}, ${randevu.saat} randevunuz silinecek.` : ''}
        onayMetni="İptal et"
        onayla={() => {
          setRandevu(null);
          toast('Randevu iptal edildi.');
        }}
      />
    </ScrollView>
  );
}

function Bolum({ baslik, tema, children }: { baslik: string; tema: Palette; children: React.ReactNode }) {
  return (
    <View className="gap-3">
      <Metin tur="small" renk={tema.muted} accessibilityRole="header">
        {baslik}
      </Metin>
      {children}
    </View>
  );
}

function Satir({ etiket, deger, tema }: { etiket: string; deger: string; tema: Palette }) {
  return (
    <View className="flex-row justify-between gap-3">
      <Metin tur="body" renk={tema.muted}>
        {etiket}
      </Metin>
      <Metin tur="body" className="font-medium" renk={tema.fg} selectable>
        {deger}
      </Metin>
    </View>
  );
}
