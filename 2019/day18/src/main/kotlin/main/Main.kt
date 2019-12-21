package main

import coordinates.Direction
import coordinates.Point
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


data class GameSection(val position: Point, val topLeft: Point, val bottomRight: Point)

data class GameState(val position: Point, val keysRequired: Set<Char>) {
    val solved: Boolean
        get() = keysRequired.isEmpty()
}

data class MultiplayGameState(val positions: List<Point>, val keysRequired: Set<Char>) {
    val solved: Boolean
        get() = keysRequired.isEmpty()

    fun move(index: Int, direction: Direction, artifact: Char): MultiplayGameState {
        val newPositions = positions.mapIndexed{ curIndex, it ->
            if(index == curIndex)
                it.move(direction)
            else
                it
        }
        return MultiplayGameState(newPositions, keysRequired.minus(artifact))
    }
}


class PuzzleMap(val input: List<String>, val walls: List<Point> = listOf()) {
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
                var point = Point(x, y)
                if(point in walls) {
                    mutMap[point] = '#'
                } else if(artifact == '@') {
                    me = point
                    mutMap[point] = '.'
                } else {
                    mutMap[point] = artifact
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

    fun nextMultiplayStates(state: MultiplayGameState): List<MultiplayGameState> =
        state.positions.indices.map { index ->
            Direction.all.filter {direction ->
                val newPosition = grid.getOrDefault(
                        state.positions[index].move(direction),
                        '#'
                )
                if(newPosition in 'A'..'Z') {
                    val requiredKey = newPosition + 32
                    requiredKey !in state.keysRequired
                } else newPosition != '#'
            }.map { direction ->
                val newPosition = state.positions[index].move(direction)
                state.move(index, direction, grid[newPosition]!!)
            }
        }.flatten()

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

    fun keysInArea(topLeft: Point, bottomRight: Point): Set<Char> =
        grid.filter {it.value in 'a'..'z'}.filter {it.key.inArea(topLeft, bottomRight)}.map {it.value}.toSet()

    private fun getBottomRightHey(): Point {
        val maxX  = grid.keys.map {it.x}.max()
        val maxY  = grid.keys.map {it.y}.max()

        return Point(maxX!!, maxY!!)
    }
}

fun main() {
    part2()
}

fun part1() {
    val puzzleMap = parsePuzzleInput()
    puzzleMap.drawMap()
    println(solver(puzzleMap))
}

fun part2() {
    val startingPoint = Point(x=40, y=40)
    val walls = Direction.all.map{startingPoint.move(it)}
    val puzzleMap = parsePuzzleInput(walls)
    //println(multiSolver(puzzleMap))
    val topLeft = Point(40, 40)
    val bottomRight = Point(80, 80)
    val requiredKeys = puzzleMap.keysInArea(topLeft, bottomRight)

    val gameSection = GameSection(Point(41, 41), topLeft, bottomRight)

    println("Keys to collect: $requiredKeys")
    println(solver(puzzleMap, gameSection))
    println("Bottom right of complete map: ${puzzleMap.bottomRight}")
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

fun solver(puzzleMap: PuzzleMap, gameSection: GameSection? = null): Int {
    val initialState = if(gameSection != null)
        GameState(gameSection.position, puzzleMap.keysInArea(gameSection.topLeft, gameSection.bottomRight))
    else
        GameState(puzzleMap.initialPosition, puzzleMap.keys)

    var states = setOf(initialState)
    val seenStates = states.toMutableSet()

    fun nextMove(states: Set<GameState>): Set<GameState> =
        states.map {puzzleMap.nextStates(it)}
                .flatten()
                .filter{it !in seenStates}
                .toSet()

    var steps = 0
    while(true) {
        steps++
        states = nextMove(states)

        if(states.isEmpty()) {
            println("Couldn't find solution")
            break
        }
        if(!states.none { it.solved })
            break

        if(steps % 100 == 0) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            var currentDate = sdf.format(Date())
            var minRemaining = states.map {it.keysRequired.size }.min()
            println("$currentDate $steps steps. States: ${seenStates.size} Keys remaining: $minRemaining")
        }

        seenStates.addAll(states)
    }
    return steps
}

fun multiSolver(puzzleMap: PuzzleMap): Int {
    val initialPositions = listOf(
            Pair(-1, -1),
            Pair(1, 1),
            Pair(1, -1),
            Pair(-1, 1)
    ).map {
        Point(puzzleMap.initialPosition.x + it.first, puzzleMap.initialPosition.y + it.second)
    }
    val initialState = MultiplayGameState(initialPositions, puzzleMap.keys)

    var states = setOf(initialState)
    val seenStates = states.toMutableSet()

    fun nextMove(states: Set<MultiplayGameState>): Set<MultiplayGameState> =
            states.map {puzzleMap.nextMultiplayStates(it)}
                    .flatten()
                    .filter{it !in seenStates}
                    .toSet()

    var steps = 0
    while(true) {
        steps++
        states = nextMove(states)
        if(!states.none { it.solved })
            break

        if(steps % 20 == 0) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            var currentDate = sdf.format(Date())
            var minRemaining = states.map {it.keysRequired.size }.min()
            println("$currentDate $steps steps. States: ${seenStates.size} Keys remaining: $minRemaining")
        }

        seenStates.addAll(states)
    }
    return steps
}

fun parsePuzzleInput(walls: List<Point> = listOf()): PuzzleMap {
    val contents = File("/Users/justinpurrington/Downloads/day18.txt").readLines()
    return PuzzleMap(contents, walls)
}