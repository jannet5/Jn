# yt-dlp plugin: list an Instagram profile's latest posts or reels without logging in.
# Uses the same logged-out web GraphQL queries the instagram.com profile page uses
# (doc_ids taken from gallery-dl 1.32.17). Does not need the profile HTML page, which
# Instagram rate-limits hard for logged-out clients.
#
#   https://www.instagram.com/USER/          -> posts tab (photos, carousels, reels shared to feed)
#   https://www.instagram.com/USER/posts/    -> posts tab
#   https://www.instagram.com/USER/reels/    -> reels tab
#
# Entries are url results pointing at /reel/CODE/ or /p/CODE/, newest first (pinned items are
# merged back into date order). Extra entry fields: media_type (1 image, 2 video, 8 carousel),
# has_video, pinned.
# Extractor args (key "instagramprofile"): videos_only=true  -> skip posts without any video
#                                          posts_doc_id=...  /  reels_doc_id=...  -> override doc_ids
import heapq
import itertools
import json

from yt_dlp.extractor.common import InfoExtractor
from yt_dlp.extractor.instagram import InstagramIE
from yt_dlp.utils import ExtractorError, int_or_none, str_or_none, traverse_obj, urlencode_postdata

_POSTS = ('PolarisProfilePostsTabContentQuery_connection', '39229011733413759',
          'xdt_api__v1__feed__user_timeline_graphql_connection')
_REELS = ('PolarisProfileReelsTabContentQuery_connection', '28989741540630190',
          'fetch__XDTUserDict')
_IG_EPOCH_MS = 1314220021721


class InstagramProfileIE(InfoExtractor):
    IE_NAME = 'instagram:profile'
    IE_DESC = 'Instagram profile posts/reels tab (logged-out GraphQL)'
    _VALID_URL = (r'https?://(?:www\.)?instagram\.com/'
                  r'(?!(?:p|tv|reels?|explore|stories|accounts|direct|about|legal|developer|web|api|graphql|share)/)'
                  r'(?P<id>[A-Za-z0-9._]{1,30})/?(?:(?P<tab>reels|posts)/?)?(?:[?#]|$)')
    _PAGE_SIZE = 12

    def _setup(self, username):
        webpage = self._download_webpage(
            'https://www.instagram.com/', username, 'Setting up session', headers={
                'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
                'Sec-Fetch-Site': 'none', 'Sec-Fetch-Mode': 'navigate',
                'Sec-Fetch-Dest': 'document', 'Sec-Fetch-User': '?1',
            })
        eqmc = self._search_json(r'<script\b[^>]*\bid="__eqmc"[^>]*>', webpage, 'eqmc', username, default={})
        self._lsd = (traverse_obj(eqmc, ('l', {str}))
                     or self._search_regex(r'\["LSD",\[\],\{"token":"([^"]+)"', webpage, 'lsd', default=''))

    def _gql(self, query, variables, video_id, referer, note):
        opname, doc_id, field = query
        doc_id = self._configuration_arg(
            'posts_doc_id' if query is _POSTS else 'reels_doc_id', [doc_id], ie_key='instagramprofile')[0]
        csrf = self._get_cookies('https://www.instagram.com/').get('csrftoken')
        data = self._download_json(
            'https://www.instagram.com/graphql/query', video_id, note, headers={
                'Accept': '*/*',
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-FB-Friendly-Name': opname,
                'X-Root-Field-Name': field,
                'X-CSRFToken': csrf.value if csrf else '',
                'X-IG-App-ID': '936619743392459',
                'X-ASBD-ID': '359341',
                'X-FB-LSD': self._lsd,
                'Origin': 'https://www.instagram.com',
                'Referer': referer,
                'Sec-Fetch-Site': 'same-origin', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Dest': 'empty',
            }, data=urlencode_postdata({
                'av': '0', '__d': 'www', '__user': '0', '__a': '1', '__comet_req': '7', 'dpr': '1',
                'lsd': self._lsd, 'fb_api_caller_class': 'RelayModern',
                'fb_api_req_friendly_name': opname, 'server_timestamps': 'true',
                'variables': json.dumps(variables, separators=(',', ':')), 'doc_id': doc_id,
            }))
        conn = traverse_obj(data, ('data', field))
        if not isinstance(conn, dict):
            raise ExtractorError(
                f'Instagram GraphQL {opname} returned no data: {json.dumps(data)[:300]}', expected=True)
        if 'edges' not in conn:
            conn = next((v for v in conn.values() if isinstance(v, dict) and 'edges' in v), None)
        if conn is None:
            raise ExtractorError(f'Instagram GraphQL {opname}: no edges', expected=True)
        return conn

    def _posts_page(self, username, after, count):
        return self._gql(_POSTS, {
            'after': after, 'before': None,
            'data': {'count': count, 'include_reel_media_seen_timestamp': True,
                     'include_relationship_info': True, 'latest_besties_reel_media': True,
                     'latest_reel_media': True},
            'first': count, 'include_multi_captions': True, 'last': None, 'username': username,
            '__relay_internal__pv__PolarisMultiCaptionCarouselEnabledrelayprovider': True,
            '__relay_internal__pv__PolarisShortDramaEnabledrelayprovider': True,
            '__relay_internal__pv__PolarisThreadsStyleCommentsEnabledrelayprovider': False,
            '__relay_internal__pv__PolarisReelsRecoDebugOverlayEnabledrelayprovider': False,
        }, username, f'https://www.instagram.com/{username}/', 'Downloading posts page')

    def _reels_page(self, user_id, username, after, count):
        return self._gql(_REELS, {
            'after': after or '', 'data': {'include_feed_video': True, 'page_size': count,
                                           'target_user_id': user_id},
            'first': count, 'id': user_id,
            '__relay_internal__pv__PolarisShortDramaEnabledrelayprovider': False,
        }, username, f'https://www.instagram.com/{username}/reels/', 'Downloading reels page')

    @staticmethod
    def _entry_info(node, pinned_key):
        code = node.get('code')
        pk = int_or_none(node.get('pk'))
        media_type = int_or_none(node.get('media_type'))
        carousel = node.get('carousel_media') or []
        has_video = (media_type == 2 or bool(node.get('video_versions'))
                     or any(int_or_none(c.get('media_type')) == 2 for c in carousel))
        ts = int_or_none(node.get('taken_at'))
        if ts is None and pk:
            ts = ((pk >> 23) + _IG_EPOCH_MS) // 1000
        is_reel = node.get('product_type') == 'clips' or (media_type == 2 and not carousel)
        return {
            'code': code, 'timestamp': ts, 'media_type': media_type, 'has_video': has_video,
            'pinned': bool(node.get(pinned_key)),
            'url': f'https://www.instagram.com/{"reel" if is_reel else "p"}/{code}/',
            'title': traverse_obj(node, ('caption', 'text', {str}, {lambda s: s.split('\n')[0][:100]})),
        }

    def _real_extract(self, url):
        username, tab = self._match_valid_url(url).group('id', 'tab')
        tab = tab or 'posts'
        videos_only = self._configuration_arg('videos_only', ['false'], ie_key='instagramprofile')[0] == 'true'
        self._setup(username)

        first = self._posts_page(username, None, self._PAGE_SIZE)
        owner = next((u for u in traverse_obj(first, ('edges', ..., 'node', 'user', {dict}))
                      if (u.get('username') or '').lower() == username.lower()), None)
        if not owner and not first.get('edges'):
            raise ExtractorError(f'{username}: no posts (or private / nonexistent profile)', expected=True)
        owner = owner or {}
        user_id = str_or_none(owner.get('pk') or owner.get('id'))
        full_name = owner.get('full_name')
        handle = owner.get('username') or username

        def node_pages():
            if tab == 'posts':
                conn, after = first, None
                while True:
                    yield [e['node'] for e in conn.get('edges') or []], 'timeline_pinned_user_ids'
                    info = conn.get('page_info') or {}
                    if not info.get('has_next_page') or not info.get('end_cursor'):
                        return
                    after = info['end_cursor']
                    conn = self._posts_page(username, after, self._PAGE_SIZE)
            else:
                if not user_id:
                    raise ExtractorError(f'{username}: could not determine user id for reels tab', expected=True)
                after = None
                while True:
                    conn = self._reels_page(user_id, handle, after, self._PAGE_SIZE)
                    yield [e['node']['media'] for e in conn.get('edges') or []], 'clips_tab_pinned_user_ids'
                    info = conn.get('page_info') or {}
                    if not info.get('has_next_page') or not info.get('end_cursor'):
                        return
                    after = info['end_cursor']

        def entries():
            pinned_heap, seen, counter = [], set(), itertools.count()

            def emit(info):
                return self.url_result(
                    info['url'], InstagramIE, info['code'], info['title'],
                    timestamp=info['timestamp'], channel=handle, uploader=full_name,
                    channel_id=user_id, media_type=info['media_type'],
                    has_video=info['has_video'], pinned=info['pinned'])

            for nodes, pinned_key in node_pages():
                for node in nodes:
                    info = self._entry_info(node, pinned_key)
                    if not info['code'] or info['code'] in seen:
                        continue
                    seen.add(info['code'])
                    if videos_only and not info['has_video']:
                        continue
                    if info['pinned']:
                        heapq.heappush(pinned_heap, (-(info['timestamp'] or 0), next(counter), info))
                        continue
                    while pinned_heap and -pinned_heap[0][0] >= (info['timestamp'] or 0):
                        yield emit(heapq.heappop(pinned_heap)[2])
                    yield emit(info)
            while pinned_heap:
                yield emit(heapq.heappop(pinned_heap)[2])

        return self.playlist_result(
            entries(), f'{handle}:{tab}', f'{full_name or handle} - {tab}',
            channel=handle, uploader=full_name, channel_id=user_id, uploader_id=user_id,
            webpage_url=f'https://www.instagram.com/{handle}/{"reels/" if tab == "reels" else ""}')

