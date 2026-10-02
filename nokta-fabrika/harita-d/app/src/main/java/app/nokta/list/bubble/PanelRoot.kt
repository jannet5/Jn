package app.nokta.list.bubble

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.FrameLayout

/** Panel penceresinin kok gorunumu: Geri tusunu yakalar (overlay penceresi Activity degildir). */
class PanelRoot @JvmOverloads constructor(c: Context, a: AttributeSet? = null) : FrameLayout(c, a) {
    var onBack: (() -> Unit)? = null

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.action == KeyEvent.ACTION_UP) onBack?.invoke()
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}
