# yt-dlp plugin: let InstagramIE use its logged-out GraphQL path without curl_cffi.
# Instagram only needs browser-like Sec-Fetch-* headers there, not a TLS fingerprint.
from yt_dlp.extractor.instagram import InstagramIE


class InstagramNoImpersonateIE(InstagramIE, plugin_name='noimp'):
    _can_impersonate = True  # yt-dlp only warns ("attempting impersonation") and sends a normal request

    @property
    def _api_headers(self):
        return {
            **super()._api_headers,
            'Sec-Fetch-Site': 'same-origin',
            'Sec-Fetch-Mode': 'cors',
            'Sec-Fetch-Dest': 'empty',
        }
