import engine.*
import engine.Position
import kotlin.test.Test
import kotlin.test.assertEquals


internal class MovegenTest {

    @Suppress("SpellCheckingInspection")
    val enpas = "rnbqkbnr/1pp1pppp/p7/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3"

    @Test
    fun genMoves() {
        assertEquals(20, Position(STARTFEN).genMoves().size)
        assertEquals(48, Position(KIWIPETE).genMoves().size)
        assertEquals(31, Position(enpas).genMoves().size)
    }
}