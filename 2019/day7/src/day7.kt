import intcode.operate
import intcode.permute
import java.io.File

fun main() {
    println(part2())
}

fun part1() {
    val perms = permute(listOf(0, 1, 2, 3, 4))
    //val opCodes = listOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0)
    val opCodes = parsePuzzleInput()

    val outputs = mutableListOf<Int>()
    for(perm in perms) {
        var output = 0
        for (phase in perm)
            output = operate(opCodes, listOf(phase, output))
        outputs.add(output)
    }

    println(outputs.max())
}

fun part2(): Int? {
    val perms = permute(listOf(5, 6, 7, 8, 9))
    val opCodes = listOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5)

    //val opCodes = parsePuzzleInput()

    return perms.map { feebackLoop(opCodes, it) }.max()
}

fun feebackLoop(opCodes: List<Int>, phaseConfiguration: List<Int>): Int {
    var output = 0

    val outputs = mutableSetOf<Int>()

    while(true) {
        for (phase in phaseConfiguration) {
            //println(phaseConfiguration)
            //println(phase)
            output = operate(opCodes, listOf(phase, output))
        }
        if (output in outputs) {
            println("Computed $phaseConfiguration: $output")
            break
        }

        outputs.add(output)
    }

    return outputs.max()!!
}

fun parsePuzzleInput(): List<Int> {
    val contents = File("/Users/justinpurrington/Downloads/day7.txt").readLines()
    return contents[0].split(',').map {it.toInt()}
}