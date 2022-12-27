package day25

import kotlin.math.pow


const val file = "input"

val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day25/$file.txt").readLines()

fun main() {
    println("Day 25")

    println(lines)
    var sum = 0L
    lines.forEach {
        sum += snafuToDecimal(it)
        //val quin = decimalToQuinary(num)
        //println("$it\t\t $num \t $quin")
    }
    println(quinaryToSnafu(decimalToQuinary(sum)))

/*
    val year = decimalToQuinary(4890L)
    println("quinary $year")
    println("snafu ${quinaryToSnafu(year)}")

 */
}

fun snafuToDecimal(snafu:String):Long {
    var sum = 0L

    snafu.forEachIndexed{ i, c ->
        val placeIdx = snafu.length - i - 1
        val snafuDigit = when (c) {
            '-' -> -1L
            '=' -> -2L
            else -> {c.toString().toLong()}
        }
        val place = 5.0.pow(placeIdx).toLong()
        val product = snafuDigit * place
        //println("$snafuDigit in the $place place, index $placeIdx = $product")
        sum += product
    }

    return sum
}

fun decimalToQuinary(num:Long): String {
    var str = ""

    var quotiant = num
    while (quotiant > 0) {
        val rem = quotiant % 5
        str += rem
        quotiant /= 5
    }

    return str.reversed()
}

fun quinaryToSnafu(str:String): String {
    var snafu = ""

    var reversed = str.reversed()
    var carry = 0L
    println("$str reversed is $reversed")
    for (i in 0 until reversed.length) {

        val digit = when (reversed[i]) {
            '3' -> {carry = 1; '=' }
            '4' -> {carry = 1; '-' }
            else -> { carry = 0; reversed[i] }
        }
        snafu += digit
        println("Start: Digit index $i is ${reversed[i]}, converts to $digit snafu so far $snafu")

        reversed = propagateCarry(reversed, i + 1, carry)
        println("End: Digit index $i is ${reversed[i]}, converts to $digit snafu so far $snafu")
    }
    if (carry > 0) {
        snafu = "${snafu}1"
    }

    return snafu.reversed()
}

fun propagateCarry(reversed: String, i: Int, carry: Long): String {
    if (carry == 0L) {
        return reversed
    }
    println("propagateCarry $reversed index $i carry $carry")

    var shouldCarry = carry > 0
    val str = StringBuilder(reversed)
    var idx = i
    while (shouldCarry) {
        var num = if (idx >= reversed.length) {
            1
        } else {
            reversed[idx].toString().toLong() + carry
        }
        if (num == 5L) {
            num = 0
            shouldCarry = true
            println("Carry to next place ${idx + 1}")
        } else {
            shouldCarry = false
        }
        if (idx >= reversed.length) {
            str.append('1')
        } else {
            val ch = num.toInt().toString()[0]
            println("Setting ${str[idx]} at $idx to $ch")
            str[idx] = ch
        }
        idx++
        println("num $num idx $idx str $str")
    }
    return str.toString()
}
