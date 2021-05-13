import engine.*
import kotlin.test.Test
import kotlin.test.assertEquals


internal class AttacksTest {

    @Test
    fun attackBB() {
        assertEquals(Bitboard(B3, C2), attackBB(KNIGHT, A1))
        assertEquals(Bitboard(F7, G6), attackBB(KNIGHT, H8))
        assertEquals(Bitboard(B2, B6, C3, C5), attackBB(KNIGHT, A4))
        assertEquals(Bitboard(B3, B5, C2, C6, E2, E6, F3, F5), attackBB(KNIGHT, D4))
    }

    @Test
    fun attackBBKing() {
        assertEquals(Bitboard(A2, B1, B2), attackBB(KING, A1))
        assertEquals(Bitboard(C3, C4, C5, D3, D5, E3, E4, E5), attackBB(KING, D4))
    }

    @Test
    fun attackBBBishop() {
        assertEquals(Bitboard(B2, C3, D4, E5, F6, G7, H8), attackBB(BISHOP, A1))
        assertEquals(Bitboard(B2, C3, D4, E5), attackBB(BISHOP, A1, Bitboard(E5)))
        assertEquals(Bitboard(A1, B2, C3, E5, F6, G7, H8, A7, B6, C5, E3, F2, G1), attackBB(BISHOP, D4))
        assertEquals(Bitboard(C3, C5, E3, E5), attackBB(BISHOP, D4, Bitboard(C3, C5, E3, E5)))
    }

    @Test
    fun attackBBRook() {
        assertEquals(Bitboard(A2, A3, A4, A5, A6, A7, A8, B1, C1, D1, E1, F1, G1, H1), attackBB(ROOK, A1))
        assertEquals(Bitboard(A2, A3, A4, B1, C1, D1), attackBB(ROOK, A1, Bitboard(A4, D1)))
        assertEquals(Bitboard(A4, B4, C4, E4, F4, G4, H4, D1, D2, D3, D5, D6, D7, D8), attackBB(ROOK, D4))
        assertEquals(Bitboard(C4, D3, D5, E4), attackBB(ROOK, D4, Bitboard(C4, D3, D5, E4)))
    }

    @Test
    fun attackBBQueen() {
        assertEquals(
            Bitboard(A4, B4, C4, E4, F4, G4, H4, D1, D2, D3, D5, D6, D7, D8) or
                    Bitboard(A1, B2, C3, E5, F6, G7, H8, A7, B6, C5, E3, F2, G1),
            attackBB(QUEEN, D4)
        )
    }

    @Test
    fun pawnAttackBB() {
        assertEquals(Bitboard(), pawnAttackBB(WHITE, D8))
        assertEquals(Bitboard(), pawnAttackBB(BLACK, D1))
        assertEquals(Bitboard(B3), pawnAttackBB(WHITE, A2))
        assertEquals(Bitboard(B1), pawnAttackBB(BLACK, A2))
        assertEquals(Bitboard(G3), pawnAttackBB(WHITE, H2))
        assertEquals(Bitboard(G1), pawnAttackBB(BLACK, H2))
        assertEquals(Bitboard(A3, C3), pawnAttackBB(WHITE, B2))
        assertEquals(Bitboard(A1, C1), pawnAttackBB(BLACK, B2))
    }
}