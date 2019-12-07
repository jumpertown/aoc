package intcode

import java.math.BigDecimal

fun operate(inputCodes: List<Int>, inputs: List<BigDecimal>): BigDecimal {
    var position = 0
    var inputNum = 0

    var overriddenCodes = mutableMapOf<Int, BigDecimal>()

    fun read(index: Int): BigDecimal {
        return if(overriddenCodes.containsKey(index)) {
            overriddenCodes[index]!!
        } else {
            BigDecimal(inputCodes[index])
        }
    }

    fun write(index: Int, value: BigDecimal) {
        overriddenCodes[index] = value
    }

    fun run(): BigDecimal {
        val tests = mutableListOf<BigDecimal>()
        while(true) {
            var opCode =  read(position)
            //println(opCode)
            var operator = Operator.create(opCode.toInt(), ::read, ::write)

            if(operator.isTerminated)
                break

            var numOperands = operator.numOperands

            var operands = ((position + 1)..(position + numOperands)).map { read(it) }

            if(operator.requiresInput) {
                var input =
                    if(inputNum >= inputs.size) {
                        //println("Is this right inputNum: $inputNum...")
                        inputs.last()
                    } else {
                        inputs[inputNum]
                    }

                operands += listOf(input)
                inputNum += 1
            }

            val ret = operator.operate(operands)

            if(ret != null) {
                if (operator.isJump) {
                    position = ret.toInt()
                    continue
                }
                tests.add(ret)
            }

            position += (numOperands + 1)
        }

        return tests.last()
    }

    return run()
}

