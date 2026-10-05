# Hosting: deploying the static build on Workers

## Contents

- Workers static assets, not Pages
- Config
- Framework builds
- Commands
- The dry run
- Custom domain
- Redirects, headers, caching
- The 404 page
- Previews and staged rollouts
- Files to keep out of the repository and the upload

## Workers static assets, not Pages

Cloudflare's Workers best-practices page: "Workers Static Assets is the recommended way to deploy static sites, single-page applications, and full-stack apps on Cloudflare. If you are starting a new project, use Workers instead of Pages." Pages still works, but new features go to Workers.

One exception decides it the other way. Workers only serves custom domains whose nameservers are managed by Cloudflare. Pages can use a custom domain outside a Cloudflare zone. If the user cannot or will not move the domain's DNS to Cloudflare, say so and stop before choosing; this skill does not cover a Pages setup.

## Config

A static page with no form needs no Worker script. `wrangler.jsonc` at the project root:

```jsonc
{
  "name": "landing",
  "compatibility_date": "2026-10-04",
  "assets": {
    "directory": "./dist",
    "not_found_handling": "404-page"
  }
}
```

- `name` becomes the Worker name and part of the `workers.dev` URL. Use the project's name.
- `compatibility_date`: today's date when you write the file.
- `directory`: the folder the build writes, not the source folder.
- `wrangler.json` and `wrangler.toml` also work; use `wrangler.jsonc` unless the project already has one of the others.

A page with a form or an A/B test adds `main`, a `binding` for the assets, `run_worker_first` and the D1 and Analytics Engine bindings; the full file is in `forms.md`. A page whose only call to action is a link needs just the beacon Worker and a config without D1, in `analytics.md`. Only one assets directory can be configured per Worker.

## Framework builds

Build first; `wrangler deploy` uploads the output folder.

| Stack | `assets.directory` | Notes |
|---|---|---|
| Plain HTML | the folder with `index.html` (`public/` in Cloudflare's static template) | nothing to build |
| Astro, static | `./dist` | build with `npx astro build`; no `main` for a static site. A purely static Astro build cannot use bindings, so the form Worker is a separate `main` file (`src/index.js`) beside the Astro project, as in `forms.md`. Astro 6 and 7 need Node.js 22.12.0 or later |
| Vite with React | `./dist` | Cloudflare's setup uses `@cloudflare/vite-plugin` in `vite.config.ts` (`plugins: [react(), cloudflare()]`) and `"not_found_handling": "single-page-application"`. A one-page landing with a real `404.html` can keep `"404-page"` instead, so unknown URLs answer 404 rather than the home page |
| Next.js | the static export folder | static export with `output: "export"`; point `directory` at the folder the export writes. Cloudflare recommends vinext for Next.js on Workers (beta; run `npx vinext check` first) when the site is not a static export |

## Commands

| Purpose | Command | Touches the account |
|---|---|---|
| Local server | `npx wrangler dev` | no |
| Check the project compiles, without deploying | `npx wrangler deploy --dry-run` | no |
| Deploy to production | `npx wrangler deploy` | yes |
| Preview deploy | `npx wrangler preview` | yes |
| Upload a version without deploying it | `npx wrangler versions upload` | yes |

Install Wrangler in the project (`npm install --save-dev wrangler`) so every run uses the same version. Commands that touch the account need the user's Cloudflare login, which they do themselves.

Do not use `npx wrangler deploy --temporary` for the user's site. It deploys to a throwaway account that must be claimed within 60 minutes; it is meant for experiments, not a real launch.

## The dry run

`npx wrangler deploy --dry-run` compiles the project and stops before uploading. Run it after every config change; it is the check that the config is valid.

Observed with Wrangler 4.147.0 and no login: it read the assets directory ("Read N files from the assets directory"), printed the total upload size and the bindings, and ended with `--dry-run: exiting now.` The docs do not say whether it enforces the asset limits, so check them yourself after the build:

```bash
find dist -type f | wc -l          # at most 20,000 files on the Free plan
find dist -type f -size +25M       # must print nothing: 25 MiB per file at most
```

## Custom domain

Requirements: the domain is an active zone in the user's Cloudflare account, and the hostname has no existing CNAME record. Cloudflare then creates the DNS record and the certificate.

```jsonc
"routes": [
  { "pattern": "example.com", "custom_domain": true }
],
"workers_dev": false
```

`"workers_dev": false` turns off the `*.workers.dev` URL so the page has one address. Set it in the config: if it is only turned off in the dashboard, the next `wrangler deploy` turns it back on. The same can be done in the dashboard under Workers & Pages > the Worker > Settings > Domains & Routes > Add > Custom Domain.

Adding a custom domain changes the user's DNS. Write the `routes` block into the config, explain what it will do, and deploy it only with the user's go-ahead.

## Redirects, headers, caching

Both files go in the assets directory and are not served themselves. Cloudflare's docs say `_redirects` does not apply to requests served by Worker code. For `_headers` the docs are silent; observed locally with `wrangler dev`: a `/*` rule in `_headers` appeared on static pages and on the `/` response the A/B Worker builds from `env.ASSETS.fetch`, but not on responses the Worker creates itself (`/api/lead`), and a `_redirects` rule for `/api/convert` was ignored because the Worker handles that path. Treat this as a local observation and check it after deploy; set any header a Worker response needs in the Worker code.

`_redirects`, one rule per line, `source destination [code]` (301, 302, 303, 307 or 308; default 302):

```txt
/join / 301
/book / 302
```

Limits: 2,000 static and 100 dynamic rules, 1,000 characters per line.

`_headers`, a URL pattern then indented `Name: value` lines:

```txt
/assets/*
  Cache-Control: public, max-age=31556952, immutable
```

Limits: 100 rules, 2,000 characters per line.

Both examples were run with `npx wrangler dev`: `/join` answered 301 to `/`, `/book` 302, a file under `/assets/` carried the `immutable` header, and `/_headers` itself returned 404.

Caching: every static asset response gets `Cache-Control: public, max-age=0, must-revalidate` and an `ETag` by default, so browsers revalidate each file on every use. That is right for HTML, which changes on each deploy. For files whose names carry a content hash (most framework builds put them in one folder), add the `immutable` rule above for that folder, and only for that folder: a long cache on a file whose name does not change keeps visitors on the old version.

## The 404 page

`"not_found_handling": "404-page"` serves the nearest `404.html` with status 404. Without a `404.html` the response has no body. Give the 404 page the site's layout, a link to `/`, and the call to action. Tested locally: an unknown path returned `404.html` with status 404.

`html_handling` defaults to `auto-trailing-slash`. Observed locally: a request for `/index.html` is redirected (`307`) to `/`.

## Previews and staged rollouts

Previews are Cloudflare's recommended way to check a change before production.

- Requires Wrangler 4.135.0 or later and a `previews` block in `wrangler.jsonc` (it may be empty). Top-level settings are production; `previews` holds the Preview values, for example `"previews": { "vars": { "TURNSTILE_HOSTNAME": "<preview hostname>" } }`.
- Storage bindings (D1, Analytics Engine, KV and others) are declared in the `previews` block, with the same binding name pointed at a preview-safe resource. If you point them at the production database and dataset, every test sign-up on a preview lands in the real `leads` table and the real conversion counts. Give previews their own:

  ```jsonc
  "previews": {
    "vars": { "TURNSTILE_HOSTNAME": "<preview hostname>" },
    "d1_databases": [
      { "binding": "DB", "database_name": "landing-leads-preview", "database_id": "<preview database id>" }
    ],
    "analytics_engine_datasets": [
      { "binding": "EVENTS", "dataset": "landing_events_preview" }
    ]
  }
  ```

  The preview database is a second `npx wrangler d1 create`, followed by the schema with `--remote`; both change the account and need the user's go-ahead. Secrets are set separately for previews: `npx wrangler preview base-config secret put TURNSTILE_SECRET` sets one for every new preview. This block was not deployed for this skill; confirm it in Cloudflare's Previews documentation (configuration and resources pages) before relying on it.
- `npx wrangler preview` creates or updates a preview named after the current git branch (`--name` to choose). `npx wrangler preview delete --name <preview-name>` removes it.
- URLs: `<preview-name>-<worker-name>.<subdomain>.workers.dev` always shows the latest; each deployment also gets a fixed `<deployment-id>-<worker-name>.<subdomain>.workers.dev`. `workers.dev` previews send `X-Robots-Tag: noindex`; custom-domain previews do not, so protect those with Cloudflare Access. With `"workers_dev": false`, `workers.dev` preview URLs may default to off; `"preview_urls": true` turns them on.
- Preview URLs are public. If the page is confidential before launch, protect them with Cloudflare Access.
- Free plan: 100 previews per Worker, 100 deployments each; the oldest is deleted at the limit. No production deploy is needed before the first preview.

For a staged production rollout, `npx wrangler versions upload` uploads a version without deploying it and `npx wrangler versions deploy <version-id>@<percentage> ... -y` splits traffic between versions. Do not use Version URLs to test branches; Cloudflare's docs say to use Previews for that.

A preview is a deployment to the user's account. It needs their go-ahead like a production deploy.

## Files to keep out of the repository and the upload

- `.gitignore`: `.dev.vars*`, `.wrangler/`, `node_modules/`.
- `.assetsignore` in the assets directory (same syntax as `.gitignore`) lists files that must not be uploaded. Do not list `_headers` or `_redirects` there; examples written for Pages migrations do, and in a Workers project that switches them off.
