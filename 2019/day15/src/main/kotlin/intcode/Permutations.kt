package intcode

fun permute(input: List<Int>): List<List<Int>> =
    if(input.size == 1)
        listOf(listOf(input[0]))
    else
        input.flatMap { me ->
            val theRest = input.filter { it != me }
            permute(theRest).map {listOf(me) + it}
        }