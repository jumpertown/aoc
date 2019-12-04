package day3

enum class Direction (val unit_x: Int, val unit_y: Int) {
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    companion object {
        private val directionMap = mapOf(
            "U" to UP,
            "D" to DOWN,
            "L" to LEFT,
            "R" to RIGHT
        )
        fun fromString(str: String): Direction = directionMap[str]!!
    }
}