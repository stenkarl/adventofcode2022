package day9

import kotlin.math.abs


val rope = mutableListOf<Pair<Int, Int>>()
val visited = mutableSetOf(Pair(0, 0))

fun main() {
    println("Day 9")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day9/input.txt").readLines()

    val cmds = lines.map { Pair(it[0], it.substring(2).toInt())}

    part(2, cmds)

    println("Part 1 " + visited.size)
    visited.clear()
    visited.add(Pair(0, 0))

    part(10, cmds)
    println("Part 2 " + visited.size)

}

fun part(numKnots:Int, cmds:List<Pair<Char, Int>>) {
    initRope(numKnots)
    for (cmd in cmds) {
        processCmd(cmd)
    }
}

fun initRope(numKnots:Int) {
    repeat(numKnots) {
        rope.add(Pair(0, 0))
    }
}

fun processCmd(cmd: Pair<Char, Int>) {
    when (cmd.first) {
        'U' -> {
            move(moveUp, cmd.second)
        }
        'D' -> {
            move(moveDown, cmd.second)
        }
        'L' -> {
            move(moveLeft, cmd.second)
        }
        else -> {
            move(moveRight, cmd.second)
        }
    }
}

fun move(dir:(Pair<Int, Int>) -> Pair<Int, Int>, num:Int) {
    repeat(num) {
        rope[0] = dir(rope[0])
        checkRope()
    }
}

fun checkRope() {
    for (i in 1 until rope.size) {
        rope[i] = checkKnot(rope[i - 1], rope[i])
    }
    visited.add(rope.last())
}

fun checkKnot(hd:Pair<Int, Int>, tl:Pair<Int, Int>):Pair<Int, Int> {
    var newPos = tl
    if (!isAdjacent(hd, tl)) {
        newPos = moveTail(hd, tl)
    }
    return newPos
}

fun isAdjacent(first:Pair<Int, Int>, second:Pair<Int, Int>):Boolean =
    abs(first.first - second.first) <= 1 && abs(first.second - second.second) <= 1


fun moveTail(hd:Pair<Int, Int>, tl:Pair<Int, Int>):Pair<Int, Int> {
    var t = tl
    if (hd.first > tl.first && hd.second == tl.second) { // right
        t = moveRight(t)
    } else if (hd.first < tl.first && hd.second == tl.second) { // left
        t = moveLeft(t)
    } else if (hd.first == tl.first && hd.second > tl.second) { // above
        t = moveUp(t)
    } else if (hd.first == tl.first && hd.second < tl.second) { // below
        t = moveDown(t)
    } else if (hd.first > tl.first && hd.second > tl.second) {  // 1st quadrant
        t = moveUp(t)
        t = moveRight(t)
    } else if (hd.first < tl.first && hd.second > tl.second) { // 2nd quadrant
        t = moveUp(t)
        t = moveLeft(t)
    } else if (hd.first < tl.first) { // 3rd quadrant
        t = moveDown(t)
        t = moveLeft(t)
    } else if (hd.first > tl.first) { // 4th quadrant
        t = moveDown(t)
        t = moveRight(t)
    }
    return t
}

val moveUp = fun (which:Pair<Int, Int>):Pair<Int, Int> = Pair(which.first, which.second + 1)
val moveDown = fun (which:Pair<Int, Int>):Pair<Int, Int> = Pair(which.first, which.second - 1)
val moveLeft = fun (which:Pair<Int, Int>):Pair<Int, Int> = Pair(which.first - 1, which.second)
val moveRight = fun (which:Pair<Int, Int>):Pair<Int, Int> = Pair(which.first + 1, which.second)