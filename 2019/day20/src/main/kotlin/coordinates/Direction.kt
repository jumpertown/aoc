package coordinates

enum class Direction (val unit_x: Int, val unit_y: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    companion object {
        fun fromString(str: String): Direction =
            when(str) {
                "U" -> UP
                "D" -> DOWN
                "L" -> LEFT
                "R" -> RIGHT
                else -> throw IllegalStateException("Unknown direction $str")
            }

        val all = listOf(UP, DOWN, LEFT, RIGHT)
    }
}
