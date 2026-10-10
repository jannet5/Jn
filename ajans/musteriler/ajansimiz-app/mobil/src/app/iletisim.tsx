import { Mail, MessageCircle, RotateCcw } from 'lucide-react-native';
import { useState } from 'react';
import { Linking, ScrollView, View } from 'react-native';

import { OnaySayfasi } from '@/components/alt-sayfa';
import { Buton } from '@/components/buton';
import { useKatman } from '@/components/katman';
import { Metin } from '@/components/metin';
import { demoyuSifirla } from '@/lib/kalici';
import { whatsappAc } from '@/lib/whatsapp';
import { siteConfig } from '@/site.config';
import { useTema } from '@/theme/tokens';

const NELER = [
  { baslik: 'Uygulama ve web sitesi', metin: 'Kendi renkleriniz, menünüz, randevu ya da sadakat sisteminizle.' },
  { baslik: 'Günler içinde ilk sürüm', metin: 'Telefonunuzda deneyebileceğiniz önizlemeyle ilerleriz.' },
  { baslik: 'Her ay değer defteri', metin: 'Ne değişti, kaç kişi geldi; sade bir raporla.' },
];

export default function IletisimEkrani() {
  const t = useTema();
  const { toast } = useKatman();
  const [sifirlaAcik, setSifirlaAcik] = useState(false);

  return (
    <ScrollView contentInsetAdjustmentBehavior="automatic" style={{ backgroundColor: t.bg }} contentContainerClassName="gap-10 px-5 pb-12 pt-4">
      <View className="gap-3">
        <Metin tur="h2" renk={t.fg} accessibilityRole="header">
          Sizin uygulamanızı birlikte çizelim.
        </Metin>
        <Metin tur="lead" renk={t.muted}>
          Önce ücretsiz dijital check-up: sitenize, Google kaydınıza ve Instagram’ınıza bakıp tek sayfalık rapor gönderiyoruz.
        </Metin>
      </View>

      <View className="gap-3">
        <Buton
          tema={t}
          baslik="WhatsApp’tan yaz"
          ikon={<MessageCircle size={18} strokeWidth={1.75} color={t.accentFg} />}
          onPress={async () => {
            const acildi = await whatsappAc(siteConfig.whatsapp, siteConfig.checkupMesaji);
            if (!acildi) toast(siteConfig.whatsapp ? 'WhatsApp açılamadı. Uygulama yüklü mü?' : 'WhatsApp numarası henüz eklenmedi (site.config).', 'uyari');
          }}
        />
        <Buton
          tema={t}
          tur="ikincil"
          baslik="E-posta gönder"
          ikon={<Mail size={18} strokeWidth={1.75} color={t.fg} />}
          onPress={() => {
            if (!siteConfig.eposta) return toast('E-posta adresi henüz eklenmedi (site.config).', 'uyari');
            Linking.openURL(`mailto:${siteConfig.eposta}`).catch(() => toast('E-posta uygulaması açılamadı.', 'uyari'));
          }}
        />
      </View>

      <View className="gap-5">
        <Metin tur="small" renk={t.muted} accessibilityRole="header">
          Neler yapıyoruz
        </Metin>
        {NELER.map((n, i) => (
          <View key={n.baslik} className="flex-row gap-4">
            <Metin tur="h3" renk={t.accent} style={{ fontVariant: ['tabular-nums'] }}>
              {i + 1}
            </Metin>
            <View className="flex-1 gap-1">
              <Metin tur="body" className="font-medium" renk={t.fg}>
                {n.baslik}
              </Metin>
              <Metin tur="body" renk={t.muted}>
                {n.metin}
              </Metin>
            </View>
          </View>
        ))}
      </View>

      <View className="gap-2 pt-2" style={{ borderTopWidth: 1, borderColor: t.border }}>
        <Metin tur="small" renk={t.muted} className="pt-4">
          Sunum öncesi
        </Metin>
        <Buton tema={t} tur="ikincil" baslik="Demoyu sıfırla" ikon={<RotateCcw size={18} strokeWidth={1.75} color={t.fg} />} onPress={() => setSifirlaAcik(true)} />
      </View>

      <OnaySayfasi
        acik={sifirlaAcik}
        kapat={() => setSifirlaAcik(false)}
        tema={t}
        baslik="Demo sıfırlansın mı?"
        aciklama="Damgalar, randevu ve sepet temizlenir. Bir sonraki görüşmeye temiz başlarsınız."
        onayMetni="Sıfırla"
        onayla={async () => {
          await demoyuSifirla();
          toast('Demo sıfırlandı.', 'basari');
        }}
      />
    </ScrollView>
  );
}
