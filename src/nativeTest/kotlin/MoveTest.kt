import engine.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe


internal class MoveTest : FunSpec({

    test("toUCI") {
        Move(B1, C3).toUCI() shouldBe "b1c3"
        Move(A5, B6, ENPAS).toUCI() shouldBe "a5b6"
        Move(A7, A8, PROMO, KNIGHT).toUCI() shouldBe "a7a8n"
        Move(A7, A8, PROMO, QUEEN).toUCI() shouldBe "a7a8q"
        Move(E1, G1, CASTLE).toUCI() shouldBe "e1g1"
    }

    test("from") {
        Move(A1, A2).from() shouldBe A1
    }

    test("to") {
        Move(A1, A2).to() shouldBe A2
    }

    test("type") {
        Move(A1, A2).type() shouldBe NORMAL
        Move(A5, B6, ENPAS).type() shouldBe ENPAS
        Move(E1, C1, CASTLE).type() shouldBe CASTLE
        Move(A7, B8, PROMO, QUEEN).type() shouldBe PROMO
    }

    test("promo") {
        Move(A7, B8, PROMO, KNIGHT).promo() shouldBe KNIGHT
        Move(A7, B8, PROMO, BISHOP).promo() shouldBe BISHOP
        Move(A7, B8, PROMO, ROOK).promo() shouldBe ROOK
        Move(A7, B8, PROMO, QUEEN).promo() shouldBe QUEEN
    }
})
