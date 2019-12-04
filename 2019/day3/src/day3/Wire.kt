package day3

data class Wire(val movements: List<Movement>) {
    fun points(): Set<Point> {
        var position = Point()
        val pts = mutableSetOf<Point>()
        for(movement in movements) {
            var next = position.move(movement)
            position.path(next).map {pts.add(it)}
            position = next
        }
        return pts.toSet()
    }

    fun pointSteps(): Map<Point, Int> {
        var position = Point()
        var distance = 0
        val pts = mutableMapOf<Point, Int>()
        for(movement in movements) {
            var next = position.move(movement)
            for(point in position.path(next)) {
                distance += 1
                if(!pts.containsKey(point))
                    pts[point] = distance
            }
            position = next
        }
        return pts.toMap()
    }
}