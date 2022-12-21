package day15

import kotlin.concurrent.thread
import kotlin.math.abs


val sensors = mutableListOf<Sensor>()

fun main() {
    println("Day 15")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day15/input.txt").readLines()

    buildSenorsAndBeacons(lines)
    println("Sensors: $sensors")

    //println("Part 1, number of points ${part1(2000000).size}")

    println("Part 2, tuning frequency ${part2(4000000)}")
    //println("Part 2, tuning frequency ${part2(20)}")
}


fun part1(y:Int):Set<Point> {
    val points = mutableSetOf<Point>()
    sensors.forEach {s ->
        points.addAll(s.pointsOnLine(y))
    }
    sensors.forEach { s ->
        points.remove(s.point)
        points.remove(s.beacon)
    }
    return points
}

fun getXsOnRow(y:Int):List<Pair<Int, Int>> {
    val xs = mutableListOf<Pair<Int, Int>>()
    sensors.forEach {s ->
        val range = s.getXsOnLine(y)
        if (range.second >= 0) {
            xs.add(range)
        }
    }
    return xs.sortedWith { r1, r2 -> r1.first.compareTo(r2.first) }
}

fun part2(ss:Int):Long {

    for (row in 0..ss) {
        val startTime = System.currentTimeMillis()
        val xs = getXsOnRow(row)
        //println("Sorted row $row $xs")
        val gapX = checkForGap(xs, ss)
        if (gapX > -1) {
            println("Found ($gapX, $row)")
            return gapX * 4000000L + row
        }
        /*
        for (col in 0..ss) {
            if (!xs.contains(col)) {
                println("Found ($col, $row)")
                return col * 4000000L + row
            }
        }*/
        val time = (System.currentTimeMillis() - startTime) / 1000
        println("Searched $row (${((row * 1.0)/ss)*100}%) in $time sec")

    }
    return -1
}

fun checkForGap(xs:List<Pair<Int, Int>>, ss:Int):Int {
    if (xs.isEmpty()) {
        return -1
    }
    val first = xs.first()
    if (first.first > 0) {
        println("First range is after zero: $first")
        return -1
    }
    // at this point first.first <= 0 and first.second > 0
    var curX = first.second
    for (i in 1 until xs.size) {
        val curRange = xs[i]
        val nextX = curX + 1
        if (nextX < curRange.first) {
            println("Found gap $nextX")
            return nextX
        }
        if (curRange.second > curX) {
            curX = curRange.second
        }
    }

    return -1
}

fun buildSenorsAndBeacons(lines:List<String>) {
    lines.forEach {line ->
        val tokens = line.split(": closest beacon is at x=")
        val sensor = tokens[0].substring(12).split(", y=")
        val beacon = tokens[1].split(", y=")
        sensors.add(Sensor(Point(sensor[0].toInt(), sensor[1].toInt()), Point(beacon[0].toInt(), beacon[1].toInt())))
    }
}

data class Sensor(val point:Point, val beacon:Point) {


    val slope = abs(beacon.x - point.x) + abs(beacon.y - point.y)

    fun pointsOnLine(y:Int):Set<Point> {
        val points = mutableSetOf<Point>()
        if (doesLineIntersect(y)) {
            val yExtent = abs(y - point.y)
            val xExtent = abs(slope - yExtent)
            val minX = point.x - xExtent
            val maxX = point.x + xExtent
            //println("xExtent $xExtent, yExtent $yExtent, MinX $minX, MaxX $maxX $this")
            for (x in minX..maxX) {
                //println("Adding point $x, $y for $this")
                points.add(Point(x, y))
            }
        }
        return points
    }

    fun getXsOnLine(y:Int):Pair<Int, Int> {
        if (doesLineIntersect(y)) {
            val yExtent = abs(y - point.y)
            val xExtent = abs(slope - yExtent)
            val minX = point.x - xExtent
            val maxX = point.x + xExtent
            //println("Row $y: xExtent $xExtent, yExtent $yExtent, MinX $minX, MaxX $maxX $this")
            return Pair(minX, maxX)
        }
        return Pair(-1, -1)
    }

    private fun doesLineIntersect(y:Int):Boolean =
        y <= point.y + slope && y >= point.y - slope

    override fun toString(): String =
        "Sensor([${point.x}, ${point.y}], b=[${beacon.x}, ${beacon.y}], slope=${slope})"
}

data class Point(val x:Int, val y:Int)