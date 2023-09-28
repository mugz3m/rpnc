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
            shouldThrow<IllegalArgumentsNumberException> { Calculator.calculate("log(2,)") }
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
            Calculator.calculate("log(2, 4)") shouldBe 2
            Calculator.calculate("pow(2, 4)") shouldBe 16
            Calculator.calculate("pow(4, 1/2)") shouldBe 16
            Calculator.calculate("1 - 4 * log(1 + 4 * pow(3 * 4, 3), 2) + 3") shouldBe 3.686399856649637
        }
    }
}
