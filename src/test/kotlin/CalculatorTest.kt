import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CalculatorTest {
    @Test
    fun illegalArgumentsNumber() {
        assertSoftly {
            shouldThrow<IllegalArgumentsNumberException> { Calculator.calculate("10+") }
            shouldThrow<IllegalArgumentsNumberException> { Calculator.calculate("+10.9") }
            shouldThrow<IllegalArgumentsNumberException> { Calculator.calculate("(11.3-9)*") }
            shouldThrow<IllegalArgumentsNumberException> { Calculator.calculate("*(11.3-9)") }
        }
    }

    @Test
    fun calculationTest() {
        assertSoftly {
            Calculator.calculate("1+1") shouldBe 2.0
            Calculator.calculate("--20") shouldBe 20.0
            Calculator.calculate("((10*(6/((9+3)*-11)))+17)+5") shouldBe 21.545454545454547
            Calculator.calculate("1^0") shouldBe 1
            Calculator.calculate("0^0") shouldBe 1
            Calculator.calculate("2^2") shouldBe 4
            Calculator.calculate("2^2") shouldBe 4
        }
    }
}
