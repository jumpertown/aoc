import day14.Chemical
import day14.ChemicalQuantity
import day14.Reaction
import java.math.BigInteger

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
                ChemicalQuantity(Chemical("ORE"), BigInteger("14")),
                ChemicalQuantity(Chemical("B"), BigInteger("6")),
                ChemicalQuantity(Chemical("ORE"), BigInteger("10")),
                ChemicalQuantity(Chemical("A"), BigInteger("9")),
                ChemicalQuantity(Chemical("B"), BigInteger("34"))
        ),
        output=ChemicalQuantity(Chemical("BLAH"), BigInteger("10"))
).simplify()

Reaction.fromString("7 A, 1 B => 9 C")




















