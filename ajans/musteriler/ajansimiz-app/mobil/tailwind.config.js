/** Değerler DESIGN.md'den. Renkler JS token'larından (src/theme/tokens.ts) inline gelir; burada boyut/aralık/tipografi. */
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{ts,tsx}'],
  presets: [require('nativewind/preset')],
  theme: {
    extend: {
      fontFamily: {
        body: ['Archivo_400Regular'],
        medium: ['Archivo_500Medium'],
        heavy: ['Archivo_800ExtraBold'],
      },
      fontSize: {
        display: ['40px', { lineHeight: '40px', letterSpacing: '-1.2px' }],
        h2: ['28px', { lineHeight: '30px', letterSpacing: '-0.6px' }],
        h3: ['20px', { lineHeight: '24px', letterSpacing: '-0.2px' }],
        lead: ['17px', { lineHeight: '26px' }],
        body: ['16px', { lineHeight: '24px' }],
        small: ['14px', { lineHeight: '20px' }],
      },
      borderRadius: { sm: '8px', md: '14px', lg: '24px' },
    },
  },
  plugins: [],
};
