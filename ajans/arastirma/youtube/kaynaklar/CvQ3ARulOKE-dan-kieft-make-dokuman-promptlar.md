# Dan Kieft — Make sosyal medya otomasyonu kurulum dokümanı + promptlar + blueprint özeti

- Kaynak (ana kurulum dokümanı): https://docs.google.com/document/d/1XufToDr7pqcutF0kSt8ftLn12DCF23kGe8RhiLb6jS0/edit
- Prompt dokümanı 1 (IG/LinkedIn/X): https://docs.google.com/document/d/12VPCK7E4ezK-AMifzvM3MfkiDo1qDE8EEKEM92rmxmI/edit
- Prompt dokümanı 2 (YT Short script + başlık): https://docs.google.com/document/d/1PM2hdtvVEFxcOn30QQRbk6DmwOzFceyBlL8f5YPlaFQ/edit
- Google Sheet şablonu: https://docs.google.com/spreadsheets/d/1yC-hTzybUWUJCuWL3gaP2Dqa0esDrSpH865anQLp-vM (tek sütun başlığı: `URL`)
- Make blueprint JSON (Drive): https://drive.google.com/file/d/1ht8IUyhXicoKlNnhBikH8UAGmtk56RYz (indirildi, ~364 KB; modül özeti aşağıda)
- Video: CvQ3ARulOKE — "NEW AI Social Media Automation will save you HOURS *Fully Automated*" (Dan Kieft)

---

## 1) Ana kurulum dokümanı (metin olduğu gibi)

```
﻿How To make a Social Media Automation using Make
* By Dan Kieft




Note: Click on file > Make a copy


Step 1: Sign up to make.com
You can get started for free and continue to use the free plan for this automation.


* Go to make.com (use this link to get 1 month of make pro for free)


Step 2: Copy this automation 
Download this file here


* Download the JSON file here
* Create a new scenario, click on the three dots and import the file




Step 3: Copy this spreadsheet
make spreadsheet


* Copy the spreadsheet to your drive
* Search by path and find the spreadsheet ID
* Enter ‘yes’ on table contains headers
* Row with headers: A1:Z1
* Limit: set to 1




Step 4: Create Dumpling AI account
They have a free trial so you can play around with it, after the trial you unfortunately have to pay. I’m not sponsored with them but I do have this discount code: DAN20 for 20% off.
Alternatively watch this video to use airtable and a bit of coding to get the job done.


* Sign up to Dumpling AI
* Generate API Key & copy it in Make.com
* Set’ YouTube video URL’ To ‘URL (A)’
* Set ‘include timestamps to combine’ to ‘No’




________________


Step 5: Create ChatGPT integration


* Go to https://platform.openai.com/api-keys & login with your account
* Add $5 to your account to generate an API Key
* Generate API key and copy to Make.com
Step 6: Add ChatGPT prompts to each ChatGPT connection


* For ‘Model’ I use ‘o1-mini’ But other models like gpt-4o will work too
* Insert this  ChatGPT Prompts into the messages
* For YouTube insert this YT Short chatgpt prompt  Add ‘Dumpling AI transcript’ after the text: ‘Here's the transcript to convert:’




Step 7: Finish setting up Instagram, LinkedIn and Twitter


* Sign in to instagram > use chatGPT ‘URL’ in ‘photo URL’ > ‘result’ in caption
* Sign into LinkedIn > use ‘result’ in content
* Sign into X (twitter) by following this website or watch this YT vid




Congrats! Now you’ve setup your Instagram, LinkedIn and Twitter. You could add Facebook too if you want, it’s pretty much the same process as with instagram. 


The next part of the tutorial goes into the ai avatar for YouTube shorts, here’s a detailed video on that: I Generated 100 Monetizable YouTube Shorts With My AI Clone on AutoPilot


Step 8: Setup Heygen & Elevenlabs
You can try it out with the free plan.
Optionally - for best results clone your voice in elevenlabs. This ain’t free tho 😪


* Clone yourself in Heygen
* Optional - clone voice in elevenlabs with professional voice mode
* In HeyGen add voices & integrate 3rd party voice - Insert elevenlabs API Key
* Copy HeyGen API key In make.com
* Select your avatar clone
* Talking style Normal
* Input text put ‘chatGPT Result’ 
* Voice pitch 50
* Voice emotion - excited (play around with this)
* Dimension: width 720, Height 1280




Step 9: Retrieve HeyGen video


* Connect heygen ID
* Video ID insert heygen video ID here.
* Run it to test it out.




Step 10: Download Heygen video through HTTP


* Set up retrieve video through video_URL


From here it is optional what you do, you can directly upload it to YouTube or you can put it in a google drive folder so you or your editor can edit it for you. 


Step 11: Connect YouTube account and upload


* Title video [insert chatgpt created]
* File - http get a file
* Privacy status - private (so you can check before it goes live)




Step 12: Google drive & Email


* Connect your google drive account
* Get the file from HTTP to google drive
* Connect your email provider
* Insert web view link in the content
```

## 2) Prompt dokümanı 1 (olduğu gibi)

```
﻿Make concept


ChatGPT 1 prompt - Instagram Caption
Make a short instagram caption from this youtube transcript. Pull out a problem, a fact a benefit, the tone should be like you’re having a thought about this idea and you’re just writing out what you’re thinking. Don’t use hashtags and also no emojis. Keep it short and concise.




ChatGPT prompt 2 - LinkedIn prompt
 Make a short LinkedIn post from this youtube transcript. Pull out a problem, a fact a benefit, the tone should be like you’re having a thought about this idea and you’re just writing out what you’re thinking. Don’t use hashtags and also no emojis. Keep it short and concise.




ChatGPT prompt 3: X (twitter)  Prompt 
Make a short twitter tweet from this youtube transcript. Pull out a problem, a fact a benefit, the tone should be like you’re having a thought about this idea and you’re just writing out what you’re thinking. Don’t use hashtags and also no emojis. Keep it short and concise.  Use max 200 characters.
```

## 3) Prompt dokümanı 2 (olduğu gibi)

```
﻿I want you to act as an expert YouTube short scriptwriter who specializes in transforming long-form content into viral 30-50 second scripts. You understand the exact patterns that drive engagement and keep viewers watching until the end.
Your task is to convert the following transcript into an youtube short script using these exact patterns:


HOOK (Choose one pattern):
"So [surprising fact/statistic]"
"[Industry/Topic] is about to change forever"
"This [new thing] just [dramatic action]"
"You're doing [common activity] wrong"


TRANSITION (Choose one):
"Here's what you need to know"
"Check this out"
"Let me show you"
"Here's how it works"


MAIN POINTS:
Use these transition phrases: • "First up is" • "Then we've got" • "The best part is"
Include these emphasis words: • "game-changing" • "groundbreaking" • "This is huge"
Use these transition words: • "Now" • "Next" • "And then"


CLOSING (Choose one pattern):
Question + Call to Action
Bold Prediction + Engagement Ask
Quick Summary + Value Statement End with:
"Pretty cool, right?"
"What do you think?"
"Follow for more [topic]"


Requirements:
Keep it between 80-120 words
Use casual, conversational tone
Make it punchy and high-energy
Break complex ideas into simple points
Write it exactly how you would speak it


ONLY output the exact video script. do not output anything else. Never include your thoughts, emojis, notes or formatting. 


Here's the transcript to convert:



SECOND Prompt for YouTube Titles

Using the text input that I give to you I want you to come up with 1 YouTube Title for my video. Avoid creating titles that are too vague or misleading. Title should be clear, concise, and directly related to the content of the video. Titles should ideally be less than 54 characters.  Only create 1 title.


Here is the text input you can use as inspiration
```

## 4) Blueprint modül özeti ("social media automation")

Senaryo ayarı: maxErrors 3, sequential false.

1. [29] google-sheets:watchRows (limit 1, A1:Z1 başlık)
2. [4] dumplingai:getYoutubeTranscript — videoUrl = `{{29.`0`}}`, includeTimestamps false, preferredLanguage en
3. [32] Router
   - Kol A (Instagram): [33] OpenAI CreateCompletion `chatgpt-4o-latest` (IG caption prompt + `{{4.transcript}}`) → [41] DALL-E 3 GenerateImage 1024x1024, style vivid, prompt `Make an image based on this text: {{33.result}}` → [46] instagram-business:CreatePostPhoto (image_url = `{{41.data[].url}}`, caption = `{{33.result}}`)
   - Kol B (LinkedIn): [34] OpenAI `chatgpt-4o-latest` (LinkedIn prompt) → [48] linkedin:CreatePost (PUBLIC, MAIN_FEED)
   - Kol C (X): [35] OpenAI `chatgpt-4o-latest` — DİKKAT: blueprint'te prompt "Make a short LinkedIn post ... Use max 200 characters." (dokümandaki "twitter tweet" yerine LinkedIn kelimesi kalmış) → [47] twitter:createATweet
   - Kol D (YouTube Short / AI klon): [10] OpenAI `o1-mini` (Instagram Reel/YT Short script prompt'u + `{{4.transcript}}`) → [36] Router
     - [14] heygen:createAnAvatarVideoV2 (720x1280, voice pitch 50, speed 1, emotion Excited, avatar_style normal, input_text = `{{10.result}}`) → [16] Sleep 300 sn → [31] Sleep 180 sn → [18] heygen:getVideo → [26] http:ActionGetFile (video_url) → [19] Router
       - Kol D1: [27] google-drive:uploadAFile → [28] email:ActionSendEmail (editöre: subject "New video to edit! {{27.createdTime}}", gövde "Hey [editor name]\n\nI got a new video that you can edit.\nYou can find it here {{27.webViewLink}}")
       - Kol D2: [40] OpenAI `chatgpt-4o-latest` (YouTube başlık prompt'u + `{{10.result}}`) → [20] youtube:uploadVideo (title = `{{40.result}}`, description = `{{10.result}}`, categoryId 26, privacyStatus private, madeForKids false)

Blueprint'teki Reel script prompt'u (o1-mini) doküman 2'deki YT Short prompt'uyla aynı yapıda; farkı "YouTube short" yerine "Instagram Reel" yazması ve sonunda `Here's the transcript to convert: {{4.transcript}}` olması.
