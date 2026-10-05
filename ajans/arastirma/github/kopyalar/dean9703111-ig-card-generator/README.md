# IG Card Generator

一個 **Claude Code Skill**：把知識素材（文章、講稿、筆記、逐字稿，甚至只有一個主題）提煉成 IG 輪播知識圖卡——封面 hook → 每卡一概念 → 結尾 CTA。

內容用約定的 Markdown DSL 撰寫，經 lint 把關（卡數、字數、禁用語、來源考證⋯⋯全過才准出圖），再由 build script 以 Puppeteer 產出 1080×1350 PNG 系列圖，深／淺色主題一次搞定。

| 深色主題 | 淺色主題 |
|---|---|
| ![深色主題範例](example/skill-plugin-mcp/assets/cards/dark/contact-sheet.png) | ![淺色主題範例](example/skill-plugin-mcp/assets/cards/light/contact-sheet.png) |

## 特色

- **素材進、圖卡出**：Claude 讀你的素材 → 提煉核心主張與 hook → 寫成 DSL → lint → 出圖，全流程在對話中完成
- **Markdown DSL**：內容只寫語意（`[stat]`、`[vs]`、`[chat]`、`[bar]`、`[ring]`、`[flow]`、`[timeline]`⋯⋯20+ 種元件），長相由品牌 token 決定，視覺永遠一致
- **Lint 把關**：卡數 5–10、封面必含 hook、一卡一概念、字數上限、CTA 必含行動動詞與留言關鍵字、禁 AI 腔、未考證數字警告——error 修到零才出圖
- **雙主題輸出**：深色＝黑體科技感、淺色＝明體雜誌感，`--theme both` 一次出兩套
- **溢版檢查**：任何一張內容超出畫布即中止並指出卡號，不靠縮字塞版
- **附加產出**：`index.html` 瀏覽器預覽（可切換深淺色）、`alt-text.md`（IG 替代文字）、contact sheet 總覽圖、`deck.json` 結構資料
- **封面 A/B**：`--covers` 模式產出多個 hook 變體的並排對比圖，挑完再正式出圖

## 安裝

### 1. 安裝為 Claude Code Skill

```bash
# 專案層級（推薦，放在你的內容專案裡）
git clone https://github.com/dean9703111/ig-card-generator.git .claude/skills/ig-card-generator

# 或個人層級（所有專案都能用）
git clone https://github.com/dean9703111/ig-card-generator.git ~/.claude/skills/ig-card-generator
```

### 2. 安裝依賴

在 skill 目錄（或你的 repo 根目錄）執行：

```bash
npm i -D yaml puppeteer
```

> claude.ai 沙盒環境改用 `npm i yaml puppeteer-core`（自動偵測系統 Chrome）。

### 3. 建立品牌設定

在你的內容專案根目錄建立 `config/global.yaml`（handle、主題色、背景風格、頭像等），完整範例見 [reference/config-example.yaml](reference/config-example.yaml)。

## 使用方式

### 跟 Claude 說就好

安裝後在 Claude Code 直接說：

> 「把這篇文章做成 IG 圖卡」「幫我做一組知識圖卡」「做輪播圖」

Claude 會依 [SKILL.md](SKILL.md) 的流程：判斷輸入 → 產出提煉摘要（核心主張、受眾、hook 公式、排卡結構）給你確認 → 寫 `content.md` → lint → 出圖。

### 手動執行 CLI

```bash
# Lint：criteria 逐項檢查，全過才出圖
node scripts/lint.mjs <deck-dir>

# Build：出 PNG（依 config 的 brand.theme）
node scripts/build.mjs <deck-dir>

# 常用旗標
node scripts/build.mjs <deck-dir> --theme both   # 深淺各出一套
node scripts/build.mjs <deck-dir> --only 3,5     # 只重出第 3、5 張
node scripts/build.mjs <deck-dir> --ratio 9:16   # 改輸出比例（4:5 / 1:1 / 9:16）
node scripts/build.mjs <deck-dir> --covers       # 封面 A/B 對比模式
```

### 試跑範例

```bash
npm i -D yaml puppeteer
npm run smoke   # = node scripts/build.mjs example/skill-plugin-mcp --theme both
```

會產出 11 張 × 2 主題的完整範例（涵蓋大部分 DSL 元件），輸出在 `example/skill-plugin-mcp/assets/cards/`。

## DSL 快速一覽

```markdown
---
title: 我的圖卡
---
@cover
[badge] ⚡ 系列標籤
# 每天用 AI，怎麼還是==沒變快==？
副標補一句轉折

===
@point
## 先講結論
[stat] 3 次 | 重複超過 3 次就包起來
[tip] 這是這張卡的記憶點

===
@cta
# 想要完整範本？
留言 **關鍵字** 我私訊你
```

`===` 分卡；`@cover / @point / @code / @cta / @recap / @section` 定卡型；`[stat]`、`[vs]`、`[chat]`、`[bar]`、`[ring]`、`[check]`、`[flow]`、`[timeline]`、`[ok]/[no]`、`[warn]`、`[tip]`⋯⋯等 20+ 元件見 [SKILL.md](SKILL.md#dsl-語法約定) 完整表格，完整範例見 [reference/content-example.md](reference/content-example.md)。

## 專案結構

```
ig-card-generator/
├── SKILL.md               # Skill 主文件（工作流程、DSL 語法、內容鐵律）
├── scripts/
│   ├── dsl.mjs            # DSL 解析＋渲染
│   ├── config.mjs         # 兩層 config 載入（global → deck → frontmatter）
│   ├── lint.mjs           # criteria 檢查
│   └── build.mjs          # index.html + Puppeteer 出 PNG
├── reference/
│   ├── base.html          # 渲染模板（design token + 元件 CSS）
│   ├── hooks.md           # Hook 公式庫、排卡模板、CTA 公式
│   ├── content-example.md # 完整 DSL 範例
│   └── config-example.yaml
├── test/                  # node --test 單元測試（golden 快照）
└── example/               # 可直接 build 的範例 deck
```

## 測試

```bash
npm test   # = node --test test/*.test.mjs
```

## Troubleshooting

- **找不到 Chrome**：build 會自動掃描常見安裝位置；都沒有時手動設 `PUPPETEER_EXECUTABLE_PATH`
- **沙盒中文字型出現豆腐字**：安裝 `fonts-noto-cjk`
- 更多細節見 [SKILL.md](SKILL.md#依賴與-troubleshooting)

## 內容鐵律

1. **禁止捏造**：統計數字、價格、案例必須出自素材或可考證
2. **一卡一概念**：想講兩件事就拆卡
3. **禁 AI 腔**：lint 強制檢查禁用語
4. **CTA 完整**：行動動詞＋留言關鍵字
5. **封面先共鳴**：封面不是摘要，主標用讀者會說出口的痛點句
6. **禁止縮字塞版**：溢版一律精簡內容或拆卡

## License

[MIT](LICENSE)
