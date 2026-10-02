package app.nokta.c

import org.json.JSONArray
import org.json.JSONObject

/** Düz JSON dosya biçimi: {"v":1,"items":[{"id":1,"t":"metin","d":false}]} */
object NoktaJson {
    fun encode(items: List<Item>): String {
        val arr = JSONArray()
        for (it in items) arr.put(JSONObject().put("id", it.id).put("t", it.text).put("d", it.done))
        return JSONObject().put("v", 1).put("items", arr).toString()
    }

    /** Bozuk/boş girdide null döner (çağıran karar verir). Geçersiz öğeleri atlar. */
    fun decode(s: String?): List<Item>? {
        if (s.isNullOrBlank()) return null
        return try {
            val arr = JSONObject(s).getJSONArray("items")
            val out = ArrayList<Item>(arr.length())
            val seen = HashSet<Long>()
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                val id = o.optLong("id", -1L)
                val t = o.optString("t", "")
                if (id < 0 || t.isEmpty() || !seen.add(id)) continue
                out.add(Item(id, t, o.optBoolean("d", false)))
            }
            out
        } catch (e: Exception) {
            null
        }
    }
}
