import engine.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime


@ExperimentalTime
internal class SearchTest {

    val mx: (Int, String) -> String = { d, fen -> Position(fen).minimax(0, d).toUCI() }
    val mate:  (Int) -> String = { ply ->   (MATE - ply).toUCI() }
    val mated: (Int) -> String = { ply -> (-(MATE - ply)).toUCI() }

    @Test
    fun mate() {
        assertEquals(mated(0), mx(1, "8/4B3/8/8/8/R7/k1K5/8 b - - 4 3"))
        assertEquals(mate( 1), mx(2, "8/4B3/8/8/8/3R4/k1K5/8 w - - 3 3"))
        assertEquals(mated(2), mx(3, "8/4B3/8/8/8/3R4/2K5/k7 b - - 2 2"))
        assertEquals(mate( 3), mx(4, "8/4B3/8/8/8/8/2KR4/k7 w - - 1 2"))
        assertEquals(mated(4), mx(5, "8/4B3/8/8/8/8/k1KR4/8 b - - 0 1"))
    }
}