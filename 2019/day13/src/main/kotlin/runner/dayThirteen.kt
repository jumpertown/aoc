package runner

import intcode.Computer
import java.io.File
import java.math.BigDecimal

data class Coord(val x: Int, val y: Int)

fun main(args: Array<String>) {
    println(part2())
}

enum class Move(val value: Int) {
    LEFT(-1),
    RIGHT(1),
    STAY(0);
}

class BreakoutGame(inputCodes: List<BigDecimal>) {
    private val gameMap = mutableMapOf<Coord, Int>()
    private val computer = Computer(inputCodes)

    var ballPosition: Coord? = null
    var paddlePosition: Coord? = null
    var score = 0

    init {
        val moves = computer.operate(listOf<BigDecimal>())
        this.updateGameState(moves)
    }

    fun movePaddle(move: Move) {
        val moves = computer.operate(listOf(BigDecimal(move.value)))
        updateGameState(moves)
    }

    private fun updateGameState(moves: List<BigDecimal>) {
        for(move in moves.map { it.toInt() }.chunked(3)) {
            var position = Coord(move[0], move[1])
            var tile = move[2]

            if(position == scorePosition)
                score = tile

            if(tile == TILE_BALL)
                ballPosition = position
            else if(tile == TILE_PADDLE)
                paddlePosition = position

            gameMap[position] = tile
        }
    }

    override fun toString(): String {
        val coords = gameMap.keys.filter{it != scorePosition}

        val maxX = coords.map{it.x}.max()!!
        val minX = coords.map{it.x}.min()!!
        val maxY = coords.map{it.y}.max()!!
        val minY = coords.map{it.y}.min()!!

        val output = StringBuilder()

        output.append("SCORE: $score\n\n")

        for(y in minY..maxY) {
            for (x in minX..maxX) {
                var char = when(gameMap.getOrDefault(Coord(x, y), -1)) {
                    TILE_EMPTY -> ' '
                    TILE_WALL  -> '#'
                    TILE_BLOCK -> 'B'
                    TILE_PADDLE -> '_'
                    TILE_BALL -> 'o'
                    else -> ' '
                }
                output.append(char)
            }
            output.append('\n')
        }
        return output.toString()
    }

    companion object {
        const val TILE_EMPTY = 0
        const val TILE_WALL = 1
        const val TILE_BLOCK = 2
        const val TILE_PADDLE = 3
        const val TILE_BALL = 4
        val scorePosition = Coord(-1, 0)
    }
}

fun part1(): Int {
    val inputCodes = parsePuzzleInput()
    val computer = Computer(inputCodes)
    val output = computer.operate(listOf<BigDecimal>())

    val gameMap = output.map {it.toInt()}.chunked(3).map {Coord(it[0], it[1]) to it[2]}.toMap()


    return gameMap.values.filter {it == 2}.count()
}

fun part2() {
    val inputCodes = parsePuzzleInput().mapIndexed {idx, existing -> if(idx == 0) BigDecimal(2) else existing }
    val game = BreakoutGame(inputCodes)

    println(game)

    for(i in 0..10000) {
        var move = decideMove(game)
        game.movePaddle(move)

        if (i % 1000 == 0)
            println(game)
    }


    println(game)
}

fun decideMove(game: BreakoutGame): Move =
        when {
            game.paddlePosition!!.x < game.ballPosition!!.x -> Move.RIGHT
            game.paddlePosition!!.x > game.ballPosition!!.x -> Move.LEFT
            else -> Move.STAY
        }

fun parsePuzzleInput(): List<BigDecimal> {
    val contents = File("/Users/justinpurrington/Downloads/day13.txt").readLines()
    return contents[0].split(',').map {it.toBigDecimal()}
}
