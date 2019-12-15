package day15

import directions.Direction
import intcode.Computer
import java.io.File
import java.math.BigDecimal

data class DirectionResult(val path: List<Direction>, val isValid:Boolean, val isSolution: Boolean)

object Controller {
    val opCodes = parsePuzzleInput()

    fun tryPath(directions: List<Direction>): DirectionResult {
        val computer = Computer(opCodes)
        val output = computer.operate(directions.map {BigDecimal(it.value)})

        return when(val lastOutput = output.last()) {
            BigDecimal.ZERO -> DirectionResult(directions, isValid = false, isSolution = false)
            BigDecimal.ONE -> DirectionResult(directions, isValid = true, isSolution = false)
            BigDecimal("2") -> DirectionResult(directions, isValid = true, isSolution = true)
            else -> throw IllegalStateException("Unknown return code $lastOutput")
        }
    }
}

fun main() {
    part2()
}

fun part1(): List<DirectionResult> {
    var previousResults = listOf(DirectionResult(listOf(), isValid = true, isSolution = false))
    val pathToOrigin = listOf<Direction>()

    var steps = 0
    var solutions = listOf<DirectionResult>()
    while(true) {
        steps++
        var results = nextLevel(previousResults, pathToOrigin)
        solutions = results.filter{it.isSolution}
        if(!solutions.isEmpty())
            break
        previousResults = results
    }
    println(steps)
    return solutions
}

fun part2() {
    // start from the Oxygen tank
    var previousResults = listOf(DirectionResult(listOf(), isValid = true, isSolution = false))
    var pathToOrigin = part1()!![0].path

    var steps = 0
    while(true) {
        var results = nextLevel(previousResults, pathToOrigin)
        if(results.isEmpty())
            break
        previousResults = results
        steps++
    }

    println(steps)
}



fun nextSteps(path: List<Direction>, pathToOrigin: List<Direction>): List<DirectionResult> {
    val previousStep = if(path.isEmpty()) null else path.last()
    val directionsToTry = Direction.ALL.filter {it != previousStep?.opposite ?: null }
    val pathsToTry = directionsToTry.map { pathToOrigin.plus(path).plus(it) }

    return pathsToTry.map {Controller.tryPath(it)}.filter{it.isValid}
}

fun nextLevel(previousResults: List<DirectionResult>, pathToOrigin: List<Direction>): List<DirectionResult> =
    previousResults.map {nextSteps(it.path, pathToOrigin)}.flatMap { it }


fun parsePuzzleInput(): List<BigDecimal> {
    val contents = File("/Users/justinpurrington/Downloads/day15.txt").readLines()
    return contents[0].split(',').map {it.toBigDecimal()}
}
