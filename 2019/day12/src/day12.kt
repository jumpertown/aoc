import moons.Moon
import moons.Vector
import java.math.BigDecimal
import java.math.BigInteger

/**
 * So each state could only be reached from a single previous state so the first
 * state to be repeated would be the initial state. Can we apply multiple steps at a time
 * between crossings
 */

fun periodicity(axis: Int, initialMoons: List<Moon>): BigDecimal {
    val initialPositions = initialMoons.map { it.position[axis] }
    val initialVelocities = initialMoons.map { it.velocity[axis] }

    var moons = initialMoons
    var steps = BigDecimal(0)


    while (true) {
        steps++
        if (steps % BigDecimal(10000000) == BigDecimal(0)) {
            println("$steps steps")
        }
        moons = moons.map { it.applyGravity(moons) }

        var positions = moons.map { it.position[axis] }
        var velocities = moons.map { it.velocity[axis] }

        if (positions == initialPositions && velocities == initialVelocities)
            return steps
    }
}
//fun part2(moons: List<Moon>) {
//    var steps = BigDecimal(0)
//    val initialPositions = moons.map{it.position}
//    val initialVelocities = moons.map{it.velocity}
//
//    while(true) {
//        steps++
//        if(steps % BigDecimal(10000000) == BigDecimal(0)) {
//            println("$steps steps")
//        }
//        for (moon in moons) {
//            moon.applyGravity(moons)
//        }
//        for (moon in moons) {
//            moon.move()
//        }
//
//        var positions = moons.map{it.position}
//        var velocities = moons.map{it.velocity}
//
//        if(positions == initialPositions && velocities == initialVelocities) {
//            break
//        }
//    }
//
//    println("Steps: $steps")
//}

fun part1(startingMoons: List<Moon>, steps: Int) {
    var moons = startingMoons
    for (step in 1..steps)
        moons = moons.map{it.applyGravity(moons)}

    for(moon in moons) {
        println(moon)
    }

    val energy = moons.sumBy { it.energy }
    println("Energy: $energy")
}

/**
 * <x=-1, y=0, z=2>
 * <x=2, y=-10, z=-7>
 * <x=4, y=-8, z=8>
 * <x=3, y=5, z=-1>
 */
fun test1() {
    val moons = listOf(
        Moon("Io", position= Vector(-1, 0, 2)),
        Moon("Europa", position=Vector(2, -10, -7)),
        Moon("Callisto", position=Vector(4, -8, 8)),
        Moon("Ganymede", position=Vector(3, 5, -1))
    )
    part1(moons, 10)
}

/**
 * <x=1, y=-4, z=3>
 * <x=-14, y=9, z=-4>
 * <x=-4, y=-6, z=7>
 * <x=6, y=-9, z=-11>
 */
fun puzzleInput() {
    val moons = listOf(
        Moon("Io", position= Vector(1, -4, 3)),
        Moon("Europa", position=Vector(-14, 9, -4)),
        Moon("Callisto", position=Vector(-4, -6, 7)),
        Moon("Ganymede", position=Vector(6, -9, -11))
    )
    val xPeriodicity = periodicity(0, moons)
    val yPeriodicity = periodicity(1, moons)
    val zPeriodicity = periodicity(2, moons)

    println("$xPeriodicity $yPeriodicity $zPeriodicity")
    //part2(moons)
}

fun main() {
    puzzleInput()
    //test1()
}
