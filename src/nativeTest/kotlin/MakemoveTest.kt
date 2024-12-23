@file:Suppress("SpellCheckingInspection")

import engine.*
import io.kotest.core.spec.style.FunSpec
import kotlin.test.assertEquals


internal class MakemoveTest : FunSpec({

    test("makeMoveBasic") {
        assertEquals(
            Position("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"),
            Position(STARTFEN).apply { makeMove(Move(E2, E4)) }
        )
    }

    test("takeMoveBasic") {
        assertEquals(
            Position(STARTFEN),
            Position(STARTFEN).apply {
                makeMove(Move(E2, E4))
                takeMove()
            }
        )
    }

    test("makeMoveCapture") {
        assertEquals(
            Position("rnbqkbnr/ppp1pppp/8/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2"),
            Position(STARTFEN).apply {
                makeMove(Move(E2, E4))
                makeMove(Move(D7, D5))
                makeMove(Move(E4, D5))
            }
        )
    }

    test("takeMoveCapture") {
        assertEquals(
            Position(STARTFEN),
            Position(STARTFEN).apply {
                makeMove(Move(E2, E4))
                makeMove(Move(D7, D5))
                makeMove(Move(E4, D5))
                takeMove()
                takeMove()
                takeMove()
            }
        )
    }

    test("makeMoveEnpas") {
        assertEquals(
            Position("rnbqkbnr/1pp1pppp/p2P4/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 3"),
            Position(STARTFEN).apply {
                makeMove(Move(E2, E4))
                makeMove(Move(A7, A6))
                makeMove(Move(E4, E5))
                makeMove(Move(D7, D5))
                makeMove(Move(E5, D6, ENPAS))
            }
        )
    }

    test("takeMoveEnpas") {
        assertEquals(
            Position(STARTFEN),
            Position(STARTFEN).apply {
                makeMove(Move(E2, E4))
                makeMove(Move(A7, A6))
                makeMove(Move(E4, E5))
                makeMove(Move(D7, D5))
                makeMove(Move(E5, D6, ENPAS))
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
            }
        )
    }

    test("makeMoveCastle") {
        assertEquals(
            Position("1nbqkbnr/1ppppppp/8/1p6/4P3/5N2/rPPP1PPP/RNBQ1RK1 b k - 1 4"),
            Position(STARTFEN).apply {
                makeMove(Move(E2, E4))
                makeMove(Move(A7, A6))
                makeMove(Move(F1, B5))
                makeMove(Move(A6, B5))
                makeMove(Move(G1, F3))
                makeMove(Move(A8, A2))
                makeMove(Move(E1, G1, CASTLE))
            }
        )
    }

    test("takeMoveCastle") {
        assertEquals(
            Position(STARTFEN),
            Position(STARTFEN).apply {
                makeMove(Move(E2, E4))
                makeMove(Move(A7, A6))
                makeMove(Move(F1, B5))
                makeMove(Move(A6, B5))
                makeMove(Move(G1, F3))
                makeMove(Move(A8, A2))
                makeMove(Move(E1, G1, CASTLE))
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
            }
        )
    }

    test("makeMovePromo") {
        assertEquals(
            Position("rnbqkbnQ/1ppppp1p/8/8/8/8/P1PPPPP1/RNqQKBNR w KQq - 0 6"),
            Position(STARTFEN).apply {
                makeMove(Move(H2, H4))
                makeMove(Move(A7, A5))
                makeMove(Move(H4, H5))
                makeMove(Move(A5, A4))
                makeMove(Move(H5, H6))
                makeMove(Move(A4, A3))
                makeMove(Move(H6, G7))
                makeMove(Move(A3, B2))
                makeMove(Move(G7, H8, PROMO, QUEEN))
                makeMove(Move(B2, C1, PROMO, QUEEN))
            }
        )
    }

    test("takeMovePromo") {
        assertEquals(
            Position(STARTFEN),
            Position(STARTFEN).apply {
                makeMove(Move(H2, H4))
                makeMove(Move(A7, A5))
                makeMove(Move(H4, H5))
                makeMove(Move(A5, A4))
                makeMove(Move(H5, H6))
                makeMove(Move(A4, A3))
                makeMove(Move(H6, G7))
                makeMove(Move(A3, B2))
                makeMove(Move(G7, H8, PROMO, QUEEN))
                makeMove(Move(B2, C1, PROMO, QUEEN))
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
                takeMove()
            }
        )
    }
})
