package app.nokta.list

import app.nokta.list.core.ListModel
import app.nokta.list.data.Item
import app.nokta.list.data.ItemStore
import org.junit.Assert.*
import org.junit.Test

/** Room kaydinin sozlesmesini bellek ici sahte ile dener: replaceAll semantigi (tum goruntu yazilir). */
class StorageTest {
    private class FakeStore : ItemStore {
        var rows: List<Item> = emptyList()
        var writes = 0
        // Room replaceAll gibi: eskiyi at, yenisini yaz; okuma position sirasina gore.
        override fun save(items: List<Item>) { rows = items.toList(); writes++ }
        override fun load(): List<Item> = rows.sortedBy { it.position }
    }

    @Test fun saveLoadKeepsOrderAndDoneState() {
        val s = FakeStore()
        val m = ListModel()
        listOf("c", "b", "a").forEach { m.add(it) }
        m.toggle(m.items[1].id); m.move(0, 2)
        s.save(m.items)
        val back = ListModel(s.load())
        assertEquals(m.items, back.items)
    }

    @Test fun loadedModelContinuesIdsWithoutCollision() {
        val s = FakeStore()
        val m = ListModel(); m.add("a"); m.add("b")
        s.save(m.items)
        val back = ListModel(s.load()); back.add("c")
        assertEquals(3, back.items.map { it.id }.toSet().size)
    }

    @Test fun loadHealsUnsortedOrMissingPositions() {
        val back = ListModel(listOf(Item(5, "z", false, 9), Item(2, "y", true, 3)))
        assertEquals(listOf("y", "z"), back.items.map { it.text })
        assertEquals(listOf(0, 1), back.items.map { it.position })
    }

    @Test fun undoneDeleteIsPersistedAsLatestSnapshot() {
        val s = FakeStore()
        val m = ListModel(); m.add("a"); m.add("b")
        s.save(m.items)
        m.delete(m.items[0].id); s.save(m.items)
        assertEquals(1, s.load().size)
        m.undoLast(); s.save(m.items)
        assertEquals(listOf("b", "a"), s.load().map { it.text })
    }
}
