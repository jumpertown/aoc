package directions

enum class Direction(val value: Int) {
    NORTH(1),
    SOUTH(2),
    WEST(3),
    EAST(4);

    val opposite: Direction
        get() = when(this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            WEST -> EAST
            EAST -> WEST
        }

    companion object {
        val ALL = listOf(NORTH, SOUTH, EAST, WEST)
    }
}