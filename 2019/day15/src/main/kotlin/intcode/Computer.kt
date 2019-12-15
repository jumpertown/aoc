package intcode

import java.math.BigDecimal
import java.util.*

class Computer(val inputCodes: List<BigDecimal>) {
    init {
        RelativeBase.value = 0
    }
    var isTerminated = false
    var position = 0
    var overriddenCodes = mutableMapOf<Int, BigDecimal>()

    fun operate(inputs: List<BigDecimal>): List<BigDecimal> {
        val inputQueue = ArrayDeque<BigDecimal>(inputs)

        fun read(index: Int): BigDecimal {
            val ret = if (overriddenCodes.containsKey(index)) {
                overriddenCodes[index]!!
            } else if(index < 0 || index >= inputCodes.size) {
                BigDecimal(0)
            } else {
                inputCodes[index]
            }
            //println("Reading index $index ($ret)")
            return ret
        }

        fun write(index: Int, value: BigDecimal) {
            //println("Writing $value to $index")
            overriddenCodes[index] = value
        }

        fun run(): List<BigDecimal> {
            val outputs = mutableListOf<BigDecimal>()

            while (true) {
                var opCode = read(position)
                //println("Pos: $position OpCode: $opCode 0: ${read(0)}, 63: ${read(63)}")
                var operator = Operator.create(opCode.toInt(), ::read, ::write)

                if (operator.isTerminated) {
                    //println("TERMINATING...")
                    this.isTerminated = true
                    break
                }

                var numOperands = operator.numOperands

                var operands = ((position + 1)..(position + numOperands)).map { read(it) }

                if (operator.requiresInput) {
                    //println("Input opCode: $opCode position: $position inputQueue: $inputQueue")
                    if (inputQueue.isEmpty()) {
                        // Need more inputs
                        break
                    }

                    var input = inputQueue.remove()

                    //println("Input arg: $input")
                    operands += listOf(input)
                }

                val ret = operator.operate(operands)

                if (ret != null) {
                    if (operator.isJump) {
                        //println("Jumping to $ret")
                        position = ret.toInt()
                        continue
                    }
                    //println("Output: $ret")
                    outputs.add(ret)
                }

                position += (numOperands + 1)
            }

            return outputs.toList()
        }

        return run()
    }
}

