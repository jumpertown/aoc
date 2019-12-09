import java.io.File

data class Layer(val value: String, val width: Int, val height: Int) {
    fun counter(): Map<Char, Int> {
        val counter = mutableMapOf('0' to 0, '1' to 0, '2' to 0)

        for(char in this.value)
            counter[char] = counter.getOrDefault(char, 0) + 1

        return counter.toMap()
    }

    fun overLay(other: Layer): Layer {
        assert(this.value.length == other.value.length)

        val newChars = (this.value.indices).map {overlayChar(this.value[it], other.value[it])}

        return Layer(
            newChars.joinToString(separator = ""),
            width,
            height
        )
    }

    override fun toString(): String {
        return this.value
            .chunked(width)
            .joinToString(separator = "\n")
            .replace('0', ' ')
            .replace('1', '.')
    }

    companion object {
        fun overlayChar(below: Char, above: Char): Char =
            if(above in listOf('0', '1')) above else below
    }
}

fun main() {
    part2()
}

fun part1() {
    val height = 6
    val width = 25
    val layers = parsePuzzleInput().chunked(height * width).map {Layer(it, width, height)}

    val minZeros = layers.minBy { it.counter().getOrDefault('0', 0) }

    val leastZerosCount = minZeros!!.counter()
    println(leastZerosCount['1']!! * leastZerosCount['2']!!)
}

fun part2() {
    val height = 6
    val width = 25
    val layers = parsePuzzleInput().chunked(height * width).map {Layer(it, width, height)}

    var endLayer = layers.last()

    for(layer in layers.reversed())
        endLayer = endLayer.overLay(layer)

    println(endLayer)
}

fun parsePuzzleInput(): String {
    val contents = File("/Users/justinpurrington/Downloads/day8.txt").readLines()
    return contents[0]
}