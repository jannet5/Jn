package app.nokta.b.ui

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.nokta.b.R
import app.nokta.b.bubble.BubbleService
import app.nokta.b.core.Repo

class MainActivity : AppCompatActivity() {
    private lateinit var repo: Repo
    private lateinit var controller: ListController
    private lateinit var banner: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repo = Repo.get(this)
        banner = findViewById(R.id.perm_banner)
        controller = ListController(findViewById(R.id.list_root), repo, ::showMenu, R.drawable.ic_menu)
        findViewById<View>(R.id.perm_action).setOnClickListener {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
        }
        controller.focusAdd()
    }

    override fun onStart() {
        super.onStart()
        controller.attach()
    }

    override fun onResume() {
        super.onResume()
        val can = Settings.canDrawOverlays(this)
        banner.visibility = if (can) View.GONE else View.VISIBLE
        if (can && BubbleService.wantsBubble(this) && !BubbleService.running) {
            askNotificationPermission()
            BubbleService.start(this)
        }
    }

    override fun onStop() {
        controller.detach()
        super.onStop()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED &&
            !notifAsked()
        ) {
            getSharedPreferences("nokta", Context.MODE_PRIVATE).edit().putBoolean("notif_asked", true).apply()
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }
    }

    private fun notifAsked() = getSharedPreferences("nokta", Context.MODE_PRIVATE).getBoolean("notif_asked", false)

    private fun showMenu() {
        val anchor = controller.actionButton
        val m = PopupMenu(this, anchor)
        val running = BubbleService.visible
        m.menu.add(0, 1, 0, if (running) R.string.bubble_hide else R.string.bubble_show)
        m.menu.add(0, 2, 1, R.string.clear_done).isEnabled = repo.doneCount > 0
        m.menu.add(0, 3, 2, R.string.share_backup).isEnabled = repo.items.isNotEmpty()
        m.menu.add(0, 4, 3, R.string.import_clip)
        m.setOnMenuItemClickListener {
            when (it.itemId) {
                1 -> toggleBubble(running)
                2 -> repo.clearDone()
                3 -> share()
                4 -> importClip()
            }
            true
        }
        m.show()
    }

    private fun toggleBubble(running: Boolean) {
        if (running) { BubbleService.stop(this); return }
        if (!Settings.canDrawOverlays(this)) { banner.visibility = View.VISIBLE; return }
        BubbleService.setWanted(this, true)
        askNotificationPermission()
        BubbleService.start(this)
    }

    private fun share() {
        val send = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, repo.export())
        startActivity(Intent.createChooser(send, getString(R.string.share_backup)))
    }

    private fun importClip() {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val t = cm.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(this)?.toString().orEmpty()
        val n = if (t.isBlank()) 0 else repo.importText(t)
        Toast.makeText(this, if (n == 0) getString(R.string.import_none) else getString(R.string.import_done, n), Toast.LENGTH_SHORT).show()
    }
}
