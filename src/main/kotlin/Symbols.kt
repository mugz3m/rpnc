sealed interface SymbolsSet {
    val values: Set<Symbol>
}

sealed interface Symbol {
    val char: String
}

data object SupportedSymbols : SymbolsSet {
    override val values =
        setOf(Delimiter, Negation, Dot, Comma) + Parenthesis.values + Operations.values + Functions.values
}

data object Delimiter : Symbol {
    override val char = " "
}

data object Negation : Symbol {
    override val char = "n"
}

data object Dot : Symbol {
    override val char = "."
}

data object Comma : Symbol {
    override val char = ","
}

data object Parenthesis : SymbolsSet {
    override val values = setOf(Opening, Closing)

    data object Opening : Symbol {
        override val char = "("
    }

    data object Closing : Symbol {
        override val char = ")"
    }
}

sealed interface Operation : Symbol

data object Operations : SymbolsSet {
    override val values = setOf(Plus, Minus, Multiplication, Division, Pow)

    data object Plus : Operation {
        override val char = "+"
    }

    data object Minus : Operation {
        override val char = "-"
    }

    data object Multiplication : Operation {
        override val char = "*"
    }

    data object Division : Operation {
        override val char = "/"
    }

    data object Pow : Operation {
        override val char = "^"
    }
}

sealed interface Function : Symbol {
    val name: String
}

data object Functions : SymbolsSet {
    override val values = setOf(Pow, Log)

    data object Pow : Function {
        override val char = "p"
        override val name = "pow"
    }

    data object Log : Function {
        override val char = "l"
        override val name = "log"
    }
}
