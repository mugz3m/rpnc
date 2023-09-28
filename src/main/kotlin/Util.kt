fun String.toSymbol(): Symbol {
    if (SupportedSymbols.values.none { it.char == this }) throw IllegalSymbolException("Invalid symbol '$this'")

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
        Functions.Pow.name, Functions.Pow.char -> Functions.Pow
        Functions.Log.name, Functions.Log.char -> Functions.Log
        else -> throw IllegalSymbolException("Invalid symbol '$this'")
    }
}
