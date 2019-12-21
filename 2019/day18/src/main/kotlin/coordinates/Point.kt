package coordinates

data class Point(val x: Int, val y: Int) {
    fun move(direction: Direction): Point =
        Point(x + direction.unit_x, y + direction.unit_y)

    fun inArea(topLeft: Point, bottomRight: Point): Boolean =
            x in topLeft.x..bottomRight.x && y in topLeft.y..bottomRight.y

    fun carriageReturn(): Point = Point(0, y + 1)
}