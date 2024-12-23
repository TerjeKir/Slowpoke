import engine.*
import io.kotest.core.spec.style.FunSpec
import kotlin.test.assertEquals


internal class MovegenTest : FunSpec({

    @Suppress("SpellCheckingInspection")
    val enpas = "rnbqkbnr/1pp1pppp/p7/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3"

    test("genMoves") {
        assertEquals(20, Position(STARTFEN).genMoves().size)
        assertEquals(48, Position(KIWIPETE).genMoves().size)
        assertEquals(31, Position(enpas).genMoves().size)
    }
})
