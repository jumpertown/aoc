import day3.Movement
import day3.Wire
import java.io.File

fun main() {
    part2()
}

fun part1() {
    var puzzleInput = parsePuzzleInput()

    val nearest = (puzzleInput[0].points() intersect puzzleInput[1].points()).map {
        it.distance()
    }.filter {
        it > 0
    }.min()

    println(nearest)
}

fun part2() {
    var puzzleInput = parsePuzzleInput()

    val wire1Path = puzzleInput[0].pointSteps()
    val wire2Path = puzzleInput[1].pointSteps()

    val firstIntersection = (wire1Path.keys intersect wire2Path.keys).minBy {
        wire1Path[it]!! + wire2Path[it]!!
    }

    if (firstIntersection != null) {
        println(wire1Path[firstIntersection]!! + wire2Path[firstIntersection]!!)
    }
}

fun parsePuzzleInput(): List<Wire> {
    val contents = File("/Users/justinpurrington/Downloads/day3.txt").readLines()

    val wire1 = Wire(contents[0].split(',').map {Movement.fromString(it)})
    val wire2 = Wire(contents[1].split(',').map {Movement.fromString(it)})

    return listOf(wire1, wire2)
}