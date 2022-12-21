package day20

const val file = "sample"


fun main() {
    println("Day 20")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day20/$file.txt").readLines()

    val list = lines.map { it.toInt()}

    part1(list)
}

fun part1(list:List<Int>):Int {
    val unencrypted = list.toMutableList()
    println(unencrypted)
    list.forEach { num: Int ->
        move(unencrypted, num)
    }

    return 0
}

fun move(src:MutableList<Int>, num:Int) {
    println("before move $src")
    val srcIdx = src.indexOf(num)
    val targetIdx = if (num > 0) srcIdx + num + 1 else srcIdx + num
    val destIdx = getWrappedIndex( targetIdx, src.size)
    println("move $num to index $destIdx bumping ${src[destIdx]}")
    src.add(destIdx, num)
    println("after move $src")

    if (destIdx > srcIdx) {
        println("Removing ${src[srcIdx]} at index $srcIdx")
        src.removeAt(srcIdx)
    } else {
        val removeIdx = if (srcIdx + 1 >= src.size) 0 else srcIdx + 1
        println("Removing ${src[removeIdx]} at index ${removeIdx}")

        src.removeAt(removeIdx)
    }
    println(src)
}

fun getWrappedIndex(index:Int, size:Int):Int =
    if (index >= size) {
        index % size
    } else if (index < 0) {
        val wrapped = (size + index) % size
        println("Wrapping $index to $wrapped")
        wrapped
    } else {
        index
    }
