import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe


internal class EvalTest : FunSpec({

    val startPos = Position(STARTFEN)
    val kiwipete = Position(KIWIPETE)

    test("eval") {
        startPos.eval() shouldBe 0
        kiwipete.eval() shouldBe 0
    }
})
