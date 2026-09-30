package app.nokta.list

import android.content.Context

/** Single process-wide list persisted as JSON in SharedPreferences (tiny + fast). */
object Store {
    private const val PREFS = "nokta"
    private const val KEY = "tasks"
    const val KEY_BUBBLE = "bubble"

    @Volatile private var list: TaskList? = null

    fun get(ctx: Context): TaskList = list ?: synchronized(this) {
        list ?: TaskList.fromJson(prefs(ctx).getString(KEY, null)).also { list = it }
    }

    fun save(ctx: Context) {
        val l = list ?: return
        prefs(ctx).edit().putString(KEY, l.toJson()).apply()
    }

    fun prefs(ctx: Context) = ctx.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
