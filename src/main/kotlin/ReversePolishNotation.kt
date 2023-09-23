import ArgumentsClassifier.isComma
import ArgumentsClassifier.isFunctionChar
import ArgumentsClassifier.isFunctionName
import ArgumentsClassifier.isNegation
import ArgumentsClassifier.isNumber
import ArgumentsClassifier.isOperation
import ArgumentsClassifier.isParenthesis
import ArgumentsClassifier.isValid
import java.util.*

object ReversePolishNotation {
    fun convert(string: String): List<String> {
        if (string.isBlank()) throw BlankStringException("$string is blank")

        val stringWithoutSpaces = removeSpaces(string)
        val tokens = ArrayList<String>()
        val operations = Stack<Symbol>()

        var i = 0
        while (i < stringWithoutSpaces.length) {
            val char = stringWithoutSpaces[i].toString()
            i += when {
                !isValid(char) -> throw IllegalSymbolException("Invalid char '$char'")
                isComma(char) -> handleComma(operations, tokens, char)
                isNegation(stringWithoutSpaces, i, char) -> handleNegation(operations)
                isParenthesis(char) -> handleParenthesis(operations, tokens, char)
                isOperation(char) -> handleOperation(operations, tokens, char)
                isFunctionName(stringWithoutSpaces, i) -> handleFunction(operations, stringWithoutSpaces, i)
                else -> handleNumber(tokens, operations, stringWithoutSpaces, i)
            }
        }

        tokens.addAll(operations.map { it.char })

        return tokens
    }

    private fun removeSpaces(string: String) = buildString { string.forEach { if (it != ' ') append(it) } }

    private fun getPriority(symbol: Symbol) = when (symbol) {
        Parenthesis.Opening, Parenthesis.Closing, Negation -> 0
        Operations.Plus, Operations.Minus -> 1
        Operations.Multiplication, Operations.Division -> 2
        Operations.Pow -> 3
        Functions.Log, Functions.Pow -> 4
        else -> throw IllegalSymbolException("Invalid symbol '$symbol'")
    }

    private fun handleComma(operations: Stack<Symbol>, tokens: ArrayList<String>, char: String): Int {
        if (operations.isEmpty()) throw IllegalSymbolException("Invalid char '$char'")

        while (operations.peek() !is Parenthesis.Opening) {
            tokens.add(operations.pop().char)
        }
        operations.pop()

        return 1
    }

    private fun handleNegation(operations: Stack<Symbol>): Int {
        operations.push(Negation)

        return 1
    }

    private fun handleParenthesis(operations: Stack<Symbol>, tokens: ArrayList<String>, char: String): Int {
        when (char) {
            Parenthesis.Opening.char -> operations.push(Parenthesis.Opening)
            Parenthesis.Closing.char -> {
                if (operations.isEmpty()) throw IllegalSymbolException("Invalid char '$char'")

                while (operations.peek() !is Parenthesis.Opening && !isFunctionChar(operations.peek().char)) {
                    tokens.add(operations.pop().char)
                }

                if (operations.peek() is Parenthesis.Opening) {
                    operations.pop()
                } else {
                    tokens.add(operations.pop().char)
                }
            }
        }

        return 1
    }

    private fun handleOperation(operations: Stack<Symbol>, tokens: ArrayList<String>, char: String): Int {
        when {
            operations.isEmpty() -> operations.push(char.toSymbol())
            else -> {
                val charPriority = getPriority(char.toSymbol())
                while (operations.isNotEmpty() && charPriority <= getPriority(operations.peek()) &&
                    !isFunctionChar(operations.peek().char)
                ) {
                    tokens.add(operations.pop().char)
                }
                operations.push(char.toSymbol())
            }
        }

        return 1
    }

    private fun handleFunction(operations: Stack<Symbol>, string: String, index: Int): Int {
        for (function in Functions.values) {
            val name = function.name
            when {
                index + name.length > string.length -> continue
                name == string.substring(index, index + name.length) -> {
                    when (name) {
                        Functions.Pow.name -> operations.add(Functions.Pow)
                        Functions.Log.name -> operations.add(Functions.Log)
                    }

                    return name.length
                }
            }
        }

        throw UnsupportedFunctionException("Unsupported function in '$string'")
    }

    private fun handleNumber(tokens: ArrayList<String>, operations: Stack<Symbol>, string: String, index: Int): Int {
        var i = index
        val number = StringBuilder()
        while (i < string.length && isNumber(string, i)) {
            val char = string[i].toString()
            if (!isValid(char)) throw IllegalSymbolException("Invalid char '$char'")

            number.append(char)
            i++
        }
        tokens.add(number.toString())

        if (operations.isNotEmpty() && operations.peek() == Negation) tokens.add(operations.pop().char)

        return number.length
    }
}
