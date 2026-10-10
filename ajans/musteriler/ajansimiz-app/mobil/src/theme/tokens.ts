import { useColorScheme } from 'react-native';

// DESIGN.md §2 ve §2b. Burada olmayan renk kodda kullanılmaz.
export type Palette = {
  bg: string;
  surface: string;
  fg: string;
  muted: string;
  border: string;
  accent: string;
  accentPressed: string;
  accentFg: string;
  success: string;
};

const marka: Record<'light' | 'dark', Palette> = {
  light: { bg: '#FFFFFF', surface: '#F3F5F2', fg: '#0E1512', muted: '#4F5B55', border: '#DCE2DE', accent: '#D7381E', accentPressed: '#B92E17', accentFg: '#FFFFFF', success: '#1F7A4D' },
  dark: { bg: '#0E1512', surface: '#151E1A', fg: '#EEF2EF', muted: '#A3AFA9', border: '#26322C', accent: '#FF5A3C', accentPressed: '#E34A2E', accentFg: '#0E1512', success: '#3DBE7E' },
};

const kafe: Record<'light' | 'dark', Palette> = {
  light: { bg: '#FBF7F2', surface: '#F1E8DD', fg: '#2A1A10', muted: '#6B5444', border: '#E2D4C4', accent: '#8A4B24', accentPressed: '#713C1C', accentFg: '#FFFFFF', success: '#1F7A4D' },
  dark: { bg: '#17110C', surface: '#241A13', fg: '#F3E9DF', muted: '#BFA996', border: '#3A2B20', accent: '#D99560', accentPressed: '#C4814D', accentFg: '#17110C', success: '#3DBE7E' },
};

const berber: Record<'light' | 'dark', Palette> = {
  light: { bg: '#F6F5F1', surface: '#EBE8E0', fg: '#111111', muted: '#55524B', border: '#D9D5CA', accent: '#111111', accentPressed: '#2B2B2B', accentFg: '#FFFFFF', success: '#1F7A4D' },
  dark: { bg: '#0D0D0D', surface: '#1A1A1A', fg: '#F2EFE8', muted: '#A9A497', border: '#2C2C2C', accent: '#C9A24A', accentPressed: '#B38E3B', accentFg: '#0D0D0D', success: '#3DBE7E' },
};

const restoran: Record<'light' | 'dark', Palette> = {
  light: { bg: '#FFFDF8', surface: '#F3F0E6', fg: '#1B1E17', muted: '#555A4E', border: '#DEDACB', accent: '#2F6B3A', accentPressed: '#25562E', accentFg: '#FFFFFF', success: '#1F7A4D' },
  dark: { bg: '#121410', surface: '#1C1F19', fg: '#EEEDE6', muted: '#A7AA9E', border: '#2C3027', accent: '#7CC489', accentPressed: '#68B075', accentFg: '#121410', success: '#3DBE7E' },
};

export const temalar = { marka, kafe, berber, restoran };
export type TemaAdi = keyof typeof temalar;

export function useTema(ad: TemaAdi = 'marka'): Palette & { scheme: 'light' | 'dark' } {
  const scheme = useColorScheme() === 'dark' ? 'dark' : 'light';
  return { ...temalar[ad][scheme], scheme };
}

export const sheetShadow = '0 -8px 30px rgba(14,21,18,0.18)';
export const easeOut = [0.2, 0.7, 0.2, 1] as const;
