package moons

import kotlin.math.absoluteValue

data class Vector(val x: Int=0, val y: Int=0, val z: Int=0) {
    operator fun plus(other: Vector): Vector {
        return Vector(
            this.x + other.x,
            this.y + other.y,
            this.z + other.z
        )
    }

    override fun toString(): String = "<$x, $y, $z>"
}

class Moon (val name: String, var position: Vector) {
    var velocity = Vector()

    val potentialEnergy: Int
        get() = position.x.absoluteValue + position.y.absoluteValue + position.z.absoluteValue

    val kineticEnergy: Int
        get() = velocity.x.absoluteValue + velocity.y.absoluteValue + velocity.z.absoluteValue

    val energy: Int
        get() = this.potentialEnergy * this.kineticEnergy

    fun applyGravity(others: List<Moon>) {
        val effects = others.filter {it.name != this.name}.map { compareVectors(it.position, this.position) }

        for (effect in effects)
            velocity += effect
    }

    fun move() {
        position += velocity
    }

    override fun toString(): String = "<Moon: $name, $position, $velocity>"

    companion object {
        fun compareVectors(a: Vector, b: Vector): Vector =
            Vector(
                compareInts(a.x, b.x),
                compareInts(a.y, b.y),
                compareInts(a.z, b.z)
            )

        fun compareInts(a: Int, b: Int): Int =
            if(a > b) 1
            else if(b > a) -1
            else 0
    }
}