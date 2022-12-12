package day10


var cycle = 1
var x = 1
var sumSignalStrength = 0

val frameBuffer = mutableListOf<Char>()

// RBPARAGF

fun main() {
    println("Day 10")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day10/input.txt").readLines()

    val part1 = part1(buildCommands(lines))
    println("Part 1 $part1")

    part2(buildCommands(lines))
}

fun buildCommands(lines:List<String>): List<Command> =
    lines.map { it ->
        val tokens = it.split(" ")
        when (tokens[0]) {
            "addx" -> Addx(tokens[1].toInt())

            else -> Noop()
        }
    }

fun part1(cmds: List<Command>): Int {
    cmds.forEach { it.execute() }
    return sumSignalStrength
}

fun tick() {
    duringCycle()
    cycle++
}

fun duringCycle() {
    //println("$cycle $x")
    drawPixel()
    if (cycle == 20 || (cycle - 20) % 40 == 0) {
        val signalStrength = cycle * x
        sumSignalStrength += signalStrength
        //println("cycle $cycle, x $x, ss $signalStrength, sss, $sumSignalStrength")
    }
}

fun drawPixel() {
    val framePos = (cycle - 1) % 40
    val ch = if (framePos == x - 1 || framePos == x || framePos == x + 1) {
        '#'
    } else {
        '.'
    }
    //println("c $cycle, x $x, fp $framePos, $ch")
    frameBuffer.add(ch)
    printDebug(framePos)
}

fun printDebug(framePos:Int) {
    println("During cycle  $cycle: CRT draws pixel in position $framePos")
    println("Current CRT row: $frameBuffer")
    println()
}

fun printFrameBuffer() {
    var str = ""
    frameBuffer.forEachIndexed { index, ch ->
        if (index % 40 == 0) {
            str += "\n"
        }
        str += ch
    }

    println(str)
}

fun part2(cmds: List<Command>) {
    cmds.forEach { it.execute() }
    printFrameBuffer()
    println(frameBuffer)
}

interface Command {

    fun execute()
}

class Noop : Command {

    override fun execute() {
        //println("$cycle: noop")
        tick()
    }

}

class Addx(private val amt:Int) : Command {

    override fun execute() {
        //println("$cycle: addx $amt")
        println("Start cycle   $cycle: begin executing addx $amt")
        tick()
        tick()
        x += amt
        println("End of cycle  $cycle: finish executing addx $amt (Register X is now $x)")

    }
}