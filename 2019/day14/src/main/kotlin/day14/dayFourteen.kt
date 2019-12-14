package day14

import java.io.File
import java.math.BigDecimal
import java.math.BigInteger

data class Chemical(val name: String)

val ORE = Chemical("ORE")

data class ChemicalQuantity(val chemical: Chemical, val quantity: Int) {
    operator fun times(multiplier: Int): ChemicalQuantity =
            ChemicalQuantity(chemical, quantity * multiplier)

    companion object {
        fun fromString(str: String): ChemicalQuantity {
            val re = "^[ ]*([0-9]+) ([A-Z]+)$".toRegex()

            val match = re.matchEntire(str) ?: throw IllegalStateException("Cannot match: $str")
            val groups = match.groups

            return ChemicalQuantity(
                    Chemical(groups[2]!!.value),
                    groups[1]!!.value.toInt()
            )
        }
    }
}

data class ComplexReaction(val input: Set<ChemicalQuantity>, val output: Set<ChemicalQuantity>) {
    fun toOre(): ComplexReaction {
        return this
    }
}

data class Reaction(val input: List<ChemicalQuantity>, val output: ChemicalQuantity) {
    operator fun times(multiplier: Int): Reaction {
        val newOutput = output * multiplier
        val newInput = input.map {it * multiplier}
        return Reaction(newInput, newOutput)
    }

    fun simplify(): Reaction {
        val mix = mutableMapOf<Chemical, Int>()
        for(chemicalQuantity in input) {
            var chemical = chemicalQuantity.chemical
            var quantity = mix.getOrDefault(chemical, 0) + chemicalQuantity.quantity
            mix[chemical] = quantity
        }

        val simplifiedInput = mix.map{entry -> ChemicalQuantity(entry.key, entry.value)}
        return Reaction(simplifiedInput, this.output)
    }

    fun toOre(): Reaction {
        return this
    }

    companion object {
        fun fromString(str: String): Reaction {
            val reactionRe = "^([A-Z0-9, ]+) => ([A-Z0-9 ]+)$".toRegex()

            val match = reactionRe.matchEntire(str) ?: throw IllegalStateException("Cannot match: $str")

            val groups = match.groups

            val input = groups[1]!!.value
            val output = groups[2]!!.value
            //val outputQuantity = groups[2]!!.value.toInt()
            //val outputChemical = Chemical(groups[3]!!.value)

            return Reaction(
                    input.split(',').map{ ChemicalQuantity.fromString(it) },
                    ChemicalQuantity.fromString(output)
            )
        }
    }
}

fun reactionMap(reactions: List<Reaction>): Map<Chemical, MutableList<Reaction>> {
    val recipes = mutableMapOf<Chemical, MutableList<Reaction>>()

    for(reaction in reactions) {
        val chemical = reaction.output.chemical

        if(recipes.containsKey(chemical)) {
            recipes[chemical]!!.add(reaction)
        } else {
            recipes[chemical] = mutableListOf(reaction)
        }
    }

    return recipes.toMap()
}

fun waysOfMaking() {
    val reactions = parsePuzzleInput()
    val recipes = reactionMap(reactions)

    println(recipes.values.map {it.size}.max())
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
            ChemicalQuantity(Chemical("FUEL"), 1),
            recipes
    ))
}

fun part1() {
    val reactions = parsePuzzleInput()
    val recipes = reactions.map { it.output.chemical to it }.toMap()

    println(makeChemicalFromOre(
            ChemicalQuantity(Chemical("FUEL"), 1),
            recipes
    ))
}

fun main() {
    part1()
    //val reactions = parsePuzzleInput()

    // See from the waysOfMaking test that there's only one way to make each chemical
    //val recipes = reactions.map{ it.output.chemical to it }.toMap()
}

fun lcm(a: Int, b: Int): Int {
    val bigA = BigInteger.valueOf(a.toLong())
    val bigB = BigInteger.valueOf(b.toLong())
    return (bigA * (bigB / bigA.gcd(bigB))).toInt()
}

fun quantityMultiple(required: Int, canMake: Int): Int =
    if (required % canMake == 0)
        required / canMake
    else
        required / canMake + 1


fun makeChemicalFromOre(
        chemicalQuantity: ChemicalQuantity,
        recipes: Map<Chemical, Reaction>
): Int {
    val store = mutableMapOf<Chemical, Int>()

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

            if(quantity == 0) {
                return listOf<ChemicalQuantity>()
            }
        }

        // Follow the recipe
        var recipe = recipes[chemical]!!
        val multiple = quantityMultiple(quantity, recipe.output.quantity)
        recipe *= multiple

        //Put the excess back in the store
        val excess = recipe.output.quantity - quantity
        if(excess != 0) {
            store[chemical] = store.getOrDefault(chemical, 0) + excess
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