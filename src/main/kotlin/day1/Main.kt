package day1

import java.io.File


fun main(args:Array<String>) {
    println("Day 7")

    val lines = File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day1/input.txt").readLines()

    part1(lines)
}

fun part1(lines: List<String>) {
    val sums = mutableListOf<Int>()

    var curSum = 0
    lines.forEach { s ->
        if (s == "") {
            sums.add(curSum)
            curSum = 0
        } else {
            curSum += s.toInt()
        }
    }
    sums.add(curSum)

    println("Part 1 " + sums.max())

    part2(sums)
}

fun part2(sums: List<Int>) {
    val topThree = sums.sorted().reversed().take(3).sum()

    println("Part 2 $topThree")
}