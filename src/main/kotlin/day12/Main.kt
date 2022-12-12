package day12


val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day12/input.txt").readLines()

val end = find('E')

var visited = mutableListOf<Pair<Int, Int>>()

fun main() {
    println("Day 12")

    //val shortest = part1(find('S'))

    //println("Part 1, shortest path is $shortest steps")

    println("Part 2, shortest path is ${part2()} steps")
}

fun find(which:Char): Pair<Int, Int> {
    for (i in 0 until lines.size) {
        for (j in 0 until lines[i].length) {
            if (lines[i][j] == which) {
                val p = Pair(j, i)
                println("Found $which at $p")
                return p
            }
        }
    }
    println("Couldn't find $which")

    return Pair(-1, -1)
}

fun part1(start:Pair<Int, Int>):Int {
    visited.clear()
    val queue = mutableListOf<Node>()
    queue.add(Node(start, 0, null))

    while (queue.isNotEmpty()) {
        val cur = queue.minByOrNull { it.steps }!!
        queue.remove(cur)
        visited.add(cur.point)
        //println("Min is $cur. Queue size is now ${queue.size}")

        if (cur.point == end) {
            println("Found End ${cur.getPath()}")
            return cur.steps
        }
        addNeighbors(cur, queue)
    }
    //println("Ran out of nodes before finding the end")

    return Int.MAX_VALUE
}

fun part2(): Int {
    val list = collectAs()
    println("Number of As ${list.size} $list")
    var min = Int.MAX_VALUE
    list.forEachIndexed{ i, p ->
        println("Checking $i, $p")
        val cur = part1(p)
        if (cur < min) {
            println("New min: $cur (was $min)")
            min = cur
        }
    }
    return min
}

fun collectAs():List<Pair<Int, Int>> {
    val list = mutableListOf<Pair<Int, Int>>()
    for (i in 0 until lines.size) {
        for (j in 0 until lines[i].length) {
            if (lines[i][j] == 'a') {
                list.add(Pair(j, i))
            }
        }
    }
    return list
}

fun addNeighbors(node:Node, queue:MutableList<Node>) {
    val p = node.point
    val ch = lines[p.second][p.first]
    val left = Pair(p.first - 1, p.second)
    if (left.first >= 0 && isEligible(left, ch)) {
        addIfAbsentOrBetter(Node(left, node.steps + 1, node), queue)
    } else {
        //println("Left $left isn't eligible")
    }
    val right = Pair(p.first + 1, p.second)
    if (right.first < lines[p.second].length && isEligible(right, ch)) {
        addIfAbsentOrBetter(Node(right, node.steps + 1, node), queue)
    } else {
        //println("Right $right isn't eligible")
    }
    val up = Pair(p.first, p.second - 1)
    if (up.second >= 0 && isEligible(up, ch)) {
        addIfAbsentOrBetter(Node(up, node.steps + 1, node), queue)
    } else {
        //println("Up $up isn't eligible")

    }
    val down = Pair(p.first, p.second + 1)
    if (down.second < lines.size && isEligible(down, ch)) {
        addIfAbsentOrBetter(Node(down, node.steps + 1, node), queue)
    } else {
        //println("Down $down isn't eligible")
    }
}

fun addIfAbsentOrBetter(n:Node, queue:MutableList<Node>) {
    if (visited.contains(n.point)) {
        return
    }
    val existing = queue.find { it.point == n.point }
    if (existing == null || existing.steps > n.steps) {
        //println("Adding $n")
        queue.remove(existing)
        queue.add(n)
    }
}

fun isEligible(p:Pair<Int, Int>, ch:Char):Boolean =
    ch == 'S' || (ch == 'z' && lines[p.second][p.first] == 'E') || (lines[p.second][p.first] != 'E' && lines[p.second][p.first] <= (ch + 1))

data class Node(val point:Pair<Int, Int>, val steps:Int, val prev:Node?) {

    fun getPath():String {
        var str = ""
        if (prev != null) {
            str += prev.getPath()
        }
        return str + point.toString()
    }
}