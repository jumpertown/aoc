package day16

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

fun main() {
    println(part2())
}

fun part2(): List<Int> {
    val puzzleInput = "59715091976660977847686180472178988274868874248912891927881770506416128667679122958792624406231072013221126623881489317912309763385182133601840446469164152094801911846572235367585363091944153574934709408511688568362508877043643569519630950836699246046286262479407806494008328068607275931633094949344281398150800187971317684501113191184838118850287189830872128812188237680673513745269645219228183633986701871488467284716433953663498444829748364402022393727938781357664034739772457855166471802886565257858813291667525635001823584650420815316132943869499800374997777130755842319153463895364409226260937941771665247483191282218355610246363741092810592458"
    val intPuzzleInput = puzzleInput.map {it.toString().toInt()}
    val megaPuzzleInput = repeatSignal(intPuzzleInput, 1000)
    val output = processRepeatedly(megaPuzzleInput, 100)

    val offset = output.subList(0, 7)

    return output.subList(0, 8)
}


fun processRepeatedly(signal: List<Int>, repeat: Int): List<Int> {
    var processingSignal = signal

    for(i in 1..repeat) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        var currentDate = sdf.format(Date())
        println("$currentDate: iteration $i...")
        processingSignal = processAttemptVier(processingSignal)
    }

    return processingSignal
}


fun process(signal: List<Int>): List<Int> =
    (1..signal.size).map{indexedDigitAttemptTrois(signal, it)}

fun processAttemptVier(signal: List<Int>): List<Int> {
    val output = mutableListOf<Int>()
    for (i in 1..signal.size)
        output.add(indexedDigitAttemptTrois(signal, i))

    return output.toList()
}


fun indexedDigit(signal: List<Int>, outputDigit: Int): Int {
    val ret = signal
        .mapIndexed { index, i ->  i * getMultiplier(index + 1, outputDigit) }
        .sum()

    return units(ret)
}

fun indexedDigitImproved(signal: List<Int>, outputDigit: Int): Int {
    val len = signal.size
    val positiveContrib = positiveRangeStarts(len, outputDigit)
        .map {signal.subList(it, if(it + outputDigit > len) len else it + outputDigit).sum()}
        .sum()

    val negativeContrib = negativeRangeStarts(len, outputDigit)
        .map {signal.subList(it, if(it + outputDigit > len) len else it + outputDigit).sum()}
        .sum()

    return units(positiveContrib - negativeContrib)
}

fun indexedDigitAttemptTrois(signal: List<Int>, outputDigit: Int): Int =
    signal.asSequence()
        .drop(outputDigit - 1)
        .chunked(outputDigit)
        .chunked(4)
        .map{
            val positive = if(it.isNotEmpty()) it[0].sum() else 0
            val negative = if(it.size >= 3) it[2].sum() else 0
            positive - negative
            //it.getOrElse(0){listOf()}.sum() - it.getOrElse(2){listOf()}.sum()
        }
        .map { units(it) }
        .sum()

fun getMultiplier(index: Int, repeat: Int): Int {
    val fullPatternLength = 4 * repeat
    val fullPatternIndex = index % fullPatternLength

    return when(fullPatternIndex / repeat) {
        0 -> 0
        1 -> 1
        2 -> 0
        else -> -1
    }
}

fun units(value: Int) =
    (value % 10).absoluteValue

fun repeatSignal(signal: List<Int>, repeatTimes: Int): List<Int> =
    (1..repeatTimes).map{signal}.flatten()

fun positiveRangeStarts(signalLen: Int, repeat: Int): List<Int> {
    val max = (signalLen - repeat) / (4 * repeat)
    return (0..max).map {repeat - 1 + 4 * repeat * it}
}

fun negativeRangeStarts(signalLen: Int, repeat: Int): List<Int> =
    positiveRangeStarts(signalLen, repeat).map{it + 2 * repeat}.filter{it < signalLen}
