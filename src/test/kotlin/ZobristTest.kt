import engine.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ZobristTest {

    private val startPos = Position(STARTFEN)
    private val kiwipete = Position(KIWIPETE)

    @Test
    fun hasRepeated() {
        assert(startPos.apply {
            makeMove(Move(G1, F3))
            makeMove(Move(G8, F6))
            makeMove(Move(F3, G1))
            makeMove(Move(F6, G8))
        }.hasRepeated())

        assert(kiwipete.apply {
            makeMove(Move(E2, B5))
            makeMove(Move(A6, B7))
            makeMove(Move(B5, A6))
            makeMove(Move(B7, C8))
            makeMove(Move(A6, E2))
            makeMove(Move(C8, A6))
        }.hasRepeated())
    }

    @Test
    fun key() {
        fun Position.makeKey(): Key {
            var key: Key = 0
            for (sq in A1..H8)
                if (pieceOn(sq) != 0) key = key xor PieceKeys[pieceOn(sq)][sq]
            if (stm == WHITE) key = key xor SideKey
            if (ep != 0) key = key xor PieceKeys[0][ep]
            key = key xor CastleKeys[cr]
            return key
        }
        assertEquals(startPos.key, startPos.makeKey())
        assertEquals(kiwipete.key, kiwipete.makeKey())
    }
}
