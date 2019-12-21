package day17

import coordinates.Direction
import coordinates.Point
import intcode.Computer
import java.io.File
import java.lang.IllegalStateException
import java.math.BigDecimal

fun main() {
    part2()
}

fun testInteract() {

    println("Enter Two number")
    var (a, b) = readLine()!!.split(' ') // !! this operator use for NPE(NullPointerException).

    println("$a and $b")
}

fun part1() {
    val intCodes = parsePuzzleInput()
    val puzzleMap = PuzzleMap(intCodes)
    puzzleMap.drawMap()

    val intersections = puzzleMap.getIntersections()

    // 17189 too high
    println(intersections.map {it.x * it.y}.sum())
    println(intersections)
}

fun part2() {
    val intCodes = parsePuzzleInput()
    val puzzleMap = PuzzleMap(intCodes.mapIndexed {index, it ->  if (index == 0) BigDecimal("2") else it})

    //puzzleMap.interact()
    puzzleMap.inputInstructions()
}

enum class Artifact(val value: Char, val direction: Direction, val isTurtle: Boolean){
    SCAFFOLD('#', Direction.NONE, isTurtle=false),
    SPACE('.', Direction.NONE, isTurtle=false),
    UPTURTLE('^', Direction.UP, isTurtle=true),
    DOWNTURTLE('v', Direction.DOWN, isTurtle=true),
    LEFTTURTLE('<', Direction.LEFT, isTurtle=true),
    RIGHTTURTLE('>', Direction.RIGHT, isTurtle=true),
    DEADTURTLE('X', Direction.NONE, isTurtle=true),
    MONITOR( 'M', Direction.NONE, isTurtle=false),
    FOO( 'a', Direction.NONE, isTurtle=false),
    UNKNOWN('?', Direction.NONE, isTurtle=false);

    fun turnLeft(): Artifact =
        when(this) {
            UPTURTLE -> LEFTTURTLE
            DOWNTURTLE -> RIGHTTURTLE
            LEFTTURTLE -> DOWNTURTLE
            RIGHTTURTLE -> UPTURTLE
            else -> this
        }

    fun turnRight(): Artifact =
        when(this) {
            UPTURTLE -> RIGHTTURTLE
            DOWNTURTLE -> LEFTTURTLE
            LEFTTURTLE -> UPTURTLE
            RIGHTTURTLE -> DOWNTURTLE
            else -> this
        }

    companion object {
        fun fromAscii(value: Int): Artifact =
            when(value) {
                35 -> SCAFFOLD
                46 -> SPACE
                60 -> LEFTTURTLE
                62 -> RIGHTTURTLE
                94 -> UPTURTLE
                118 -> DOWNTURTLE
                88 -> DEADTURTLE
                else -> throw IllegalStateException("Unknown Artifact: $value")
            }
    }
}

enum class TurtleMove {
    LEFT, RIGHT, FORWARD;
}

data class Instruction(val move: TurtleMove, val magnitude: Int=0) {
    override fun toString(): String =
        when(move) {
            TurtleMove.LEFT -> "L"
            TurtleMove.RIGHT -> "R"
            TurtleMove.FORWARD -> "$magnitude"
        }
}

class PuzzleMap(val opCodes: List<BigDecimal>) {
    private val computer = Computer(opCodes)
    private val intcodeMap: MutableMap<Point, Artifact>
    private val topLeft = Point(0, 0)
    private val bottomRight: Point

    var turtlePosition: Point
    val instructions = mutableListOf<Instruction>()
    val mainInstruction = "A,B,A,B,A,C,B,C,A,C"
    val aInstruction = "L,6,R,12,L,6"
    val bInstruction = "R,12,L,10,L,4,L,6"
    val cInstruction = "L,10,L,10,L,4,L,6"

    init {
        intcodeMap = build()
        bottomRight = getBottomRight()
        turtlePosition = intcodeMap.filterValues { it.isTurtle }.keys.single()
    }

    fun turnLeft() {
        instructions.add(Instruction(TurtleMove.LEFT))
        intcodeMap[turtlePosition] = intcodeMap[turtlePosition]!!.turnLeft()
    }

    fun turnRight() {
        instructions.add(Instruction(TurtleMove.RIGHT))
        intcodeMap[turtlePosition] = intcodeMap[turtlePosition]!!.turnRight()
    }

    fun goForward() {
        val turtle = intcodeMap[turtlePosition]!!
        val direction = turtle.direction
        var newPosition = turtlePosition
        var length = 0

        while(true) {
            var testPoint = newPosition.move(direction)
            if (intcodeMap.getOrDefault(testPoint, Artifact.SPACE) == Artifact.SPACE)
                break
            length++
            newPosition = testPoint
        }

        if(newPosition != turtlePosition) {
            instructions.add(Instruction(TurtleMove.FORWARD, length))
            intcodeMap[turtlePosition] = Artifact.SCAFFOLD
            intcodeMap[newPosition] = turtle
            turtlePosition = newPosition
        }
    }

    /** From interactive get this path:
     *  A = L, 6, R, 12, L, 6
     *  B = R, 12, L, 10, L, 4, L, 6
     *  A = L, 6, R, 12, L, 6
     *  B = R, 12, L, 10, L, 4, L, 6
     *  A = L, 6, R, 12, L, 6
     *  C = L, 10, L, 10, L, 4, L, 6
     *  B = R, 12, L, 10, L, 4, L, 6
     *  C = L, 10, L, 10, L, 4, L, 6
     *  A = L, 6, R, 12, L, 6
     *  C = L, 10, L, 10, L, 4, L, 6
     */
    fun interact() {
        while(true) {
            drawMap()
            var instruction = readLine()

            if (instruction == "a") {
                turnLeft()
            } else if(instruction == "d") {
                turnRight()
            } else if(instruction == "w") {
                goForward()
            } else {
                print(instructions)
                break
            }
        }
    }

    fun inputInstructions() {
        val instructions = listOf(
            mainInstruction,
            aInstruction,
            bInstruction,
            cInstruction,
            "n"
        )

        instructions.forEach {instuction ->
            val input = instuction.map {it.toInt().toBigDecimal()} + listOf(BigDecimal(10))
            val output = computer.operate(input)
            println(output)
            println(output.map{it.toInt().toChar()})
        }
    }


    private fun build(): MutableMap<Point, Artifact> {
        val output = computer.operate(listOf())
        var point = Point(0, 0)
        val initialMap = mutableMapOf<Point, Artifact>()
        var previousCode = 0
        var mapComplete = false

        for (i in output) {
            if(!mapComplete) {
                val intOutput = i.toInt()
                if (intOutput == 10) {
                    if(previousCode == 10) {
                        mapComplete = true
                    }
                    point = point.carriageReturn()

                } else {
                    val artifact = Artifact.fromAscii(intOutput)
                    initialMap[point] = artifact
                    point = point.move(Direction.RIGHT)
                }
                previousCode = intOutput
            } else {
                print(i.toInt().toChar())
            }
        }

        return initialMap
    }

    private fun getBottomRight(): Point {
        val maxX  = intcodeMap.keys.map {it.x}.max()
        val maxY  = intcodeMap.keys.map {it.y}.max()

        return Point(maxX!!, maxY!!)
    }

    fun drawMap() {
        for(x in 0..bottomRight.x) {
            print(x % 10)
        }
        println("")

        for (y in topLeft.y..bottomRight.y) {
            print(y % 10)
            for (x in topLeft.x..bottomRight.x) {
                var artifact = intcodeMap.getOrDefault(Point(x, y), Artifact.UNKNOWN)
                print(artifact.value)
            }
            println("")
        }
    }

    fun isIntersection(point: Point): Boolean {

        if(intcodeMap.getOrDefault(point, Artifact.SPACE) == Artifact.SPACE)
            return false

        val neighbours = Direction.all.map { direction ->
            point.move(direction)
        }.filter { neighbour ->
            intcodeMap.getOrDefault(neighbour, Artifact.SPACE) != Artifact.SPACE
        }

        return neighbours.size > 2
    }

    fun getIntersections(): List<Point> =
        intcodeMap.keys.filter { isIntersection(it) }
}


fun parsePuzzleInput(): List<BigDecimal> {
    val contents = File("/Users/justinpurrington/Downloads/day17.txt").readLines()
    return contents[0].split(',').map {it.toBigDecimal()}
}