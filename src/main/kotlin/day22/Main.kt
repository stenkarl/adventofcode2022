package day22

import day22.Direction.*

const val file = "sample2"

val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day22/$file.txt").readLines()
val grid = lines.subList(0, lines.size - 2)
val route = buildRoute(lines.last())

enum class Direction {
    RIGHT, DOWN, LEFT, UP
}
var curDirection = RIGHT
var curPosition = findStart()

fun main() {
    println("Day 22")

    println(route)
    println(grid)
    println(curPosition)

    println("Part 1, ${part1()}")
}

fun part1(): Int {

    route.forEach {
        it.eval()
    }
    println(curPosition)
    return 1000 * (curPosition.y + 1) + 4 * (curPosition.x + 1) + curDirection.ordinal
}

fun findStart():Position {
    val x = grid[0].indexOfFirst { it == '.' }

    return Position(x, 0)
}

fun buildRoute(str:String):List<Instruction> {
    val moves = mutableListOf<Instruction>()
    var idx = 0
    while (idx < str.length) {
        if (str[idx].isDigit()) {
            val sz = if (idx + 1 < str.length && str[idx + 1].isDigit()) 2 else 1
            moves.add(Move(str.substring(idx, idx + sz).toInt()))
            idx += sz
        } else {
            moves.add(Turn(str[idx]))
            idx++
        }
    }

    return moves
}

fun turnRight() {
    curDirection = when (curDirection) {
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        else -> RIGHT
    }
    println("Turn Right to $curDirection")
}

fun turnLeft() {
    curDirection = when (curDirection) {
        RIGHT -> UP
        UP -> LEFT
        LEFT -> DOWN
        else -> RIGHT
    }
    println("Turn Left to $curDirection")
}

fun moveRight(steps:Int) {
    println("Right $steps")
    val row = grid[curPosition.y]
    for (s in 0 until steps) {
        if (curPosition.x + 1 < row.length) {
            if (row[curPosition.x + 1] == '#') {
                println("Blocked moving RIGHT at x=${curPosition.x + 1}, y=${curPosition.y}")
                return
            } else {
                curPosition.x += 1
            }
        } else {
            val lastSpaceOrZero = row.lastIndexOf(" ") + 1
            if (row[lastSpaceOrZero] != '#') {
                curPosition.x = lastSpaceOrZero
                println("Wrapped from right to left $curPosition")
            } else {
                println("Blocked from wrapping right to left x=$lastSpaceOrZero, y=${curPosition.y}")
                return
            }
        }
    }
}

fun moveLeft(steps:Int) {
    println("Left $steps")
    val row = grid[curPosition.y]
    for (s in 0 until steps) {
        var shouldWrap = false
        if (curPosition.x > 0) {
            if (row[curPosition.x - 1] == '#') {
                println("Blocked moving LEFT at x=${curPosition.x - 1}, y=${curPosition.y}")
                return
            } else if (row[curPosition.x - 1] == ' ') {
                shouldWrap = true
            } else {
                curPosition.x -= 1
            }
        }
        if (curPosition.x == 0 || shouldWrap) {
            if (row.last() != '#') {
                curPosition.x = row.length - 1
                println("Wrapped from the left to the right $curPosition")
            } else {
                println("Blocked from wrapping from left to right x=${row.length - 1}, y=${curPosition.y}")
                return
            }
        }
    }
}

fun moveUp(steps:Int) {
    println("Up $steps")
    for (s in 0 until steps) {
        var shouldWrap = false
        if (curPosition.y > 0) {
            val col = grid[curPosition.y - 1]
            if (col.length > curPosition.x) {
                if (col[curPosition.x] == '#') {
                    println("Blocked moving UP at x=${curPosition.x}, y=${curPosition.y - 1}")
                    return
                } else if (col[curPosition.x] == '.') {
                    curPosition.y -= 1
                    continue
                } else if (col[curPosition.x] == ' ') {
                    shouldWrap = true
                }
            }
        }
        if (curPosition.y == 0 || shouldWrap) { // wrapped around to the bottom
            println("Wrapping from top to bottom at $curPosition")
            var lastInBlock = false
            var idx = curPosition.y + 1 // search down
            while (!lastInBlock) {
                if (idx == grid.size || curPosition.x > grid[idx].length || grid[idx][curPosition.x] == ' ') {
                    lastInBlock = true
                    if (grid[idx - 1][curPosition.x] != '#') { // the bottom is not blocked from wrapping
                        curPosition.y = idx - 1
                        println("Wrapped to bottom at $curPosition")
                    } else {
                        println("Blocked at the bottom from wrapping")
                        return
                    }
                }
                idx++
            }
        }
    }
}

fun moveDown(steps:Int) {
    println("Down $steps")
    for (s in 0 until steps) {
        var shouldWrap = false
        if (curPosition.y < grid.size - 1) {
            val col = grid[curPosition.y + 1]
            if (col.length > curPosition.x) {
                if (col[curPosition.x] == '#') {
                    println("Blocked moving DOWN at x=${curPosition.x}, y=${curPosition.y + 1}")
                    return
                } else if (col[curPosition.x] == '.') {
                    curPosition.y++
                    continue
                } else if (col[curPosition.x] == ' ') {
                    shouldWrap = true
                }
            }
        }
        if (curPosition.y == grid.size - 1 || shouldWrap) { // wrapped to the top
            println("Attempting to Wrap from bottom to top at $curPosition")
            var firstInBlock = false
            var idx = curPosition.y - 1  // search up
            while (!firstInBlock) {
                if (idx < 0 || curPosition.x > grid[idx].length || grid[idx][curPosition.x] == ' ') {
                    firstInBlock = true
                    if (grid[idx + 1][curPosition.x] != '#') { // the top is not blocked from wrapping
                        curPosition.y = idx + 1
                        println("Wrapped to top at $curPosition")
                    } else {
                        println("Blocked at the top from wrapping")
                        return
                    }
                }
                idx--
            }
        }
    }

}

abstract class Instruction() {
    abstract fun eval()
}

class Move(private val steps:Int) : Instruction() {

    override fun eval() {
        when (curDirection) {
            RIGHT -> moveRight(steps)
            LEFT -> moveLeft(steps)
            UP -> moveUp(steps)
            else -> moveDown(steps)
        }
        println(curPosition)
    }

    override fun toString(): String = "Move($steps)"

}

class Turn(val dir:Char) : Instruction() {
    override fun eval() {
        when (dir) {
            'L' -> turnLeft()
            else -> turnRight()
        }
    }

    override fun toString(): String = "Turn($dir)"

}

data class Position(var x:Int, var y:Int)