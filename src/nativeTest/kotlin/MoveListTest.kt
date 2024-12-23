import engine.*
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalNativeApi::class)
internal class MoveListTest {

    @Test
    fun test() {
        val ml = MoveList().apply {
            add(Move(B1, C3))
            add(Move(B2, C4))
            add(Move(B3, C5))
            add(Move(B4, C6))
        }
        for (move in ml) {
            assert(move.from() in B1..B4)
            assert(move.to() in C3..C6)
            assertEquals(C3 - B1, move.to() - move.from())
        }
    }
}
