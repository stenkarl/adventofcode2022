package day5

import java.io.File


fun main(args:Array<String>) {
    println("Day 5")

    val lines = File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day5/input.txt").readLines()

    //val part1 = part1(buildCommands(lines), buildInputStacks())
    //println("Part 1 $part1")

    val part2 = part2(buildCommands(lines), buildInputStacks())
    println("Part 2 $part2")
}

fun buildCommands(lines:List<String>): List<Triple<Int, Int, Int>> =
    lines.map {it ->
        val tokens = it.split(" ")
        Triple(tokens[1].toInt(), tokens[3].toInt() - 1, tokens[5].toInt() - 1)
    }

fun part1(cmds:List<Triple<Int, Int, Int>>, list:List<ArrayDeque<Char>>): String {
    println(list)

    //println(cmds)

    cmds.forEach { move(it.first, list[it.second], list[it.third]) }

    println(list)

    return String(list.map { it.last()}.toCharArray())
}

fun part2(cmds:List<Triple<Int, Int, Int>>, list:List<ArrayDeque<Char>>): String {
    println(list)

    //println(cmds)

    cmds.forEach { collectAndMove(it.first, list[it.second], list[it.third]) }

    println(list)

    return String(list.map { it.last()}.toCharArray())
}

fun move(num:Int, src:ArrayDeque<Char>, dest:ArrayDeque<Char>) {
    //println("move $num, $src, $dest")
    for (i in 1..num) {
        dest.addLast(src.removeLast())
    }
    //println("moved $src, $dest")
}

fun collectAndMove(num:Int, src:ArrayDeque<Char>, dest:ArrayDeque<Char>) {
    //println("move $num, $src, $dest")
    val list = mutableListOf<Char>()
    for (i in 1..num) {
        list.add(src.removeLast())
    }
    list.reverse()
    for (i in 0 until num) {
        dest.addLast(list[i])
    }
    //println("moved $src, $dest")
}

/*
    [D]
[N] [C]
[Z] [M] [P]
 1   2   3
 */
fun buildSampleStacks(): List<ArrayDeque<Char>> {
    val list = mutableListOf<ArrayDeque<Char>>()

    stackOf(list, 'N', 'Z')
    stackOf(list, 'D', 'C', 'M')
    stackOf(list, 'P')

    return list
}

/*
    [H]         [D]     [P]
[W] [B]         [C] [Z] [D]
[T] [J]     [T] [J] [D] [J]
[H] [Z]     [H] [H] [W] [S]     [M]
[P] [F] [R] [P] [Z] [F] [W]     [F]
[J] [V] [T] [N] [F] [G] [Z] [S] [S]
[C] [R] [P] [S] [V] [M] [V] [D] [Z]
[F] [G] [H] [Z] [N] [P] [M] [N] [D]
 1   2   3   4   5   6   7   8   9
 */
fun buildInputStacks(): List<ArrayDeque<Char>> {
    val list = mutableListOf<ArrayDeque<Char>>()

    stackOf(list,      'W', 'T', 'H', 'P', 'J', 'C', 'F')
    stackOf(list, 'H', 'B', 'J', 'Z', 'F', 'V', 'R', 'G')
    stackOf(list,                     'R', 'T', 'P', 'H')
    stackOf(list,           'T', 'H', 'P', 'N', 'S', 'Z')
    stackOf(list, 'D', 'C', 'J', 'H', 'Z', 'F', 'V', 'N')
    stackOf(list,      'Z', 'D', 'W', 'F', 'G', 'M', 'P')
    stackOf(list, 'P', 'D', 'J', 'S', 'W', 'Z', 'V', 'M')
    stackOf(list,                          'S', 'D', 'N')
    stackOf(list,                'M', 'F', 'S', 'Z', 'D')

    return list
}

fun stackOf(list:MutableList<ArrayDeque<Char>>, vararg chars:Char): Unit {
    list.add(ArrayDeque(chars.reversed()))
}