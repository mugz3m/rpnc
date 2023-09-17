fun main() {
    while (true) {
        val input = readln()
        try {
            println("${Calculator.calculate(input)}\n")
        } catch (e: RuntimeException) {
            println("${e.message}\n")
        }
    }
}
