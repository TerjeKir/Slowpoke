import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlin.test.fail


internal class BitboardTest : FunSpec({

    val fullBB = Bitboard(ULong.MAX_VALUE)
    val emptyBB = Bitboard()
    val startBB = Bitboard(
        *(A1..H2).toList().toIntArray(),
        *(A7..H8).toList().toIntArray()
    )

    test("isEmpty") {
        emptyBB.isEmpty().shouldBeTrue()
        Bitboard(A1).isEmpty().shouldBeFalse()
    }

    test("notEmpty") {
        emptyBB.nonEmpty().shouldBeFalse()
        Bitboard(A1).nonEmpty().shouldBeTrue()
    }

    test("lsb") {
        Bitboard(A1).lsb() shouldBe A1
        Bitboard(C8).lsb() shouldBe C8
    }

    test("popcnt") {
        emptyBB.popcnt() shouldBe 0
        startBB.popcnt() shouldBe 32
        Bitboard(A2, B5, H1).popcnt() shouldBe 3
        Bitboard(*(E4..D5).toList().toIntArray()).popcnt() shouldBe 8
    }

    test("shift") {
        emptyBB.shift(8).isEmpty().shouldBeTrue()
        // Basic move
        Bitboard(A1).shift(EAST) shouldBe Bitboard(B1)
        Bitboard(B1).shift(WEST) shouldBe Bitboard(A1)
        // Moves off the board
        Bitboard(H1).shift(EAST) shouldBe emptyBB
        Bitboard(A1).shift(NORTH+WEST) shouldBe emptyBB
    }

    test("iterator") {
        startBB.fold(0) { count, _ -> count + 1 } shouldBe 32
        Bitboard(F1, G8).fold(0) { count, _ -> count + 1 } shouldBe 2

        for (sq in emptyBB) {
            fail("Should not get here")
        }
    }

    test("operators") {
        Bitboard(C4) shl 1 shouldBe Bitboard(D4)
        Bitboard(C4) shr 1 shouldBe Bitboard(B4)

        emptyBB and startBB shouldBe emptyBB
        startBB and startBB shouldBe startBB

        emptyBB or startBB shouldBe startBB
        startBB or startBB shouldBe startBB

        emptyBB xor startBB shouldBe startBB
        startBB xor startBB shouldBe emptyBB

        emptyBB.inv() shouldBe fullBB
        fullBB.inv() shouldBe emptyBB
    }
})
