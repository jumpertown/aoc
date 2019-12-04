package day3

import kotlin.math.abs

data class Point(val x: Int = 0, val y: Int = 0) {
    fun distance(other: Point = Point(0, 0)): Int =
        abs(this.x - other.x) + abs(this.y - other.y)

    fun move(movement: Movement): Point =
        Point(
            this.x + movement.magnitude * movement.direction.unit_x,
            this.y + movement.magnitude * movement.direction.unit_y
        )

    fun path(other: Point): List<Point> =
        if(other.x == this.x) {
            makeRange(this.y, other.y).map {Point(this.x, it)}
        } else {
            makeRange(this.x, other.x).map {Point(it, this.y)}
        }

    companion object {
        private fun makeRange(from: Int, to: Int) =
            if(from > to)
                from - 1 downTo to
            else
                (from + 1)..to
    }
}