package day14

import java.io.File
import java.math.BigInteger

data class Chemical(val name: String)

val ORE = Chemical("ORE")

data class ChemicalQuantity(val chemical: Chemical, val quantity: BigInteger) {
    operator fun times(multiplier: BigInteger): ChemicalQuantity =
            ChemicalQuantity(chemical, quantity * multiplier)

    companion object {
        fun fromString(str: String): ChemicalQuantity {
            val re = "^[ ]*([0-9]+) ([A-Z]+)$".toRegex()

            val match = re.matchEntire(str) ?: throw IllegalStateException("Cannot match: $str")
            val groups = match.groups

            return ChemicalQuantity(
                    Chemical(groups[2]!!.value),
                    groups[1]!!.value.toBigInteger()
            )
        }
    }
}

data class Reaction(val input: List<ChemicalQuantity>, val output: ChemicalQuantity) {
    operator fun times(multiplier: BigInteger): Reaction {
        val newOutput = output * multiplier
        val newInput = input.map {it * multiplier}
        return Reaction(newInput, newOutput)
    }

    fun simplify(): Reaction {
        val mix = mutableMapOf<Chemical, BigInteger>()
        for(chemicalQuantity in input) {
            var chemical = chemicalQuantity.chemical
            var quantity = mix.getOrDefault(chemical, BigInteger.ZERO) + chemicalQuantity.quantity
            mix[chemical] = quantity
        }

        val simplifiedInput = mix.map{entry -> ChemicalQuantity(entry.key, entry.value)}
        return Reaction(simplifiedInput, this.output)
    }

    companion object {
        fun fromString(str: String): Reaction {
            val reactionRe = "^([A-Z0-9, ]+) => ([A-Z0-9 ]+)$".toRegex()

            val match = reactionRe.matchEntire(str) ?: throw IllegalStateException("Cannot match: $str")

            val groups = match.groups

            val input = groups[1]!!.value
            val output = groups[2]!!.value

            return Reaction(
                    input.split(',').map{ ChemicalQuantity.fromString(it) },
                    ChemicalQuantity.fromString(output)
            )
        }
    }
}

/**
 * 10 ORE => 10 A
 * 1 ORE => 1 B
 * 7 A, 1 B => 1 C
 * 7 A, 1 C => 1 D
 * 7 A, 1 D => 1 E
 * 7 A, 1 E => 1 FUEL
 */
fun test1() {
    val recipes = listOf(
            "10 ORE => 10 A",
            "1 ORE => 1 B",
            "7 A, 1 B => 1 C",
            "7 A, 1 C => 1 D",
            "7 A, 1 D => 1 E",
            "7 A, 1 E => 1 FUEL"
    ).map {Reaction.fromString(it)}.map { it.output.chemical to it }.toMap()

    println(makeChemicalFromOre(
            ChemicalQuantity(Chemical("FUEL"), BigInteger.ONE),
            recipes
    ))
}

fun part1() {
    val reactions = parsePuzzleInput()
    val recipes = reactions.map { it.output.chemical to it }.toMap()

    println(makeChemicalFromOre(
            ChemicalQuantity(Chemical("FUEL"), BigInteger.ONE),
            recipes
    ))
}

fun part2() {
    val reactions = parsePuzzleInput()
    val recipes = reactions.map { it.output.chemical to it }.toMap()

    // Binary Search
    val totalOre = BigInteger("1000000000000")

    var attempt = BigInteger("2048")
    var tooLow = attempt
    var tooHigh: BigInteger? = null

    // Get bounds
    while(true) {
        var oreRequired = makeChemicalFromOre (
                ChemicalQuantity(Chemical("FUEL"), attempt),
                recipes
        )

        if(oreRequired > totalOre) {
            tooHigh = attempt
        } else if(oreRequired < totalOre) {
            tooLow = attempt
        } else {
            println("Bang on $attempt")
            break
        }

        if(tooHigh == null) {
            attempt *= BigInteger.TWO
        } else {
            if(tooHigh - tooLow == BigInteger.ONE)
                break

            attempt = tooLow + (tooHigh - tooLow) / BigInteger.TWO
        }
    }

    println("Too low $tooLow, too high $tooHigh")


}

fun main() {
    part2()
    //val reactions = parsePuzzleInput()

    // See from the waysOfMaking test that there's only one way to make each chemical
    //val recipes = reactions.map{ it.output.chemical to it }.toMap()
}

fun quantityMultiple(required: BigInteger, canMake: BigInteger): BigInteger =
    if (required % canMake == BigInteger.ZERO)
        required / canMake
    else
        (required / canMake) + BigInteger.ONE


fun makeChemicalFromOre(
        chemicalQuantity: ChemicalQuantity,
        recipes: Map<Chemical, Reaction>
): BigInteger {
    val store = mutableMapOf<Chemical, BigInteger>()

    tailrec fun _makeChemical(chemicalQuantity: ChemicalQuantity): List<ChemicalQuantity> {
        val chemical = chemicalQuantity.chemical
        var quantity = chemicalQuantity.quantity

        // Can't make ORE
        if(chemical == ORE)
            return listOf(chemicalQuantity)

        // Do I have any in the store?
        if(store.containsKey(chemical)) {
            val storeVal = store[chemical]!!
            val take = if(quantity > storeVal) storeVal else quantity
            quantity -= take
            store[chemical] = storeVal - take

            if(quantity == BigInteger.ZERO) {
                return listOf<ChemicalQuantity>()
            }
        }

        // Follow the recipe
        var recipe = recipes[chemical]!!
        val multiple = quantityMultiple(quantity, recipe.output.quantity)
        recipe *= multiple

        //Put the excess back in the store
        val excess = recipe.output.quantity - quantity
        if(excess != BigInteger.ZERO) {
            store[chemical] = store.getOrDefault(chemical, BigInteger.ZERO) + excess
        }

        return recipe.input.flatMap {_makeChemical(it)}
    }

    val chemicalsNeeded = _makeChemical(chemicalQuantity)
    val recipe = Reaction(chemicalsNeeded, chemicalQuantity).simplify()

    // Should all be ORE
    assert(recipe.input.size == 1 && recipe.input[0].chemical == ORE)

    return recipe.input[0].quantity

}

fun parsePuzzleInput(): List<Reaction> {
    val contents = File("/Users/justinpurrington/Downloads/day14.txt").readLines()

    return  contents.map{Reaction.fromString(it)}
}