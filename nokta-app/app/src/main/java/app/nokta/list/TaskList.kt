package app.nokta.list

import org.json.JSONArray
import org.json.JSONObject

data class Task(val id: Long, val text: String, val done: Boolean)

/** Pure list logic (no Android deps) so it is unit-testable. */
class TaskList(initial: List<Task> = emptyList()) {
    private val items = initial.toMutableList()
    private var nextId = (initial.maxOfOrNull { it.id } ?: 0L) + 1

    val all: List<Task> get() = items.toList()
    val remaining: Int get() = items.count { !it.done }

    fun add(text: String): Task? {
        val t = text.trim()
        if (t.isEmpty()) return null
        val task = Task(nextId++, t, false)
        items.add(task)
        return task
    }

    fun toggle(index: Int) {
        if (index !in items.indices) return
        items[index] = items[index].let { it.copy(done = !it.done) }
    }

    fun remove(index: Int): Task? = if (index in items.indices) items.removeAt(index) else null

    fun move(from: Int, to: Int) {
        if (from !in items.indices || to !in items.indices || from == to) return
        items.add(to, items.removeAt(from))
    }

    fun toJson(): String = JSONArray().also { arr ->
        items.forEach {
            arr.put(JSONObject().put("id", it.id).put("text", it.text).put("done", it.done))
        }
    }.toString()

    companion object {
        fun fromJson(json: String?): TaskList {
            if (json.isNullOrBlank()) return TaskList()
            return try {
                val arr = JSONArray(json)
                TaskList((0 until arr.length()).map {
                    val o = arr.getJSONObject(it)
                    Task(o.getLong("id"), o.getString("text"), o.optBoolean("done"))
                })
            } catch (e: Exception) {
                TaskList()
            }
        }
    }
}
