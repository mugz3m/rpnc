fun String.toSymbol(): Symbol {
    if (SupportedSymbols.values.none { it.char == this }) throw IllegalSymbolException("Illegal symbol $this")

    return when (this) {
        Delimiter.char -> Delimiter
        Negation.char -> Negation
        Parenthesis.Opening.char -> Parenthesis.Opening
        Parenthesis.Closing.char -> Parenthesis.Closing
        Operations.Plus.char -> Operations.Plus
        Operations.Minus.char -> Operations.Minus
        Operations.Multiplication.char -> Operations.Multiplication
        Operations.Division.char -> Operations.Division
        Operations.Pow.char -> Operations.Pow
        else -> throw IllegalSymbolException("Illegal symbol '$this'")
    }
}

fun StringBuilder.appendSeparately(any: Any): StringBuilder {
    append(any).append(Delimiter.char)
    return this
}
