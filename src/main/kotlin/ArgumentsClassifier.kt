object ArgumentsClassifier {
    fun isValid(char: String) = char.toIntOrNull() != null || SupportedSymbols.values.any { it.char == char }

    fun isOperation(char: String) = Operations.values.any { it.char == char }

    fun isParenthesis(char: String) = Parenthesis.values.any { it.char == char }

    fun isNegation(string: String, index: Int, char: String) = char == Operations.Minus.char &&
            (index == 0 || isOperation(string[index - 1].toString()) ||
                    string[index - 1].toString() == Parenthesis.Opening.char)
}