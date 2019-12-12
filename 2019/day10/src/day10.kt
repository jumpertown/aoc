import asteroid.Asteroid
import asteroid.Direction
import java.io.File
import java.util.*


fun main() {
    val asteroids = parseInput()
    println(part2(asteroids)[199])
}

fun part1(asteroids: List<Asteroid>): Asteroid {
    var numAsteroidsVisible = mutableMapOf<Asteroid, Int>()
    for(asteroid in asteroids) {
        var asteroidsVisible = mutableMapOf<Direction, Asteroid>()
        var otherAsteroids = asteroids.filter {it != asteroid}

        for(other in otherAsteroids) {
            var path = asteroid.pathTo(other)
            var direction = path.direction
            if(!asteroidsVisible.containsKey(direction))
                asteroidsVisible[direction] = other
            else if(path.magnitude < asteroid.pathTo(asteroidsVisible[direction]!!).magnitude) {
                asteroidsVisible[direction] = other
            }
        }

        numAsteroidsVisible[asteroid] = asteroidsVisible.size
    }

    val bestPosition = numAsteroidsVisible.maxBy { it.value }!!
    println(bestPosition.value)
    return bestPosition.key
}

fun part2(asteroids: List<Asteroid>): List<Asteroid> {
    val station = part1(asteroids)

    println("Station = $station")

    val others = asteroids.filter {it != station}
    val othersByDirection = mutableMapOf<Direction, PriorityQueue<Asteroid>>()

    for(asteroid in others) {
        var path = station.pathTo(asteroid)
        if(!othersByDirection.containsKey(path.direction))
            othersByDirection[path.direction] = PriorityQueue { a: Asteroid, b: Asteroid ->
                station.pathTo(a).magnitude - station.pathTo(b).magnitude
            }
        othersByDirection[path.direction]!!.add(asteroid)
    }

    val clockwiseDirections = othersByDirection.keys.sortedBy { it.angle() }

    val destroyedAsteroids = mutableListOf<Asteroid>()

    while(destroyedAsteroids.size < others.size) {
        for (direction in clockwiseDirections) {
            var asteroid = othersByDirection[direction]!!.poll()
            if (asteroid != null)
                destroyedAsteroids.add(asteroid)
        }
    }

    return destroyedAsteroids.toList()
}

fun parseInput(): List<Asteroid> {
    var y = 0

    val asteroids = mutableListOf<Asteroid>()
    File("/Users/justinpurrington/Downloads/day10.txt").forEachLine {
        var x = 0
        for(char in it) {
            if(char == '#')
                asteroids.add(Asteroid(x, y))
            x++
        }
        y++
    }

    return asteroids.toList()
}