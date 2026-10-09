// Marka ve iletişim bilgileri TEK yerden değişir.
// TODO: müşteriye sor — gerçek numara, e-posta ve Instagram kullanıcı adı.
export const site = {
  brand: 'Vitrin Atölyesi',
  whatsapp: '905XXXXXXXXX',
  phone: '+905XXXXXXXXX',
  email: 'merhaba@vitrinatolyesi.com',
  instagram: 'vitrinatolyesi',
  whatsappMessage: 'Merhaba, işletmem için ücretsiz dijital check-up istiyorum.',
};

export const waLink = `https://wa.me/${site.whatsapp}?text=${encodeURIComponent(site.whatsappMessage)}`;
export const telLink = `tel:${site.phone}`;
export const mailLink = `mailto:${site.email}`;
export const igLink = `https://instagram.com/${site.instagram}`;
