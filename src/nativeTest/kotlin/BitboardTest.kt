import engine.*
import io.kotest.core.spec.style.FunSpec
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.assertEquals


@OptIn(ExperimentalNativeApi::class)
internal class BitboardTest : FunSpec({

    val fullBB = Bitboard(ULong.MAX_VALUE)
    val emptyBB = Bitboard()
    val startBB = Bitboard(
        *(A1..H2).toList().toIntArray(),
        *(A7..H8).toList().toIntArray()
    )

    test("isEmpty") {
        assert(emptyBB.isEmpty())
        assert(!Bitboard(A1).isEmpty())
    }

    test("notEmpty") {
        assert(!emptyBB.nonEmpty())
        assert(Bitboard(A1).nonEmpty())
    }

    test("lsb") {
        assertEquals(A1, Bitboard(A1).lsb())
        assertEquals(C8, Bitboard(C8).lsb())
    }

    test("popcnt") {
        assertEquals(0, emptyBB.popcnt())
        assertEquals(32, startBB.popcnt())
        assertEquals(3, Bitboard(A2, B5, H1).popcnt())
        assertEquals(8, Bitboard(*(E4..D5).toList().toIntArray()).popcnt())
    }

    test("shift") {
        assert(emptyBB.shift(8).isEmpty())
        // Basic move
        assertEquals(Bitboard(B1), Bitboard(A1).shift(1))
        assertEquals(Bitboard(A1), Bitboard(B1).shift(-1))
        // Moves off the board
        assertEquals(emptyBB, Bitboard(H1).shift(1))
        assertEquals(emptyBB, Bitboard(A1).shift(7))
    }

    test("iterator") {
        assertEquals(32, startBB.fold(0) { count, _ -> count + 1 })
        assertEquals(2, Bitboard(F1, G8).fold(0) { count, _ -> count + 1 })

        for (sq in emptyBB) {
            assert(false)
        }
    }

    test("operators") {
        assertEquals(Bitboard(D4), Bitboard(C4) shl 1)
        assertEquals(Bitboard(B4), Bitboard(C4) shr 1)

        assertEquals(emptyBB, emptyBB and startBB)
        assertEquals(startBB, startBB and startBB)

        assertEquals(startBB, emptyBB or startBB)
        assertEquals(startBB, startBB or startBB)

        assertEquals(startBB, emptyBB xor startBB)
        assertEquals(emptyBB, startBB xor startBB)

        assertEquals(fullBB, emptyBB.inv())
        assertEquals(emptyBB, fullBB.inv())
    }
})
