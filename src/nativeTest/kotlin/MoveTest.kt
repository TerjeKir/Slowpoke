import engine.*
import kotlin.test.Test
import kotlin.test.assertEquals


internal class MoveTest {

    @Test
    fun toUCI() {
        assertEquals("b1c3",  Move(B1, C3).toUCI())
        assertEquals("a5b6",  Move(A5, B6, ENPAS).toUCI())
        assertEquals("a7a8n", Move(A7, A8, PROMO, KNIGHT).toUCI())
        assertEquals("a7a8q", Move(A7, A8, PROMO, QUEEN).toUCI())
        assertEquals("e1g1",  Move(E1, G1, CASTLE).toUCI())
    }

    @Test
    fun from() {
        assertEquals(A1, Move(A1, A2).from())
    }

    @Test
    fun to() {
        assertEquals(A2, Move(A1, A2).to())
    }

    @Test
    fun type() {
        assertEquals(NORMAL, Move(A1, A2).type())
        assertEquals(ENPAS,  Move(A5, B6, ENPAS).type())
        assertEquals(CASTLE, Move(E1, C1, CASTLE).type())
        assertEquals(PROMO,  Move(A7, B8, PROMO, QUEEN).type())
    }

    @Test
    fun promo() {
        assertEquals(KNIGHT, Move(A7, B8, PROMO, KNIGHT).promo())
        assertEquals(BISHOP, Move(A7, B8, PROMO, BISHOP).promo())
        assertEquals(ROOK,   Move(A7, B8, PROMO, ROOK).promo())
        assertEquals(QUEEN,  Move(A7, B8, PROMO, QUEEN).promo())
    }
}