package day11

import java.math.BigInteger


var mul = BigInteger.ONE

fun main() {
    println("Day 9")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day11/input.txt").readLines()

    val monkeys = buildMonkeys(lines)
    val monkeyMap = monkeys.map { it.id to it}.toMap()
    println(monkeys)

    println("part1 " + part1(monkeys, monkeyMap))
}

fun part1(monkeys:List<Monkey>, monkeyMap:Map<Int, Monkey>): BigInteger {

    repeat (10000) {
        monkeys.forEach { it.round(monkeyMap) }
    }

    val sorted = monkeys.sortedBy { it.numInspected }.reversed()
    val highest = sorted.first().numInspected.toBigInteger()
    val secondHighest = sorted[1].numInspected.toBigInteger()

    println("Highest ${highest}, Second Highest ${secondHighest}")

    return highest.times(secondHighest)
}


fun buildMonkeys(lines:List<String>): List<Monkey> {
    val monkeys = mutableListOf<Monkey>()
    var curLine = 0
    while (curLine < lines.size) {
        monkeys.add(Monkey.build(lines.subList(curLine, curLine + 6)))
        curLine += 7
    }
    return monkeys
}

class Monkey (val id:Int, val items:MutableList<BigInteger>, val divideBy:BigInteger,
              val ifTrue:Int, val ifFalse:Int, val op:(BigInteger) -> BigInteger) {

    var numInspected:Int = 0

    override fun toString(): String {
        var str = "\nMonkey $id:\n"
        str += "Starting items: $items\n"
        str += "Operation: $op\n"
        str += "Test: divisible by $divideBy\n"
        str += "  If true: throw to monkey $ifTrue\n"
        str += "  If false: throw to monkey $ifFalse\n"

        return str
    }

    fun round(monkeys:Map<Int, Monkey>) {
        items.forEach {
            numInspected++
            //println("Monkey $id")
            var worry = op(it)
            worry %= mul
            //println("Increased Worry $worry")
            //worry = worry.div(3.toBigInteger())
            //println("Divided Worry $worry")
            val giveTo = if (worry.mod(divideBy).equals(BigInteger.ZERO)) ifTrue else ifFalse
            //println("Give to $giveTo")
            monkeys[giveTo]?.addItem(worry)
        }
        items.clear()
    }

    fun addItem(item:BigInteger) {
        items.add(item)
    }

    companion object {

        fun build(line:List<String>): Monkey {
            val id = line[0][line[0].length - 2].toString().toInt()
            val nums = line[1].split(": ")[1]

            val list = nums.split(", ").map { it.toBigInteger() }.toMutableList()
            val divBy = line[3].split(" ").last().toBigInteger()

            mul *= divBy

            val ifTrue = line[4].last().toString().toInt()
            val ifFalse = line[5].last().toString().toInt()

            return Monkey(id, list, divBy, ifTrue, ifFalse, buildInputOp(id))
        }

        fun buildInputOp(id:Int):(BigInteger) -> BigInteger =
            when (id) {
                0 -> {it -> it.times(3.toBigInteger())}
                1 -> {it -> it.times(11.toBigInteger())}
                2 -> {it -> it.plus(6.toBigInteger())}
                3 -> {it -> it.plus(4.toBigInteger())}
                4 -> {it -> it.plus(8.toBigInteger())}
                5 -> {it -> it.plus(2.toBigInteger())}
                6 -> {it -> it.times(it)}
                else -> {it -> it.plus(5.toBigInteger())}
            }

        fun buildSampleOp(id:Int):(Int) -> Int =
            when (id) {
                0 -> {it -> it * 19}
                1 -> {it -> it + 6}
                2 -> {it -> it * it}
                else -> {it -> it + 3}
            }


    }
}