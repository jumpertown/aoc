package main

import intcode.Computer
import java.io.File
import java.math.BigDecimal

fun main() {
    part2()
}

fun part1(): Int {
    var pointsPulled = 0
    for(y in 0 until 50) {
        for(x in 0 until 50) {
            val computer = Computer(parsePuzzleInput())
            val output = computer.operate(listOf(BigDecimal(x), BigDecimal(y)))
            if(output.isNotEmpty())
                pointsPulled += output[0].toInt()
        }
    }

    return pointsPulled
}

fun part2() {
    val puzzleMap = PuzzleMap(parsePuzzleInput(), 100, 100)
    //puzzleMap.fillObject(objectRange = 9..15, xRange = 0..100, yRange = 0..100)
    puzzleMap.fillObject(objectRange = 100..100, xRange = 639..1000, yRange = 829..1000)

}

data class Point(val x: Int, val y: Int)

class PuzzleMap(val input: List<BigDecimal>, val width: Int, val height: Int) {
    val grid: Map<Point, Boolean>

    init {
        grid = build()
    }

    fun fillObject(objectRange: IntRange, xRange: IntRange, yRange: IntRange) {
        for(objectWidth in objectRange) {
            for(y in yRange) {
                for(x in xRange) {
                    val topLeft = Point(x, y)
                    val bottomLeft = Point(x, y + objectWidth - 1)
                    val topRight = Point(x + objectWidth - 1, y)
                    val bottomRight = Point(x + objectWidth - 1, y + objectWidth - 1)

                    val covered = isPointPulled(topLeft) &&
                            isPointPulled(topRight) &&
                            isPointPulled(bottomLeft) &&
                            isPointPulled(bottomRight)

                    if(covered) {
                        println("$objectWidth: $topLeft")
                    }
                }
            }
        }
    }

    private fun isPointPulled(point: Point): Boolean {
        val computer = Computer(input)
        val output = computer.operate(listOf(
            BigDecimal(point.x),
            BigDecimal(point.y)
        ))
        return output[0]!! == BigDecimal.ONE

    }

    private fun build(): Map<Point, Boolean> {
        val buildingGrid = mutableMapOf<Point, Boolean>()
        for(y in 0 until height) {
            for(x in 0 until width) {
                var point = Point(x, y)
                buildingGrid[point] = isPointPulled(point)
            }
        }
        return buildingGrid.toMap()
    }

    fun drawMap(startingWidth: Int=0, startingHeight: Int=0) {
        print("  ")
        for(x in startingWidth until width) {
            print(x / 10 % 10)
        }
        println("")

        print("  ")
        for(x in startingWidth until width) {
            print(x % 10)
        }
        println("")

        for (y in startingHeight until height) {
            print(y / 10 % 10)
            print(y % 10)
            for (x in startingWidth until width)  {
                var coord = Point(x, y)
                var artifact = if(grid[coord]!!)
                    '#'
                else
                    '.'
                print(artifact)
            }
            println("")
        }
    }

}

fun parsePuzzleInput(): List<BigDecimal> {
    val contents = File("/Users/justinpurrington/Downloads/day19.txt").readLines()
    return contents[0].split(',').map {it.toBigDecimal()}
}