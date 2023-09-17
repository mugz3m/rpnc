import ArgumentsClassifier.isNegation
import ArgumentsClassifier.isOperation
import ArgumentsClassifier.isParenthesis
import ArgumentsClassifier.isValid
import java.util.*

object ReversePolishNotation {
    fun convert(string: String): List<String> {
        if (string.isBlank()) throw BlankStringException("$string is blank")

        val stringWithoutSpaces = removeSpaces(string)
        val tokens = ArrayList<String>()
        val operations = Stack<String>()

        var i = 0
        while (i < stringWithoutSpaces.length) {
            val char = stringWithoutSpaces[i].toString()
            i += when {
                !isValid(char) -> throw IllegalSymbolException("Invalid char $char")
                isNegation(stringWithoutSpaces, i, char) -> handleNegation(operations)
                isParenthesis(char) -> handleParenthesis(operations, tokens, char)
                isOperation(char) -> handleOperation(operations, tokens, char)
                else -> handleNumber(tokens, operations, stringWithoutSpaces, i)
            }
        }

        tokens.addAll(operations)

        return tokens
    }

    private fun removeSpaces(string: String) = buildString { string.forEach { if (it != ' ') append(it) } }

    private fun getPriority(symbol: Symbol) = when (symbol) {
        Parenthesis.Opening, Parenthesis.Closing, Negation -> 0
        Operations.Plus, Operations.Minus -> 1
        Operations.Multiplication, Operations.Division -> 2
        Operations.Pow -> 3
        else -> throw IllegalSymbolException("$symbol is unsupported")
    }

    private fun handleNumber(tokens: ArrayList<String>, operations: Stack<String>, string: String, index: Int): Int {
        var i = index
        val number = StringBuilder()
        while (i < string.length && !isParenthesis(string[i].toString()) && !isOperation(string[i].toString())) {
            val char = string[i].toString()
            if (!isValid(char)) throw IllegalSymbolException("Invalid char $char")

            number.append(char)
            i++
        }
        tokens.add(number.toString())

        if (operations.isNotEmpty() && operations.peek() == Negation.char)
            tokens.add(operations.pop())

        return number.length
    }

    private fun handleParenthesis(operations: Stack<String>, tokens: ArrayList<String>, char: String): Int {
        when (char) {
            Parenthesis.Opening.char -> operations.push(char)
            Parenthesis.Closing.char -> {
                while (operations.peek() != Parenthesis.Opening.char) {
                    tokens.add(operations.pop().toString())
                }
                operations.pop()
            }
        }

        return 1
    }

    private fun handleOperation(operations: Stack<String>, tokens: ArrayList<String>, char: String): Int {
        when {
            operations.isEmpty() -> operations.push(char)
            else -> {
                val charPriority = getPriority(char.toSymbol())
                while (operations.isNotEmpty() && charPriority <= getPriority(operations.peek().toSymbol())) {
                    tokens.add(operations.pop())
                }
                operations.push(char)
            }
        }

        return 1
    }

    private fun handleNegation(operations: Stack<String>): Int {
        operations.push(Negation.char)
        return 1
    }
}
