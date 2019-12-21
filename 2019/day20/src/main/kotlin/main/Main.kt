package main

import coordinates.Direction
import coordinates.Point
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph
import java.io.File
import java.math.BigDecimal

fun main() {
    val maze = parseMaze()
    val solver = RecursiveMazeSolver(maze)
    println(solver.solve())
}

fun parseMaze(): Map<Point, ValidPoint> {
    val contents = File("/Users/justinpurrington/Downloads/day20.txt").readLines()
    val portals = parsePortals(contents).asSequence()
    val points = parsePoints(contents).asSequence()

    return (portals + points).map { it.key to it.value}.toMap()
}

fun showMaze(maze: Map<Point, ValidPoint>) {
    val maxX = maze.keys.maxBy { it.x }!!.x
    val maxY = maze.keys.maxBy { it.y }!!.y

    for(y in 0..maxY) {
        for(x in 0..maxX) {
            val point = Point(x, y)
            if(point in maze) {
                print(maze[point])
            } else {
                print(" ")
            }
        }
        println("")
    }
}

class MazeSolver(val maze: Map<Point, ValidPoint>) {
    fun validMoves(point: Point): List<Point> =
        Direction.all.filter{
            val destination = point.move(it)
            maze.containsKey(destination)
        }.map{
            val destination = point.move(it)
            maze[destination]!!.travelTo(point, destination)
        }

    private fun makeGraph(start: Point, end: Point): SimpleWeightedGraph<Point, DefaultWeightedEdge> {
        val graph = SimpleWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

        // Add Vertices
        maze.filter {!it.value.isPortal}.forEach { (point, _) ->
            graph.addVertex(point)
        }
        graph.addVertex(start)
        graph.addVertex(end)

        // Add Edges
        maze.filter {!it.value.isPortal}.forEach { (point, _) ->
            for(destination in validMoves(point)) {
                graph.addEdge(point, destination)
            }
        }

        for(destination in validMoves(start)) {
            graph.addEdge(start, destination)
        }

        for(destination in validMoves(end)) {
            graph.addEdge(end, destination)
        }

        return graph
    }

    fun solve(): Int {
        val start = Point(x=69, y=118)
        val end = Point(x=53, y=118)
        val graph = makeGraph(start, end)
        val shortestPath = DijkstraShortestPath.findPathBetween(graph, start, end)
        return shortestPath.length
    }
}

data class ThreeDPoint(val x: Int, val y: Int, val z: Int)  {
    fun move(direction: Direction): ThreeDPoint =
        ThreeDPoint(x + direction.unit_x, y + direction.unit_y, z)

    val xyProjection: Point get() = Point(x, y)
}

class RecursiveMazeSolver(val maze: Map<Point, ValidPoint>) {
    fun validMoves(point: ThreeDPoint): List<ThreeDPoint> =
        Direction.all.filter{
            val destination = point.move(it)
            maze.containsKey(destination.xyProjection)
        }.map{
            val destination = point.move(it)
            maze[destination.xyProjection]!!.travelTo(point, destination)
        }.filter{
            it.z >= 0 && it.z <= 100
        }

    private fun makeGraph(start: ThreeDPoint, end: ThreeDPoint): SimpleWeightedGraph<ThreeDPoint, DefaultWeightedEdge> {
        val levelsToAdd = 100
        val graph = SimpleWeightedGraph<ThreeDPoint, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

        // Add Vertices
        for(level in 0..levelsToAdd) {
            maze.filter { !it.value.isPortal }.forEach { (point, _) ->
                graph.addVertex(ThreeDPoint(point.x, point.y, level))
            }
        }
        graph.addVertex(start)
        graph.addVertex(end)

        // Add Edges
        for(level in 0..levelsToAdd) {
            maze.filter { !it.value.isPortal }.forEach { (point, _) ->
                val threeDPoint = ThreeDPoint(point.x, point.y, level)
                for (destination in validMoves(threeDPoint)) {
                    graph.addEdge(threeDPoint, destination)
                }
            }
        }

        for(destination in validMoves(start)) {
            graph.addEdge(start, destination)
        }

        for(destination in validMoves(end)) {
            graph.addEdge(end, destination)
        }

        return graph
    }

    fun solve(): Int {
        val start = ThreeDPoint(x=69, y=118, z=0)
        val end = ThreeDPoint(x=53, y=118, z=0)
        val graph = makeGraph(start, end)
        val shortestPath = DijkstraShortestPath.findPathBetween(graph, start, end)
        return shortestPath.length
    }
}



fun testGraphing() {
    println("Day20")
    val graph = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

    val portsmouth = graph.addVertex("London")
    graph.addVertex("Portsmouth")
    graph.addVertex("Manchester")
    graph.addVertex("Liverpool")
    graph.addVertex("Birmingham")

    val edgeLp = graph.addEdge("London", "Portsmouth")


    val edgeLb = graph.addEdge("London", "Birmingham")
    graph.addEdge("Birmingham", "Manchester")
    graph.addEdge("Birmingham", "Liverpool")
    graph.addEdge("Manchester", "Liverpool")

    println(graph.getEdgeWeight(edgeLb))
    graph.setEdgeWeight(edgeLp, 10.0);
    println(graph.getEdgeWeight(edgeLp))

    val path = DijkstraShortestPath(graph)
    println(DijkstraShortestPath.findPathBetween(graph, "Portsmouth", "Liverpool"))
}

data class Portal(val name: String,  val pointA: Point, val pointB: Point)

data class ValidPoint(val portal: Portal? = null) {
    fun travelTo(fromPoint: Point, toPoint: Point): Point =
        if(portal != null) {
            if (fromPoint == portal.pointA) portal.pointB else portal.pointA
        } else {
            toPoint
        }

    fun travelTo(fromPoint: ThreeDPoint, toPoint: ThreeDPoint): ThreeDPoint {
        return if (portal != null) {
            val xyDest = if (fromPoint.xyProjection == portal.pointA) portal.pointB else portal.pointA

            // Are we going up or down
            ThreeDPoint(xyDest.x, xyDest.y, fromPoint.z + zChange(fromPoint))
        } else {
            toPoint
        }
    }

    val isPortal: Boolean get() = portal != null

    override fun toString(): String =
        portal?.name?.substring(0..0) ?: "."

    companion object {
        fun zChange(fromPoint: ThreeDPoint): Int =
            if(
                fromPoint.x == 2 ||
                fromPoint.x == 112 ||
                fromPoint.y == 2 ||
                fromPoint.y == 118
            ) -1
            else 1
    }
}

data class PortalMapping(
    val xRange: IntRange,
    val yRange: IntRange,
    val letterDirection: Direction,
    val useFirstPosition: Boolean,
    val connectionDirection: Direction
)

fun parsePoints(contents: List<String>): Map<Point, ValidPoint> {
    val maze = mutableMapOf<Point, ValidPoint>()
    for((y, line) in contents.withIndex()) {
        for((x, char) in line.withIndex()) {
            if(char == '.') {
                maze[Point(x, y)] = ValidPoint()
            }
        }
    }

    return maze.toMap()
}

fun parsePortals(contents: List<String>): Map<Point, ValidPoint> {
    val portalChars = mutableMapOf<Point, Char>()
    for((y, line) in contents.withIndex()) {
        for((x, char) in line.withIndex()) {
            if(char in 'A'..'Z')
                portalChars[Point(x, y)] = char
        }
    }

    val mappings = listOf(
        PortalMapping(
            xRange = 0..0, yRange = 0..119,
            letterDirection = Direction.RIGHT, useFirstPosition = false,
            connectionDirection = Direction.RIGHT
        ),
        PortalMapping(
            xRange = 31..31, yRange = 20..90,
            letterDirection = Direction.RIGHT, useFirstPosition = true,
            connectionDirection = Direction.LEFT
        ),
        PortalMapping(
            xRange = 82..82, yRange = 20..90,
            letterDirection = Direction.RIGHT, useFirstPosition = false,
            connectionDirection = Direction.RIGHT
        ),
        PortalMapping(
            xRange = 113..113, yRange = 0..119,
            letterDirection = Direction.RIGHT, useFirstPosition = true,
            connectionDirection = Direction.LEFT
        ),
        PortalMapping(
            xRange = 0..113, yRange = 0..0,
            letterDirection = Direction.DOWN, useFirstPosition = false,
            connectionDirection = Direction.DOWN
        ),
        PortalMapping(
            xRange = 20..90, yRange = 31..31,
            letterDirection = Direction.DOWN, useFirstPosition = true,
            connectionDirection = Direction.UP
        ),
        PortalMapping(
            xRange = 20..90, yRange = 88..88,
            letterDirection = Direction.DOWN, useFirstPosition = false,
            connectionDirection = Direction.DOWN

        ),
        PortalMapping(
            xRange = 0..113, yRange = 119..119,
            letterDirection = Direction.DOWN, useFirstPosition = true,
            connectionDirection = Direction.UP
        )
    )

    val portalPositions = mappings.map {mapping ->
        portalChars.filter {
            it.key.x in mapping.xRange && it.key.y in mapping.yRange
        }.map {
            val secondLetterPosition = it.key.move(mapping.letterDirection)
            val secondLetter = portalChars[secondLetterPosition]!!
            val label = "${it.value}$secondLetter"
            val position = if(mapping.useFirstPosition) it.key else secondLetterPosition
            val connectionPoint = position.move(mapping.connectionDirection)
            Triple(label, position, connectionPoint)
        }
    }.flatten()

    println(portalPositions)

    val portalMap = mutableMapOf<String, MutableList<Pair<Point, Point>>>()

    for(position in portalPositions) {
        val locations = Pair(position.second, position.third)
        if(portalMap.containsKey(position.first)) {
            portalMap[position.first]!!.add(locations)
        } else {
            portalMap[position.first] = mutableListOf(locations)
        }
    }

    println("Start: ${portalMap["AA"]!![0].second}")
    println("End: ${portalMap["ZZ"]!![0].second}")

    return portalMap.filter {it.key !in listOf("AA", "ZZ")}.map {
        val portal = Portal(it.key, it.value[0]!!.second, it.value[1]!!.second)
        listOf(
            Pair(it.value[0]!!.first, portal),
            Pair(it.value[1]!!.first, portal)
        )
    }.flatten().map {it.first to ValidPoint(it.second)}.toMap()
}

