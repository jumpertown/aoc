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

    operator fun get(idx: Int): Int =
        when(idx) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException()
        }

    operator fun times(factor: Int): Vector {
        return Vector(
            this.x * factor,
            this.y * factor,
            this.z * factor
        )
    }

    override fun toString(): String = "<$x, $y, $z>"
}

data class Moon (val name: String, val position: Vector, val velocity:Vector=Vector()) {
    val potentialEnergy: Int
        get() = position.x.absoluteValue + position.y.absoluteValue + position.z.absoluteValue

    val kineticEnergy: Int
        get() = velocity.x.absoluteValue + velocity.y.absoluteValue + velocity.z.absoluteValue

    val energy: Int
        get() = this.potentialEnergy * this.kineticEnergy

    fun boosts(moons: List<Moon>): List<Vector> =
        moons.filter {it.name != this.name}.map { compareVectors(it.position, this.position) }

    fun applyGravity(moons: List<Moon>, steps:Int=1): Moon {
        var boost = Vector()
        for (effect in boosts(moons))
            boost += effect

        val newVelocity = this.velocity + boost * steps
        val newPosition = this.position + this.velocity * steps + boost * (steps * (steps + 1) / 2)

        return Moon(name, newPosition, newVelocity)
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