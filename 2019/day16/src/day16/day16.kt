package day16

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

fun main() {
    val puzzleInput = "59715091976660977847686180472178988274868874248912891927881770506416128667679122958792624406231072013221126623881489317912309763385182133601840446469164152094801911846572235367585363091944153574934709408511688568362508877043643569519630950836699246046286262479407806494008328068607275931633094949344281398150800187971317684501113191184838118850287189830872128812188237680673513745269645219228183633986701871488467284716433953663498444829748364402022393727938781357664034739772457855166471802886565257858813291667525635001823584650420815316132943869499800374997777130755842319153463895364409226260937941771665247483191282218355610246363741092810592458"
    println(part2AllInplace(puzzleInput))
    //println(part2("03036732577212944063491565474664"))
}

fun part2(inputSignal: String): List<Int> {
    //val puzzleInput = "59715091976660977847686180472178988274868874248912891927881770506416128667679122958792624406231072013221126623881489317912309763385182133601840446469164152094801911846572235367585363091944153574934709408511688568362508877043643569519630950836699246046286262479407806494008328068607275931633094949344281398150800187971317684501113191184838118850287189830872128812188237680673513745269645219228183633986701871488467284716433953663498444829748364402022393727938781357664034739772457855166471802886565257858813291667525635001823584650420815316132943869499800374997777130755842319153463895364409226260937941771665247483191282218355610246363741092810592458"
    val intPuzzleInput = inputSignal.map {it.toString().toInt()}
    val offset = inputSignal.take(7).toInt()
    val megaPuzzleInput = repeatSignal(intPuzzleInput, 10000).takeLast(inputSignal.length * 10000 - offset)
    val output = processRepeatedly(megaPuzzleInput, 100, offset = offset)

    return output.take(8)
}

fun part2AllInplace(inputSignal: String) {
    //val puzzleInput = "59715091976660977847686180472178988274868874248912891927881770506416128667679122958792624406231072013221126623881489317912309763385182133601840446469164152094801911846572235367585363091944153574934709408511688568362508877043643569519630950836699246046286262479407806494008328068607275931633094949344281398150800187971317684501113191184838118850287189830872128812188237680673513745269645219228183633986701871488467284716433953663498444829748364402022393727938781357664034739772457855166471802886565257858813291667525635001823584650420815316132943869499800374997777130755842319153463895364409226260937941771665247483191282218355610246363741092810592458"
    val intPuzzleInput = inputSignal.map {it.toString().toInt()}
    val offset = inputSignal.take(7).toInt()
    val megaPuzzleInput = repeatSignal(intPuzzleInput, 10000).takeLast(inputSignal.length * 10000 - offset)

    val totals = megaPuzzleInput.toIntArray()

    println("Iterating over the indices: ${totals.size}")
    for(iter in 1..100) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        var currentDate = sdf.format(Date())
        println("$currentDate: iteration $iter...")
        for (i in totals.indices) {
            if (i % 100000 == 0) {
                println("Processing $i...")
            }
            for (j in 0 until i) {
                totals[j] += getMultiplier(i + offset + 1, j + offset + 1) * totals[i]
            }
        }

        println("Taking the units...")
        for (i in totals.indices) {
            totals[i] = units(totals[i])
        }
    }

    println(totals.take(8))
}


fun processRepeatedly(signal: List<Int>, repeat: Int, offset: Int = 0): List<Int> {
    var processingSignal = signal

    for(i in 1..repeat) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        var currentDate = sdf.format(Date())
        println("$currentDate: iteration $i...")
        processingSignal = processAttemptVier(processingSignal, offset)
    }

    return processingSignal
}


fun process(signal: List<Int>, offset: Int = 0): List<Int> =
    (1..signal.size).map{indexedDigit(signal, it, offset)}

fun processAttemptVier(signal: List<Int>, offset: Int = 0): List<Int> {
    val output = mutableListOf<Int>()
    //val output = signal.take(5971508).toMutableList()
    for (i in 1..signal.size)
        output.add(indexedDigitImproved(signal, i, offset))

    return output.toList()
}

fun processAttemptFunf(signal: List<Int>): List<Int> {
    // Calculate all figures in one pass and sum in place
    val totals = signal.toIntArray()

    for (i in totals.indices) {
        for (j in 0 until i) {
            totals[j] += getMultiplier(i + 1, j + 1) * totals[i]
        }
    }

    for (i in totals.indices) {
        totals[i] = units(totals[i])
    }

    return totals.toList()
}


fun indexedDigit(signal: List<Int>, outputDigit: Int, offset:Int = 0): Int {
    val ret = signal
        .mapIndexed { index, i ->  i * getMultiplier(index + offset + 1, outputDigit + offset) }
        .sum()

    return units(ret)
}

fun indexedDigitImproved(signal: List<Int>, outputDigit: Int,  offset:Int = 0): Int {
    val len = signal.size
    val positiveContrib = positiveRangeStarts(len, outputDigit + offset)
        .map {signal.subList(it, if(it + outputDigit + offset > len) len else it + outputDigit + offset).sum()}
        .sum()

    val negativeContrib = negativeRangeStarts(len, outputDigit + offset)
        .map {signal.subList(it, if(it + outputDigit + offset > len) len else it + outputDigit + offset).sum()}
        .sum()

    return units(positiveContrib - negativeContrib)
}

fun indexedDigitAttemptTrois(signal: List<Int>, outputDigit: Int, startingDigit: Int = 0): Int =
    signal.asSequence()
        .drop(outputDigit - 1)
        .chunked(outputDigit + startingDigit)
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

fun positiveRangeStarts(signalLen: Int, repeat: Int, offset: Int = 0): List<Int> {
    val max = (signalLen - repeat) / (4 * repeat)
    return (0..max).map {repeat - 1 + 4 * repeat * it}.filter{it > offset}.map{it - offset}
}

fun negativeRangeStarts(signalLen: Int, repeat: Int, offset: Int = 0): List<Int> =
    positiveRangeStarts(signalLen, repeat).map{it + 2 * repeat}.filter{it < signalLen}
