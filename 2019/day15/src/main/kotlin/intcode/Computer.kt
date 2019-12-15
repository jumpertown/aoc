package intcode

import java.math.BigDecimal
import java.util.*

data class Computer(
        val programme: List<BigDecimal>,
        val state: Map<Int, BigDecimal> = mapOf(),
        val position: Int = 0,
        val isTerminated: Boolean = false,
        val relativeBase: Int = 0,
        val outputs: List<BigDecimal> = listOf()
) {
    fun operate(inputs: List<BigDecimal>): Computer {
        val inputQueue = ArrayDeque<BigDecimal>(inputs)
        var overriddenCodes = state.toMutableMap()

        fun read(index: Int): BigDecimal =
            if (overriddenCodes.containsKey(index)) {
                overriddenCodes[index]!!
            } else if(index < 0 || index >= programme.size) {
                BigDecimal(0)
            } else {
                programme[index]
            }

        fun write(index: Int, value: BigDecimal) {
            //println("Writing $value to $index")
            overriddenCodes[index] = value
        }

        fun run(): Computer {
            val operationOutputs = mutableListOf<BigDecimal>()
            var operationTerminated = isTerminated
            var operationPosition = position

            while (true) {
                var opCode = read(operationPosition)
                var operator = Operator.create(opCode.toInt(), ::read, ::write)

                if (operator.isTerminated) {
                    operationTerminated = true
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
                        operationPosition = ret.toInt()
                        continue
                    }
                    operationOutputs.add(ret)
                }

                operationPosition += (numOperands + 1)
            }

            return Computer(
                    programme = programme,
                    state = overriddenCodes.toMap(),
                    position = operationPosition,
                    isTerminated = operationTerminated,
                    relativeBase = relativeBase,
                    outputs = operationOutputs.toList()
            )
        }

        return run()
    }
}

