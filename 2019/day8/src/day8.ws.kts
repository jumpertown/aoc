val input = "00123456"

val layers =  input.chunked(4)

val counter = mutableMapOf('0' to 0, '1' to 0, '3' to 0)

for(char in "0012")
    counter[char] = counter.getOrDefault(char, 0) + 1

counter

val counters = listOf(
    mutableMapOf('0' to 4, '1' to 2, '3' to 3),
    mutableMapOf('0' to 2, '1' to 4, '3' to 7)
)

counters.maxBy { it.getOrDefault('0', 0) }


listOf('a', 'b', 'c').joinToString(separator = "")










