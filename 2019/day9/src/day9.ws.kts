import intcode.Computer
import intcode.RelativeBase
import java.math.BigDecimal

var test1 = listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)

val comp = Computer(test1.map{ BigDecimal(it) }, 0)

RelativeBase.value = 0
val ret = comp.operate(listOf<BigDecimal>())

//ret


val x = 10





