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
            shouldThrow<IllegalSymbolException> { ReversePolishNotation.convert(",") }
            shouldThrow<IllegalSymbolException> { ReversePolishNotation.convert("18,") }
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
            ReversePolishNotation.convert("1+(-(12 + 3))") shouldContainInOrder
                    listOf("1", "12", "3", "+", "n", "+")
        }
    }

    @Test
    fun operationsPriority() {
        ReversePolishNotation.convert("((10 + 2) - 4 / 2) * 2") shouldContainInOrder
                listOf("10", "2", "+", "4", "2", "/", "-", "2", "*")
    }

    @Test
    fun doubleNumbers() {
        ReversePolishNotation.convert("11.3/0.4+8") shouldContainInOrder listOf("11.3", "0.4", "/", "8", "+")
    }

    @Test
    fun functions() {
        ReversePolishNotation.convert("log(2, 4)") shouldContainInOrder listOf("2", "4", "l")
        ReversePolishNotation.convert("log(-1, -1)") shouldContainInOrder listOf("1", "n", "1", "n", "l")
        ReversePolishNotation.convert("pow(4, 1 / 2)") shouldContainInOrder listOf("4", "1", "2", "/", "p")
        ReversePolishNotation.convert("log(2,8)*3-2") shouldContainInOrder
                listOf("2", "8", "l", "3", "*", "2", "-")

        ReversePolishNotation.convert("log(2,3.9)*3-2") shouldContainInOrder
                listOf("2", "3.9", "l", "3", "*", "2", "-")

        ReversePolishNotation.convert("1 - log(1, 2) + 3") shouldContainInOrder
                listOf("1", "1", "2", "l", "-", "3", "+")

        ReversePolishNotation.convert("log(pow(2, 2), 2) + 3") shouldContainInOrder
                listOf("2", "2", "p", "2", "l", "3", "+")

        ReversePolishNotation.convert("log(1 - pow(2, 2), 2) + 3") shouldContainInOrder
                listOf("1", "2", "2", "p", "-", "2", "l", "3", "+")

        ReversePolishNotation.convert("4 * log(1 - pow(2, 2), 2) + 3") shouldContainInOrder
                listOf("4", "1", "2", "2", "p", "-", "2", "l", "*", "3", "+")

        ReversePolishNotation.convert("4 * log(1 - 9 * pow(2, 2), 2) + 3") shouldContainInOrder
                listOf("4", "1", "9", "2", "2", "p", "*", "-", "2", "l", "*", "3", "+")

        ReversePolishNotation.convert("1 - 4 * log(1 + 4 * pow(3 * 4, 3), 2) + 3") shouldContainInOrder
                listOf("1", "4", "1", "4", "3", "4", "*", "3", "p", "*", "+", "2", "l", "*", "-", "3", "+")
    }
}
