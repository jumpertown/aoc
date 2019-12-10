import intcode.Computer
import intcode.RelativeBase
import java.io.File
import java.math.BigDecimal

fun main() {
    println(part2())
}

fun part1 (): List<BigDecimal> {
    val comp = Computer(parsePuzzleInput(), 1)
    val ret = comp.operate(listOf<BigDecimal>())
    println("Terminated: ${comp.isTerminated}")
    return ret
}

fun part2 (): List<BigDecimal> {
    val comp = Computer(parsePuzzleInput(), 2)
    val ret = comp.operate(listOf<BigDecimal>())
    println("Terminated: ${comp.isTerminated}")
    return ret
}

fun test1 (): List<BigDecimal> {
    val test1 = listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)

    val comp = Computer(test1.map{ BigDecimal(it) }, 0)
    return comp.operate(listOf<BigDecimal>())
}

fun test2 (): List<BigDecimal> {
    val test2 = listOf(1102,34915192,34915192,7,4,7,99,0)
    val comp = Computer(test2.map{ BigDecimal(it) }, 0)
    return comp.operate(listOf<BigDecimal>())
}

fun test3 (): List<BigDecimal> {
    val test3 = listOf(BigDecimal(104),BigDecimal(1125899906842624),BigDecimal(99))
    val comp = Computer(test3, 0)
    return comp.operate(listOf<BigDecimal>())
}

fun parsePuzzleInput(): List<BigDecimal> {
    val contents = File("/Users/justinpurrington/Downloads/day9.txt").readLines()
    return contents[0].split(',').map {it.toBigDecimal()}
}