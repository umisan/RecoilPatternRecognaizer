package image

import entity.Point
import entity.Rectangle
import kotlin.math.max
import kotlin.math.min

class GridBreadthFirstSearcher constructor(val height: Int, val width: Int) {
    val AVAILABLE_NODE = 0
    val NOT_AVAILABLE_NODE = 1

    val grid = Array(height) {Array(width) {AVAILABLE_NODE} }

    private val visitedFlags =  Array(height) {Array(width) {false} }

    fun markNodeAsNotAvailable(x: Int, y:Int ) {
        grid[y][x] = NOT_AVAILABLE_NODE
    }

    fun startAt(x: Int, y: Int): Rectangle? {
        if (visitedFlags[y][x] || grid[y][x] == NOT_AVAILABLE_NODE) {
            return null
        }
        visitedFlags[y][x] = true
        val queue = ArrayDeque<Point>()
        queue.add(Point(x, y))
        var maxX = -1
        var minX = x
        var maxY = -1
        var minY = y
        while (queue.isNotEmpty()) {
            val top = queue.removeFirst()
            //左
            if (top.x - 1 > 0 && !visitedFlags[top.y][top.x - 1] && grid[top.y][top.x - 1] == AVAILABLE_NODE) {
                queue.add(Point(top.x - 1, top.y))
                visitedFlags[top.y][top.x - 1] = true
                minX = min(minX, top.x - 1)
            }
            //右
            if (top.x + 1 < width && !visitedFlags[top.y][top.x + 1] && grid[top.y][top.x + 1] == AVAILABLE_NODE) {
                queue.add(Point(top.x + 1, top.y))
                visitedFlags[top.y][top.x + 1] = true
                maxX = max(maxX, top.x + 1)
            }
            //上
            if (top.y - 1 > 0 && !visitedFlags[top.y - 1][top.x] && grid[top.y - 1][top.x] == AVAILABLE_NODE) {
                queue.add(Point(top.x, top.y - 1))
                visitedFlags[top.y - 1][top.x] = true
                minY = min(minY, top.y - 1)
            }
            //下
            if (top.y + 1 < height && !visitedFlags[top.y + 1][top.x] && grid[top.y + 1][top.x] == AVAILABLE_NODE) {
                queue.add(Point(top.x, top.y + 1))
                visitedFlags[top.y + 1][top.x] = true
                maxY = max(maxY, top.y + 1)
            }
        }
        return Rectangle(Point(minX, minY), Point(maxX, maxY))
    }
}