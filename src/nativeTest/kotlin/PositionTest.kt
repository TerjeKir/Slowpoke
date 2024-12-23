@file:Suppress("SpellCheckingInspection")

import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlin.test.assertEquals


internal class PositionTest : FunSpec({

    val startPos = Position(STARTFEN)
    val shortFen = Position("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -")
    val kiwipete = Position(KIWIPETE)

    test("getPieceBB") {
        startPos.apply {
            pieceBB[  PAWN] shouldBe Bitboard(0x00FF00000000FF00U)
            pieceBB[KNIGHT] shouldBe Bitboard(0x4200000000000042U)
            pieceBB[BISHOP] shouldBe Bitboard(0x2400000000000024U)
            pieceBB[  ROOK] shouldBe Bitboard(0x8100000000000081U)
            pieceBB[ QUEEN] shouldBe Bitboard(0x0800000000000008U)
            pieceBB[  KING] shouldBe Bitboard(0x1000000000000010U)
        }
    }

    test("getColorBB") {
        startPos.colorBB[WHITE] shouldBe Bitboard(0x000000000000FFFFU)
        startPos.colorBB[BLACK] shouldBe Bitboard(0xFFFF000000000000U)
    }

    test("getBoard") {
        startPos.apply {

            for (sq in A2..H2) {
                makePiece(WHITE, PAWN) shouldBe pieceOn(sq)
            }
            for (sq in A7..H7) {
                makePiece(BLACK, PAWN) shouldBe pieceOn(sq)
            }

            makePiece(WHITE,   ROOK) shouldBe pieceOn(A1)
            makePiece(WHITE, KNIGHT) shouldBe pieceOn(B1)
            makePiece(WHITE, BISHOP) shouldBe pieceOn(C1)
            makePiece(WHITE,  QUEEN) shouldBe pieceOn(D1)
            makePiece(WHITE,   KING) shouldBe pieceOn(E1)
            makePiece(WHITE, BISHOP) shouldBe pieceOn(F1)
            makePiece(WHITE, KNIGHT) shouldBe pieceOn(G1)
            makePiece(WHITE,   ROOK) shouldBe pieceOn(H1)

            makePiece(BLACK,   ROOK) shouldBe pieceOn(A8)
            makePiece(BLACK, KNIGHT) shouldBe pieceOn(B8)
            makePiece(BLACK, BISHOP) shouldBe pieceOn(C8)
            makePiece(BLACK,  QUEEN) shouldBe pieceOn(D8)
            makePiece(BLACK,   KING) shouldBe pieceOn(E8)
            makePiece(BLACK, BISHOP) shouldBe pieceOn(F8)
            makePiece(BLACK, KNIGHT) shouldBe pieceOn(G8)
            makePiece(BLACK,   ROOK) shouldBe pieceOn(H8)
        }
    }

    test("getStm") {
        startPos.stm shouldBe WHITE
        kiwipete.stm shouldBe WHITE
    }

    test("getCr") {
        startPos.cr shouldBe ALL_CASTLES
        kiwipete.cr shouldBe ALL_CASTLES
    }

    test("getEp") {
        startPos.ep shouldBe A1
        kiwipete.ep shouldBe A1
    }

    test("getMr50") {
        startPos.mr50 shouldBe 0
        shortFen.mr50 shouldBe 0
        kiwipete.mr50 shouldBe 0
    }

    test("getFullmove") {
        startPos.fullmove shouldBe 1
        shortFen.fullmove shouldBe 1
        kiwipete.fullmove shouldBe 1
    }

    test("testToString") {
        assertEquals(STARTFEN, startPos.toString())
        assertEquals(STARTFEN, shortFen.toString())
        assertEquals(KIWIPETE, kiwipete.toString())
        startPos.toString() shouldBe STARTFEN
        shortFen.toString() shouldBe STARTFEN
        kiwipete.toString() shouldBe KIWIPETE
    }

    test("squaredAttacked") {
        startPos.apply {
            sqAttacked(B1, WHITE).shouldBeTrue()
            sqAttacked(E2, WHITE).shouldBeTrue()
            sqAttacked(E4, WHITE).shouldBeFalse()
            sqAttacked(G8, BLACK).shouldBeTrue()
            sqAttacked(C6, BLACK).shouldBeTrue()
            sqAttacked(E4, BLACK).shouldBeFalse()
        }
    }

    test("inCheck") {
        startPos.inCheck(WHITE).shouldBeFalse()
        startPos.inCheck(BLACK).shouldBeFalse()
        Position("rnb1kbnr/pppp1ppp/8/4p3/4P2q/5P2/PPPP2PP/RNBQKBNR w KQkq - 1 3").inCheck(WHITE).shouldBeTrue()
        Position("rnbq1bnr/ppppkppp/4p1N1/8/8/8/PPPPPPPP/RNBQKB1R b KQ - 3 3").inCheck(BLACK).shouldBeTrue()
        Position("rnbq2nr/ppppbppp/4pkN1/6P1/8/8/PPPPPP1P/RNBQKB1R b KQ - 0 5").inCheck(BLACK).shouldBeTrue()
    }
})
