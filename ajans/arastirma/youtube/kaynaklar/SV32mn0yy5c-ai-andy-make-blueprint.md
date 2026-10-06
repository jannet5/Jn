# AI Andy — Make.com blueprint: "Sheet To Social Media" (video SV32mn0yy5c)

Kaynak: Notion sayfasındaki Google Drive dosyası (https://drive.google.com/file/d/1abnXQ_VrbiOqCgB38KPmFaCh-ZXrNmLU). Make'e aktarma: sol alttaki üç nokta → Import Blueprint.

## Modüller ve eşlemeler (mapper)
- **google-sheets:watchRows** (id 3)
- **perplexity-ai:createAChatCompletion** (id 2): model="llama-3.1-sonar-small-128k-online"; max_tokens="50000"; temperature="1"
- **builtin:BasicRouter** (id 4)
  - **openai-gpt-3:messageAssistantAdvanced** (id 5): role="user"; message="{{2.choices[].message.content}}"; assistantId="asst_snzqFE96DZ07GRGVsnZYrlnE"
  - **twitter:createATweet** (id 17): text="{{5.result}}"
  - **openai-gpt-3:messageAssistantAdvanced** (id 11): role="user"; message="{{2.choices[].message.content}}"; assistantId="asst_2nyzuMkpLc4WCgy9QMcOa2ef"
  - **linkedin:CreatePost** (id 19): content="{{11.result}}"; visibility="PUBLIC"; feedDistribution="MAIN_FEED"; isReshareDisabledByAuthor=false
  - **openai-gpt-3:messageAssistantAdvanced** (id 7): role="user"; message="{{2.choices[].message.content}}"; assistantId="asst_jxTMMCs9EIZc18LJNNJBzg2V"
  - **openai-gpt-3:GenerateImage** (id 8): size="1024x1024"; model="dall-e-3"; style="vivid"; prompt="Make an image based on this text: {{7.result}}"; quality="standard"; response_format="url"
  - **instagram-business:CreatePostPhoto** (id 15): caption="{{7.result}}"; accountId="17841455357129006"; image_url="{{8.data[].url}}"
  - **openai-gpt-3:messageAssistantAdvanced** (id 9): role="user"; message="{{2.choices[].message.content}}"; assistantId="asst_PXhnSZ8EmwufE2LYBkTHMNyP"
  - **openai-gpt-3:GenerateImage** (id 10): size="1024x1024"; model="dall-e-3"; style="vivid"; prompt="Make an image based on this text: {{9.result}}"; quality="standard"; response_format="url"
  - **facebook-pages:CreatePost** (id 14): link="{{10.data[].url}}"; message="{{9.result}}"; page_id="1512666705493261"

## Ham blueprint (JSON)
