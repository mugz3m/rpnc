import ArgumentsClassifier.isFunctionChar
import ArgumentsClassifier.isNegationChar
import ArgumentsClassifier.isOperation
import java.util.Stack
import kotlin.math.log
import kotlin.math.pow

object Calculator {
    fun calculate(string: String): Double {
        val tokens = ReversePolishNotation.convert(string)
        val numbers = Stack<String>()

        for (token in tokens) {
            when {
                isNegationChar(token) -> handleNegation(numbers)
                isOperation(token) -> handleOperation(numbers, token.toSymbol())
                isFunctionChar(token) -> handleFunction(numbers, token.toSymbol())
                else -> numbers.push(token)
            }
        }

        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()

        return numbers.pop().toDouble()
    }

    private fun handleNegation(numbers: Stack<String>) {
        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()
        val b = numbers.pop().toDouble()
        val result = b * -1
        numbers.push(result.toString())
    }

    private fun handleOperation(numbers: Stack<String>, operation: Symbol) {
        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()

        val b = numbers.pop().toDouble()
        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()

        val a = numbers.pop().toDouble()
        val result = when (operation) {
            Operations.Plus -> a + b
            Operations.Minus -> a - b
            Operations.Multiplication -> a * b
            Operations.Division -> a / b
            Operations.Pow -> a.pow(b)
            else -> throw IllegalSymbolException("Unsupported operation $operation")
        }
        numbers.push(result.toString())
    }

    private fun handleFunction(numbers: Stack<String>, function: Symbol) {
        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()

        val b = numbers.pop().toDouble()
        if (numbers.isEmpty()) throw IllegalArgumentsNumberException()

        val a = numbers.pop().toDouble()
        val result = when (function) {
            Functions.Pow -> a.pow(b)
            Functions.Log -> log(b, a)
            else -> throw UnsupportedFunctionException("Unsupported function $function")
        }
        numbers.push(result.toString())
    }
}
