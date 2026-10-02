package app.nokta.a.model

import org.json.JSONArray
import org.json.JSONObject

/** Disk biçimi: {"v":1,"next":3,"items":[{"id":1,"t":"süt","d":false}]} */
object ListCodec {
    const val VERSION = 1

    fun encode(state: ListState): String {
        val arr = JSONArray()
        state.items.forEach {
            arr.put(JSONObject().put("id", it.id).put("t", it.text).put("d", it.done))
        }
        return JSONObject().put("v", VERSION).put("next", state.nextId).put("items", arr).toString()
    }

    /** Bozuk girdide null döner (çağıran yedekleyip boş başlar). Eksik/geçersiz öğeler atlanır. */
    fun decode(json: String?): ListState? {
        if (json.isNullOrBlank()) return null
        return try {
            val o = JSONObject(json)
            val arr = o.getJSONArray("items")
            val seen = HashSet<Long>()
            val items = ArrayList<Item>()
            for (i in 0 until arr.length()) {
                val e = arr.optJSONObject(i) ?: continue
                val id = e.optLong("id", -1)
                val t = ListState.normalize(e.optString("t", ""))
                if (id < 0 || t.isEmpty() || !seen.add(id)) continue
                items.add(Item(id, t, e.optBoolean("d", false)))
            }
            val next = maxOf(o.optLong("next", 1L), (items.maxOfOrNull { it.id } ?: 0L) + 1)
            ListState(items, next)
        } catch (e: Exception) {
            null
        }
    }
}
