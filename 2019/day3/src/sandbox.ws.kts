import day3.Direction
import day3.Movement
import day3.Point
import day3.Wire

val x = 12


val point = Point(3, 2)
val otherPoint = Point(-4, 3)

point.distance(otherPoint)
point.distance()
point.move(Movement.fromString("U12"))

Point(1, 2).path(Point( -4, 2))

val wire1Directions = listOf(
    "R75", "D30", "R83", "U83", "L12", "D49", "R71", "U7", "L72"
).map {Movement.fromString(it)}
val wire1 = Wire(wire1Directions)

val wire2Directions = listOf(
    "U62","R66","U55","R34","D71","R55","D58","R83"
).map {Movement.fromString(it)}
val wire2 = Wire(wire2Directions)

(wire1.points() intersect wire2.points()).map {
    it.distance()
}.filter {
    it > 0
}.min()

























