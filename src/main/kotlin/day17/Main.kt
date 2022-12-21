package day17

var jetIndex = 0
var curHeight = 4L
val pieces = buildPieces()
val grid = initGrid()
val jets = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day17/sample.txt").readLines().first()
val lcm = jets.length * 5

fun main() {
    println("Day 17")

    //println("jets size ${jets.length}")
    val part1 = part1()
    println("Part 1: $part1, half = ${part1 / 1}")
}

fun part1():Long {
    var curIndex = 0
    var numPieces = 0L
    var curPiece = pieces[curIndex]
    //repeat(4) {
    println("lcm $lcm")
    while (numPieces < 10 * lcm) {
        if (!tick(curPiece)) {
            //println("Coming to a rest: #$numPieces")
            curPiece.rest()
            //println("Grid is now $grid")
            numPieces++
            curIndex = nextPieceIndex(curIndex)
            curPiece = pieces[curIndex]
            curPiece.resetToHeight()
            if (numPieces % lcm == 0L) {
                println("Run ${numPieces / lcm} is ${getGridHeight()} high")
            }
        }
        //printGrid(curPiece)

    }
    println("Dropped $numPieces pieces")
    //printGrid(curPiece)
    return getGridHeight()
}

fun pushPiece(piece:Piece):Boolean {
    val ret = when (jets[jetIndex]) {
        '<' -> piece.left()
        else -> piece.right()
    }
    jetIndex = nextJetIndex(jetIndex)

    return ret
}

fun tick(piece:Piece): Boolean {
    pushPiece(piece)

    return piece.drop()
}

fun nextPieceIndex(cur:Int):Int = if (cur + 1 < pieces.size) cur + 1 else 0

fun nextJetIndex(cur:Int):Int = if (cur + 1 < jets.length) cur + 1 else 0

fun buildPieces():List<Piece> {
    val straight = Piece(mutableListOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)))
    val cross = Piece(mutableListOf(Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2)))
    val el = Piece(mutableListOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2)))
    val vertical = Piece(mutableListOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3)))
    val square = Piece(mutableListOf(Point(0, 0), Point(0, 1), Point(1, 0), Point(1, 1)))

    return listOf(straight, cross, el, vertical, square)
}

fun initGrid():MutableSet<Point> {
    val g = mutableSetOf<Point>()
    for (x in 0 until 7) {
        g.add(Point(x.toLong(), 0L))
    }
    return g
}

fun printGrid(curPiece:Piece) {
    println("Grid is ${getGridHeight()} units high")
    val height = getGridHeight() + 7
    val width = 7L
    var str = ""
    for (row in height downTo 0) {
        val r = if (row < 10) "$row " else "$row"
        str += "$r |"
        for (col in 0L until width) {
            val curPoint = Point(col, row)
            str += if (curPiece.contains(curPoint)) {
                "@"
            }
            else if (grid.contains(curPoint)) {
                "#"
            } else {
                "."
            }
        }
        str += "|\n"
    }

    println(str)
}

fun getGridHeight():Long {
    return grid.maxByOrNull { it.y }?.y ?: 0L
}

data class Piece(val parts:MutableList<Point>) {

    private val coordinates = mutableListOf<Point>()

    init {
        resetToHeight()
    }

    fun contains(p:Point) = coordinates.contains(p)

    fun resetToHeight() {
        val list = parts.map{ Point(it.x + 2, it.y + curHeight)}
        reset(list)
        //println("Reset Height to $curHeight $coordinates")
    }

    private fun reset(list:List<Point>) {
        //println("reset to $list")
        coordinates.clear()
        coordinates.addAll(list)
    }

    fun drop():Boolean {
        //println("----- Drop ------")
        //println("Grid $grid")
        for (p in coordinates) {
            if (grid.contains(Point(p.x, p.y - 1))) {
                //println("Can't drop any further $coordinates")
                return false
            }
        }
        val copy = coordinates.map {p -> Point(p.x, p.y - 1)}
        reset(copy)
        //println("Dropped to $coordinates")
        return true
    }

    fun rest() {
        grid.addAll(coordinates)
        curHeight = getGridHeight() + 4L
    }

    fun left():Boolean {
        for (p in coordinates) {
            if (p.x == 0L || grid.contains(Point(p.x - 1, p.y))) return false
        }
        val copy = coordinates.map {p -> Point(p.x - 1, p.y)}
        reset(copy)

        return true
    }

    fun right():Boolean {
        for (p in coordinates) {
            if (p.x == 6L || grid.contains(Point(p.x + 1, p.y))) return false
        }
        val copy = coordinates.map {p -> Point(p.x + 1, p.y)}
        reset(copy)

        return true
    }

}

data class Point(val x:Long, val y:Long)