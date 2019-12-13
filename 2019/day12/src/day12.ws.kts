import moons.Vector
import java.math.BigInteger

val a = Vector(1 ,3, 7)
val b = Vector(2, 2, 5)

a[0]
a[2]


a + b

val x = BigInteger("161428")
val y = BigInteger("231614")
val z = BigInteger("116328")

fun lcm(a: BigInteger, b: BigInteger): BigInteger {
    return a * (b / a.gcd(b))
}

lcm(lcm(x, y), z)






