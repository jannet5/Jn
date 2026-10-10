# Website Learners — Make.com "social media automation" blueprint (video 00obLp8vowQ)

Kaynak: https://bit.ly/3DAhnE6 → https://drive.google.com/file/d/1HWKQ3SJGLOnmWPhoklBn2YrZHZcN3wHt (Make blueprint JSON, 431 KB)
Video: https://www.youtube.com/watch?v=00obLp8vowQ ("How to Automate Social Media Posts with AI", Website Learners)

Google Sheet şablonu (https://docs.google.com/spreadsheets/d/1PeoqK5GiCuO2imtY1NwL7_rQkgoxqScmIOwsJtXFOvM) sütunları (CSV export ile doğrulandı):
`Text, Link, Image Url, Action button, Progress, Facebook content, Linkedin content, Insta content, Image url, Post Action` (+ blueprint K/L/M sütunlarına Facebook / Instagram / LinkedIn post linklerini yazıyor).

## Modül ağacı (blueprint'ten)
```
1  google-sheets:watchUpdatedCells   (Make for Google Sheets eklentisi webhook'u)
2  google-sheets:getSheetContent
55 Router
 ├─ Kol A "Content Generation"  filtre: D (Action button) = "Create & Post" VE F,G,H boş
 │   84 updateCell E = "Processing🔃"
 │   7 Router
 │    ├─ (A sütunu Text varsa) 8 OpenAI gpt-4o → 14 TextAggregator → 18 SetVariable "text output"
 │    ├─ (B sütunu Link varsa) 4 Perplexity llama-3-sonar-large-32k-online → 15 → 19 SetVariable "URL output"
 │    ├─ (C sütunu Image Url varsa) 6 OpenAI gpt-4-vision-preview analyzeImages → 16 → 20 SetVariable "Image output"
 │    └─ 21 BasicAggregator → 22 GetVariables → 23 OpenAI gpt-4o (birleştir+özetle) → 74 getSheetContent → 25 Router
 │         ├─ 26 gpt-4o Facebook   → 82 updateCell F
 │         ├─ 27 gpt-4o LinkedIn   → 81 updateCell G
 │         ├─ 28 gpt-4o Instagram  → 80 updateCell H
 │         ├─ (görsel yoksa) 60 gpt-4o başlık çıkar → 90 Leonardo generateImage → 91 E="Completed✅" → 83 I = görsel URL
 │         └─ 92 E="Completed✅" → (görsel varsa) 76 I = kullanıcının görsel URL'si
 └─ Kol B "Post Content"  filtre: J (Post Action) = "Publish" VE F,G,H dolu
     56 Router
      ├─ 100 facebook-pages:CreatePostWithPhotos → 101 GetPost → 93 updateCell K = permalink_url
      ├─ 102 instagram-business:CreatePostPhoto → 103 GetMedia → 94 updateCell L = permalink
      └─ 106 http:ActionGetFile → 107 linkedin:ShareImage → 95 updateCell M = https://www.linkedin.com/embed/feed/update/{{107.id}}
```

## Prompt'lar (AYNEN)
Metin özeti (gpt-4o):
```
Analyze the text content {{2.`0`}} and summarize it 
```
Link özeti (Perplexity, llama-3-sonar-large-32k-online):
```
{{2.`1`}} Analyze this URL and summarize the entire content with key points and the main context. 
```
Görsel analizi (gpt-4-vision-preview):
```
Analyze the image to understand its content and context.Provide a detailed description and summary of the image. {{2.`2`}}
```
Birleştirme (gpt-4o):
```
{{22.`text output`}}{{22.`URL output`}}{{22.`Image output`}}combine these data and summarzie completely
```
Facebook (gpt-4o):
```
Based on this following summary"{{23.result}}", 

create a Facebook post as a digital marketing specialist. The content should be engaging, relatable, and visually appealing, with an inspirational message. Use emojis and relevant hashtags. Do not any image placeholders.

 If a URL is provided here: '{{74.`1`}}', then include it at the end of the post like this: "If you want to know more about this, follow this link: <URL>. 
If URL is not provided above, then do not write anything about URL.
```
LinkedIn (gpt-4o):
```
Based on this following summary "{{23.result}}",

create a LinkedIn post as a digital marketing specialist. The content should be professional, insightful, and thought-provoking, with a focus on delivering value and sparking conversation. Use relevant hashtags. Do not include any image placeholders.

If a URL is provided here: '{{74.`1`}}', then include it at the end of the post like this: 'To learn more, follow this link: <URL>'.

If a URL is not provided above, then do not write anything about a URL
```
Instagram (gpt-4o):
```
Based on this following summary "{{23.result}}",

Create an  catchy Instagram post as a digital marketing specialist. The content should be visually engaging, relatable, and inspiring, Use emojis and relevant hashtags. Do not include any image placeholders.

If a URL is provided here: '{{74.`1`}}', then include it at the end of the post like this: 'Want to know more? Check out this link: <URL>'.

If a URL is not provided above, then do not write anything about a URL.
```
Görsel başlığı (gpt-4o, yalnızca kullanıcı görsel vermediyse):
```
{{23.result}}Extract a catchy title around 35 characters from this summary, perfect for a social media post image.
```
Leonardo AI görsel prompt'u (modelId 6b645e3a-d64f-4341-a6d8-7a3690fbf042, num_images 1):
```
Create a typography image with this title '{{60.result}}' in bold, 3D, modern metallic, and energetic fonts. Ensure the text is clear . Use a vibrant and dynamic color palette that stands out. The text should have a pronounced 3D effect with depth and shadow, making it pop off the screen. Surround the text with minimal dynamic, abstract geometric shapes, lines, and energy beams to convey motion and excitement. The background should feature a modern pattern to enhance the futuristic feel. The overall composition should be balanced and engaging, with a sleek, modern aesthetic. Set the image dimensions to 1080x1080 pixels, perfect for social media sharing.
```

Not: Blueprint'teki spreadsheetId'ler yaratıcının kendi tablolarına işaret ediyor (1EVF7F2..., 1lwqgMA...); import sonrası kendi kopyanızı seçmeniz gerekir. Model adları (gpt-4-vision-preview, llama-3-sonar-large-32k-online) artık eski; güncel modellerle değiştirilmeli.
