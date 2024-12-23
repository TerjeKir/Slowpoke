import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import search.Stack
import search.alphabeta


internal class SearchTest : FunSpec({

    val mx: (Int, String) -> String = { d, fen ->
        Position(fen).alphabeta(Array(100) { Stack() }, -INF, INF, 0, d).toUCI()
    }
    val mate:  (Int) -> String = { ply ->   (MATE - ply).toUCI() }
    val mated: (Int) -> String = { ply -> (-(MATE - ply)).toUCI() }

    test("mate") {
        mx(1, "8/4B3/8/8/8/R7/k1K5/8 b - - 4 3")  shouldBe mated(0)
        mx(2, "8/4B3/8/8/8/3R4/k1K5/8 w - - 3 3") shouldBe mate( 1)
        mx(3, "8/4B3/8/8/8/3R4/2K5/k7 b - - 2 2") shouldBe mated(2)
        mx(4, "8/4B3/8/8/8/8/2KR4/k7 w - - 1 2")  shouldBe mate( 3)
        mx(5, "8/4B3/8/8/8/8/k1KR4/8 b - - 0 1")  shouldBe mated(4)

        mx(5, "6R1/8/7k/r5p1/8/1r4K1/8/7r w - - 4 60") shouldBe mated(4)
    }
})
