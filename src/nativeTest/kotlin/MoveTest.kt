import engine.*
import io.kotest.core.spec.style.FunSpec
import kotlin.test.assertEquals


internal class MoveTest : FunSpec({

    test("toUCI") {
        assertEquals("b1c3",  Move(B1, C3).toUCI())
        assertEquals("a5b6",  Move(A5, B6, ENPAS).toUCI())
        assertEquals("a7a8n", Move(A7, A8, PROMO, KNIGHT).toUCI())
        assertEquals("a7a8q", Move(A7, A8, PROMO, QUEEN).toUCI())
        assertEquals("e1g1",  Move(E1, G1, CASTLE).toUCI())
    }

    test("from") {
        assertEquals(A1, Move(A1, A2).from())
    }

    test("to") {
        assertEquals(A2, Move(A1, A2).to())
    }

    test("type") {
        assertEquals(NORMAL, Move(A1, A2).type())
        assertEquals(ENPAS,  Move(A5, B6, ENPAS).type())
        assertEquals(CASTLE, Move(E1, C1, CASTLE).type())
        assertEquals(PROMO,  Move(A7, B8, PROMO, QUEEN).type())
    }

    test("promo") {
        assertEquals(KNIGHT, Move(A7, B8, PROMO, KNIGHT).promo())
        assertEquals(BISHOP, Move(A7, B8, PROMO, BISHOP).promo())
        assertEquals(ROOK,   Move(A7, B8, PROMO, ROOK).promo())
        assertEquals(QUEEN,  Move(A7, B8, PROMO, QUEEN).promo())
    }
})
