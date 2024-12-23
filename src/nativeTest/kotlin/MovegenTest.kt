import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe


internal class MovegenTest : FunSpec({

    @Suppress("SpellCheckingInspection")
    val enpas = "rnbqkbnr/1pp1pppp/p7/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3"

    test("genMoves") {
        Position(STARTFEN).genMoves().size shouldBe 20
        Position(KIWIPETE).genMoves().size shouldBe 48
        Position(enpas).genMoves().size shouldBe 31
    }
})
