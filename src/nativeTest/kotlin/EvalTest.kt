import engine.*
import io.kotest.core.spec.style.FunSpec
import kotlin.test.assertEquals


internal class EvalTest : FunSpec({

    val startPos = Position(STARTFEN)
    val kiwipete = Position(KIWIPETE)

    test("eval") {
        assertEquals(0, startPos.eval())
        assertEquals(0, kiwipete.eval())
    }
})
