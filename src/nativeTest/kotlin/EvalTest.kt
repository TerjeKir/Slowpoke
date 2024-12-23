import engine.*
import engine.Position
import kotlin.test.Test
import kotlin.test.assertEquals


internal class EvalTest {

    private val startPos = Position(STARTFEN)
    private val kiwipete = Position(KIWIPETE)

    @Test
    fun eval() {
        assertEquals(0, startPos.eval())
        assertEquals(0, kiwipete.eval())
    }
}