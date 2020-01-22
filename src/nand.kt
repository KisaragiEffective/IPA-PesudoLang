sealed class Operation

class Store(val destination: Int, val value: Int) : Operation()

class NAND(val left: Int, val right: Int): Operation(), (Int, Int) -> Int {
    override fun invoke(p1: Int, p2: Int): Int {
        return p1.inv() and p2
    }
}

class Out(val v: Int): Operation() {
    fun getOutput() = String(byteArrayOf(
            (v and 0xFF000000.toInt()).shr(24).toByte(),
            (v and 0x00FF0000)        .shr(16).toByte(),
            (v and 0x0000FF00)        .shr(8) .toByte(),
            (v and 0x000000FF)                         .toByte()
    ), Charsets.UTF_32)
}

