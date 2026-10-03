# Yol D — shadcn/ui registry bloğu (v0'ın hedef yığını; AI'sız taban çizgisi)
```bash
npm create vite@latest d-shadcn-registry -- --template react-ts --no-interactive
cd d-shadcn-registry && npm i && npm i tailwindcss @tailwindcss/vite && npm i -D @types/node
# tsconfig: "@/*" → "./src/*" yolu; vite.config.ts: tailwindcss() eklentisi + alias + base "./"
npx shadcn@4.21.1 init -y -b radix -p nova --no-monorepo </dev/null
npx shadcn@4.21.1 add dashboard-01 -y </dev/null
# src/App.tsx = bloğun page.tsx'i (yalnız import yolları), TooltipProvider ile sarılı
npm run build
```
Bulgular: preset sorusu `-p` verilmeden etkileşimli kalıyor; blok TS strict `noUnusedLocals` ile derlenmiyor
(site-header.tsx kullanılmayan `Button` importu) → bayrak kapatıldı; `/avatars/shadcn.jpg` 404; JS paketi ~1 MB.
