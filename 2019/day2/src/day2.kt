import java.io.File

fun main() {
    var puzzleInput = parsePuzzleInput()

    println(part2(puzzleInput))
}

fun part1(puzzleInput: List<Int>): Int {
    return operate(puzzleInput, 2, 12)
}

fun part2(puzzleInput: List<Int>): Int {
    for (noun in 0..99) {
        for (verb in 0..99) {
            if (operate(puzzleInput, noun, verb) == 19690720) {
                println("Noun $noun, Verb $verb")
                return 100 * noun + verb
            }
        }
    }
    return -1
}

fun operate(inputCodes: List<Int>, noun: Int, verb: Int): Int {
    var position = 0

    val opCodeTerminator = 99
    val opCodeAdder = 1
    val opCodeMultiplier = 2

    var overriddenCodes = mutableMapOf(1 to noun, 2 to verb)


    fun getCodeValue(pos: Int): Int {
        return if(overriddenCodes.containsKey(pos)) {
            overriddenCodes[pos]!!
        } else {
            inputCodes[pos]
        }
    }

    fun printState() {
        print("POSITION: $position STATE:")
        for (i in inputCodes.indices)
            print("${getCodeValue(i)} ")
        println()
    }

    fun run(): Int {
        while(true) {
            var opCode =  getCodeValue(position)

            //printState()

            if(opCode == opCodeTerminator) {
                break
            }

            var lhOperand = getCodeValue(position + 1)
            var rhOperand = getCodeValue(position + 2)
            var target = getCodeValue(position + 3)

            if (opCode == opCodeAdder) {
                overriddenCodes[target] = getCodeValue(lhOperand) + getCodeValue(rhOperand)
            } else if(opCode == opCodeMultiplier) {
                overriddenCodes[target] = getCodeValue(lhOperand) * getCodeValue(rhOperand)
            } else {
                throw IllegalStateException("Unknown opcode $opCode")
            }
            position += 4
        }

        return getCodeValue(0)
    }

    return run()
}

fun parsePuzzleInput(): List<Int> {
    val contents = File("/Users/justinpurrington/Downloads/day2.txt").readLines()
    return contents[0].split(',').map {it.toInt()}
}