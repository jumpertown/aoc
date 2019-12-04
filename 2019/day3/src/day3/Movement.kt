package day3

data class Movement(val direction: Direction, val magnitude: Int){
    companion object {
        fun fromString(movement: String): Movement {
            val dir = movement.substring(0, 1)
            val mag = movement.substring(1).toInt()
            return Movement(Direction.fromString(dir), mag)
        }
    }
}
