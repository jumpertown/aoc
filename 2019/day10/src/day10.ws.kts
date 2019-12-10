import asteroid.Asteroid
import asteroid.Direction
import kotlin.math.*

val a= Asteroid(10, 20)
val b= Asteroid(40, -80)

a.pathTo(b)


// Clockwise turn
Direction(0, -1).angle()
Direction(0, -10).angle()
Direction(1, -4).angle()
Direction(1, -3).angle()
Direction(1, -1).angle()
Direction(1, 0).angle()
Direction(1, 1).angle()
Direction(0, 1).angle()
Direction(-1, 1).angle()
Direction(-1, 0).angle()
Direction(-1, -1).angle()


Asteroid(4, 4).pathTo(Asteroid(4, 12))














