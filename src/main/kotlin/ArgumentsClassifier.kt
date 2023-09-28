object ArgumentsClassifier {
    fun isValid(char: String) = char.toIntOrNull() != null || SupportedSymbols.values.any { it.char == char }

    fun isOperation(char: String) = Operations.values.any { it.char == char }

    fun isParenthesis(char: String) = Parenthesis.values.any { it.char == char }

    fun isComma(char: String) = Comma.char == char

    fun isNegation(string: String, index: Int, char: String): Boolean {
        return if (index == 0) {
            char == Operations.Minus.char
        } else {
            val previous = string[index - 1].toString()
            char == Operations.Minus.char && (isOperation(previous) || isFunctionName(string, index - 1) ||
                    previous == Parenthesis.Opening.char || previous == Comma.char)
        }
    }

    fun isNegationChar(char: String) = char == Negation.char

    fun isFunctionName(string: String, index: Int): Boolean {
        for (function in Functions.values) {
            when {
                index + function.name.length > string.length -> continue
                function.name == string.substring(index, index + function.name.length) -> return true
            }
        }
        return false
    }

    fun isFunctionChar(char: String) = Functions.values.any { it.char == char }

    fun isNumber(string: String, index: Int): Boolean {
        val char = string[index].toString()
        return isValid(char) && !isOperation(char) && !isParenthesis(char) && !isComma(char) &&
                !isFunctionName(string, index)
    }
}
