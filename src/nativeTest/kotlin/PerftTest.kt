import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import misc.perft


internal class PerftTest : FunSpec({

    fun testPerft(depth: Int, fen: String) = perft("perft $depth $fen", false)

    test("kiwipete") {
        testPerft(1, KIWIPETE) shouldBe      48
        testPerft(2, KIWIPETE) shouldBe    2039
        testPerft(3, KIWIPETE) shouldBe   97862
        testPerft(4, KIWIPETE) shouldBe 4085603
    }

    test("startfen") {
        testPerft(1, STARTFEN) shouldBe   20
        testPerft(2, STARTFEN) shouldBe  400
        testPerft(3, STARTFEN) shouldBe 8902
    }
})
