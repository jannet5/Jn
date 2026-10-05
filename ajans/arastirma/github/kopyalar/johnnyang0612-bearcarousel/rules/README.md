# rules/ — 可更新的判斷力

這個資料夾是 BearCarousel 的腦。

引擎決定一張圖**畫得出來**，rulepack 決定一張圖**該不該被放出去**。
自動對齊是護城河的一半，這裡是另一半。

## 什麼是 rulepack

一份版本化、機器可讀的規則資料。它回答的是判斷題，不是計算題：

- 這張的主標超過幾個字就該擋？
- 只要 6 張的時候，該砍哪一張、該併哪兩張？
- 這一型框架的第 3 張該問使用者什麼？
- 什麼樣的內容值得被收藏，什麼樣的只是好看？

| 檔案 | 內容 | 對應 SPEC |
|---|---|---|
| `VERSION` | 一行 semver | §4.8 |
| `CHANGELOG.md` | 每版改了什麼 | §4.8 |
| `base/boundaries.json` | 設計邊界：logo／標題／內容／版面／顏色／CTA／語言 | §4.5 |
| `base/viral.json` | 內容判準：演算法目標、6 種 hook、逐張硬標準、紅線 | §4 |
| `base/frameworks.json` | 六型框架：張數區間、每張的角色與 ask／example | §3.2 §4.7 |
| `base/restructure.json` | 改張數時怎麼重組敘事 | §4.6 |

## 三個前端，同一份判斷

```
                    rules/base/*.json
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
   engine/audit.py    web/index.html    使用者的 AI agent
   （出圖後把關）      （填的時候提示）    （代填、代改、代交件）
```

人用 UI 手填、agent 代填，走的是同一份 rulepack 與同一顆引擎——
所以人跟 agent 產出的品質邊界一致。這是刻意的設計，不是巧合。

**程式裡不硬編任何門檻。** 所有數值都從 JSON 讀：
框架與 ask 讀這裡，字數與對比的實際門檻讀 brand JSON 的 `limits` 區塊
（見 `brands/_blank.json`）。rulepack 的 `brand_limits_key` 欄位就是這兩者的接點——
它告訴你這條規則的門檻該去 brand 的哪個 key 調。

## 誠實標記：規則 ≠ 已實作

`boundaries.json` 每條規則都有這三個欄位，請先看它們再相信任何一條規則會生效：

| 欄位 | 意思 |
|---|---|
| `severity` | 這個工具**今天實際**的行為 |
| `spec_severity` | BUILD SPEC 原本要求的強度 |
| `enforced` | `true` ＝ `engine/audit.py` 真的會產生這個 finding；`false` ＝ 只是引導，沒有任何程式檢查它 |

兩個 severity 不一樣時，`note` 會說明為什麼。
未實作與實作了但沒作用的規則，集中列在 `boundaries.json` 的 `known_gaps`。

寫得漂亮但沒人執行的規則，比沒有規則更危險——它會讓人以為有東西在把關。

## 怎麼更新

改判斷力＝發一版新的 rulepack，不動 `engine/`、不動 UI。

1. 改 `base/` 底下的 JSON。
2. 更新 `VERSION`（semver 規則見 `CHANGELOG.md` 開頭）。
3. 在 `CHANGELOG.md` 開一則新版本，寫清楚改了什麼、為什麼。
4. 跑一次驗證：

   ```bash
   python -c "import json,glob; [json.load(open(f,encoding='utf-8')) for f in glob.glob('rules/**/*.json',recursive=True)]; print('rules json ok')"
   ```

5. 如果這次改動需要 engine 配合（例如新增一條 `enforced: true` 的規則），
   那就是兩件事：先發 rulepack 標 `enforced: false`，
   engine 跟上之後再發一版把它翻成 `true`。
   **不要在 rulepack 裡宣稱程式做得到的事。**

改規則的時候順手問自己一句：這條門檻是從哪一張沒人看完的圖學來的？
答不出來的規則，多半不該加。

## free / pro 分層

對齊「免費衝滲透、付費賣判斷」：付費買的不是功能鎖，是**判斷力持續更新**。
我們每次調 viral 規則、框架、邊界，付費用戶的圖就自動變聰明一點。

| | 內容 | 狀態 |
|---|---|---|
| `rules/base` | 6 套框架、基礎 ask／example、基礎設計邊界、自適應重組演算法 | **已存在，免費** |
| `rules/pro` | 更多框架與視覺風格、依主題自動建議框架、Hook 打分、進階 AI 起草 prompt、更細的爆點與收藏優化規則 | **尚未存在** |

`rules/pro/` 目前是計畫，不是產品。這個 repo 裡沒有這個資料夾，
也沒有任何程式會去讀它——不要照著它寫整合。
真的做出來的時候，載入順序會是 `base` 先、`pro` 疊上去覆寫，
沒有 pro 的人拿到的仍然是一套完整可用的工具。
