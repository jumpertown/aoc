import java.io.File

import operations.Operator

fun main() {
    var puzzleInput = parsePuzzleInput()

    println(part1(puzzleInput))
}

fun part1(puzzleInput: List<Int>): List<Int> = operate(puzzleInput)

fun operate(inputCodes: List<Int>): List<Int> {
    var position = 0

    var overriddenCodes = mutableMapOf<Int, Int>()

    fun read(index: Int): Int {
        return if(overriddenCodes.containsKey(index)) {
            overriddenCodes[index]!!
        } else {
            inputCodes[index]
        }
    }

    fun write(index: Int, value: Int) {
        overriddenCodes[index] = value
    }

    fun run(): List<Int> {
        val tests = mutableListOf<Int>()
        while(true) {
            var opCode =  read(position)
            println(opCode)
            var operator = Operator.create(opCode, ::read, ::write)

            if(operator.isTerminated)
                break

            var numOperands = operator.numOperands

            val operands = ((position + 1)..(position + numOperands)).map { read(it) }
            val ret = operator.operate(operands)

            if(ret != null) {
                if (operator.isJump) {
                    position = ret
                    continue
                }
                tests.add(ret)
            }

            position += (numOperands + 1)
        }

        return tests.toList()
    }

    return run()
}

fun parsePuzzleInput(): List<Int> {
    val contents = File("/Users/justinpurrington/Downloads/day5.txt").readLines()
    return contents[0].split(',').map {it.toInt()}
}