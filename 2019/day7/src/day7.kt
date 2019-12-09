import intcode.Computer
import intcode.permute
import java.io.File
import java.math.BigDecimal

fun main() {
    //println(part1())
    println(part2())
    //partTest()
}

fun part1(): BigDecimal? {
    val perms = permute(listOf(0, 1, 2, 3, 4))
    //val opCodes = listOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0)
    val opCodes = parsePuzzleInput()

    val outputs = mutableListOf<BigDecimal>()
    for(perm in perms) {
        var output = listOf(BigDecimal(0))
        for (phase in perm) {
            output = Computer(opCodes, phase).operate(output)
            //output = operate(opCodes, listOf(BigDecimal(phase), output))
        }
        outputs.add(output[0])
    }

    return outputs.max()
}

fun part2(): BigDecimal? {
    val perms = permute(listOf(5, 6, 7, 8, 9))
    //val opCodes = listOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5)

    //34909438
    //35961106
    val opCodes = parsePuzzleInput()

    return perms.map { feedbackLoop(opCodes, it) }.max()
}

fun partTest(): BigDecimal? {
    val opCodes = listOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5)
    //val opCodes = listOf(3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10)
    return feedbackLoop(opCodes, listOf(5, 6, 7, 8, 9))
}

fun feedbackLoop(opCodes: List<Int>, phaseConfiguration: List<Int>): BigDecimal {
    val computers = phaseConfiguration.map {Computer(opCodes, it)}

    var runNum = 0
    var input = listOf(BigDecimal(0))

    val outputs = mutableSetOf<BigDecimal>()

    while(true) {
        var computerNum = runNum % 5
        var computer = computers[computerNum]

        //println("---------------------")
        //println("Running $computerNum($input)")
        input = computer.operate(input)

        // Assume only one item returned for now
        if(input.size > 1)
            throw IllegalStateException("Multiple items in input queue: $input")

        if(computerNum == 4 && computer.isTerminated) {
            println("$phaseConfiguration: ${input[0]}")
            return input[0]
        }

//        if(input[0] in outputs)
//            return outputs.max()!!

        outputs.add(input[0])

        if(outputs.size % 100000 == 0)
            println("Output max: ${outputs.max()}")

        runNum++

//        if(runNum == 50)
//            return BigDecimal(1)
    }
}

fun parsePuzzleInput(): List<Int> {
    val contents = File("/Users/justinpurrington/Downloads/day7.txt").readLines()
    return contents[0].split(',').map {it.toInt()}
}