package day18

const val file = "input"
val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day18/$file.txt").readLines()

val points:List<Point> = buildPoints(lines)

fun main() {
    println("Day 18")

    val points = buildPoints(lines)
    println(points)

    val surfaceArea = part1()
    println("Part 1, Surface Area: $surfaceArea")
}

fun buildPoints(lines:List<String>):List<Point> =
    lines.map {
        val tokens = it.split(",")

        Point(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt())
    }

fun part1():Int {
    var curArea = 0
    points.forEach { p ->
        curArea += getSurfaceArea(p)
    }
    return curArea
}

fun getSurfaceArea(p:Point):Int =
    getSide(p.x + 1, p.y, p.z) + getSide(p.x - 1, p.y, p.z) +
            getSide(p.x, p.y + 1, p.z) + getSide(p.x, p.y - 1, p.z) +
            getSide(p.x, p.y, p.z + 1) + getSide(p.x, p.y, p.z - 1)

fun getSide (x:Int, y:Int, z:Int):Int =
    if (points.contains(Point(x, y, z))) 0 else 1


fun part2():Int {
    return 0
}

data class Point(val x:Int, val y:Int, val z:Int)