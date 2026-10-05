# 怎麼自己加一套新框架

丟一個 JSON 檔進 `templates/frameworks/`，就多一套框架。**不用改任何程式**。
「框架」決定的是一套輪播有幾張、每張講什麼角色、用哪種版型；文案本身寫在 content 檔裡。

## 檔案放哪

```
templates/frameworks/<你的框架名>.json   ← 結構（幾張、什麼順序）
templates/styles/<你的風格名>.json       ← 外觀（字級、顏色配置）
```

檔名要用小寫英文加連字號，例如 `pas.json`、`case.json`。

## 欄位長什麼樣

```
id           檔名去掉 .json，必須完全一致（pas.json → "id": "pas"）
name         繁中短名，選單上看到的名字
description  一句話：這套框架適合講什麼
best_for     一句話：什麼情況該選它
slides       陣列，一個元素就是一張
  role       這張的角色，小寫英文加底線（hook / problem / cta …）
  pattern    版型，只能是下面四個其中一個
  guide      繁中一句話，告訴填文案的人這張要寫什麼
```

**不要加上面沒列到的欄位。**

## 四種版型（只有這四種，寫錯不會出圖）

| pattern  | 什麼時候用 |
|----------|-----------|
| `hook`   | 純大字。開場、觀點、轉折、CTA 都用它 |
| `photo`  | 文字壓在照片上（會自動描邊）。創辦人講話、真實案例用 |
| `list`   | 條列 3–5 項時用。做法、錯誤、檢查點 |
| `mockup` | 要放圖表、截圖、數據圖的那張 |

## 兩條硬規則

1. **第一張的 `role` 一定是 `hook`** — 滑到你就要被抓住。
2. **最後一張的 `role` 一定是 `cta`，而且整套只能有一張 `cta`** — 一次只叫讀者做一件事。

張數建議 6–10 張。

## guide 要怎麼寫

寫給不會寫程式的人看，要具體到他知道該打什麼字。

- 好：`真實案例，要有金額／時間／人數。`
- 好：`3–5 條可執行的做法，每條 ≤18 字。`
- 壞：`寫得好一點。`
- 壞：`這裡放內容。`

## 最小可用範例（複製改就能跑）

存成 `templates/frameworks/my-framework.json`：

```json
{
  "id": "my-framework",
  "name": "我的框架",
  "description": "用三個步驟把一個觀念講完的簡短型。",
  "best_for": "主題單純、不需要鋪很多背景的時候",
  "slides": [
    { "role": "hook",   "pattern": "hook", "guide": "最痛的一句，疑問或數字擇一。" },
    { "role": "reason", "pattern": "hook", "guide": "為什麼會這樣，講機制不要講感想。" },
    { "role": "steps",  "pattern": "list", "guide": "3–5 條做法，每條 ≤18 字。" },
    { "role": "proof",  "pattern": "photo", "guide": "真實案例，要有金額／時間／人數。" },
    { "role": "cta",    "pattern": "hook", "guide": "單一動作。只有這張能放 CTA。" }
  ]
}
```

## 存檔後怎麼確認沒寫壞

跑一次出圖，看有沒有紅字（RED）：

```bash
python -c "import json; json.load(open('templates/frameworks/my-framework.json', encoding='utf-8'))"
```

沒有噴錯就代表 JSON 格式對了。接著照 README 出一次圖，稽核報告全綠才算完成。
常見錯誤：`pattern` 打錯字、`id` 跟檔名不一致、最後一張忘了設成 `cta`、少一個逗號。
