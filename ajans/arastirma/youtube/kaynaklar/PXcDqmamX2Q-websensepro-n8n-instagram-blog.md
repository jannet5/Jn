# WebSensePro — How To Post To Instagram Via N8N (blog + workflow JSON)

Kaynak: https://websensepro.com/blog/how-to-post-to-instagram-via-n8n-ai-automation/
Video: PXcDqmamX2Q — How To Post To Instagram Via N8N [AI Automation] — WebSensePro

Ready to automate your Instagram posts with n8n in 2025? Meta’s API credential process can be tricky, but our latest tutorial simplifies it. Follow along to configure your Facebook Graph API and connect n8n to start scheduling content effortlessly.

## Step 1: Create a Meta App
Go to https://developers.facebook.com/apps and create a new app.
Give it a name and an email address and click next.
When asked to select a use case choose other .
For app type choose business .
Review your details and click “ create app “.
You do not need to choose a business portfolio yet.
Click “ Create App ” and enter your Facebook password when prompted.

## Step 2: Configure App Permissions
From the app dashboard, choose “ Instagram ” and click “ Set Up ” to load the standard permissions.
Next you will see the IG API configuration dashboard.
Click app roles in the left side bar.
Choose roles .
Then click “ Add people “.
Choose IG tester from the pop up box.
On the same pop up search for the username of the IG account you want to create credentials for.
Once found click Add .
This sends an invite to the IG account which you need to accept.

## Step 3: Accept the tester role invite in IG
Log into your IG account in your browser https://instagram.com To accept head to https://www.instagram.com/accounts/manage_access Click into the IG tester tab and accept the invite that is shown there.
This gives the necessary permissions you need to create the credential n8n will need.

## Step 4: Link your IG account to the new app
Return to the app dashboard and use the left side menu to navigate back to the IG configuration dashboard.
Look for “ 1. Generate Access Token “. You may need to expand the section using the small arrow on the right.
Your IG account should be listed.
If it is not listed, click add account.
Click continue on the box that pops up.
Then follow the steps to log into your IG account.
Your IG account must be a “ professional ” one to be able to access it programmatically.
If it is not, you will be prompted to switch your accounts status to a “professional”.
Once done your account will show listed in the app dashboard.

## Step 5: Get access token
In the meta dashboard, look for Tools in the main menu and click into graph api explorer .
A new screen will open. Look for “ meta app ” on the right of the page and make sure the correct app is selected.
Under permissions, click “ add permissions “.
Choose “ other ” then select all the IG permissions.
Then click the big blue “ Generate Access Token ” Button.
You will be asked to grant access permissions to your IG account. If you have a lot of accounts you may need to scroll down to see all your accounts.
Follow the steps until you are returned to the generate access token page where you will see the access token.

## Step 6: Extend the duration of the token.
Copy the token.
In the main menu, under tools click into access token debugger .
Paste your token and click debug.
This will show all the access token permissions and life span.
Click extend access token button at the bottom of the page.
This will generate a new long duration access token.

## Step 7: Create your n8n credential.
Copy the token.
In n8n create a new credential “ Facebook Graph API “ Rename the credential so you can recognise it.
Paste in your access token.
Click save.
This will test the connection and the bar will turn green if successful.

## Step 8: Test if you can post to IG from n8n
Cut and paste the JSON below into your n8n canvas.
Make sure the FB nodes are using your new IG credentials Edit the set node or leave it with the sample data.
Add in your IG Account ID In the meta dashboard, in the IG API Configuration dashboard Expand generate access token section.
Copy the number below your IG account name Paste that into the set node and replace <Insert your IG account ID> with your account number Run the workflow.
Check your IG account for a new post.

## Workflow JSON (aynen)

```json
{
  "name": "Instagram Posting Workflow",
  "nodes": [
    {
      "parameters": {
        "values": {
          "string": [
            {
              "name": "imageUrl",
              "value": "https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aHVtYW58ZW58MHx8MHx8fDA%3D&w=1000&q=80"
            },
            {
              "name": "captionText",
              "value": "Hello from n8n! 👋 This is an automated post. #n8n #automation #instagram"
            },
            {
              "name": "Node",
              "value": "<your node id>"
            }
          ]
        },
        "options": {}
      },
      "name": "Set Image & Caption",
      "type": "n8n-nodes-base.set",
      "typeVersion": 1,
      "position": [
        580,
        -60
      ],
      "id": "df7b62aa-ce17-464d-8747-6cea80916f8a"
    },
    {
      "parameters": {},
      "type": "n8n-nodes-base.manualTrigger",
      "typeVersion": 1,
      "position": [
        320,
        -60
      ],
      "id": "1c9e377a-9be6-4bdd-8994-33d9b542e3f2",
      "name": "When clicking ‘Test workflow’"
    },
    {
      "parameters": {
        "httpRequestMethod": "POST",
        "graphApiVersion": "v22.0",
        "node": "={{ $json.Node }}",
        "edge": "media",
        "options": {
          "queryParameters": {
            "parameter": [
              {
                "name": "image_url",
                "value": "={{ $json.imageUrl }}"
              },
              {
                "name": "caption",
                "value": "={{ $json.captionText }}"
              }
            ]
          }
        }
      },
      "type": "n8n-nodes-base.facebookGraphApi",
      "typeVersion": 1,
      "position": [
        800,
        -60
      ],
      "id": "ef976b5d-2695-4edd-9b31-0cca8680436b",
      "name": "Creating Container ID",
      "credentials": {
        "facebookGraphApi": {
          "id": "pOnac7YH8fubtA2J",
          "name": "Instagram Access Token"
        }
      }
    },
    {
      "parameters": {
        "httpRequestMethod": "POST",
        "graphApiVersion": "v22.0",
        "node": "={{ $('Set Image & Caption').item.json.Node }}",
        "edge": "media_publish",
        "options": {
          "queryParameters": {
            "parameter": [
              {
                "name": "creation_id",
                "value": "={{ $json.id }}"
              },
              {
                "name": "image_url",
                "value": "={{ $('Set Image & Caption').item.json.imageUrl }}"
              },
              {
                "name": "caption",
                "value": "={{ $('Set Image & Caption').item.json.captionText }}"
              }
            ]
          }
        }
      },
      "type": "n8n-nodes-base.facebookGraphApi",
      "typeVersion": 1,
      "position": [
        1020,
        -60
      ],
      "id": "92016499-eefe-46c1-be43-9aed1baef20d",
      "name": "Facebook Graph API",
      "credentials": {
        "facebookGraphApi": {
          "id": "pOnac7YH8fubtA2J",
          "name": "Instagram Access Token"
        }
      }
    }
  ],
  "pinData": {},
  "connections": {
    "Set Image & Caption": {
      "main": [
        [
          {
            "node": "Creating Container ID",
            "type": "main",
            "index": 0
          }
        ]
      ]
    },
    "When clicking ‘Test workflow’": {
      "main": [
        [
          {
            "node": "Set Image & Caption",
            "type": "main",
            "index": 0
          }
        ]
      ]
    },
    "Creating Container ID": {
      "main": [
        [
          {
            "node": "Facebook Graph API",
            "type": "main",
            "index": 0
          }
        ]
      ]
    }
  },
  "active": false,
  "settings": {
    "executionOrder": "v1"
  },
  "versionId": "c213637c-e0d9-45d0-8d3f-ea8877d28b95",
  "meta": {
    "templateCredsSetupCompleted": true,
    "instanceId": "7d6a5bcda733904ebcb54fc0e889f6735550f40f670703e17874c0872e05a9cb"
  },
  "id": "Ev54Zrdmu5nZes5t",
  "tags": []
}
```
