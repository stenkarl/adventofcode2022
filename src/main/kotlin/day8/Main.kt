package day8


fun main(args:Array<String>) {
    println("Day 8")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day8/input.txt").readLines()

    println(lines)
    //println(part1(Forest(lines)))
    println(part2(Forest(lines)))
}

fun part1(forest: Forest): Int {
    println(forest)
    var numVisible = forest.w * 2 + (forest.w - 2) * 2

    for (i in 1 until forest.w - 1) {
        for (j in 1 until forest.w - 1) {
            numVisible += if (forest.isVisible(i, j)) {
                1
            } else {
                0
            }
        }
    }

    return numVisible
}

fun part2(forest:Forest): Int {
    var max = 0
    for (i in 0 until forest.w) {
        for (j in 0 until forest.w) {
            val cur = forest.scenicScore(i, j)
            println("scenic score for $i, $j is $cur")
            if (cur > max) {
                max = cur
            }
        }
    }
    return max
}

class Forest(private val lines:List<String>) {

    val w = lines.size
    private val grid: Array<IntArray> = build()

    private fun build():Array<IntArray> {
        val g = Array(w) { intArrayOf(w) }

        for (i in 0 until w) {
            val row:IntArray = lines[i].toCharArray().map { it.toString().toInt()}.toIntArray()
            g[i] = row
        }

        return g
    }

    fun isVisible(row:Int, col:Int): Boolean {
        return checkNorth(row, col) || checkSouth(row, col) || checkEast(row, col) || checkWest(row, col)
    }

    fun scenicScore(row:Int, col:Int): Int {
        return northView(row, col) * southView(row, col) * westView(row, col) * eastView(row, col)
    }

    private fun checkNorth(row:Int, col:Int): Boolean {
        val h = grid[row][col]
        for (i in 0 until row) {
            if (grid[i][col] >= h) {
                return false
            }
        }
        println("checkNorth $h is visible ($row, $col)")
        return true
    }

    private fun checkSouth(row:Int, col:Int): Boolean {
        val h = grid[row][col]
        for (i in row + 1 until w) {
            if (grid[i][col] >= h) {
                return false
            }
        }
        println("checkSouth $h is visible ($row, $col)")
        return true
    }

    private fun checkEast(row:Int, col:Int): Boolean {
        val h = grid[row][col]
        for (i in col + 1 until w) {
            if (grid[row][i] >= h) {
                return false
            }
        }
        println("checkEast $h is visible ($row, $col)")
        return true
    }

    private fun checkWest(row:Int, col:Int): Boolean {
        val h = grid[row][col]
        for (i in 0 until col) {
            if (grid[row][i] >= h) {
                return false
            }
        }
        println("checkWest $h is visible ($row, $col)")
        return true
    }

    private fun northView(row:Int, col:Int): Int {
        val h = grid[row][col]
        var count = 0

        for (i in (row - 1)downTo 0) {
            count++
            if (grid[i][col] >= h) {
                break
            }
        }
        return count
    }

    private fun southView(row:Int, col:Int): Int {
        val h = grid[row][col]
        var count = 0

        for (i in row + 1 until w) {
            count++
            if (grid[i][col] >= h) {
                break
            }
        }
        return count
    }

    private fun westView(row:Int, col:Int): Int {
        val h = grid[row][col]
        var count = 0
        for (i in (col - 1) downTo 0) {
            count++
            if (grid[row][i] >= h) {
                break
            }
        }

        return count
    }

    private fun eastView(row:Int, col:Int): Int {
        val h = grid[row][col]
        var count = 0
        for (i in col + 1 until w) {
            count++
            if (grid[row][i] >= h) {
                break
            }
        }

        return count
    }

    override fun toString(): String {
        var str = ""
        for (i in 0 until w) {
            for (j in 0 until w) {
                str += grid[i][j]
            }
            if (i < w - 1) {
                str += "\n"
            }
        }
        return str
    }

}