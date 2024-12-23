@file:Suppress("SpellCheckingInspection")

import engine.*
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalNativeApi::class)
internal class PositionTest {

    private val startPos = Position(STARTFEN)
    private val shortFen = Position("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -")
    private val kiwipete = Position(KIWIPETE)

    @Test
    fun getPieceBB() {
        startPos.apply {
            assertEquals(Bitboard(0x00FF00000000FF00U), pieceBB[PAWN])
            assertEquals(Bitboard(0x4200000000000042U), pieceBB[KNIGHT])
            assertEquals(Bitboard(0x2400000000000024U), pieceBB[BISHOP])
            assertEquals(Bitboard(0x8100000000000081U), pieceBB[ROOK])
            assertEquals(Bitboard(0x0800000000000008U), pieceBB[QUEEN])
            assertEquals(Bitboard(0x1000000000000010U), pieceBB[KING])
        }
    }

    @Test
    fun getColorBB() {
        assertEquals(Bitboard(0x000000000000FFFFU), startPos.colorBB[WHITE])
        assertEquals(Bitboard(0xFFFF000000000000U), startPos.colorBB[BLACK])
    }

    @Test
    fun getBoard() {
        startPos.apply {

            for (sq in A2..H2) {
                assertEquals(makePiece(WHITE, PAWN), pieceOn(sq))
            }
            for (sq in A7..H7) {
                assertEquals(makePiece(BLACK, PAWN), pieceOn(sq))
            }

            assertEquals(makePiece(WHITE,   ROOK), pieceOn(A1))
            assertEquals(makePiece(WHITE, KNIGHT), pieceOn(B1))
            assertEquals(makePiece(WHITE, BISHOP), pieceOn(C1))
            assertEquals(makePiece(WHITE,  QUEEN), pieceOn(D1))
            assertEquals(makePiece(WHITE,   KING), pieceOn(E1))
            assertEquals(makePiece(WHITE, BISHOP), pieceOn(F1))
            assertEquals(makePiece(WHITE, KNIGHT), pieceOn(G1))
            assertEquals(makePiece(WHITE,   ROOK), pieceOn(H1))

            assertEquals(makePiece(BLACK,   ROOK), pieceOn(A8))
            assertEquals(makePiece(BLACK, KNIGHT), pieceOn(B8))
            assertEquals(makePiece(BLACK, BISHOP), pieceOn(C8))
            assertEquals(makePiece(BLACK,  QUEEN), pieceOn(D8))
            assertEquals(makePiece(BLACK,   KING), pieceOn(E8))
            assertEquals(makePiece(BLACK, BISHOP), pieceOn(F8))
            assertEquals(makePiece(BLACK, KNIGHT), pieceOn(G8))
            assertEquals(makePiece(BLACK,   ROOK), pieceOn(H8))
        }
    }

    @Test
    fun getStm() {
        assertEquals(WHITE, startPos.stm)
        assertEquals(WHITE, kiwipete.stm)
    }

    @Test
    fun getCr() {
        assertEquals(ALL_CASTLES, startPos.cr)
        assertEquals(ALL_CASTLES, kiwipete.cr)
    }

    @Test
    fun getEp() {
        assertEquals(A1, startPos.ep)
        assertEquals(A1, kiwipete.ep)
    }

    @Test
    fun getMr50() {
        assertEquals(0, startPos.mr50)
        assertEquals(0, shortFen.mr50)
        assertEquals(0, kiwipete.mr50)
    }

    @Test
    fun getFullmove() {
        assertEquals(1, startPos.fullmove)
        assertEquals(1, shortFen.fullmove)
        assertEquals(1, kiwipete.fullmove)
    }

    @Test
    fun testToString() {
        assertEquals(STARTFEN, startPos.toString())
        assertEquals(STARTFEN, shortFen.toString())
        assertEquals(KIWIPETE, kiwipete.toString())
    }

    @Test
    fun squaredAttacked() {
        startPos.apply {
            assert( sqAttacked(B1, WHITE))
            assert( sqAttacked(E2, WHITE))
            assert(!sqAttacked(E4, WHITE))
            assert( sqAttacked(G8, BLACK))
            assert( sqAttacked(C6, BLACK))
            assert(!sqAttacked(E4, BLACK))
        }
    }

    @Test
    fun inCheck() {
        assert(!startPos.inCheck(WHITE))
        assert(!startPos.inCheck(BLACK))
        assert(Position("rnb1kbnr/pppp1ppp/8/4p3/4P2q/5P2/PPPP2PP/RNBQKBNR w KQkq - 1 3").inCheck(WHITE))
        assert(Position("rnbq1bnr/ppppkppp/4p1N1/8/8/8/PPPPPPPP/RNBQKB1R b KQ - 3 3").inCheck(BLACK))
        assert(Position("rnbq2nr/ppppbppp/4pkN1/6P1/8/8/PPPPPP1P/RNBQKB1R b KQ - 0 5").inCheck(BLACK))
    }
}
