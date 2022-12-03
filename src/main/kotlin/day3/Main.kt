package day3

import java.io.File


fun main(args:Array<String>) {
    println("Day 3")

    val lines = File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day3/input.txt").readLines()

    println("Part 1 " + part1(lines))
    println("Part 2 " + part2(lines))
}

fun part1(lines:List<String>):Int {

    return lines.sumOf { p -> priority(p) }
}

fun part2(lines:List<String>):Int {
    val numBlocks = lines.size / 3
    var sum = 0
    for (i in 0 until numBlocks) {
        val j = i * 3
        val first = lines[0 + j].toSet()
        val second = lines[1 + j].toSet()
        val third = lines[2 + j].toSet()
        val intersection = first.intersect(second.intersect(third)).first()

        sum += codeToPriority(intersection)
    }

    return sum
}

fun priority(pack:String):Int {
    val first = pack.substring(0, pack.length / 2)
    val second = pack.substring(pack.length / 2)

    val intersection = first.toCharArray().intersect(second.toSet()).first()

    return codeToPriority(intersection)
}

fun codeToPriority(c:Char):Int =
    if (c.isUpperCase()) {
        c.code - (65 - 27)
    } else {
        c.code - (97 - 1)
    }

