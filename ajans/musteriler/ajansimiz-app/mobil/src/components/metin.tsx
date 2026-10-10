import { Text, type TextProps } from 'react-native';

type Tur = 'display' | 'h2' | 'h3' | 'lead' | 'body' | 'small' | 'etiket';

const siniflar: Record<Tur, string> = {
  display: 'font-heavy text-display',
  h2: 'font-heavy text-h2',
  h3: 'font-heavy text-h3',
  lead: 'font-body text-lead',
  body: 'font-body text-body',
  small: 'font-medium text-small',
  etiket: 'font-medium text-small',
};

export function Metin({
  tur = 'body',
  renk,
  className = '',
  style,
  ...rest
}: TextProps & { tur?: Tur; renk?: string; className?: string }) {
  return (
    <Text
      className={`${siniflar[tur]} ${className}`}
      style={[renk ? { color: renk } : null, style]}
      maxFontSizeMultiplier={1.6}
      {...rest}
    />
  );
}
