package asteroid

import java.math.BigInteger
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.asin
import kotlin.math.sqrt

data class Direction(val x: Int, val y: Int) {
    fun angle(): Double {
        // I can't do rotations - let's just go between 0 and PI/2
        val xDub = x.absoluteValue.toDouble()
        val yDub = y.absoluteValue.toDouble()
        val h = sqrt(xDub*xDub + yDub*yDub)

        return if (x>= 0 && y < 0) {  // NE
            asin(xDub/h)
        } else if (x>= 0 && y >= 0) {  // SE
            asin(yDub/h) + PI/2
        } else if (x < 0 && y >= 0) {  // SW
            asin(xDub/h) + PI
        } else {  // NW
            asin(yDub/h) + 3 * PI/2
        }
    }
}

data class Vector(val magnitude: Int, val direction: Direction)

data class Asteroid(val x: Int, val y: Int)  {
    fun pathTo(other: Asteroid): Vector {
        val xDiff = other.x - this.x
        val yDiff = other.y - this.y

        val gcd = xDiff.toBigInteger().gcd(yDiff.toBigInteger()).toInt()
        val direction = Direction(xDiff / gcd, yDiff / gcd)

        return Vector(gcd, direction)
    }
}


