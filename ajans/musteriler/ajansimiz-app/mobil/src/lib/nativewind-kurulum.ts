import { cssInterop } from 'nativewind';
import Animated from 'react-native-reanimated';

// Reanimated bileşenleri NativeWind'in çekirdek listesinde yok: className → style eşlemesini elle aç.
cssInterop(Animated.View, { className: 'style' });
