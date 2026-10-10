# n8n şablonu 8877 — Automate Instagram carousel creation with GPT-5, Nano Banana, and Blotato

- Kaynak URL: https://n8n.io/workflows/8877-automate-instagram-carousel-creation-with-gpt-5-nano-banana-and-blotato/
- Ham JSON: https://api.n8n.io/api/templates/workflows/8877 (curl ile çekildi, 2026-10-10)
- Video: id22R7iBTjo — "I Built a Viral Instagram Carousel Machine (n8n + Nanobanana + Blotato)" — Automate with Marc
- Yazar: marconi (Automate With Marc)

## Şablon açıklaması (aynen)

## 🎨 Instagram Carousel & Caption Generator on Autopilot (GPT-5 + Nano Banana + Blotato + Google Sheets)

### Description

**Watch the full step-by-step tutorial on YouTube:**
**https://youtu.be/id22R7iBTjo**
![automateinstagramcarouselnanobananan8nbuildalong 1.jpg](fileId:2970)

Disclaimer (self-hosted requirement):
This template assumes you have valid API credentials for OpenAI, Wavespeed/Nano Banana, Blotato, and Google.
If using n8n Self-Hosted, ensure HTTPS access and credentials are set in your instance.

### How It Works

Chat Trigger – Receive a topic/idea (e.g. “5 best podcast tips”).

Image Prompt Generator (GPT-5) – Creates 5 prompts using the “Hook → Problem → Insight → Solution → CTA” framework.

Structured Output Parser – Formats output into a JSON array.

Generate Images (Nano Banana) – Converts prompts into high-quality visuals.

Wait for Render – Ensures image generation completes.

Fetch Rendered Image URLs – Retrieves image links.

Upload to Blotato – Hosts and prepares images for posting.

Collect Media URLs – Gathers all uploaded image URLs.

Log to Google Sheets – Stores image URLs + timestamps for tracking.

Caption Generator (GPT-5) – Writes an SEO-friendly caption.

Merge Caption + Images – Combines data.

Post Carousel (Blotato) – Publishes directly to Instagram.

### Step-by-Step Setup Instructions
1) Prerequisites

n8n (Cloud or Self-Hosted)

OpenAI API Key (GPT-5)

Wavespeed API Key (Nano Banana)

Blotato API credentials (connected to Instagram)

Google Sheets OAuth credentials

2) Add Credentials in n8n

OpenAI: Settings → Credentials → Add “OpenAI API”

Wavespeed: HTTP Header Auth (e.g. Authorization: Bearer &lt;API_KEY&gt;)

Blotato: Add “Blotato API”

Google Sheets: Add “Google Sheets OAuth2 API”

3) Configure & Test

Run with an idea like “Top 5 design hacks”.

Check generated images, caption, and logged sheet entry.

Confirm posting works via Blotato.

4) Optional

Add a Schedule Trigger for weekly automation.

Insert a Slack approval loop before posting.

### Customization Guide

✏️ Change design style: Modify adjectives in the Image Prompt Generator.

📑 Adjust number of slides: Change Split node loop count.

💬 Tone of captions: Edit Caption Generator’s system prompt.

⏱️ Adjust render wait time: If image generation takes longer, increase the Wait node duration from 30 seconds to 60 seconds or more.

🗂️ Log extra data: Add columns in Google Sheets for campaign or topic.

🔁 Swap posting tool: Replace Blotato with your scheduler or email node.

### Requirements

OpenAI API key (GPT-5 or compatible)

Wavespeed API key (Nano Banana)

Blotato API credentials

Google Sheets OAuth credentials

n8n account (Cloud or Self-Hosted)

## Bağlantılar (connections, JSON'dan)

- Code → Append row in sheet (main, giriş 0)
- Split Out → Nano Banana (main, giriş 0)
- Nano Banana → Wait For Render (main, giriş 0)
- Wait For Render → Get Nano Banana Image Result (main, giriş 0)
- Caption Generator → Merge Caption + Images (main, giriş 1)
- OpenAI Chat Model → Image Prompt Generator (ai_languageModel, giriş 0)
- Append row in sheet → Merge Caption + Images (main, giriş 0)
- Image Prompt Generator → Split Out (main, giriş 0)
- Image Prompt Generator → Caption Generator (main, giriş 0)
- Structured Output Parser → Image Prompt Generator (ai_outputParser, giriş 0)
- When chat message received → Image Prompt Generator (main, giriş 0)
- NOT: Yayınlanan JSON'da `Get Nano Banana Image Result → Upload media → Code` ve `Merge Caption + Images → Post to Instagram` bağlantıları ve Blotato düğümlerinin parametreleri boş/eksik geliyor (şablon temizlenirken düşmüş). Videodaki akışa göre elle bağlanmalı.

## Düğümler ve parametreler (aynen)

### When chat message received — `@n8n/n8n-nodes-langchain.chatTrigger` v1.3

```json
{
 "options": {}
}
```

### Image Prompt Generator — `@n8n/n8n-nodes-langchain.agent` v2.2

```json
{
 "text": "={{ $json['IG Idea'] }}",
 "options": {
  "systemMessage": "=You are an expert Viral Instagram Carousel creator. Your role is to create carousel image prompt for Nano Banana. You'll receive a user prompt to give you the topic/ideas for the carousel.\n\nHere's the framework that you should use in coming up with the content:\nViral 5-Framework Carousel (Automation Demo)\nSlide 1 — Hook (Stop the Scroll)\n\nBig, bold, curiosity-driven opener.\n👉 “Stop wasting hours making Instagram posts 👇”\n\nSlide 2 — Problem (Pain Point)\n\nCall out the frustration that everyone feels.\n👉 “Posting daily feels impossible. You design… you write… and still barely grow.”\n\nSlide 3 — Insight (The A-ha Moment)\n\nDrop the truth bomb about why carousels matter.\n👉 “Carousels get more saves + shares than any other format.\nMore saves → more reach → more chance to go viral.”\n\nSlide 4 — Solution (Your Workflow Demo)\n\nShow your automation stack clearly.\n👉 “Here’s how to post viral carousels on autopilot:\n1️⃣ Create polished slides with NanoBanana\n2️⃣ Format your post in n8n\n3️⃣ Auto-post with Blotato”\n\n(Visually show the workflow screenshots or arrows → to keep it swipe-worthy.)\n\nSlide 5 — CTA (Engagement Driver)\n\nEnd with a strong action.\n👉 “Save this workflow. Share it with a friend.\nAnd start going viral without the grind.”\n\n##Rules\nReturn the 5 prompts as a JSON object with a field called ‘prompts’ that contains an array of the prompt strings.\n\nPrompt1 - Prompt to Nano Banana (Image 1)\n\nPrompt2 - Prompt to Nano Banana (Image 2)\n\nPrompt3 - Prompt to Nano Banana (Image 3)\n\nPrompt4 - Prompt to Nano Banana (Image 4)\n\nPrompt5 - Prompt to Nano Banana (Image 5)\n\n\n## Example:\n\nImage 1\n\nPrompt to NanoBanana:\n“Minimalist bold Instagram carousel cover, dark background with neon accent, eye-catching text space, viral growth theme.”\n\nImage 1 Text (Header & Subheader):\nHeader: Stop Wasting Hours on IG Posts\nSubheader: Here’s how to go viral without the grind 🚀\n\nImage 2\n\nPrompt to NanoBanana:\n“Frustrated person at laptop surrounded by messy drafts and notes, cartoon style, editable blank space for text overlay.”\n\nImage 2 Text:\nHeader: The Problem\nSubheader: Posting daily feels impossible. You design, you write… and growth stays flat.\n\nImage 3\n\nPrompt to NanoBanana:\n“Infographic-style design showing upward trending arrow with Instagram logo, colorful and clean, strong highlight space for text.”\n\nImage 3 Text:\nHeader: The A-Ha Moment\nSubheader: Carousels = saves + shares → algorithm boost → viral potential\n\nImage 4\n\nPrompt to NanoBanana:\n“Automation workflow concept — arrows connecting AI brain → workflow nodes → Instagram app logo, sleek tech aesthetic.”\n\nImage 4 Text:\nHeader: The Solution\nSubheader: NanoBanana + n8n + Blotato = Viral Carousels on Autopilot\n\nImage 5\n\nPrompt to NanoBanana:\n“Bright clean design with Instagram ‘save’ and ‘share’ icons, motivational style, bold colors, clear CTA space.”\n\nImage 5 Text:\nHeader: Your Turn\nSubheader: Save this, share it, and start posting smarter.\n\nInstagram Carousel Caption:\n\n✨ Carousels are the #1 way to grow on Instagram in 2025.\nThe problem? They take HOURS to design and post consistently.\n\nHere’s the 3-step automation workflow I use to stay consistent without burning out:\n1️⃣ Generate on-brand images in seconds with NanoBanana\n2️⃣ Automate formatting and scheduling with n8n\n3️⃣ Auto-post to Instagram using Blotato\n\nThe result → More saves, more shares, more growth 🚀\n\n💡 Steal this workflow and never miss a post again.\n👉 Save this carousel if you want to try it."
 },
 "promptType": "define",
 "hasOutputParser": true
}
```

### OpenAI Chat Model — `@n8n/n8n-nodes-langchain.lmChatOpenAi` v1.2

```json
{
 "model": {
  "__rl": true,
  "mode": "list",
  "value": "gpt-5",
  "cachedResultName": "gpt-5"
 },
 "options": {}
}
```

### Structured Output Parser — `@n8n/n8n-nodes-langchain.outputParserStructured` v1.3

```json
{
 "jsonSchemaExample": "={\n  \"prompts\": [\n    \"{{prompt1}}\",\n    \"{{prompt2}}\",\n    \"{{prompt3}}\",\n    \"{{prompt4}}\",\n    \"{{prompt5}}\"\n  ]\n}"
}
```

### Split Out — `n8n-nodes-base.splitOut` v1

```json
{
 "options": {},
 "fieldToSplitOut": "output.prompts"
}
```

### Nano Banana — `n8n-nodes-base.httpRequest` v4.2

```json
{
 "url": "https://api.wavespeed.ai/api/v3/google/nano-banana/text-to-image",
 "method": "POST",
 "options": {
  "redirect": {
   "redirect": {}
  }
 },
 "sendBody": true,
 "authentication": "genericCredentialType",
 "bodyParameters": {
  "parameters": [
   {
    "name": "enable_base64_output",
    "value": "false"
   },
   {
    "name": "enable_sync_mode",
    "value": "false"
   },
   {
    "name": "output_format",
    "value": "png"
   },
   {
    "name": "prompt",
    "value": "={{ $json['output.prompts'] }}"
   }
  ]
 },
 "genericAuthType": "httpHeaderAuth"
}
```

### Get Nano Banana Image Result — `n8n-nodes-base.httpRequest` v4.2

```json
{
 "url": "=https://api.wavespeed.ai/api/v3/predictions/{{ $json.data.id }}/result",
 "options": {
  "redirect": {
   "redirect": {}
  }
 },
 "authentication": "genericCredentialType",
 "genericAuthType": "httpHeaderAuth"
}
```

### Upload media — `@blotato/n8n-nodes-blotato.blotato` v2

```json
{}
```

### Code — `n8n-nodes-base.code` v2

```json
{
 "jsCode": "// Combine all 'url' fields from incoming items into a single array\nreturn [\n  {\n    json: {\n      urls: items.map(item => item.json.url)\n    }\n  }\n];"
}
```

### Append row in sheet — `n8n-nodes-base.googleSheets` v4.7

```json
{
 "operation": "append",
 "sheetName": {
  "__rl": true,
  "mode": "list",
  "value": ""
 },
 "documentId": {
  "__rl": true,
  "mode": "list",
  "value": ""
 }
}
```

### Caption Generator — `@n8n/n8n-nodes-langchain.openAi` v1.8

```json
{
 "modelId": {
  "__rl": true,
  "mode": "list",
  "value": "gpt-5",
  "cachedResultName": "GPT-5"
 },
 "options": {},
 "messages": {
  "values": [
   {
    "content": "=Prompt 1: {{ $json.output.prompts[0] }}\nPrompt 2: {{ $json.output.prompts[1] }}\nPrompt 3: {{ $json.output.prompts[2] }}\nPrompt 4: {{ $json.output.prompts[3] }}\nPrompt 5: {{ $json.output.prompts[4] }}"
   },
   {
    "role": "system",
    "content": "=You are an expert Instagram Caption Agent. You will receive a set of prompts that were used to generate Instagram carousel images. Based on these prompts, craft a compelling caption that is:\n\nHigh-Hook: Start with a strong, attention-grabbing opening line.\n\nSEO-Optimized: Naturally include relevant keywords that increase discoverability.\n\nEngaging: Encourage saves, shares, and comments through questions, CTAs, or relatable phrasing.\n\nPlatform-Ready: Use a concise, mobile-friendly writing style with line breaks for readability.\n\nHashtag-Savvy: Include a well-balanced mix of trending and niche hashtags (without overstuffing).\n\nThe final caption should feel natural, authentic, and designed to maximize reach and engagement on Instagram.\n\n##Rules\nOutput only post-ready Instagram Caption without any explanation."
   }
  ]
 }
}
```

### Post to Instagram — `@blotato/n8n-nodes-blotato.blotato` v2

```json
{}
```

### Wait For Render — `n8n-nodes-base.wait` v1.1

```json
{
 "amount": 60
}
```

### Merge Caption + Images — `n8n-nodes-base.merge` v3.2

```json
{
 "mode": "combine",
 "options": {},
 "combineBy": "combineAll"
}
```

## Image Prompt Generator system prompt (okunur biçim, aynen)

```
You are an expert Viral Instagram Carousel creator. Your role is to create carousel image prompt for Nano Banana. You'll receive a user prompt to give you the topic/ideas for the carousel.

Here's the framework that you should use in coming up with the content:
Viral 5-Framework Carousel (Automation Demo)
Slide 1 — Hook (Stop the Scroll)

Big, bold, curiosity-driven opener.
👉 “Stop wasting hours making Instagram posts 👇”

Slide 2 — Problem (Pain Point)

Call out the frustration that everyone feels.
👉 “Posting daily feels impossible. You design… you write… and still barely grow.”

Slide 3 — Insight (The A-ha Moment)

Drop the truth bomb about why carousels matter.
👉 “Carousels get more saves + shares than any other format.
More saves → more reach → more chance to go viral.”

Slide 4 — Solution (Your Workflow Demo)

Show your automation stack clearly.
👉 “Here’s how to post viral carousels on autopilot:
1️⃣ Create polished slides with NanoBanana
2️⃣ Format your post in n8n
3️⃣ Auto-post with Blotato”

(Visually show the workflow screenshots or arrows → to keep it swipe-worthy.)

Slide 5 — CTA (Engagement Driver)

End with a strong action.
👉 “Save this workflow. Share it with a friend.
And start going viral without the grind.”

##Rules
Return the 5 prompts as a JSON object with a field called ‘prompts’ that contains an array of the prompt strings.

Prompt1 - Prompt to Nano Banana (Image 1)

Prompt2 - Prompt to Nano Banana (Image 2)

Prompt3 - Prompt to Nano Banana (Image 3)

Prompt4 - Prompt to Nano Banana (Image 4)

Prompt5 - Prompt to Nano Banana (Image 5)


## Example:

Image 1

Prompt to NanoBanana:
“Minimalist bold Instagram carousel cover, dark background with neon accent, eye-catching text space, viral growth theme.”

Image 1 Text (Header & Subheader):
Header: Stop Wasting Hours on IG Posts
Subheader: Here’s how to go viral without the grind 🚀

Image 2

Prompt to NanoBanana:
“Frustrated person at laptop surrounded by messy drafts and notes, cartoon style, editable blank space for text overlay.”

Image 2 Text:
Header: The Problem
Subheader: Posting daily feels impossible. You design, you write… and growth stays flat.

Image 3

Prompt to NanoBanana:
“Infographic-style design showing upward trending arrow with Instagram logo, colorful and clean, strong highlight space for text.”

Image 3 Text:
Header: The A-Ha Moment
Subheader: Carousels = saves + shares → algorithm boost → viral potential

Image 4

Prompt to NanoBanana:
“Automation workflow concept — arrows connecting AI brain → workflow nodes → Instagram app logo, sleek tech aesthetic.”

Image 4 Text:
Header: The Solution
Subheader: NanoBanana + n8n + Blotato = Viral Carousels on Autopilot

Image 5

Prompt to NanoBanana:
“Bright clean design with Instagram ‘save’ and ‘share’ icons, motivational style, bold colors, clear CTA space.”

Image 5 Text:
Header: Your Turn
Subheader: Save this, share it, and start posting smarter.

Instagram Carousel Caption:

✨ Carousels are the #1 way to grow on Instagram in 2025.
The problem? They take HOURS to design and post consistently.

Here’s the 3-step automation workflow I use to stay consistent without burning out:
1️⃣ Generate on-brand images in seconds with NanoBanana
2️⃣ Automate formatting and scheduling with n8n
3️⃣ Auto-post to Instagram using Blotato

The result → More saves, more shares, more growth 🚀

💡 Steal this workflow and never miss a post again.
👉 Save this carousel if you want to try it.
```

## Caption Generator mesajları (okunur biçim, aynen)

**user:**
```
Prompt 1: {{ $json.output.prompts[0] }}
Prompt 2: {{ $json.output.prompts[1] }}
Prompt 3: {{ $json.output.prompts[2] }}
Prompt 4: {{ $json.output.prompts[3] }}
Prompt 5: {{ $json.output.prompts[4] }}
```

**system:**
```
You are an expert Instagram Caption Agent. You will receive a set of prompts that were used to generate Instagram carousel images. Based on these prompts, craft a compelling caption that is:

High-Hook: Start with a strong, attention-grabbing opening line.

SEO-Optimized: Naturally include relevant keywords that increase discoverability.

Engaging: Encourage saves, shares, and comments through questions, CTAs, or relatable phrasing.

Platform-Ready: Use a concise, mobile-friendly writing style with line breaks for readability.

Hashtag-Savvy: Include a well-balanced mix of trending and niche hashtags (without overstuffing).

The final caption should feel natural, authentic, and designed to maximize reach and engagement on Instagram.

##Rules
Output only post-ready Instagram Caption without any explanation.
```

## Sticky Note (şablon içi açıklama, aynen)

```
🎨 Instagram Carousel & Caption Generator on Autopilot (GPT-5 + Nano Banana + Blotato + Google Sheets)
Description

Turn your ideas into viral-ready Instagram carousels — automatically.
This workflow combines GPT-5, Google’s Nano Banana, and Blotato to generate carousel images, craft engaging captions, log everything to Google Sheets, and even post directly to Instagram — all on autopilot.

Just send your topic or idea, and the workflow will:

Create 5 visually consistent carousel prompts using a proven “Viral 5-Framework.”

Generate high-quality images with Nano Banana.

Write an optimized caption with GPT-5.

Upload images to Blotato, log them to Google Sheets, and post them automatically.

👉 Watch the full step-by-step tutorial on YouTube:
https://youtu.be/id22R7iBTjo

💡 Use Wavespeed’s Nano Banana via API

How It Works

Chat Trigger – Start the workflow by sending a chat message with your carousel topic or idea.

Image Prompt Generator (GPT-5) – Generates 5 image prompts using the “Hook → Problem → Insight → Solution → CTA” viral content framework.

Structured Output Parser – Formats the output into a clean JSON array ready for downstream automation.

Nano Banana (via Wavespeed API) – Autogenerates high-quality visuals from each image prompt.

Wait + Retrieve Image Result – Waits for the render to complete, then fetches the image URLs.

Upload to Blotato – Uploads all generated images to Blotato for hosting or scheduling.

Google Sheets Log – Saves image URLs and timestamps for easy reference or reuse.

Caption Generator (GPT-5) – Writes a high-hook, SEO-optimized Instagram caption based on the image prompts.

Merge + Post to Instagram – Combines image URLs and caption, then auto-posts to your connected Instagram account.

Why You’ll Love It

🚀 Completely hands-off content creation – from idea to published post.

🧠 Proven viral framework – automatically applies the “Viral 5-Framework” for high engagement.

🎨 AI-generated visuals – crafted by Google’s Nano Banana for brand-consistent aesthetics.

✍️ AI-optimized captions – GPT-5 ensures your posts hook, rank, and convert.

🗂️ Content logging built-in – every post is stored automatically in Google Sheets.

🤖 One-click scalability – turn this into a full social content engine in minutes.

Requirements

OpenAI API Key (for GPT-5)

Wavespeed API Key (Nano Banana)

Blotato API Key for media uploads and Instagram posting

Google Sheets OAuth Credentials for logging image URLs

n8n Account (self-hosted or Cloud)
```
