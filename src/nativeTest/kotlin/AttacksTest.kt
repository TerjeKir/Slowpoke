import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe


internal class AttacksTest : FunSpec({

    test("attackBB") {
        attackBB(KNIGHT, A1) shouldBe Bitboard(B3, C2)
        attackBB(KNIGHT, H8) shouldBe Bitboard(F7, G6)
        attackBB(KNIGHT, A4) shouldBe Bitboard(B2, B6, C3, C5)
        attackBB(KNIGHT, D4) shouldBe Bitboard(B3, B5, C2, C6, E2, E6, F3, F5)
    }

    test("attackBBKing") {
        attackBB(KING, A1) shouldBe Bitboard(A2, B1, B2)
        attackBB(KING, D4) shouldBe Bitboard(C3, C4, C5, D3, D5, E3, E4, E5)
    }

    test("attackBBBishop") {
        attackBB(BISHOP, A1) shouldBe Bitboard(B2, C3, D4, E5, F6, G7, H8)
        attackBB(BISHOP, A1, Bitboard(E5)) shouldBe Bitboard(B2, C3, D4, E5)
        attackBB(BISHOP, D4) shouldBe Bitboard(A1, B2, C3, E5, F6, G7, H8, A7, B6, C5, E3, F2, G1)
        attackBB(BISHOP, D4, Bitboard(C3, C5, E3, E5)) shouldBe Bitboard(C3, C5, E3, E5)
    }

    test("attackBBRook") {
        attackBB(ROOK, A1) shouldBe Bitboard(A2, A3, A4, A5, A6, A7, A8, B1, C1, D1, E1, F1, G1, H1)
        attackBB(ROOK, A1, Bitboard(A4, D1)) shouldBe Bitboard(A2, A3, A4, B1, C1, D1)
        attackBB(ROOK, D4) shouldBe Bitboard(A4, B4, C4, E4, F4, G4, H4, D1, D2, D3, D5, D6, D7, D8)
        attackBB(ROOK, D4, Bitboard(C4, D3, D5, E4)) shouldBe Bitboard(C4, D3, D5, E4)
    }

    test("attackBBQueen") {
        attackBB(QUEEN, D4) shouldBe Bitboard(
            A4, B4, C4, E4, F4, G4, H4, D1, D2, D3, D5, D6, D7, D8, A1, B2, C3, E5, F6, G7, H8, A7, B6, C5, E3, F2, G1
        )
    }

    test("pawnAttackBB") {
        pawnAttackBB(WHITE, D8) shouldBe Bitboard()
        pawnAttackBB(BLACK, D1) shouldBe Bitboard()
        pawnAttackBB(WHITE, A2) shouldBe Bitboard(B3)
        pawnAttackBB(BLACK, A2) shouldBe Bitboard(B1)
        pawnAttackBB(WHITE, H2) shouldBe Bitboard(G3)
        pawnAttackBB(BLACK, H2) shouldBe Bitboard(G1)
        pawnAttackBB(WHITE, B2) shouldBe Bitboard(A3, C3)
        pawnAttackBB(BLACK, B2) shouldBe Bitboard(A1, C1)
    }
})
