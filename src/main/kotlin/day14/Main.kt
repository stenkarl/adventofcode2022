package day14


fun main() {
    println("Day 14")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day14/input.txt").readLines()

    println(lines)

    val grid = buildGrid(lines)

    println(grid.size)

    //println("Part 1 ${part1(grid)}")
    println("Part 2 ${part2(grid)}")

}

fun part1(grid:MutableSet<Point>):Int {
    var units = 0
    val abyss:Int = grid.maxByOrNull { it.y }!!.y + 1
    println("Abyss is $abyss")
    while (dropGrain(grid, abyss)) {
        units++
    }
    return units
}

fun part2(grid:MutableSet<Point>):Int {
    var units = 0
    val floor:Int = grid.maxByOrNull { it.y }!!.y + 2
    println("Floor is $floor")
    while (dropGrainWithFloor(grid, floor)) {
        units++
    }
    return units + 1 // account for the origin
}

fun dropGrainWithFloor(grid:MutableSet<Point>, floor:Int):Boolean {
    val origin = Point(500, 0)

    var prev = origin
    var blocked = false
    while (!blocked) {
        val cur = tick(prev, grid)
        if (cur == origin) { // couldn't move -- at top

            return false
        }
        if (cur == prev || cur.y == floor - 1) {
            blocked = true
            grid.add(cur)
        }
        prev = cur
    }
    return true
}

fun dropGrain(grid:MutableSet<Point>, abyss:Int):Boolean {
    val origin = Point(500, 0)

    var prev = origin
    var blocked = false
    while (!blocked) {
        val cur = tick(prev, grid)
        if (cur == prev) {
            blocked = true
            grid.add(cur)
        }
        if (cur.y >= abyss) {
            println("Into the abyss...")
            return false
        }
        prev = cur
    }
    return true
}

fun tick(point:Point, grid:MutableSet<Point>):Point =
    if (!grid.contains(Point(point.x, point.y + 1))) { // down
        Point(point.x, point.y + 1)
    } else if (!grid.contains(Point(point.x - 1, point.y + 1))) {  // down left
        Point(point.x - 1, point.y + 1)
    } else if (!grid.contains(Point(point.x + 1, point.y + 1))) { // down right
        Point(point.x + 1, point.y + 1)
    } else { // blocked
        point
    }

fun buildGrid(lines:List<String>):MutableSet<Point> {
    val grid = mutableSetOf<Point>()

    lines.forEach {line ->
        val tokens = line.split(" -> ")

        val points = tokens.map {t ->
            val p = t.split(",")
            Point(p[0].toInt(), p[1].toInt())
        }

        fillGrid(grid, points)
    }

    return grid
}

fun fillGrid(grid:MutableSet<Point>, points:List<Point>) {
    //println("fillGrid $points")
    var prev = points[0]
    for (i in 1 until points.size) {
        val cur = points[i]
        if (cur.x == prev.x) { // vertical line
            val largerY = prev.y.coerceAtLeast(cur.y)
            val smallerY = prev.y.coerceAtMost(cur.y)
            for (y in smallerY..largerY) {
                addPoint(grid, Point(cur.x, y))
            }
        } else { // horizontal line
            val largerX = prev.x.coerceAtLeast(cur.x)
            val smallerX = prev.x.coerceAtMost(cur.x)
            for (x in smallerX..largerX) {
                addPoint(grid, Point(x, cur.y))
            }
        }
        prev = cur
    }
}

fun addPoint(grid:MutableSet<Point>, point:Point) {
    //println("Adding $point")
    grid.add(point)
}

data class Point(val x:Int, val y:Int)