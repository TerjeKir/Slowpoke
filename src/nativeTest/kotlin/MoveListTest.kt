import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe


internal class MoveListTest : FunSpec({

    test("MoveList") {
        val ml = MoveList().apply {
            add(Move(B1, C3))
            add(Move(B2, C4))
            add(Move(B3, C5))
            add(Move(B4, C6))
        }
        for (move in ml) {
            move.from() shouldBeInRange B1..B4
            move.to() shouldBeInRange C3..C6
            move.to() - move.from() shouldBe C3 - B1
        }
    }
})
