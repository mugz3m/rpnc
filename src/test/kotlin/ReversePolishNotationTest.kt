import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ReversePolishNotationTest {
    @Test
    fun emptyString() {
        shouldThrow<BlankStringException> { Calculator.calculate("") }
    }

    @Test
    fun blankString() {
        shouldThrow<BlankStringException> { Calculator.calculate("    ") }
    }

    @Test
    fun redundantSpaces() {
        val testCases = arrayOf(
            "   1+3",
            " 1  +3",
            " 1+ 3 ",
            "1+3   "
        )
        testCases.shouldForAll { Calculator.calculate(it) shouldBe 4 }
    }


    @Test
    fun illegalSymbol() {
        val testCases = arrayOf(
            "a+b",
            "1#4+1",
            "1<2",
            "3&0"
        )
        testCases.shouldForAll { shouldThrow<IllegalSymbolException> { Calculator.calculate(it) } }
    }

    @Test
    fun unaryMinus() {
        val testCases = arrayOf(
            "-1+3",
            "3+-1",
            "3+(-1)"
        )
        testCases.shouldForAll { Calculator.calculate(it) shouldBe 2 }
    }
}
