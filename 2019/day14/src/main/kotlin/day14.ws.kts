import day14.Chemical
import day14.ChemicalQuantity
import day14.Reaction

/*
* 10 ORE => 10 A
* 1 ORE => 1 B
* 7 A, 1 B => 1 C
* 7 A, 1 C => 1 D
* 7 A, 1 D => 1 E
* 7 A, 1 E => 1 FUEL
 */
Reaction(
        input=listOf(
                ChemicalQuantity(Chemical("ORE"), 14),
                ChemicalQuantity(Chemical("B"), 6),
                ChemicalQuantity(Chemical("ORE"), 10),
                ChemicalQuantity(Chemical("A"), 9),
                ChemicalQuantity(Chemical("B"), 34)
        ),
        output=ChemicalQuantity(Chemical("BLAH"), 10)
).simplify()

Reaction.fromString("7 A, 1 B => 9 C")




















