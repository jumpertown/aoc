import java.io.File

import collections.TreeNode

fun main() {
    println(part2())
}

fun part1() {
    val treeMap = parsePuzzleInput()

    println(treeMap.values.map {it.depth()}.sum())
}

fun part2(): Int {
    val treeMap = parsePuzzleInput()

    val you = treeMap["YOU"]
    val san = treeMap["SAN"]

    val ancestor = you!!.commonAncestor(san!!)

    return you.depth() + san.depth() - 2 * ancestor!!.depth() - 2

}

fun parsePuzzleInput(): MutableMap<String, TreeNode<String>> {
    val contents = File("/Users/justinpurrington/Downloads/day6.txt").readLines()

    val treeMap = mutableMapOf<String, TreeNode<String>>()

    for(content in contents.map {it.split(')')}) {
        if(content.size != 2) {
            print("Content is $content")
        }
        var parent = content[0]
        var child = content[1]

        if(!treeMap.containsKey(parent))
            treeMap[parent] = TreeNode(parent)

        if(!treeMap.containsKey(child))
            treeMap[child] = TreeNode(child)

        treeMap[parent]!!.addChild(treeMap[child]!!)
    }

    return treeMap
}