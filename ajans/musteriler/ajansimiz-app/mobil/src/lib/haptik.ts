import * as Haptics from 'expo-haptics';

const native = process.env.EXPO_OS !== 'web';

export const haptik = {
  hafif: () => native && Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light),
  orta: () => native && Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium),
  secim: () => native && Haptics.selectionAsync(),
  basari: () => native && Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success),
  uyari: () => native && Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning),
};
