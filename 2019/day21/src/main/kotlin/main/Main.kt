package main

import intcode.Computer
import java.io.File
import java.math.BigDecimal

fun main() {
    part2()
    //printPerms()
}

// NOT B - JUMP
fun printPerms() {
    for(i in 256 until 512) {
        println(i.toString(2).padStart(9, '0'))
    }

}

fun part1() {
    val computer = Computer(parsePuzzleInput())

    val alwaysJumpStrategy = listOf(
            "NOT D T",
            "NOT T J",
            "WALK"
    )
    val alwaysWalkStrategy = listOf(
            "NOT A J",
            "WALK"
    )
    // 1101 Jump
    val skipStrategy = listOf(
            "NOT A T",
            "NOT T J",
            "AND B J",
            "NOT C T",
            "AND T J",
            "AND D J",
            "NOT A T",
            "OR T J",
            "WALK"
    )
    // 1101 Jump
    // or 1001
    val skipStrategyTwo = listOf(
            "NOT A T",
            "NOT T J",
            "NOT C T",
            "AND T J",
            "AND D J",
            "NOT A T",
            "OR T J",
            "WALK"
    )

    val input = skipStrategyTwo.map { instructionToIntCode(it) }.flatten()
    val output = computer.operate(input)

    printResponse(output)
}

fun part2() {
    val safeJumpStrategy = listOf(
            "NOT F T",
            "NOT T J",  // F in J
            "OR I J",   //  (F or I) in J
            "AND E J",  // (E and (F or I) in J
            "OR H J",   //((E and (F or I)) or H) in J
            "AND D J",  // D and ((E and (F or I)) or H) in J
            "NOT A T",  // Might as well jump if not A
            "OR T J",
            "RUN"
    )

    val safeWalkStrategy = listOf(
            "NOT F J",
            "NOT J J",
            "OR  I J",
            "AND E J",

            "NOT F T",
            "NOT T T",
            "OR  C T",
            "AND B T",

            "OR T J",
            "AND A J",

            "NOT J J",

            "RUN"
    )

    /**
     * Strategy keeps options open by...
     * Walking instead of jumping
     * If walking would force us to end on E then jump (as can then walk one step to E)
     * If walking would land us on F and both D and E are available jump
     *
     * From trial and error favour jumping D to H over landing on F walking
     */
    // A and ((B and (C or (F and not (D and (E or H)))) or (E and not D))
    val optionsOpenStrategy = listOf(
            "NOT D J",
            "AND E J",

            "NOT E T",
            "NOT T T",
            "OR H T",
            "AND D T",
            "NOT T T",

            "AND F T",
            "OR C T",
            "AND B T",

            "OR T J",
            "AND A J",

            "NOT J J",  // Walking strategy

            "RUN"
    )

    val computer = Computer(parsePuzzleInput())
    val input = optionsOpenStrategy.map { instructionToIntCode(it) }.flatten()
    val output = computer.operate(input)

    printResponse(output)
}

fun printResponse(output: List<BigDecimal>) {
    output.forEach {
        val res = it.toInt()
        if(res < 256) {
            print(res.toChar())
        } else {
            print(res)
        }
    }
    println("")
}

fun instructionToIntCode(instruction: String): List<BigDecimal> =
        instruction.map {it.toInt().toBigDecimal()} + listOf(BigDecimal(10))

fun parsePuzzleInput(): List<BigDecimal> {
    val contents = File("/Users/justinpurrington/Downloads/day21.txt").readLines()
    return contents[0].split(',').map {it.toBigDecimal()}
}