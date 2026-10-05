---
name: ig-card-generator
description: 把知識素材（文章、講稿、筆記、逐字稿，或只有一個主題）提煉成 IG 輪播知識圖卡：封面 hook → 每卡一概念 → 結尾 CTA。以約定 Markdown DSL 撰寫，經 lint（criteria 全過）後由 build script 產出 1080×1350 PNG 系列圖（支援深／淺色主題）。當使用者說「做 IG 圖卡」「知識圖卡」「輪播圖」「carousel」「把這篇做成圖卡」「IG 貼文圖」「圖卡系列」時使用。出圖完成後可銜接 promo-writer 產生貼文 caption。
---

# IG Card Generator

素材 → 提煉（hook＋排卡）→ `content.md`（DSL）→ `node scripts/lint.mjs <dir>` 全過 → `node scripts/build.mjs <dir>` → `assets/cards/<theme>/NN.png` →（選用）promo-writer 產 caption

## 專案結構

```
.agents/skills/ig-card-generator/
├── SKILL.md
├── scripts/
│   ├── dsl.mjs        # DSL 解析＋渲染（build / lint 共用）
│   ├── config.mjs     # 兩層 config 載入（global → deck → frontmatter）
│   ├── lint.mjs       # criteria 檢查（可獨立執行）
│   └── build.mjs      # index.html + Puppeteer 逐卡出 PNG
├── reference/
│   ├── base.html          # 渲染模板（design token + 元件 CSS）
│   ├── hooks.md           # Hook 公式庫、排卡模板、每卡規則、CTA 公式
│   ├── content-example.md # 完整 DSL 範例
│   └── config-example.yaml
├── test/                  # node --test 單元測試（golden 快照）
└── example/               # 可直接 build 的煙霧測試 deck
    ├── config/global.yaml
    └── skill-plugin-mcp/{config.yaml, content.md}

<root>/
├── config/global.yaml     # 品牌層（handle、theme、accent/hot、background、avatar、criteria）
└── <deck-dir>/
    ├── config.yaml         # 本副牌覆蓋（選用）
    ├── content.md          # DSL 內容
    ├── brief.md            # cta_keyword 等提示（選用）
    ├── index.html          # build 產出（瀏覽器預覽，右下角切深淺色）
    ├── alt-text.md         # build 產出（每張卡的 IG 替代文字）
    ├── deck.json           # build 產出（卡片結構、字數、密度、lint 結果）
    ├── covers-preview.html # --covers 模式產出（封面並排對比預覽）
    ├── covers-preview.png  # --covers 模式產出（對比截圖）
    ├── caption.md          # promo-writer 產出（選用）
    └── assets/cards/{dark,light}/{NN.png, contact-sheet.png}
```

## Workflow

### Step 0：判斷輸入類型

| 情境 | 判斷依據 | 行動 |
|------|----------|------|
| 只有主題 | 一句話主題，無素材 | 先反問 1–2 題（受眾是誰？CTA 要導去哪？），再進 Step 1 |
| 有素材 | 文章／講稿／筆記／逐字稿 | 直接進 Step 1；若素材是單一檔案，deck 預設建立在素材同層的 `ig-card/` |
| 有現有目錄 | 指定既有 deck 資料夾 | 讀取後依需求修 content.md，跳 Step 3 |

### Step 1：提煉（產 content.md 之前，先想清楚）

讀 [hooks.md](reference/hooks.md)，產出**提煉摘要**給使用者確認（素材非常明確時可直接進 Step 2，但摘要仍要在回覆中列出）：

1. **核心主張**：一句話。這副牌只為這句話服務。
2. **受眾**：誰滑到會停下來。
3. **封面共鳴句**：先從素材找「讀者心裡話」或真實痛點，不先寫作者結論；說明為什麼這句會讓目標受眾停下來。
4. **Hook 公式**：六選一＋一句理由。用到數字時，指出數字在素材的哪裡。
5. **排卡結構**：教學型／清單型／對比型／故事型／迷思破解型 五選一，列出每張卡的一句話大綱（5–10 張）。

### Step 2：寫成 DSL content.md

依下方語法約定撰寫。核心轉換原則與 course-page-generator 相同：**萃取重點，不逐字轉錄**；內容盡量落在結構元件內，避免整卡裸段落。

#### DSL 語法約定

| 語法 | 用途 | 範例 |
|------|------|------|
| `===` | 分卡（獨立一行） | — |
| `@cover` `@point` `@code` `@cta` `@recap` `@section` | 卡片類型（卡片第一行） | 第 1 張必為 cover、最後必為 cta；`@recap` 放 CTA 前，自動彙整各卡 `##` 成回顧清單（儲存率槓桿，7 張以上 lint 會提醒）；`@section` 是章節分隔卡（自動編大號輪廓數字＋標題，計字上限 `chars.section` 預設 40） |
| `@卡型 invert` | 卡級明暗反轉（modifier） | `@section invert`——該卡整組 token 翻到相反主題（深牌出淺卡），做滑動明暗節奏；建議整副 1–3 張，用在章節卡／金句卡／CTA |
| `[badge] 文字` | 頂部膠囊標籤 | `[badge] ⚡ Claude 生態系 · 2026` |
| `[hero] 文字` | 巨型主角字（190px 級），封面唯一視覺主角用 | `[hero] !!90 秒!!` |
| `[stat] 數字 \| 標籤` | 巨型數據卡（大數字＋說明） | `[stat] 3 次 \| 重複超過 3 次就包起來` |
| `[vs] 左（✕）\| 右（✓）` | 左右對比欄，連續多行合併成同一格線 | `[vs] 手動改錯 \| 一條指令跑完` |
| `[chat] 角色: 內容` | 對話氣泡；`我/你/user` 靠右 accent，其他靠左，連續行合併 | `[chat] 我: 幫我排週二的會` |
| `[bar] 標籤 \| 顯示值 \| 寬度%` | 橫向比例條；`*` 開頭＝hot 色，連續行合併 | `[bar] *手動 \| 8 小時 \| 100` |
| `[ring] 數字 \| 標籤` | 圓環百分比（0–100，容忍尾隨 %）；連續行合併成一排，`*` 開頭＝hot 色 | `[ring] *92 \| 包成 Skill 後成功率` |
| `[check] 文字` | 自檢清單（☐）；`*` 開頭＝已勾（☑），連續行合併 | `[check] 每個數字都有來源？` |
| `[shot] 路徑 \| 圖說` | 截圖畫框（窗控 chrome＋嵌入本地圖片，路徑相對 deck 目錄） | `[shot] assets/demo.png \| 後台畫面` |
| `[big] 文字` | 一句話 punchline，整卡視覺主角 | `[big] AI 不是顧問，是==隊友==` |
| `[stamp] 文字` | 品牌章印／系列標籤，多用於 CTA | `[stamp] Dean 的 AI 開發工作流` |
| `# 大標` | H1，可連續多行堆疊 | 封面 hook 用 |
| `## 段標` | H2（自帶強調底線），**一卡限一個** | `## 先講結論` |
| 一般段落 | body 文字，連續行合併換行 | — |
| `1. **標題**：說明` | 編號卡盒 | — |
| `- **標題**：說明` | 條列卡盒 | — |
| `[ok] 文字` / `[no] 文字` | ✅／❌ 對照盒 | 對比型必備 |
| `[warn] 文字` | ⚠️ 警示條 | — |
| `[tip] 文字` | 💡 重點框 | 每卡的記憶點 |
| `[flow] A -> B -> *C` | 自適應流程卡；短流程橫向，長流程自動轉 stepper，`*` 開頭＝強調節點 | `[flow] Define -> Generate -> Eval -> *Fix` |
| `[timeline] 標籤 \| 內容` | 縱向時間軸／演進歷程；連續行合併成一組，`*` 開頭＝強調節點 | `[timeline] *2026 \| MCP 讓 AI 接上真實服務` |
| `> 引言` | 引言框（可多行）；最後一行符合 `— 人名` 格式時，該行獨立成出處，不算引言本體 | `> 先想清楚，再動手`<br>`> — Dean` |
| `<!-- source: 出處 -->` | 證據來源註解；獨立一行，不渲染、不計字，收進該卡 `sources`（`<!-- TODO: ... -->` 同理收進 `todos`，lint 會提醒） | `<!-- source: 官方文件 2026 -->` |
| ` ```lang 檔名 ` | 程式碼視窗（窗控＋檔名列＋語法上色） | ` ```yaml SKILL.md ` |
| `***` | 裝飾分隔線 | — |
| `**重點**` | 強調色粗體（品牌 accent） | — |
| `==標記==` | 螢光標記膠囊 | 封面關鍵詞 |
| `!!衝擊!!` | 衝擊色（hook 數字、警告字） | `!!99%!!` |
| `` `code` `` | 行內程式碼 | — |

frontmatter（選填，覆蓋 config）：`title` / `handle` / `ratio` / `theme` / `background`。

自動注入的元件（content.md 不用寫、也不計字）：封面 creator chip（設定 `brand.avatar.src` 時）、封面「滑 →」提示（`layout.swipe_cue`）、內容卡右下大編號水印（`layout.watermark`）、`@recap` 的回顧清單、CTA 收藏暗示（`layout.save_hint`）與追蹤膠囊、footer 的 `EP.NN`（`brand.series.episode`）。

版型自動切換：`@point` 只有 `[big]`／引言（＋段落）時，自動變成置中大字的**全版金句卡**；`@point`（balanced 以下密度）、`@code`、`@recap` 內容自動垂直置中，避免下方大片留白。

#### 封面 A/B（選用）

提煉階段先產 2–3 個 hook 變體，全部寫成開頭連續多張 `@cover`；跑 `build --covers` 出並排對比圖挑選；選定後只留 1 張 `@cover`，其餘刪除，再走正式 Step 3–4。

### Step 3：Lint（criteria 全過才准出圖）

```bash
node .agents/skills/ig-card-generator/scripts/lint.mjs <deck-dir>
```

逐項輸出 pass/fail。**error 修到零、warning 逐條人工確認**後才進 Step 4。criteria 定義在 config 的 `criteria:` 區塊（= Define 這副牌「點先算對」的尺），主要項目：卡數 5–10、封面必含 hook 記號、一卡一概念、字數上限、CTA 必含行動動詞與留言「關鍵字」、禁用語（AI 腔）、未考證數字警告、`require_sources` 開啟時統計式主張卡片必須帶來源註解。

### Step 4：Build 出圖

```bash
node .agents/skills/ig-card-generator/scripts/build.mjs <deck-dir>              # 依 config 的 brand.theme
node .agents/skills/ig-card-generator/scripts/build.mjs <deck-dir> --theme both # 深淺各出一套
```

| 旗標 | 行為 |
|------|------|
| `--theme both` | 深淺各出一套（既有） |
| `--only 3,5` | 只重出指定卡號 PNG；lint／溢版檢查／contact sheet／`deck.json` 照常全跑 |
| `--ratio 9:16` | 覆蓋 `export.ratio` |
| `--covers` | 封面 A/B 對比模式，見上方「封面 A/B」 |

Build 會自動：跑 lint → 注入品牌 token → 產 `index.html` → 產 `alt-text.md`（每張卡的替代文字，發文時貼進 IG 的 alt text 欄）→ Puppeteer **溢版檢查**（任何一張內容超出畫布即中止並指出卡號；封面主標超出 `export.grid_crop` 安全區時另印警告不擋）→ 逐卡截 PNG（尺寸依 `export.ratio`：4:5＝1080×1350、1:1＝1080×1080、9:16＝1080×1920，× `export.scale`）→ 產 `assets/cards/<theme>/contact-sheet.png`（整副縮圖總覽）→ 寫 `deck.json`（卡片結構、字數、密度、lint 結果）。溢版就回 Step 2 精簡文字或拆卡，不要用 `--force` 交差，也不要用縮字塞版。

`--covers` 模式另外產出 `covers-preview.html`／`covers-preview.png`（並排對比圖）＋逐變體 lint 報告，**不**出逐卡 PNG、不寫 `deck.json`；挑定後只留 1 張 `@cover` 再跑正式 build。

### Step 5：串 promo-writer 產 caption（使用者要發文時）

出圖完成後，改用 **promo-writer** skill，以本 deck 的 `content.md`（去除 DSL 記號後的內文）為素材產出貼文文案，並要求：

1. 開頭承接封面 hook（同一個承諾，不要另起爐灶）
2. 結尾回收 CTA 卡的**留言關鍵字**與追蹤 handle
3. 產出存 `<deck-dir>/caption.md`

## 內容鐵律

1. **禁止捏造**：統計數字、價格、案例必須出自素材或可考證；查無來源 → 換非數字 hook 或留 `<!-- TODO: 待補來源 -->`。有明確出處就在卡內加 `<!-- source: 出處 -->`（獨立一行，不渲染、不計字）——lint 對 `%`、`N 倍` 類數字發出的警告，已附 `source` 的卡會跳過；`criteria.require_sources: true` 時，統計式主張卡片沒有 `source` 註解直接判 error。
2. **一卡一概念**：一張 @point 只有一個 `##`。想講兩件事就拆卡。
3. **禁 AI 腔**：criteria 的 `forbidden` 清單（可擴充），lint 強制。
4. **CTA 完整**：行動動詞＋留言「關鍵字」，關鍵字之後 caption 要回收。
5. **封面先共鳴**：封面不是摘要。主標優先用讀者會說出口的痛點句（「每天用 AI，怎麼還是沒變快？」），再用副標補轉折；避免用「流程、模型、工作流」這類抽象詞當第一眼主角。
6. **禁止用縮字塞版**：溢版一律精簡內容或拆卡，build 不提供自動縮字。

## 視覺原則（品牌固定）

- 視覺識別**固定一套**：不新增任意 style pack、不在 content.md 內寫任何色碼或樣式。內容只寫語意（`**` `==` `!!` `[ok]`…），長相由 token 決定。
- 可調視覺變數只放 config：`brand.accent`、`brand.accent_rotation`、`brand.hot`、`brand.background`、`brand.avatar`、`brand.layout`。`accent` / `hot` 各含 dark/light 兩值；**深淺不是反轉是重新映射**——淺色模式的 accent 必須是深色（例：`#FFC24D` → `#7E570F`），確保米色底上對比 ≥ 4.5:1；`--on-acc` 由 build 自動計算（深淺兩種字色都算對比、選較高者）。
- `brand.accent_rotation`（階層換色，選用）：`[{dark, light}, …]` 額外強調色組。palette = base accent ＋ rotation，**每過一張 `@section` 前進一組**，讓讀者滑到一半也知道進入新章節；cover／CTA 永遠用 base accent 維持品牌識別。每組色一樣走 build 的 4.5:1 對比守門（兩主題各查一次）。沒有 `@section` 時 rotation 不生效。
- `brand.background` 控制整副牌背景風格，允許值：`minimal`（乾淨微光）、`grid`（科技格線）、`gradient`（大面積漸層）、`halo`（光圈＋層次，預設）；也可寫成物件形式 `{style: halo, grain: true}`，`grain: true` 疊加噪點紋理。背景「風格」整副統一、不要單卡換 style；要做單卡的明暗節奏，用 `@卡型 invert`（token 級整組反轉），不要自己改色。
- `brand.avatar.src` 指定頭像照片，路徑相對 deck 目錄，例如 `assets/avatar.jpg`；未設定或空字串時不顯示。照片出現在**封面 creator chip**（頭像＋handle，feed 上第一眼認得出是誰）與 `@cta` 的大圓形照片。
- `brand.series.episode` > 0 時，每張卡 footer 顯示 `@handle · EP.NN`，累積系列連載的辨識度。
- `brand.layout.swipe_cue`（預設開）在封面右下角顯示「滑 →」提示；`brand.layout.watermark: number`（預設）在內容卡右下鋪半透明大編號水印，`none` 關閉；`brand.layout.save_hint` 是 CTA 追蹤鈕上方的收藏暗示文字（saves 是演算法權重最高的互動），空字串停用。
- 模板會依每張卡內容密度自動套用 `hero` / `airy` / `balanced` / `dense` 尺度，空間足夠時整組字級、元件、間距一起放大；內容較多時只小幅收斂。不要只為了放大單一標題而硬塞樣式，比例要整體一致。
- `brand.layout.mode` 控制社群視覺語氣：`tutorial` 穩定教學、`punchy` 強化停留、`editorial` 偏雜誌（editorial 同時啟用字型混排：標題明體＋內文黑體，兩主題皆然，強化階層對比）。`brand.layout.cover_visual` 控制封面視覺變體：`none`（無）、`pain-flow`（痛點循環，預設是「問 AI → 複製貼上 → 自己修錯」，可用 `brand.layout.cover_steps` 依素材改成更準確的核心行為）、`checklist`（「你中了幾個」未勾選清單，`cover_steps` 每項一條，最多 4 項）、`vs`（新舊對立，`cover_steps` 必須恰好 2 項，左✕右✓，否則整段不渲染）。`brand.layout.rhythm: varied` 會讓不同主元件卡片有更明顯的節奏差。若目標是增加停留與點進率，優先用 `punchy + pain-flow + varied`。
- `brand.layout.highlight_style` 控制封面／金句的 `==標記==` 渲染：`pill`（膠囊底色，預設）或 `scribble`（手繪圈註；body 內文字不受影響，維持 pill）。
- `export.grid_crop`（`3:4` / `1:1` / `none`）：build 檢查封面主標是否落在此比例的個人頁裁切安全區，超出只印警告不擋。預覽時開 `?guides=1` 可疊裁切輔助線（1:1、3:4 框線＋角落標籤）肉眼確認。
- 深色＝黑體科技感、淺色＝明體雜誌感（字體人格由 token 切換），這是設計的一部分，不要改。

## Config 兩層

| 檔案 | 用途 | 必要性 |
|------|------|--------|
| `config/global.yaml` | 品牌層：handle、theme、accent/hot、background、avatar、export、criteria | 首次建立一次（build 從 deck 目錄向上搜尋 ≤ 4 層） |
| `<deck-dir>/config.yaml` | 本副牌覆蓋（deep merge；arrays 整個取代） | 選用 |

完整範例：[config-example.yaml](reference/config-example.yaml)

## brief.md（選用）

`<deck-dir>/brief.md` 用 YAML frontmatter 放發文提示，目前支援 `cta_keyword`：

```yaml
---
cta_keyword: SKILL
---
```

有此檔且 `cta_keyword` 有值時，lint／build 會核對 CTA 卡文字是否含「該關鍵字」，不含則 `brief.cta` warning。

## 依賴與 Troubleshooting

- 依賴：repo 內 `npm i -D yaml puppeteer`；claude.ai 沙盒則在工作目錄 `npm i yaml puppeteer-core`。
- **claude.ai 沙盒**：上傳後 skill 掛在唯讀路徑（`/mnt/skills/user/ig-card-generator/`），scripts 可直接從該路徑執行——依賴解析內建「工作目錄 fallback」，裝在 cwd 的 `node_modules` 也找得到。deck 目錄請建在可寫位置（如 `/home/claude/<deck>`），build 只往 deck 目錄寫檔，不會動到 skill 本體。
- **找不到 Chrome**：build 依序自動掃描 `PUPPETEER_EXECUTABLE_PATH` → macOS Chrome → `/usr/bin/chromium*` → `PUPPETEER_CACHE_DIR` → `~/.cache/puppeteer` → 所有 `/home/*/.cache/puppeteer`（root 執行沙盒時的常見位置，已內建，不再需要手動 export cache dir）。都沒有時才需手動指定 `PUPPETEER_EXECUTABLE_PATH`。
- **root 執行**：`--no-sandbox` 等旗標已內建於 launch args。
- **沙盒中文字型**：模板字體堆疊已含 `Noto Sans/Serif CJK TC`；若截圖出現豆腐字，安裝 `fonts-noto-cjk`。
- **煙霧測試**：`node scripts/build.mjs example/skill-plugin-mcp --theme both`（於 skill 目錄執行）應產出 11 張 × 2 主題（涵蓋 hero/stat/vs/chat/timeline/ring/check/recap/section＋invert＋accent_rotation 等元件）。
- **單元測試**：`node --test .agents/skills/ig-card-generator/test/`。

## Reference Files

- Hook 公式與排卡模板：[hooks.md](reference/hooks.md)
- DSL 完整範例：[content-example.md](reference/content-example.md)
- Config 範例：[config-example.yaml](reference/config-example.yaml)
- 渲染模板：[base.html](reference/base.html)
