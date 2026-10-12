# Dan Kieft — AI Influencer Automation (Arcads → n8n → Blotato) — Google Doc + n8n workflow (video R9c_JQrEtu8)

Kaynak (Google Doc): https://docs.google.com/document/d/1evdUNEAL6_n0mLZ7wNxQfshIdk_Ttq39gVCm_mzhF-g (export?format=txt ile çekildi)
Doc içindeki workflow linki: https://drive.google.com/file/d/14rXXi8vlopJJDH5SDA3SGMIHm0CN603t ("Travel Influencer" n8n JSON, 9.4 KB)
Doc içindeki diğer link: ChatGPT GPT "AI Influencer Prompt Bot" — https://chatgpt.com/g/g-689f02fad8a4819191bf46b26b56d03e-ai-influencer-prompt-bot (içeriği alınamaz, GPT sayfası)
Video: https://www.youtube.com/watch?v=R9c_JQrEtu8

## Google Doc metni (AYNEN)
```
Dan Kieft's AI Influencer Automation
My YouTube Channel
________________


🧰 Tools You’ll Need

* Arcads → To make talking videos of your AI influencer
* n8n → The tool we built the automation in - download my workflow 
* Blotato → To link all your socials in one platform and automatically schedule it
Step 1: Generate your AI Influencer through Arcads
* Create a new folder
* Create a new project
* Generate an actor image 
* Choose your actor voice
* Train your actor
* Add in your script and hit generate
* Generate different versions and or different scripts
Step 2: Download my n8n workflow and set it up 
* Sign up to N8N
* Import from file and upload the file
* Change the parameters by adding in your Arcads folder ID & Blotato Acc ID
* Add in your Arcads API Key in “get scripts in arcade’
* Select the credentials you just made in “get video from arcade” 
* Get your ChatGPT API key and add it to “Generate description”
Step 3: Set up blotato
* Go to blotato and sign in to your preferred social media accounts 
* Copy account ID and add it in as instagram account ID in the parameters
* Copy API Key into the value
* Test out your workflow
Note make sure your IG account is an creator account so you can use it in blotato.
```

## n8n workflow "Travel Influencer" — düğümler (JSON'dan)
1. **Set workflow parameters** (executeWorkflowTrigger) — girdiler: `Arcads Folder ID`, `Blotato Instagram Account ID`
2. **Get Scripts in Arcads Folder** — GET `https://external-api.arcads.ai/v1/folders/{{ $json["Arcads Folder ID"] }}/scripts?pageSize=9999&page=1` (HTTP Basic Auth: Arcads client ID / client secret)
3. **List generated videos** (Code):
```js
let completedIds = [];
let i = 0;
const items = $input.first().json.items;
const startDate = new Date();

for (const item of items) {
  const videos = item.videos || [];
  const text = item.text;

  for (const video of videos) {
    if (video.videoStatus === "completed") {
      const date = new Date(startDate);
      date.setUTCDate(startDate.getUTCDate() + i); 
      date.setUTCMinutes(startDate.getUTCMinutes() + 1);
      completedIds.push({ id: video.id, date: date, text });
      i++;
    }
  }
}

return completedIds;
```
   (Her tamamlanmış video için bugünden itibaren +i gün tarih atar → günde 1 post.)
4. **Loop Over Videos** (splitInBatches) → bitince "All videos are posted!" (noOp)
5. **Get video from Arcads** — GET `https://external-api.arcads.ai/v1/videos/{{ $json.id }}`
6. **Transform Video** — POST `https://api.cloudinary.com/v1_1/dazxntn9b/video/upload` (multipart: `upload_preset=ig_mp4`, `file={{videoUrl}}`) — Instagram uyumlu mp4'e çevirme
7. **Generate description** — POST `https://api.openai.com/v1/responses`, body:
```
{
    "model": "gpt-4o-mini",
    "input": "Write the Instagram description for a Reel. Respond only with the plain description, no titles, no markdown—just the description. Here's the spoken text in the reel : {{ $('Loop Over Videos').item.json.text.toJsonString().replaceAll('"', "") }}",
    "store": true
  }
```
8. **Upload on Blotato** — POST `https://backend.blotato.com/v2/media` (`url` = Cloudinary `eager[0].secure_url`)
9. **Post on Instagram** — POST `https://backend.blotato.com/v2/posts`:
```
{
  "scheduledTime": "{{ $('List generated videos').item.json.date }}",
  "post": {
    "accountId": "{{ $('Set workflow parameters').item.json['Blotato Instagram Account ID'] }}",
    "content": {
      "text": "{{ $('Generate description').item.json.output[0].content[0].text }}",
      "mediaUrls": [
        "{{ $json.url }}"
      ],
      "platform": "instagram"
    },
    "target": {
      "targetType": "instagram"
    }
  }
}
```
   → Loop'a geri döner.

Not: Cloudinary hesabı (`dazxntn9b`) ve `ig_mp4` preset'i yaratıcıya ait; videoda bu adım "you can ignore transform video" diye geçiliyor — kendi Cloudinary hesabınız ve unsigned preset'iniz olmadan çalışmayabilir.
