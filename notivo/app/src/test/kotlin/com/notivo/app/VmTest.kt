package com.notivo.app

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.notivo.app.data.Db
import com.notivo.app.ui.Vm
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class VmTest {
    private val app: Application get() = ApplicationProvider.getApplicationContext()
    private lateinit var vm: Vm

    @Before fun setUp() { resetDbSingleton(); vm = Vm(app) }
    @After fun tearDown() { resetDbSingleton() }

    private fun seed(n: Int) = runBlocking { repeat(n) { Db.get(app).dao().insert(notif(text = "mesaj $it \"tırnaklı\", virgüllü", time = 1_700_000_000_000L + it)) } }

    @Test fun backupRestoreRoundTripSkipsDuplicatesAndRejectsOtherJson() {
        seed(3)
        awaitUntil { vm.all.value.size == 3 }
        val json = vm.backupJson()
        // restoring onto the same data adds nothing
        assertEquals(0, runBlocking { vm.restore(json) })
        // wipe, then restore everything
        runBlocking { Db.get(app).dao().clearUnstarred() }
        awaitUntil { vm.all.value.isEmpty() }
        assertEquals(3, runBlocking { vm.restore(json) })
        awaitUntil { vm.all.value.size == 3 }
        assertTrue(vm.all.value.any { it.text.contains("\"tırnaklı\"") })
        assertEquals(-1, runBlocking { vm.restore("{\"app\":\"baska\",\"items\":[]}") })
        assertEquals(-1, runBlocking { vm.restore("bu json değil") })
    }

    @Test fun csvEscapesQuotesAndKeepsHeader() {
        seed(1); awaitUntil { vm.all.value.size == 1 }
        val csv = vm.exportCsv().lines()
        assertEquals("time,app,title,text", csv[0])
        assertTrue(csv[1].contains("\"mesaj 0 \"\"tırnaklı\"\", virgüllü\""))
    }

    @Test fun replyIsOfferedOnlyWhenALiveNotificationExists() {
        assertFalse(vm.canReply("com.chat", "Ayşe"))
        assertFalse(vm.reply("com.chat", "Ayşe", "selam"))
    }

    @Test fun openReturnsFalseWhenNothingCanBeOpened() {
        seed(1); awaitUntil { vm.all.value.size == 1 }
        assertFalse(vm.open(vm.all.value.first()))   // no live notification, no launcher activity for com.chat
    }
}
