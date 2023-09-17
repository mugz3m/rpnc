import ArgumentsClassifier.isOperation
import java.util.*
import kotlin.math.pow

object Calculator {
    fun calculate(string: String): Double {
        val tokens = ReversePolishNotation.convert(string)
        val numbers = Stack<String>()

        for (token in tokens) {
            when {
                isOperationOrNegation(token) -> handleOperationAndNegation(numbers, token.toSymbol())
                else -> numbers.push(token)
            }
        }

        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()

        return numbers.pop().toDouble()
    }

    private fun isOperationOrNegation(char: String) = isOperation(char) || char == Negation.char

    private fun handleOperationAndNegation(numbers: Stack<String>, operation: Symbol) {
        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()

        val b = numbers.pop().toDouble()
        val result = when (operation) {
            Negation -> b * -1
            else -> handleOperation(numbers, operation, b)
        }
        numbers.push(result.toString())
    }

    private fun handleOperation(numbers: Stack<String>, operation: Symbol, b: Double): Double {
        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()

        val a = numbers.pop().toDouble()
        return when (operation) {
            Operations.Plus -> a + b
            Operations.Minus -> a - b
            Operations.Multiplication -> a * b
            Operations.Division -> a / b
            Operations.Pow -> a.pow(b)
            else -> throw IllegalArgumentException("Unsupported operation $operation")
        }
    }
}
