// Böcek Kapıştırma oyunu - 20 konsept kartı üretici.
// Her kart tek görselde: maskot, mekan/ambiyans, rakip (böcek kapıştıran),
// 5 slotlu arena + kapanan yaprak, sis içinde bağıran böcek seyirciler, hikaye girişi.
// Kullanım: node generate.js  -> concepts.html + png/konsept-XX.png
const fs = require('fs');
const path = require('path');

const W = 1280, H = 800;

const CONCEPTS = [
  { title: 'Mantar Vadisi', mascot: { name: 'Fındık', who: 'tilki izci', ears: 'fox', fur: '#e07a2f', belly: '#fff2df', hat: 'scout', hatC: '#3f6b35' },
    rival: { name: 'Gırtlak Usta', who: 'yaşlı kurbağa', ears: 'none', fur: '#6aa84f', belly: '#d9ead3', hat: 'top', hatC: '#4a2b5c' },
    place: 'Dev mantarların altında, ateşböceği ışıklı bir alacakaranlık vadisi', sky: ['#2b1d4f', '#d86d6d'], ground: '#4b3a2a', motif: 'mushrooms',
    hook: 'Kayıp pusulasının peşindeki Fındık, mantar ışıkları altında böcek ringi kuran Gırtlak Usta ile karşılaşır.',
    line: 'Yaprağın altında ne kaldı, hatırlıyor musun evlat?' },
  { title: 'Gece Vahası', mascot: { name: 'Kumru', who: 'gözlüklü çöl kaşifi kız', ears: 'human', fur: '#c98b5a', belly: '#f3d2b3', hat: 'goggles', hatC: '#7a4a1e' },
    rival: { name: 'Bukalemun Tüccar', who: 'renk değiştiren tüccar', ears: 'none', fur: '#3fae9a', belly: '#bdeee2', hat: 'fez', hatC: '#b8322c' },
    place: 'Hurma ağaçlı bir vaha, dolunay ve kum tepeleri', sky: ['#0d1b3d', '#3c5a8a'], ground: '#c9a46a', motif: 'palms',
    hook: 'Su arayan Kumru, vahada böcek pazarı kuran Bukalemun Tüccar\'a takılır: "Kazanırsan su senin."',
    line: 'Kum kadar böcek, ama hangisi hangisini yener?' },
  { title: 'Fener Bahçesi', mascot: { name: 'Tofu', who: 'panda çırak', ears: 'round', fur: '#f4f4f4', belly: '#ffffff', patch: '#222', hat: 'band', hatC: '#d23b3b' },
    rival: { name: 'Usta Kerpeten', who: 'kılıçlı kurbağa ustası', ears: 'none', fur: '#7fb069', belly: '#e6f2d8', hat: 'straw', hatC: '#d9b45a' },
    place: 'Kağıt fenerli, köprülü ve sazanlı gece bahçesi', sky: ['#1a1030', '#5a2f5f'], ground: '#3d4f3a', motif: 'lanterns',
    hook: 'Fener festivalinde Tofu, ustasının efsane böcek turnuvasına çırak olarak katılır.',
    line: 'Sabır, Tofu. Önce gör, sonra unutma.' },
  { title: 'Karlı Zirve', mascot: { name: 'Pofuduk', who: 'yavru yeti', ears: 'round', fur: '#e9f1f7', belly: '#ffffff', hat: 'beanie', hatC: '#e0533a' },
    rival: { name: 'Komodor Pengu', who: 'madalyalı penguen', ears: 'none', fur: '#1f2a36', belly: '#f2f2f2', hat: 'captain', hatC: '#1d3557' },
    place: 'Kar fırtınası sonrası buzdan bir arena, kuzey ışıkları', sky: ['#0b2239', '#1f8a85'], ground: '#dfeaf2', motif: 'mountains',
    hook: 'Kışın uyanan buz böcekleri için Pofuduk, Komodor Pengu\'nun buz ringine meydan okur.',
    line: 'Buz böceği soğuğa dayanır ama ateş böceğine asla!' },
  { title: 'Kristal Mağara', mascot: { name: 'Burgu', who: 'madenci köstebek', ears: 'round', fur: '#6b4f3a', belly: '#c9a98a', hat: 'miner', hatC: '#f2c230' },
    rival: { name: 'DJ Yarasa', who: 'kulaklıklı yarasa', ears: 'bat', fur: '#3b2f4a', belly: '#7d6b94', hat: 'headphones', hatC: '#ff3d9a' },
    place: 'Parlayan kristallerle dolu yeraltı mağarası, yankılı tezahürat', sky: ['#120a24', '#2a1a4a'], ground: '#2c2238', motif: 'crystals',
    hook: 'Tünel kazarken bir mağaraya düşen Burgu, DJ Yarasa\'nın yeraltı böcek ligini keşfeder.',
    line: 'Bu mağarada ses yankılanır, hafızan yankılanmaz!' },
  { title: 'Sisli Bataklık', mascot: { name: 'Nil', who: 'sandalcı kız', ears: 'human', fur: '#8d5a3b', belly: '#e8b98f', hat: 'bandana', hatC: '#2f7d6d' },
    rival: { name: 'Dişlek', who: 'kurnaz timsah', ears: 'none', fur: '#4f7d3a', belly: '#c7d99a', hat: 'top', hatC: '#222' },
    place: 'Sazlıklar, ağaç kökleri ve yeşil sis arasında bir iskele', sky: ['#1d2b24', '#6d8f6a'], ground: '#3a4a33', motif: 'reeds',
    hook: 'Nil\'in sandalı Dişlek\'in iskelesine sürüklenir. Geri almak için böcek düellosu şart.',
    line: 'Sis yaprağı da yutar, aklını da evlat...' },
  { title: 'Yörünge Arenası', mascot: { name: 'Miyu', who: 'astronot kedi', ears: 'cat', fur: '#f0a04b', belly: '#ffe0b8', hat: 'helmet', hatC: '#cfe8ff' },
    rival: { name: 'BZZ-9', who: 'böcek toplayan robot', ears: 'antenna', fur: '#9aa7b4', belly: '#dfe6ec', hat: 'none', hatC: '#000' },
    place: 'Uzay istasyonunda cam kubbeli böcek serası, dışarıda gezegen', sky: ['#05051a', '#1d1f4a'], ground: '#3a3f55', motif: 'space',
    hook: 'Uzay serasından kaçan böcekleri toplayan Miyu, BZZ-9\'un turnuva protokolüne girer.',
    line: 'HAFIZA TESTİ BAŞLIYOR. YAPRAK KAPANIYOR. BİP.' },
  { title: 'Volkan Adası', mascot: { name: 'Çakıl', who: 'kertenkele kaşif', ears: 'none', fur: '#58b368', belly: '#d8f0c2', hat: 'safari', hatC: '#c7a26b' },
    rival: { name: 'Magma Dede', who: 'taş golem', ears: 'none', fur: '#5a5050', belly: '#ff7a2a', hat: 'none', hatC: '#000' },
    place: 'Dumanı tüten volkanın eteğinde siyah kumlu sahil', sky: ['#2a0e0e', '#c4471b'], ground: '#2b2424', motif: 'volcano',
    hook: 'Gemisi karaya oturan Çakıl, adanın bekçisi Magma Dede\'nin lav ringinde sınanır.',
    line: 'Ada sıcaktır, böcekler daha da sıcak!' },
  { title: 'Korsan Güvertesi', mascot: { name: 'Çaça', who: 'miço maymun', ears: 'monkey', fur: '#8b5a2b', belly: '#e8c39e', hat: 'bandana', hatC: '#c0392b' },
    rival: { name: 'Kaptan Tekbacak', who: 'tahta bacaklı papağan kaptan', ears: 'none', fur: '#2ea84a', belly: '#f5d33b', hat: 'pirate', hatC: '#1b1b1b' },
    place: 'Fırtınalı denizde sallanan korsan gemisinin güvertesi', sky: ['#14213d', '#4f6d8f'], ground: '#7a5230', motif: 'ship',
    hook: 'Gemide hazine haritası yerine böcek kavanozları çıkar. Kaptan: "Haritayı kazanırsın!"',
    line: 'Yo-ho! Yaprak kapandı, şimdi hafızanı göster miço!' },
  { title: 'Şekerleme Pazarı', mascot: { name: 'Lokum', who: 'fırıncı tavşan', ears: 'bunny', fur: '#f7d7e3', belly: '#ffffff', hat: 'chef', hatC: '#ffffff' },
    rival: { name: 'Madam Bal', who: 'arıcı ayı', ears: 'round', fur: '#b5763c', belly: '#f2cf8f', hat: 'beekeeper', hatC: '#f5f0dc' },
    place: 'Şeker tezgahlı, bayraklı rengarenk bir sokak pazarı', sky: ['#ffb4a2', '#ffe5b4'], ground: '#d9a066', motif: 'candy',
    hook: 'Pastasına üşüşen böcekleri kovalayan Lokum, Madam Bal\'ın tatlı böcek yarışmasına düşer.',
    line: 'Tatlı mı acı mı kazanır? Bak ve unutma tatlım!' },
  { title: 'Ağaç Ev Köyü', mascot: { name: 'Fıstık', who: 'sincap çocuk', ears: 'round', fur: '#c46a2b', belly: '#f3d1a8', hat: 'cap', hatC: '#2d6cdf' },
    rival: { name: 'Hakem Bilge', who: 'gözlüklü baykuş', ears: 'owl', fur: '#7a6650', belly: '#e0d2bd', hat: 'graduate', hatC: '#222' },
    place: 'Halat köprülü ağaç evler, ip ışıkları, akşam serinliği', sky: ['#29335c', '#f3a712'], ground: '#4c6b3c', motif: 'treehouses',
    hook: 'Köyün yıllık böcek şampiyonası! Fıstık ilk kez çağrılır, hakem Bilge kuralları anlatır.',
    line: 'Hu-huu! Kural basit: gördüğünü unutan kaybeder.' },
  { title: 'Mercan Resifi', mascot: { name: 'Dalgıç Deniz', who: 'dalgıç su samuru', ears: 'round', fur: '#7b5a44', belly: '#d9c0a5', hat: 'diving', hatC: '#ffd166' },
    rival: { name: 'Sekizkol', who: 'gösterişçi ahtapot', ears: 'none', fur: '#c2457a', belly: '#f2a7c5', hat: 'top', hatC: '#222' },
    place: 'Batık bir hava kubbesi içinde mercanlarla çevrili arena', sky: ['#023047', '#219ebc'], ground: '#e9c46a', motif: 'coral',
    hook: 'Batık gemiyi keşfeden Deniz, hava kubbesinde böcek gösterisi yapan Sekizkol\'u bulur.',
    line: 'Sekiz kolum var ama hafızam tek. Ya seninki?' },
  { title: 'Gece Sirki', mascot: { name: 'Pıtırcık', who: 'palyaço kız', ears: 'human', fur: '#f1c27d', belly: '#ffe1bd', hat: 'jester', hatC: '#e63946' },
    rival: { name: 'Büyücü Zıpzıp', who: 'sihirbaz tavşan', ears: 'bunny', fur: '#dcdcdc', belly: '#ffffff', hat: 'top', hatC: '#3a0ca3' },
    place: 'Çizgili sirk çadırı, spot ışıkları ve konfeti', sky: ['#240046', '#7b2cbf'], ground: '#9d0208', motif: 'circus',
    hook: 'Sirkin yeni yıldızı aranıyor: Zıpzıp şapkadan böcek çıkarır, Pıtırcık hafızasıyla yarışır.',
    line: 'Abra-kadabra! Yaprak kapandı... böcekler nerede?' },
  { title: 'Savan Kampı', mascot: { name: 'Zürafa Zeki', who: 'fotoğrafçı zürafa', ears: 'giraffe', fur: '#f4c25b', belly: '#fff0c9', hat: 'safari', hatC: '#a68a5b' },
    rival: { name: 'Aslan Paşa', who: 'emekli aslan', ears: 'round', fur: '#d9a441', belly: '#f6dfa8', mane: '#8c4a1c', hat: 'none', hatC: '#000' },
    place: 'Akasya ağaçlı savan, gün batımında kamp ateşi', sky: ['#4a1942', '#f28f3b'], ground: '#b5874a', motif: 'acacia',
    hook: 'Nadir böcek fotoğrafı arayan Zeki, Aslan Paşa\'nın kamp ateşi turnuvasına davet edilir.',
    line: 'Savanda en güçlü değil, en iyi hatırlayan kazanır.' },
  { title: 'Neon Çatı', mascot: { name: 'Kaykay Kaan', who: 'kaykaycı çocuk', ears: 'human', fur: '#a0673f', belly: '#e0aa7f', hat: 'cap', hatC: '#00f5d4' },
    rival: { name: 'Rakun Ringo', who: 'çatı rakunu', ears: 'round', fur: '#7a7a7a', belly: '#d0d0d0', patch: '#333', hat: 'hoodie', hatC: '#f15bb5' },
    place: 'Neon tabelalı şehir çatısı, yağmur sonrası parıltı', sky: ['#0f0c29', '#302b63'], ground: '#24243e', motif: 'neon',
    hook: 'Kaybolan kaykayını arayan Kaan, gizli çatı böcek ligine kafayı uydurur.',
    line: 'Çatıda kural yok, sadece hafıza var kanka!' },
  { title: 'Ahır Fuarı', mascot: { name: 'Papatya', who: 'çiftçi kız', ears: 'human', fur: '#f0c8a0', belly: '#ffe3c7', hat: 'straw', hatC: '#e9c46a' },
    rival: { name: 'Horoz Ağa', who: 'gururlu horoz', ears: 'comb', fur: '#f5f0e6', belly: '#ffffff', hat: 'none', hatC: '#000' },
    place: 'Kırmızı ahır, saman balyaları ve köy fuarı bayrakları', sky: ['#8ecae6', '#ffe8a3'], ground: '#a7c957', motif: 'barn',
    hook: 'Köy fuarında Horoz Ağa her yıl kazanır. Bu yıl Papatya bahçe böcekleriyle sahnede.',
    line: 'Ü-ürü-üü! Yaprak kapanınca herkes tavuk gibi şaşırır!' },
  { title: 'Bambu Sisi', mascot: { name: 'Kızıl', who: 'kırmızı panda', ears: 'round', fur: '#c1440e', belly: '#2b1a12', hat: 'band', hatC: '#f2e8cf' },
    rival: { name: 'Bilge Maymun', who: 'yaşlı kar maymunu', ears: 'monkey', fur: '#b9a68f', belly: '#f2a08b', hat: 'straw', hatC: '#c9a227' },
    place: 'Sisli bambu ormanında taş basamaklı bir tapınak avlusu', sky: ['#d8e2dc', '#9cc5a1'], ground: '#5b6b4f', motif: 'bamboo',
    hook: 'Bilge Maymun öğrenci kabul eder: sınav, sisin içindeki böcekleri hatırlamaktır.',
    line: 'Göz görür, yaprak örter, akıl hatırlar.' },
  { title: 'Kütüphane Tavanı', mascot: { name: 'Profesör Kurt', who: 'kitap kurdu', ears: 'none', fur: '#d98f9b', belly: '#f7d1d7', hat: 'graduate', hatC: '#2b2d42' },
    rival: { name: 'Bayan Ağ', who: 'kütüphaneci örümcek', ears: 'none', fur: '#3d405b', belly: '#81b29a', hat: 'glasses', hatC: '#e07a5f' },
    place: 'Tozlu çatı katı, kitap yığınları ve mum ışığı', sky: ['#3b2c25', '#7f5539'], ground: '#5e4232', motif: 'books',
    hook: 'Ansiklopedideki böcek sayfaları canlanır! Profesör sayfaları geri toplamak için düello eder.',
    line: 'Bu sayfayı okudun mu? Yaprak kapandı, sınav başlasın!' },
  { title: 'Bulut Adası', mascot: { name: 'Pilot Pervane', who: 'balon pilotu ördek', ears: 'none', fur: '#ffd23f', belly: '#fff3b0', hat: 'aviator', hatC: '#8b5e34' },
    rival: { name: 'Kara Karga', who: 'parlak şeyler toplayan karga', ears: 'none', fur: '#1c1c2b', belly: '#3a3a55', hat: 'monocle', hatC: '#f1c40f' },
    place: 'Gökyüzünde süzülen bulut adası, sıcak hava balonları', sky: ['#5fa8d3', '#cae9ff'], ground: '#f7f7ff', motif: 'clouds',
    hook: 'Balonu karga tarafından delinen Pervane, onarım için Kara Karga\'yla böcek bahsine girer.',
    line: 'Gak! Parlak şeyleri severim, ama hafızası parlak olanları daha çok!' },
  { title: 'Dev Bahçe', mascot: { name: 'Mini', who: 'küçülmüş kaşif çocuk', ears: 'human', fur: '#e0ac69', belly: '#ffd8a8', hat: 'helmet', hatC: '#ffb703' },
    rival: { name: 'Cüce Gnomo', who: 'bahçe cücesi heykeli', ears: 'human', fur: '#f4c7a1', belly: '#ffe3cc', beard: '#ffffff', hat: 'gnome', hatC: '#d62828' },
    place: 'Böcek boyuna küçülmüş bir çocuğun gözünden dev çimen ve çiçekler', sky: ['#a2d2ff', '#fefae0'], ground: '#6a994e', motif: 'giantgarden',
    hook: 'İcadıyla küçülen Mini, eski boyuna dönmek için bahçe cücesinin böcek turnuvasını kazanmalı.',
    line: 'Ufak boyla büyük hafıza gerek, ufaklık!' },
];

// 20 böceklik kadro (konseptlerin hepsinde ortak oyun mantığı)
const BUGS = [
  ['Gergedan Böceği', '#6b3e26'], ['Uğur Böceği', '#d62828'], ['Peygamber Devesi', '#5cb85c'], ['Ateş Böceği', '#f4d35e'],
  ['Akrep', '#8d6e63'], ['Karınca Askeri', '#3b2a1e'], ['Eşek Arısı', '#f1c40f'], ['Yusufçuk', '#2ec4b6'],
  ['Hamam Böceği', '#7f4f24'], ['Çekirge', '#90be6d'], ['Kırkayak', '#bc4749'], ['Bokböceği', '#264653'],
  ['Geyik Böceği', '#432818'], ['Su Örümceği', '#577590'], ['Tarantula', '#4a3f35'], ['Ağustos Böceği', '#9c6644'],
  ['Kelebek', '#ff70a6'], ['Sivrisinek', '#6c757d'], ['Tespih Böceği', '#adb5bd'], ['Kınkanatlı Dev', '#1b4332'],
];

const esc = s => s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/'/g, '&#39;');
let uid = 0;
const rnd = (seed => () => (seed = (seed * 16807) % 2147483647) / 2147483647);

// ---------- Karakter çizimi ----------
function character(c, x, y, s, flip) {
  const f = flip ? -1 : 1;
  let g = `<g transform="translate(${x},${y}) scale(${s * f},${s})">`;
  // gölge
  g += `<ellipse cx="0" cy="118" rx="70" ry="12" fill="#000" opacity=".25"/>`;
  // gövde
  g += `<path d="M-48 115 Q-58 40 0 30 Q58 40 48 115 Z" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/>`;
  g += `<ellipse cx="0" cy="80" rx="28" ry="32" fill="${c.belly}"/>`;
  // kollar
  g += `<path d="M-44 55 Q-75 75 -66 100" fill="none" stroke="${c.fur}" stroke-width="16" stroke-linecap="round"/>`;
  g += `<path d="M44 55 Q78 40 82 12" fill="none" stroke="${c.fur}" stroke-width="16" stroke-linecap="round"/>`;
  if (c.mane) g += `<circle cx="0" cy="-15" r="62" fill="${c.mane}" stroke="#1b1b1b" stroke-width="4"/>`;
  // kulaklar
  const E = {
    fox: `<path d="M-40 -40 L-50 -95 L-12 -60 Z M40 -40 L50 -95 L12 -60 Z" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/>`,
    cat: `<path d="M-42 -35 L-45 -88 L-10 -58 Z M42 -35 L45 -88 L10 -58 Z" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/>`,
    round: `<circle cx="-36" cy="-50" r="16" fill="${c.patch || c.fur}" stroke="#1b1b1b" stroke-width="4"/><circle cx="36" cy="-50" r="16" fill="${c.patch || c.fur}" stroke="#1b1b1b" stroke-width="4"/>`,
    bunny: `<ellipse cx="-20" cy="-95" rx="13" ry="42" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/><ellipse cx="20" cy="-95" rx="13" ry="42" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/>`,
    bat: `<path d="M-30 -45 L-62 -95 L-15 -62 Z M30 -45 L62 -95 L15 -62 Z" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/>`,
    monkey: `<circle cx="-48" cy="-15" r="16" fill="${c.belly}" stroke="#1b1b1b" stroke-width="4"/><circle cx="48" cy="-15" r="16" fill="${c.belly}" stroke="#1b1b1b" stroke-width="4"/>`,
    human: `<circle cx="-46" cy="-12" r="10" fill="${c.fur}" stroke="#1b1b1b" stroke-width="3"/><circle cx="46" cy="-12" r="10" fill="${c.fur}" stroke="#1b1b1b" stroke-width="3"/>`,
    owl: `<path d="M-38 -45 L-48 -82 L-18 -58 Z M38 -45 L48 -82 L18 -58 Z" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/>`,
    giraffe: `<rect x="-26" y="-100" width="8" height="40" fill="${c.fur}" stroke="#1b1b1b" stroke-width="3"/><rect x="18" y="-100" width="8" height="40" fill="${c.fur}" stroke="#1b1b1b" stroke-width="3"/><circle cx="-22" cy="-102" r="7" fill="#7a4a1e"/><circle cx="22" cy="-102" r="7" fill="#7a4a1e"/>`,
    antenna: `<line x1="-20" y1="-55" x2="-30" y2="-100" stroke="#333" stroke-width="4"/><circle cx="-30" cy="-102" r="7" fill="#ff4d4d"/><line x1="20" y1="-55" x2="30" y2="-100" stroke="#333" stroke-width="4"/><circle cx="30" cy="-102" r="7" fill="#4dff88"/>`,
    comb: `<path d="M-20 -55 Q-15 -95 0 -70 Q10 -100 18 -68 Q30 -90 30 -55 Z" fill="#e63946" stroke="#1b1b1b" stroke-width="3"/>`,
    none: '',
  };
  g += E[c.ears] || '';
  // kafa
  const robot = c.ears === 'antenna';
  g += robot ? `<rect x="-50" y="-58" width="100" height="88" rx="18" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/>`
             : `<circle cx="0" cy="-15" r="50" fill="${c.fur}" stroke="#1b1b1b" stroke-width="4"/>`;
  g += `<ellipse cx="0" cy="5" rx="30" ry="22" fill="${c.belly}"/>`;
  if (c.patch) g += `<ellipse cx="-18" cy="-20" rx="13" ry="16" fill="${c.patch}"/><ellipse cx="18" cy="-20" rx="13" ry="16" fill="${c.patch}"/>`;
  // gözler
  if (robot) g += `<rect x="-32" y="-30" width="64" height="20" rx="8" fill="#0ff" opacity=".85"/>`;
  else {
    g += `<circle cx="-18" cy="-20" r="10" fill="#fff"/><circle cx="18" cy="-20" r="10" fill="#fff"/>`;
    g += `<circle cx="-15" cy="-19" r="5" fill="#111"/><circle cx="21" cy="-19" r="5" fill="#111"/>`;
  }
  g += `<path d="M-12 12 Q0 22 12 12" fill="none" stroke="#1b1b1b" stroke-width="3.5" stroke-linecap="round"/>`;
  g += `<ellipse cx="0" cy="0" rx="6" ry="4" fill="#1b1b1b"/>`;
  if (c.beard) g += `<path d="M-35 5 Q0 75 35 5 Q0 25 -35 5 Z" fill="${c.beard}" stroke="#1b1b1b" stroke-width="3"/>`;
  // şapka / aksesuar
  const k = c.hatC;
  const HAT = {
    scout: `<path d="M-55 -50 L55 -50 L30 -60 L0 -95 L-30 -60 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/>`,
    top: `<rect x="-30" y="-115" width="60" height="55" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><rect x="-48" y="-64" width="96" height="10" rx="4" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><rect x="-30" y="-78" width="60" height="9" fill="#c0392b"/>`,
    goggles: `<rect x="-50" y="-48" width="100" height="10" fill="${k}"/><circle cx="-18" cy="-45" r="14" fill="#8fd3ff" stroke="#1b1b1b" stroke-width="4"/><circle cx="18" cy="-45" r="14" fill="#8fd3ff" stroke="#1b1b1b" stroke-width="4"/>`,
    fez: `<path d="M-25 -55 L-20 -100 L20 -100 L25 -55 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><line x1="0" y1="-100" x2="22" y2="-80" stroke="#111" stroke-width="3"/>`,
    band: `<rect x="-50" y="-48" width="100" height="12" fill="${k}"/><path d="M48 -44 L72 -30 L66 -48 Z" fill="${k}"/>`,
    straw: `<ellipse cx="0" cy="-58" rx="70" ry="14" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><path d="M-30 -60 Q0 -110 30 -60 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/>`,
    beanie: `<path d="M-48 -40 Q0 -120 48 -40 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><circle cx="0" cy="-88" r="12" fill="#fff" stroke="#1b1b1b" stroke-width="3"/>`,
    captain: `<path d="M-48 -52 Q0 -105 48 -52 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><rect x="-52" y="-56" width="104" height="10" fill="#111"/><circle cx="0" cy="-72" r="8" fill="#ffd166"/>`,
    miner: `<path d="M-50 -45 Q0 -115 50 -45 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><circle cx="0" cy="-75" r="12" fill="#fffbe0" stroke="#1b1b1b" stroke-width="3"/><path d="M0 -75 L-120 -160 L-40 -175 Z" fill="#fff6b0" opacity=".25"/>`,
    headphones: `<path d="M-52 -15 Q0 -110 52 -15" fill="none" stroke="#222" stroke-width="8"/><rect x="-62" y="-30" width="18" height="34" rx="6" fill="${k}"/><rect x="44" y="-30" width="18" height="34" rx="6" fill="${k}"/>`,
    bandana: `<path d="M-50 -38 Q0 -100 50 -38 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><path d="M-50 -38 L-75 -20 L-58 -50 Z" fill="${k}"/>`,
    helmet: `<circle cx="0" cy="-15" r="62" fill="${k}" opacity=".35" stroke="#fff" stroke-width="5"/>`,
    safari: `<ellipse cx="0" cy="-50" rx="66" ry="12" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><path d="M-40 -52 Q0 -110 40 -52 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/>`,
    pirate: `<path d="M-70 -50 Q0 -140 70 -50 Q0 -70 -70 -50 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><circle cx="0" cy="-78" r="9" fill="#fff"/><path d="M-26 -30 L-6 -30 L-10 -10 L-26 -12 Z" fill="#111"/>`,
    chef: `<rect x="-30" y="-85" width="60" height="35" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><circle cx="-22" cy="-92" r="18" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><circle cx="0" cy="-102" r="20" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><circle cx="22" cy="-92" r="18" fill="${k}" stroke="#1b1b1b" stroke-width="4"/>`,
    beekeeper: `<ellipse cx="0" cy="-55" rx="66" ry="12" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><path d="M-35 -58 Q0 -105 35 -58 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><path d="M-60 -50 L-55 30 L55 30 L60 -50" fill="#fff" opacity=".25"/>`,
    cap: `<path d="M-48 -42 Q0 -110 48 -42 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><path d="M20 -45 L85 -40 L40 -55 Z" fill="${k}" stroke="#1b1b1b" stroke-width="3"/>`,
    graduate: `<path d="M-60 -72 L0 -95 L60 -72 L0 -50 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><line x1="40" y1="-72" x2="48" y2="-35" stroke="#ffd166" stroke-width="4"/>`,
    diving: `<rect x="-55" y="-45" width="110" height="38" rx="14" fill="#9fe3ff" opacity=".6" stroke="${k}" stroke-width="7"/><path d="M50 -30 Q75 -60 70 -110" fill="none" stroke="${k}" stroke-width="8"/>`,
    jester: `<path d="M-50 -45 L-80 -105 L-10 -70 L0 -115 L10 -70 L80 -105 L50 -45 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><circle cx="-80" cy="-105" r="8" fill="#ffd166"/><circle cx="0" cy="-115" r="8" fill="#ffd166"/><circle cx="80" cy="-105" r="8" fill="#ffd166"/>`,
    hoodie: `<path d="M-62 30 Q-70 -80 0 -78 Q70 -80 62 30 L45 30 Q55 -50 0 -58 Q-55 -50 -45 30 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/>`,
    glasses: `<circle cx="-18" cy="-20" r="15" fill="none" stroke="${k}" stroke-width="4"/><circle cx="18" cy="-20" r="15" fill="none" stroke="${k}" stroke-width="4"/><line x1="-3" y1="-20" x2="3" y2="-20" stroke="${k}" stroke-width="4"/>`,
    aviator: `<path d="M-50 -30 Q0 -110 50 -30 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/><circle cx="-18" cy="-55" r="12" fill="#bde0fe" stroke="#1b1b1b" stroke-width="3"/><circle cx="18" cy="-55" r="12" fill="#bde0fe" stroke="#1b1b1b" stroke-width="3"/><path d="M-40 60 L60 60 L90 90" stroke="#e63946" stroke-width="10" fill="none"/>`,
    monocle: `<circle cx="18" cy="-20" r="14" fill="none" stroke="${k}" stroke-width="4"/><line x1="30" y1="-12" x2="38" y2="40" stroke="${k}" stroke-width="2"/><rect x="-28" y="-115" width="56" height="50" fill="#111" stroke="#1b1b1b" stroke-width="3"/><rect x="-45" y="-68" width="90" height="8" fill="#111"/>`,
    gnome: `<path d="M-50 -40 Q-10 -170 50 -40 Z" fill="${k}" stroke="#1b1b1b" stroke-width="4"/>`,
    none: '',
  };
  g += HAT[c.hat] || '';
  if (c.ears === 'owl' || c.ears === 'none' && /papağan|ördek|karga|horoz|penguen/.test(c.who)) {
    g += `<path d="M-10 -2 L10 -2 L0 14 Z" fill="#f4a261" stroke="#1b1b1b" stroke-width="2"/>`;
  }
  return g + '</g>';
}

// ---------- Böcek çizimi ----------
function bug(x, y, s, color, rot = 0, cheer = false) {
  let g = `<g transform="translate(${x},${y}) rotate(${rot}) scale(${s})">`;
  for (let i = -1; i <= 1; i++) {
    g += `<path d="M-6 ${i * 9} L-30 ${i * 14 - 8}" stroke="#111" stroke-width="3"/><path d="M6 ${i * 9} L30 ${i * 14 - 8}" stroke="#111" stroke-width="3"/>`;
  }
  g += `<ellipse cx="0" cy="6" rx="18" ry="24" fill="${color}" stroke="#111" stroke-width="3"/>`;
  g += `<line x1="0" y1="-14" x2="0" y2="28" stroke="#111" stroke-width="2" opacity=".6"/>`;
  g += `<circle cx="0" cy="-22" r="11" fill="#2b2b2b" stroke="#111" stroke-width="3"/>`;
  g += `<circle cx="-4" cy="-24" r="3.5" fill="#fff"/><circle cx="4" cy="-24" r="3.5" fill="#fff"/>`;
  g += `<path d="M-4 -31 Q-14 -50 -20 -48 M4 -31 Q14 -50 20 -48" stroke="#111" stroke-width="2.5" fill="none"/>`;
  if (cheer) g += `<path d="M-30 -10 L-38 -36 M30 -10 L38 -36" stroke="#111" stroke-width="4"/>`;
  return g + '</g>';
}

// ---------- Mekan motifleri ----------
function motif(kind, r, c) {
  let g = '';
  const R = (a, b) => a + r() * (b - a);
  switch (kind) {
    case 'mushrooms':
      for (let i = 0; i < 6; i++) { const x = R(0, W), h = R(180, 380), w = R(140, 260);
        g += `<rect x="${x - 18}" y="${520 - h}" width="36" height="${h}" fill="#e8d8c3" opacity=".9"/><ellipse cx="${x}" cy="${520 - h}" rx="${w / 2}" ry="${w / 4}" fill="${['#c0392b', '#8e44ad', '#e67e22'][i % 3]}" opacity=".85"/>`; }
      for (let i = 0; i < 40; i++) g += `<circle cx="${R(0, W)}" cy="${R(60, 500)}" r="${R(2, 5)}" fill="#fff59d" opacity="${R(.4, 1)}"/>`; break;
    case 'palms':
      g += `<circle cx="1050" cy="130" r="60" fill="#fffbe0"/>`;
      g += `<path d="M0 520 Q300 420 600 500 Q900 420 1280 500 L1280 560 L0 560 Z" fill="#a8844f"/>`;
      g += `<ellipse cx="640" cy="560" rx="260" ry="30" fill="#3a7ca5" opacity=".8"/>`;
      for (const x of [150, 320, 1000, 1180]) { g += `<path d="M${x} 540 Q${x + 20} 380 ${x - 10} 260" stroke="#5c3d1e" stroke-width="18" fill="none"/>`;
        for (let a = 0; a < 6; a++) g += `<path d="M${x - 10} 260 q${Math.cos(a) * 90} ${-40 + Math.sin(a) * 50} ${Math.cos(a) * 140} ${40}" stroke="#1f5f3a" stroke-width="14" fill="none"/>`; } break;
    case 'lanterns':
      g += `<path d="M300 520 Q640 380 980 520" stroke="#b5452a" stroke-width="18" fill="none"/>`;
      for (let i = 0; i < 14; i++) { const x = 40 + i * 90, y = 80 + Math.sin(i) * 25;
        g += `<line x1="${x}" y1="0" x2="${x}" y2="${y}" stroke="#222"/><ellipse cx="${x}" cy="${y + 22}" rx="20" ry="26" fill="#ff6b35"/><ellipse cx="${x}" cy="${y + 22}" rx="40" ry="40" fill="#ffb347" opacity=".18"/>`; } break;
    case 'mountains':
      g += `<path d="M0 520 L200 220 L360 420 L560 160 L780 430 L980 240 L1280 520 Z" fill="#cfe1ee"/><path d="M200 220 L240 280 L170 270 Z M560 160 L610 230 L520 225 Z M980 240 L1020 300 L950 295 Z" fill="#fff"/>`;
      g += `<path d="M0 120 Q320 40 640 130 T1280 90" stroke="#7cf5c3" stroke-width="40" fill="none" opacity=".35"/>`;
      for (let i = 0; i < 60; i++) g += `<circle cx="${R(0, W)}" cy="${R(0, 560)}" r="${R(1.5, 4)}" fill="#fff" opacity=".8"/>`; break;
    case 'crystals':
      for (let i = 0; i < 14; i++) { const x = R(0, W), y = R(100, 540), h = R(60, 180), col = ['#b388ff', '#64ffda', '#ff80ab'][i % 3];
        g += `<path d="M${x} ${y} L${x + 20} ${y - h} L${x + 40} ${y} Z" fill="${col}" opacity=".7"/><circle cx="${x + 20}" cy="${y - h / 2}" r="${h / 2}" fill="${col}" opacity=".08"/>`; }
      g += `<path d="M0 0 L1280 0 L1280 70 Q900 140 640 60 Q300 140 0 60 Z" fill="#0a0614"/>`; break;
    case 'reeds':
      for (let i = 0; i < 70; i++) { const x = R(0, W); g += `<path d="M${x} 560 Q${x + R(-20, 20)} 460 ${x + R(-30, 30)} ${R(330, 450)}" stroke="#2f4a2a" stroke-width="5" fill="none"/>`; }
      g += `<rect x="760" y="480" width="420" height="20" fill="#6b4f2a"/>`;
      for (let i = 0; i < 5; i++) g += `<ellipse cx="${R(0, W)}" cy="${R(250, 520)}" rx="${R(150, 300)}" ry="${R(25, 50)}" fill="#c8f7c5" opacity=".2"/>`; break;
    case 'space':
      for (let i = 0; i < 120; i++) g += `<circle cx="${R(0, W)}" cy="${R(0, 560)}" r="${R(.8, 2.5)}" fill="#fff"/>`;
      g += `<circle cx="1060" cy="160" r="110" fill="#e76f51"/><ellipse cx="1060" cy="160" rx="190" ry="26" fill="none" stroke="#f4a261" stroke-width="8" opacity=".8"/>`;
      g += `<path d="M120 560 Q640 40 1160 560" fill="#7ad3ff" opacity=".08" stroke="#bfe9ff" stroke-width="6"/>`; break;
    case 'volcano':
      g += `<path d="M300 540 L560 160 L720 160 L980 540 Z" fill="#3b2f2f"/><path d="M560 160 L600 260 L640 200 L680 280 L720 160 Z" fill="#ff5400"/>`;
      g += `<circle cx="640" cy="90" r="70" fill="#555" opacity=".7"/><circle cx="700" cy="40" r="60" fill="#666" opacity=".6"/>`;
      g += `<path d="M0 540 Q200 500 400 540" stroke="#4cc9f0" stroke-width="12" fill="none" opacity=".6"/>`; break;
    case 'ship':
      g += `<path d="M0 500 Q160 470 320 500 T640 500 T960 500 T1280 500 L1280 560 L0 560 Z" fill="#1d3557"/>`;
      g += `<rect x="620" y="40" width="20" height="480" fill="#5c3d1e"/><path d="M640 70 Q860 160 640 320 Z" fill="#f1faee" opacity=".9"/><path d="M640 50 L720 70 L640 90 Z" fill="#111"/>`;
      g += `<path d="M120 0 L170 160 M300 0 L330 140" stroke="#fff" stroke-width="2" opacity=".3"/>`; break;
    case 'candy':
      for (let i = 0; i < 4; i++) { const x = 60 + i * 320; g += `<rect x="${x}" y="300" width="240" height="160" fill="#fff" opacity=".85"/>`;
        for (let s = 0; s < 6; s++) g += `<rect x="${x + s * 40}" y="270" width="40" height="40" fill="${s % 2 ? '#ff8fab' : '#fff'}"/>`;
        g += `<circle cx="${x + 60}" cy="380" r="26" fill="#ffd166"/><circle cx="${x + 130}" cy="380" r="22" fill="#06d6a0"/><circle cx="${x + 190}" cy="380" r="24" fill="#ef476f"/>`; }
      for (let i = 0; i < 24; i++) g += `<path d="M${i * 56} 60 l28 40 l28 -40" fill="${['#ef476f', '#ffd166', '#06d6a0', '#118ab2'][i % 4]}"/>`; break;
    case 'treehouses':
      for (const x of [140, 1120]) { g += `<rect x="${x - 30}" y="80" width="60" height="480" fill="#5a3e2b"/><circle cx="${x}" cy="90" r="150" fill="#2d6a4f" opacity=".9"/><rect x="${x - 80}" y="200" width="160" height="110" fill="#a47148" stroke="#3b2a1e" stroke-width="5"/><path d="M${x - 95} 200 L${x} 140 L${x + 95} 200 Z" fill="#7f4f24"/><rect x="${x - 20}" y="245" width="40" height="40" fill="#ffd166"/>`; }
      g += `<path d="M220 250 Q640 340 1040 250" stroke="#8b6b4a" stroke-width="6" fill="none"/>`;
      for (let i = 0; i < 18; i++) g += `<circle cx="${230 + i * 45}" cy="${260 + Math.sin(i / 17 * Math.PI) * 70}" r="6" fill="#ffe066"/>`; break;
    case 'coral':
      for (let i = 0; i < 30; i++) g += `<circle cx="${R(0, W)}" cy="${R(0, 520)}" r="${R(3, 12)}" fill="none" stroke="#caf0f8" stroke-width="2" opacity=".6"/>`;
      for (let i = 0; i < 10; i++) { const x = R(0, W), col = ['#ff7f50', '#ff4d6d', '#ffb703', '#c77dff'][i % 4];
        g += `<path d="M${x} 560 q-10 -80 -40 -120 M${x} 560 q10 -90 30 -150 M${x} 540 q30 -50 60 -60" stroke="${col}" stroke-width="12" fill="none" stroke-linecap="round"/>`; }
      g += `<path d="M140 560 Q640 0 1140 560" fill="#fff" opacity=".07" stroke="#e0fbfc" stroke-width="5"/>`; break;
    case 'circus':
      for (let i = 0; i < 16; i++) g += `<path d="M640 0 L${i * 80} 560 L${i * 80 + 80} 560 Z" fill="${i % 2 ? '#c1121f' : '#fdf0d5'}" opacity=".35"/>`;
      g += `<path d="M300 0 L520 560 L120 560 Z M980 0 L1160 560 L760 560 Z" fill="#fff8b0" opacity=".12"/>`;
      for (let i = 0; i < 60; i++) g += `<rect x="${R(0, W)}" y="${R(0, 540)}" width="8" height="5" fill="${['#ffd166', '#06d6a0', '#ef476f', '#4cc9f0'][i % 4]}" transform="rotate(${R(0, 90)})"/>`; break;
    case 'acacia':
      g += `<circle cx="640" cy="420" r="150" fill="#ffb347" opacity=".85"/>`;
      for (const [x, s] of [[180, 1], [1100, 1.2], [900, .7]]) g += `<g transform="translate(${x},520) scale(${s})"><path d="M0 0 L0 -160 L-60 -220 M0 -160 L70 -230" stroke="#2b1d14" stroke-width="16" fill="none"/><ellipse cx="0" cy="-240" rx="170" ry="40" fill="#2b1d14"/></g>`;
      g += `<path d="M560 520 L600 470 L640 520 L680 470 L720 520 Z" fill="#ff7b00"/>`; break;
    case 'neon':
      for (let i = 0; i < 9; i++) { const x = i * 150, h = R(200, 420); g += `<rect x="${x}" y="${520 - h}" width="130" height="${h}" fill="#1a1a2e"/>`;
        for (let w = 0; w < 10; w++) g += `<rect x="${x + 15 + (w % 3) * 38}" y="${540 - h + Math.floor(w / 3) * 40}" width="22" height="18" fill="#ffd166" opacity="${R(.2, .9)}"/>`; }
      g += `<text x="220" y="210" font-size="56" font-family="Arial Black" fill="#f15bb5" opacity=".9">BÖCEK</text><text x="860" y="160" font-size="48" font-family="Arial Black" fill="#00f5d4">LİG</text>`; break;
    case 'barn':
      g += `<rect x="880" y="260" width="300" height="260" fill="#c1121f" stroke="#5c0a0a" stroke-width="6"/><path d="M860 270 L1030 150 L1200 270 Z" fill="#780000"/><path d="M960 520 L960 380 L1100 380 L1100 520 M960 380 L1100 520 M1100 380 L960 520" stroke="#fff" stroke-width="8" fill="none"/>`;
      for (const x of [100, 240]) g += `<rect x="${x}" y="440" width="120" height="80" rx="10" fill="#e9c46a" stroke="#b08930" stroke-width="5"/>`;
      for (let i = 0; i < 20; i++) g += `<path d="M${i * 64} 40 l32 40 l32 -40" fill="${['#e63946', '#f1faee', '#457b9d'][i % 3]}"/>`; break;
    case 'bamboo':
      for (let i = 0; i < 22; i++) { const x = R(0, W); g += `<rect x="${x}" y="0" width="${R(12, 24)}" height="560" fill="#4f772d" opacity="${R(.3, .8)}"/>`; }
      g += `<path d="M450 520 L830 520 L790 470 L490 470 Z" fill="#8d99ae"/><path d="M500 470 L780 470 L750 430 L530 430 Z" fill="#a3aab8"/>`;
      for (let i = 0; i < 6; i++) g += `<ellipse cx="${R(0, W)}" cy="${R(150, 500)}" rx="300" ry="50" fill="#fff" opacity=".3"/>`; break;
    case 'books':
      for (let s = 0; s < 6; s++) { let x = 20 + s * 210; for (let b = 0; b < 8; b++) { const h = R(80, 220); g += `<rect x="${x}" y="${540 - h}" width="22" height="${h}" fill="${['#9b2226', '#005f73', '#ee9b00', '#3a5a40', '#6d597a'][b % 5]}" stroke="#2b1d14"/>`; x += 24; } }
      g += `<path d="M0 0 L640 140 L1280 0" stroke="#5e4232" stroke-width="30" fill="none"/>`;
      g += `<rect x="1100" y="120" width="14" height="50" fill="#fffbe0"/><ellipse cx="1107" cy="112" rx="8" ry="14" fill="#ffb703"/><circle cx="1107" cy="130" r="90" fill="#ffd166" opacity=".12"/>`; break;
    case 'clouds':
      for (let i = 0; i < 9; i++) { const x = R(0, W), y = R(40, 400); g += `<g opacity=".9"><circle cx="${x}" cy="${y}" r="40" fill="#fff"/><circle cx="${x + 45}" cy="${y - 15}" r="50" fill="#fff"/><circle cx="${x + 95}" cy="${y}" r="38" fill="#fff"/></g>`; }
      for (const [x, y, col] of [[200, 160, '#ef476f'], [1100, 230, '#ffd166']]) g += `<circle cx="${x}" cy="${y}" r="60" fill="${col}"/><path d="M${x - 30} ${y + 50} L${x - 15} ${y + 100} L${x + 15} ${y + 100} L${x + 30} ${y + 50}" stroke="#5c3d1e" fill="none" stroke-width="3"/><rect x="${x - 18}" y="${y + 100}" width="36" height="26" fill="#a47148"/>`; break;
    case 'giantgarden':
      for (let i = 0; i < 50; i++) { const x = R(0, W); g += `<path d="M${x} 560 Q${x + R(-40, 40)} 300 ${x + R(-60, 60)} ${R(40, 260)}" stroke="#386641" stroke-width="${R(10, 22)}" fill="none" opacity=".8"/>`; }
      for (const [x, y, col] of [[200, 150, '#ff006e'], [1050, 110, '#ffbe0b'], [700, 60, '#8338ec']]) { for (let p = 0; p < 6; p++) g += `<ellipse cx="${x + Math.cos(p) * 55}" cy="${y + Math.sin(p) * 55}" rx="50" ry="30" fill="${col}" transform="rotate(${p * 60},${x + Math.cos(p) * 55},${y + Math.sin(p) * 55})"/>`; g += `<circle cx="${x}" cy="${y}" r="35" fill="#ffd166"/>`; }
      g += `<circle cx="900" cy="470" r="22" fill="#a2d2ff" opacity=".7"/>`; break;
  }
  return g;
}

// ---------- Kart ----------
function card(c, i) {
  const r = rnd(1000 + i * 77);
  const id = `k${i}`;
  let s = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${W} ${H}" width="${W}" height="${H}" font-family="'Trebuchet MS', Arial, sans-serif">`;
  s += `<defs><linearGradient id="${id}sky" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${c.sky[0]}"/><stop offset="1" stop-color="${c.sky[1]}"/></linearGradient>
  <filter id="${id}blur"><feGaussianBlur stdDeviation="14"/></filter>
  <radialGradient id="${id}vig" cx=".5" cy=".45" r=".75"><stop offset=".6" stop-color="#000" stop-opacity="0"/><stop offset="1" stop-color="#000" stop-opacity=".55"/></radialGradient></defs>`;
  s += `<rect width="${W}" height="${H}" fill="url(#${id}sky)"/>`;
  s += motif(c.motif, r, c);
  s += `<rect y="540" width="${W}" height="${H - 540}" fill="${c.ground}"/>`;
  s += `<path d="M0 540 Q320 520 640 545 T1280 535 L1280 560 L0 560 Z" fill="#000" opacity=".15"/>`;

  // Seyirci böcekler + sis (sağ arka)
  const smoke = [];
  for (let k = 0; k < 14; k++) smoke.push(`<circle cx="${820 + r() * 420}" cy="${430 + r() * 120}" r="${50 + r() * 50}" fill="#e8e8e8" opacity="${.25 + r() * .25}"/>`);
  s += `<g filter="url(#${id}blur)">${smoke.join('')}</g>`;
  for (let k = 0; k < 9; k++) s += bug(840 + (k % 5) * 85 + r() * 20, 470 + Math.floor(k / 5) * 60 + r() * 15, .9, BUGS[(i + k * 3) % 20][1], r() * 30 - 15, true);
  s += `<g filter="url(#${id}blur)" opacity=".8">${smoke.slice(0, 6).join('')}</g>`;
  for (const [x, y, t] of [[870, 420, 'HEY!'], [1040, 400, 'HEY HEY!'], [1180, 440, 'VUR!']]) s += `<text x="${x}" y="${y}" font-size="30" font-weight="900" fill="#fff" stroke="#111" stroke-width="5" paint-order="stroke" transform="rotate(${r() * 16 - 8},${x},${y})">${t}</text>`;

  // Arena: 5 slot
  s += `<ellipse cx="560" cy="650" rx="360" ry="90" fill="#000" opacity=".25"/>`;
  s += `<ellipse cx="560" cy="635" rx="350" ry="82" fill="#8a6a4a" stroke="#3b2a1e" stroke-width="6"/>`;
  s += `<ellipse cx="560" cy="628" rx="320" ry="68" fill="#a9845e"/>`;
  for (let k = 0; k < 5; k++) {
    const x = 300 + k * 130, y = 630;
    s += `<ellipse cx="${x}" cy="${y}" rx="52" ry="30" fill="#5c4330" stroke="#2b1d14" stroke-width="3"/>`;
    s += `<text x="${x}" y="${y + 50}" text-anchor="middle" font-size="20" font-weight="900" fill="#fff" stroke="#111" stroke-width="4" paint-order="stroke">${k + 1}</text>`;
    if (k < 2) s += bug(x, y - 8, .9, BUGS[(i * 3 + k * 7) % 20][1], 0);
  }
  // kapanan yaprak (slot 3-5'i örtüyor)
  s += `<g transform="translate(690,600) rotate(-6)"><path d="M-180 10 Q-40 -110 210 -10 Q60 110 -180 10 Z" fill="#3a9d23" stroke="#1d5e10" stroke-width="6"/>
    <path d="M-180 10 Q20 0 210 -10" stroke="#c7f9cc" stroke-width="5" fill="none"/>${[-100, -30, 40, 110].map(x => `<path d="M${x} 2 q30 -40 60 -50 M${x} 2 q30 30 60 40" stroke="#c7f9cc" stroke-width="3" fill="none" opacity=".8"/>`).join('')}</g>`;
  s += `<text x="700" y="720" text-anchor="middle" font-size="18" font-weight="700" fill="#fff" stroke="#111" stroke-width="4" paint-order="stroke">yaprak kapandı: 3-4-5 neydi?</text>`;

  // Karakterler
  s += character(c.mascot, 150, 560, 1.15, false);
  s += character(c.rival, 1130, 600, 1.0, true);

  // Konuşma balonu (rakipten)
  s += `<g><rect x="610" y="150" width="520" height="92" rx="22" fill="#fff" stroke="#111" stroke-width="5"/><path d="M1040 238 L1090 300 L1000 240 Z" fill="#fff" stroke="#111" stroke-width="5"/><rect x="1000" y="232" width="50" height="10" fill="#fff"/>
    <foreignObject x="628" y="160" width="485" height="76"><div xmlns="http://www.w3.org/1999/xhtml" style="font:700 22px 'Trebuchet MS',Arial;color:#111;line-height:1.25">${esc(c.line)}</div></foreignObject></g>`;

  // Başlık + isimler
  s += `<rect x="24" y="22" width="560" height="108" rx="18" fill="#000" opacity=".55"/>`;
  s += `<text x="44" y="62" font-size="20" font-weight="700" fill="#ffd166">KONSEPT ${String(i + 1).padStart(2, '0')}</text>`;
  s += `<text x="44" y="108" font-size="44" font-weight="900" fill="#fff">${esc(c.title)}</text>`;
  s += `<text x="150" y="770" text-anchor="middle" font-size="22" font-weight="900" fill="#fff" stroke="#111" stroke-width="5" paint-order="stroke">${esc(c.mascot.name)}</text>`;
  s += `<text x="1130" y="785" text-anchor="middle" font-size="22" font-weight="900" fill="#fff" stroke="#111" stroke-width="5" paint-order="stroke">${esc(c.rival.name)}</text>`;
  // palet
  [c.sky[0], c.sky[1], c.ground, c.mascot.fur, c.rival.fur, c.mascot.hatC].forEach((col, k) =>
    s += `<rect x="${604 + k * 34}" y="40" width="28" height="28" rx="6" fill="${col}" stroke="#fff" stroke-width="2"/>`);
  s += `<rect width="${W}" height="${H}" fill="url(#${id}vig)" pointer-events="none"/>`;
  return s + '</svg>';
}

// ---------- Çıktı ----------
const outDir = __dirname;
fs.mkdirSync(path.join(outDir, 'svg'), { recursive: true });
const svgs = CONCEPTS.map((c, i) => card(c, i));
svgs.forEach((s, i) => fs.writeFileSync(path.join(outDir, 'svg', `konsept-${String(i + 1).padStart(2, '0')}.svg`), s));
fs.writeFileSync(path.join(outDir, 'concepts.json'), JSON.stringify({ concepts: CONCEPTS, bugs: BUGS.map(b => b[0]) }, null, 2));
module.exports = { CONCEPTS, BUGS, svgs };
console.log(`${svgs.length} konsept üretildi`);
