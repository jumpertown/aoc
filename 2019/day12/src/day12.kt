import moons.Moon
import moons.Vector
import java.math.BigDecimal
import java.math.BigInteger

/**
 * So each state could only be reached from a single previous state so the first
 * state to be repeated would be the initial state. Can we apply multiple steps at a time
 * between crossings
 */
fun part2(moons: List<Moon>) {
    var steps = BigDecimal(0)
    val initialPositions = moons.map{it.position}
    val initialVelocities = moons.map{it.velocity}

    while(true) {
        steps++
        if(steps % BigDecimal(10000000) == BigDecimal(0)) {
            println("$steps steps")
        }
        for (moon in moons) {
            moon.applyGravity(moons)
        }
        for (moon in moons) {
            moon.move()
        }

        var positions = moons.map{it.position}
        var velocities = moons.map{it.velocity}

        if(positions == initialPositions && velocities == initialVelocities) {
            break
        }
    }

    println("Steps: $steps")
}

fun part1(moons: List<Moon>, steps: Int) {
    for (step in 1..steps) {
        for (moon in moons) {
            moon.applyGravity(moons)
        }
        for (moon in moons) {
            moon.move()
        }
    }

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
    part2(moons)
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
    part2(moons)
}

fun main() {
    puzzleInput()
    //test1()
}