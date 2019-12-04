fun main() {
    print(numPasswordsPart2(145852, 616942))
}

fun numPasswords(min: Int, max:Int): Int {
    var found = 0
    for(i in min..max) {
        var original = i.toString().toCharArray()

        var isSorted = original.toList() == original.sortedArray().toList()
        var isRepeated = original.toSet().size < original.size

        if (isSorted && isRepeated)
            found += 1
    }

    return found
}

fun numPasswordsPart2(min: Int, max:Int): Int {
    var found = 0
    for(i in min..max) {
        var original = i.toString().toCharArray()

        var isSorted = original.toList() == original.sortedArray().toList()

        if (isSorted && sortedContainsDouble(original))
            found += 1
    }

    return found
}

fun sortedContainsDouble(sorted: CharArray): Boolean {
    var prevChar = 'A'
    var charCount = 1

    for(c in sorted) {
        if(c != prevChar) {
            if(charCount == 2)
                break
            else
                prevChar = c
                charCount = 1
        } else {
            charCount += 1
        }
    }

    return charCount == 2
}
