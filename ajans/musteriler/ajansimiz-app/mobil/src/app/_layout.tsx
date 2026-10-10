import '../global.css';
import '@/lib/nativewind-kurulum';

import { Archivo_400Regular, Archivo_500Medium, Archivo_800ExtraBold, useFonts } from '@expo-google-fonts/archivo';
import { DarkTheme, DefaultTheme, Stack, ThemeProvider } from 'expo-router';
import * as SplashScreen from 'expo-splash-screen';
import { StatusBar } from 'expo-status-bar';
import { useEffect } from 'react';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import { SafeAreaProvider } from 'react-native-safe-area-context';

import { KatmanSaglayici } from '@/components/katman';
import { useTema } from '@/theme/tokens';

SplashScreen.preventAutoHideAsync();

export default function KokDuzen() {
  const [yuklendi, hata] = useFonts({ Archivo_400Regular, Archivo_500Medium, Archivo_800ExtraBold });
  const t = useTema();

  useEffect(() => {
    if (yuklendi || hata) SplashScreen.hideAsync();
  }, [yuklendi, hata]);

  if (!yuklendi && !hata) return null;

  const temel = t.scheme === 'dark' ? DarkTheme : DefaultTheme;
  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <SafeAreaProvider>
        <ThemeProvider value={{ ...temel, colors: { ...temel.colors, background: t.bg, card: t.bg, text: t.fg, primary: t.accent, border: t.border } }}>
          <KatmanSaglayici>
            <StatusBar style="auto" />
            <Stack
              screenOptions={{
                headerShadowVisible: false,
                headerBackButtonDisplayMode: 'minimal',
                headerTitleStyle: { fontFamily: 'Archivo_800ExtraBold' },
                headerLargeTitleStyle: { fontFamily: 'Archivo_800ExtraBold' },
              }}
            >
              <Stack.Screen name="index" options={{ title: 'Vitrin Atölyesi', headerLargeTitle: true }} />
              <Stack.Screen name="kafe" options={{ title: 'Sadakat kartı' }} />
              <Stack.Screen name="berber" options={{ title: 'Randevu' }} />
              <Stack.Screen name="restoran" options={{ title: 'Menü' }} />
              <Stack.Screen name="iletisim" options={{ title: 'Bize ulaşın' }} />
            </Stack>
          </KatmanSaglayici>
        </ThemeProvider>
      </SafeAreaProvider>
    </GestureHandlerRootView>
  );
}
