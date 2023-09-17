class BlankStringException(override val message: String?) : RuntimeException()

class IllegalSymbolException(override val message: String?) : RuntimeException()

class IllegalArgumentsNumberException : RuntimeException() {
    override val message = "Invalid number of arguments"
}
