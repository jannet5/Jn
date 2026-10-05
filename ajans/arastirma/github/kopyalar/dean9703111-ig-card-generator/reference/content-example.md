@cover
[badge] ⚡ Claude 生態系 · 2026

[hero] !!90 秒!!

# 分清楚==三個名詞==

Skill、Plugin、MCP 差在哪？
這 10 張卡，一次講完。

===
@point
## 先講結論

[flow] Skill -> Plugin -> MCP

1. **Skill**：一條指令＝一套固定流程
2. **Plugin**：一次安裝＝一整組工具
3. **MCP**：一個連接器＝接上真實服務

[tip] 記法——Skill 是招式、Plugin 是裝備包、MCP 是插座。

===
@point
## 什麼時候該包成 Skill？

[stat] 3 次 | 同一件事重複超過 3 次，就值得包起來

<!-- source: 個人工作流觀察，非正式統計 -->

[vs] 一次性任務、發散討論，別包 | 格式固定、會一直重複，包起來

===
@code
## Skill 的骨架，長這樣

```yaml SKILL.md
name: promo-writer
description: 把長文萃取乾貨，轉成宣傳貼文
trigger: 「幫我寫宣傳文」
steps:
  - 萃取重點        # 先抓乾貨
  - 套用語氣範本    # 保留個人風格
  - 自我檢查後輸出  # criteria 全過才交
```

[warn] description 寫太籠統，觸發就會失準。

===
@point
## 接上 MCP 之後，對話長這樣

[chat] 我: 幫我把下週二下午排一場對焦會議
[chat] AI: 已查到週二 14:00 有空檔，行程建立好了 ✓

沒接 MCP 的 AI 只能「說」，接上才能替你**做**。

===
@point
## 時間差多少？

[timeline] *手動貼上改錯 | 平均 8 小時，還常常漏改
[timeline] 包成 Skill 之後 | 20 分鐘跑完，穩定重現

[shot] assets/demo.png | 實際跑起來的畫面

===
@point
## 出手前，自檢四題

[check] 這件事重複做超過 3 次了嗎？
[check] 步驟固定、格式明確嗎？
[check] 需要接真實服務（信箱／行事曆）嗎？
[check] * 先動手做過一次，再回頭包裝

===
@point
> 你不是不會用 AI，是還沒把重複的事，收進一個可以重複按下的按鈕。
> — Dean

===
@recap
## 一張存起來，等於整篇

===
@cta
# 覺得有用？

下一篇：我的 ==8 個自製 Skill== 全公開

追蹤不迷路
留言「!!SKILL!!」搶先拿清單
