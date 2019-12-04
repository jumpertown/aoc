import java.io.File

/**
 * Fuel required to launch a given module is based on its mass.
 * Specifically, to find the fuel required for a module, take its
 * mass, divide by three, round down, and subtract 2.
 */
fun main() {
    println(parsePuzzleInput())
}

fun fuelRequiredPart1(mass: Int): Int = mass / 3 - 2

tailrec fun fuelRequiredPart2(mass: Int): Int {
    val required = fuelRequiredPart1(mass)
    return if(required <= 0) 0 else required + fuelRequiredPart2(required)
}

fun parsePuzzleInput(): Int {
    var total = 0
    File("/Users/justinpurrington/Downloads/day1.txt").forEachLine {
        total += fuelRequiredPart2(it.toInt())
    }

    return total
}

