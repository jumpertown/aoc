package intcode

import java.math.BigDecimal
import java.util.*

class Computer(val inputCodes: List<Int>, val phase: Int) {
    var isPhaseSet = false
    var isTerminated = false
    var position = 0

    fun operate(inputs: List<BigDecimal>): List<BigDecimal> {
        var overriddenCodes = mutableMapOf<Int, BigDecimal>()
        val inputQueue = ArrayDeque<BigDecimal>(inputs)

        fun read(index: Int): BigDecimal {
            return if (overriddenCodes.containsKey(index)) {
                overriddenCodes[index]!!
            } else {
                BigDecimal(inputCodes[index])
            }
        }

        fun write(index: Int, value: BigDecimal) {
            overriddenCodes[index] = value
        }

        fun run(): List<BigDecimal> {
            val outputs = mutableListOf<BigDecimal>()

            while (true) {
                var opCode = read(position)
                //println(opCode)
                var operator = Operator.create(opCode.toInt(), ::read, ::write)

                if (operator.isTerminated) {
                    //println("TERMINATING...")
                    this.isTerminated = true
                    break
                }

                var numOperands = operator.numOperands

                var operands = ((position + 1)..(position + numOperands)).map { read(it) }

                if (operator.requiresInput) {
                    //println("Input position: $position")
                    if (isPhaseSet && inputQueue.isEmpty()) {
                        // Need more inputs
                        break
                    }

                    var input = if (isPhaseSet) inputQueue.remove() else BigDecimal(phase)
                    if (!isPhaseSet)
                        isPhaseSet = true

                    //println("Input arg: $input")
                    operands += listOf(input)
                }

                val ret = operator.operate(operands)

                if (ret != null) {
                    if (operator.isJump) {
                        position = ret.toInt()
                        continue
                    }
                    outputs.add(ret)
                }

                position += (numOperands + 1)
            }

            return outputs.toList()
        }

        return run()
    }
}

