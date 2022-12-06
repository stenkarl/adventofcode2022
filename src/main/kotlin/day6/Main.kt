package day6

import java.io.File


fun main(args:Array<String>) {
    println("Day 6")

    val line = File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day6/input.txt").readLines().first()

    println(line)
    println("Part 1 " + checkForMarker(line, 4))
    println("Part 2 " + checkForMarker(line, 14))

}

fun checkForMarker(str:String, bufferSize:Int): Int {
    val buffer = mutableListOf<Char>()

    str.forEachIndexed { i, c ->
        if (buffer.size == bufferSize) {
            buffer.removeFirst()
        }
        buffer.add(c)
        if (buffer.size == bufferSize && !hasDups(buffer)) {
            println(buffer)
            return i + 1
        }
    }


    return 0
}

fun hasDups(buffer:List<Char>): Boolean {
    buffer.forEachIndexed { i, c ->
        if (checkForChar(buffer, c, i)) {
            return true
        }
    }
    return false
}

fun checkForChar(buffer:List<Char>, checkFor:Char, exceptIndex:Int): Boolean {
    buffer.forEachIndexed { i, c ->
        if (exceptIndex != i && c == checkFor) {
            return true
        }
    }
    return false
}