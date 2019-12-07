package intcode

fun operate(inputCodes: List<Int>, inputs: List<Int>): Int {
    var position = 0
    var inputNum = 0

    var overriddenCodes = mutableMapOf<Int, Int>()

    fun read(index: Int): Int {
        return if(overriddenCodes.containsKey(index)) {
            overriddenCodes[index]!!
        } else {
            inputCodes[index]
        }
    }

    fun write(index: Int, value: Int) {
        overriddenCodes[index] = value
    }

    fun run(): Int {
        val tests = mutableListOf<Int>()
        while(true) {
            var opCode =  read(position)
            //println(opCode)
            var operator = Operator.create(opCode, ::read, ::write)

            if(operator.isTerminated)
                break

            var numOperands = operator.numOperands

            var operands = ((position + 1)..(position + numOperands)).map { read(it) }

            if(operator.requiresInput) {
                var input =
                    if(inputNum >= inputs.size) {
                        println("Is this right inputNum: $inputNum...")
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
                    position = ret
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

