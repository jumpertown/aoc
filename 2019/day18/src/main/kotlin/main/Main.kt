package main

import coordinates.Direction
import coordinates.Point
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


data class GameState(val position: Point, val keysRequired: Set<Char>) {
    val solved: Boolean
        get() = keysRequired.isEmpty()
}

class PuzzleMap(val input: List<String>) {
    val grid: Map<Point, Char>
    val initialPosition: Point
    val bottomRight: Point
    val topLeft = Point(0, 0)

    init {
        val (i, g) = build()
        initialPosition = i
        grid = g
        bottomRight = getBottomRightHey()
    }

    private fun build(): Pair<Point, Map<Point, Char>> {
        var y = 0
        var me = Point(0, 0)
        val mutMap = mutableMapOf<Point, Char>()

        for(row in input) {
            var x = 0
            for(artifact in row) {
                if(artifact == '@') {
                    me = Point(x, y)
                    mutMap[Point(x, y)] = '.'
                } else {
                    mutMap[Point(x, y)] = artifact
                }
                x++
            }
            y++
        }

        return Pair(me, mutMap.toMap())
    }

    fun nextStates(state: GameState): List<GameState> =
            Direction.all.filter {
                val newPosition = grid.getOrDefault(state.position.move(it), '#')
                if(newPosition in 'A'..'Z') {
                    val requiredKey = newPosition + 32
                    requiredKey !in state.keysRequired
                } else newPosition != '#'
            }.map {move(state, it)}

    private fun move(state: GameState, direction: Direction): GameState {
        val newPosition = state.position.move(direction)
        val newArtifact = grid[newPosition]!!

        return GameState(newPosition, state.keysRequired.minus(newArtifact))
    }

    fun drawMap(me: Point=initialPosition) {
        for(x in 0..bottomRight.x) {
            print(x % 10)
        }
        println("")

        for (y in topLeft.y..bottomRight.y) {
            print(y % 10)
            for (x in topLeft.x..bottomRight.x) {
                var coord = Point(x, y)
                var artifact = if(coord == me)
                    '@'
                else
                    grid.getOrDefault(coord, '.')
                print(artifact)
            }
            println("")
        }
    }

    val keys: Set<Char>
        get() = grid.values.filter { it in 'a'..'z' }.toSet()

    private fun getBottomRightHey(): Point {
        val maxX  = grid.keys.map {it.x}.max()
        val maxY  = grid.keys.map {it.y}.max()

        return Point(maxX!!, maxY!!)
    }
}

fun main() {
    part1()
}

fun part1() {
    val puzzleMap = parsePuzzleInput()
    puzzleMap.drawMap()
    println(solver(puzzleMap))
}

fun test1() {
    var puzzleInput = listOf(
            "########################",
            "#@..............ac.GI.b#",
            "###d#e#f################",
            "###A#B#C################",
            "###g#h#i################",
            "########################"
    )
    val puzzleMap = PuzzleMap(puzzleInput)
    println(solver(puzzleMap))
}

fun test2() {
    var puzzleInput = listOf(
            "#################",
            "#i.G..c...e..H.p#",
            "########.########",
            "#j.A..b...f..D.o#",
            "########@########",
            "#k.E..a...g..B.n#",
            "########.########",
            "#l.F..d...h..C.m#",
            "#################"
    )
    val puzzleMap = PuzzleMap(puzzleInput)
    println(solver(puzzleMap))
}

fun solver(puzzleMap: PuzzleMap): Int {
    val initialState = GameState(puzzleMap.initialPosition, puzzleMap.keys)

    var states = setOf(initialState)
    var seenStates = states

    fun nextMove(states: Set<GameState>): Set<GameState> =
        states.map {puzzleMap.nextStates(it)}
                .flatten()
                .filter{it !in seenStates}
                .toSet()

    var steps = 0
    while(true) {
        steps++
        states = nextMove(states)
        if(!states.none { it.solved })
            break

        if(steps % 100 == 0) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            var currentDate = sdf.format(Date())
            var minRemaining = states.map {it.keysRequired.size }.min()
            println("$currentDate $steps steps. States: ${seenStates.size} Keys remaining: $minRemaining")
        }

        seenStates = seenStates.union(states)
    }
    return steps
}

fun parsePuzzleInput(): PuzzleMap {
    val contents = File("/Users/justinpurrington/Downloads/day18.txt").readLines()
    return PuzzleMap(contents)
}