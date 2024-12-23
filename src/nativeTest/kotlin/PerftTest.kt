import engine.*
import misc.perft
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime


internal class PerftTest {

    private fun testPerft(depth: Int, fen: String) = perft("perft $depth $fen", false)

    @Test
    fun kiwipete() {
        assertEquals(     48, testPerft(1, KIWIPETE))
        assertEquals(   2039, perft("perft 2", false))
        assertEquals(  97862, perft("perft 3", false))
        assertEquals(4085603, perft("perft", false))
    }

    @Test
    fun startfen() {
        assertEquals(   20, testPerft(1, STARTFEN))
        assertEquals(  400, testPerft(2, STARTFEN))
        assertEquals( 8902, testPerft(3, STARTFEN))
    }
}
