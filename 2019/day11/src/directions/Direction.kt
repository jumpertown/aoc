package directions

data class Point(val x: Int, val y: Int)

enum class Direction(val rep: Char) {
    UP('^'),
    RIGHT('>'),
    DOWN('v'),
    LEFT('<');

    fun turnLeft(): Direction =
        when(this) {
            UP -> LEFT
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> DOWN
        }

    fun turnRight(): Direction =
        when(this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }

    fun go(point: Point, steps: Int=1): Point =
        when(this) {
            UP -> Point(point.x, point.y + steps)
            RIGHT -> Point(point.x + steps, point.y)
            DOWN -> Point(point.x, point.y - steps)
            LEFT -> Point(point.x - steps, point.y)
        }
}