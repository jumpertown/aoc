import directions.Direction
import directions.Point
import intcode.Computer
import java.io.File
import java.lang.IllegalStateException
import java.math.BigDecimal

fun main() {
    println(part1())
}

fun displayMap(position: Point, direction: Direction, visitedPoints: MutableMap<Point, Int>) {
    val points = visitedPoints.keys.toMutableSet()
    points.add(position)

    val minX = points.minBy{it.x}
    val maxX = points.maxBy{it.x}
    val minY = points.minBy{it.y}
    val maxY = points.maxBy{it.y}

    for(y in maxY!!.y downTo minY!!.y) {
        for (x in minX!!.x..maxX!!.x) {
            var point = Point(x, y)

            var char = if(point == position) {
                direction.rep
            } else if(point in points && visitedPoints[point] == 1) {
                '#'
            } else {
                '.'
            }
            print(char)
        }
        println("")
    }
}

fun part1(): Int {
    var position = Point(0, 0)
    var direction = Direction.UP
    val computer = Computer(parsePuzzleInput())

    /* part 2 */
    val visitedPoints = mutableMapOf(position to 1)
    var numPainted = 0
    var numSteps = 0

    while(!computer.isTerminated) {
        numSteps++
        if(numSteps % 1000000 == 0) {
            println("Steps: $numSteps, Painted: $numPainted")
        }

        //println("STEP $numSteps.")
        //displayMap(position, direction, visitedPoints)
        var colour = visitedPoints.getOrDefault(position, 0)
        //println("position: $position, input: $colour")
        var outputs = computer.operate(listOf(BigDecimal(colour)))
        //println("outputs: $outputs")

        if(outputs.size != 2) {
            if(computer.isTerminated)
                break

            throw IllegalStateException("Unexpected output: $outputs")
        }

        if(outputs.filter { it < BigDecimal(0) || it > BigDecimal(1) }.isNotEmpty())
            throw IllegalStateException("Unexpected output: $outputs")

        // Paint
        if(!visitedPoints.containsKey(position))
            numPainted++
        visitedPoints[position] = outputs[0].toInt()

        // Move
        direction = if(outputs[1] == BigDecimal(0)) {
            //println("LEFT")
            direction.turnLeft()
        } else {
            //println("RIGHT")
            direction.turnRight()
        }
        //println("")

        position = direction.go(position)

        //if(numSteps == 200) break
    }
    displayMap(position, direction, visitedPoints)

    return numPainted
}

fun parsePuzzleInput(): List<BigDecimal> {
    val contents = File("/Users/justinpurrington/Downloads/day11.txt").readLines()
    return contents[0].split(',').map {it.toBigDecimal()}
}