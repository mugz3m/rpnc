import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainInOrder
import org.junit.jupiter.api.Test

class ReversePolishNotationTest {
    @Test
    fun emptyString() {
        shouldThrow<BlankStringException> { ReversePolishNotation.convert("") }
    }

    @Test
    fun blankString() {
        shouldThrow<BlankStringException> { ReversePolishNotation.convert("    ") }
    }

    @Test
    fun illegalSymbols() {
        assertSoftly {
            shouldThrow<IllegalSymbolException> { ReversePolishNotation.convert("a+b") }
            shouldThrow<IllegalSymbolException> { ReversePolishNotation.convert("1#4+1") }
            shouldThrow<IllegalSymbolException> { ReversePolishNotation.convert("1<2") }
            shouldThrow<IllegalSymbolException> { ReversePolishNotation.convert("3&0") }
            shouldThrow<IllegalSymbolException> { ReversePolishNotation.convert("-aa") }
        }
    }

    @Test
    fun redundantSpaces() {
        assertSoftly {
            ReversePolishNotation.convert("   1+3") shouldContainInOrder listOf("1", "3", "+")
            ReversePolishNotation.convert(" 1  +3") shouldContainInOrder listOf("1", "3", "+")
            ReversePolishNotation.convert(" 1+ 3 ") shouldContainInOrder listOf("1", "3", "+")
            ReversePolishNotation.convert("1+3   ") shouldContainInOrder listOf("1", "3", "+")
        }
    }

    @Test
    fun unaryMinus() {
        assertSoftly {
            ReversePolishNotation.convert("-1+3") shouldContainInOrder listOf("1", "n", "3", "+")
            ReversePolishNotation.convert("(-1)+3") shouldContainInOrder listOf("1", "n", "3", "+")
            ReversePolishNotation.convert("1+-3") shouldContainInOrder listOf("1", "3", "n", "+")
            ReversePolishNotation.convert("1+(-3)") shouldContainInOrder listOf("1", "3", "n", "+")
            ReversePolishNotation.convert("-(12 + 3)") shouldContainInOrder listOf("12", "3", "+", "n")
            ReversePolishNotation.convert("1+(-(12 + 3))") shouldContainInOrder listOf("1", "12", "3", "+", "n", "+")
        }
    }

    @Test
    fun operationsPriority() {
        val expected = listOf("10", "2", "+", "4", "2", "/", "-", "2", "*")
        ReversePolishNotation.convert("((10 + 2) - 4 / 2) * 2") shouldContainInOrder expected
    }

    @Test
    fun doubleNumbers() {
        ReversePolishNotation.convert("11.3/0.4+8") shouldContainInOrder listOf("11.3", "0.4", "/", "8", "+")
    }
}
