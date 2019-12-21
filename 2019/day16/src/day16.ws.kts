import day16.*

listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subList(1, 9)



val signal = "12345678"
val intSignal = signal.map {it.toString().toInt()}


positiveRangeStarts(8, 1)
negativeRangeStarts(8, 1)

(1..signal.length)
(1..signal.length).map{indexedDigit(intSignal, it)}
(1..signal.length).map{ indexedDigitImproved(intSignal, it) }
(1..signal.length).map{ indexedDigitAttemptTrois(intSignal, it) }


process(intSignal)
processAttemptVier(intSignal)
processAttemptFunf(intSignal)


//var longerSignal = "80871224585914546619083218645595"
//val intLongerSignal = longerSignal.map {it.toString().toInt()}
//
//// processRepeatedly(intLongerSignal, 100)
//
//val puzzleInput = "59715091976660977847686180472178988274868874248912891927881770506416128667679122958792624406231072013221126623881489317912309763385182133601840446469164152094801911846572235367585363091944153574934709408511688568362508877043643569519630950836699246046286262479407806494008328068607275931633094949344281398150800187971317684501113191184838118850287189830872128812188237680673513745269645219228183633986701871488467284716433953663498444829748364402022393727938781357664034739772457855166471802886565257858813291667525635001823584650420815316132943869499800374997777130755842319153463895364409226260937941771665247483191282218355610246363741092810592458"
//val intPuzzleInput = puzzleInput.map {it.toString().toInt()}
//
//processRepeatedly(intPuzzleInput, 100).subList(0,8)
//
//intSignal
//intSignal.map{it.toChar()}
//
//
//positiveRangeStarts(15, 3)
//negativeRangeStarts(20, 3)
//
//listOf(1, 2, 3).getOrElse(4){0}
//
//
//sequenceOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
//    .drop(2)
//    .chunked(3)
//    .chunked(4)
//    .map{
//        it.getOrElse(0){listOf()}.sum() - it.getOrElse(2){listOf()}.sum()
//    }
//    .toList()


(1..6).map {getMultiplier(7, it)}

var test = arrayOf(1, 2, 3)

test[0] += 1

test[0]




















































