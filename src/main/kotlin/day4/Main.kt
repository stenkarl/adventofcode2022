package day4

import java.io.File


fun main(args:Array<String>) {
    println("Day 4")

    val lines = File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day4/input.txt").readLines()

    println("Part 1: " + part1(lines))
    println("Part 2: " + part2(lines))

}

fun part1(lines:List<String>): Int {
    var count = 0
    lines.forEach {s ->
        val tokens = s.split(",")
        if (contains(stringToPair(tokens[0]), stringToPair(tokens[1]))) {
            count++
        }
    }

    return count
}

fun part2(lines:List<String>): Int {
    var count = 0
    lines.forEach {s ->
        val tokens = s.split(",")
        if (overlaps(stringToPair(tokens[0]), stringToPair(tokens[1]))) {
            count++
        }
    }

    return count
}

fun stringToPair(str:String): IntRange {
    val tokens = str.split("-")

    return IntRange(tokens[0].toInt(), tokens[1].toInt())
}

fun contains(first:IntRange, second:IntRange): Boolean =
    (first.contains(second.first) && first.contains(second.last)) ||
            (second.contains(first.first) && second.contains(first.last))

fun overlaps(first:IntRange, second:IntRange): Boolean =
    (second.first <= first.last && second.first >= first.first) ||
            (first.first <= second.last && first.first >= second.first)
