package day13


fun main() {
    println("Day 13")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day13/input.txt").readLines()
    //val lines = java.io.File("/Users/sten/Downloads/aoc_2022_day13_large/aoc_2022_day13_large-2.txt").readLines()

    //println(lines)

    val packets = parse(lines)
    /*
    packets.forEachIndexed { idx, p ->
        println("${idx + 1}: ")
        println("  ${p.first}")
        println("  ${p.second}")
    }*/

    //println("Part 1 ${part1(packets)}")
    part2(packets)
}

fun part2(packets: List<Pair<List<Any>, List<Any>>>) {
    val list = pairsToList(packets)
    val sorted = list.sortedWith { first, second ->
        when (isListInOrder(first, second)) {
            1 -> -1
            0 -> 1
            else -> 0
        }
    }
    println("Sorted:")
    sorted.forEach { println(it)}

    val two = sorted.indexOf(listOf(listOf(2))) + 1
    val six = sorted.indexOf(listOf(listOf(6))) + 1
    println("Two $two, Six $six, Decoder key ${two * six}")
}

fun pairsToList(packets: List<Pair<List<Any>, List<Any>>>):List<List<Any>> {
    val list = mutableListOf<List<Any>>()
    packets.forEach {
        list.add(it.first)
        list.add(it.second)
    }
    return list
}

fun part1(packets:List<Pair<List<Any>, List<Any>>>): Int {
    var sum = 0
    packets.forEachIndexed { idx, pair ->
        if (pair.first.toString() == pair.second.toString()) {
            println("Packets are the same")
            sum += idx + 1
        }else if (isPacketInOrder(pair)) {
            //println("Packet is in order (${idx + 1}) $pair")
            sum += idx + 1
        } else {
            //println("Packet is NOT in order (${idx + 1})")
            //println("  ${pair.first}")
            //println("  ${pair.second}")
            //println()
        }
    }
    return sum
}

fun isPacketInOrder(pair:Pair<List<Any>, List<Any>>): Boolean {
    val ret = isListInOrder(pair.first, pair.second)
    if (ret == 2) {
        println("Packets are the same $pair")
    }
    return ret == 1
}

fun isListInOrder(first:List<Any>, second:List<Any>): Int {
    //println("isListInOrder $first   $second")
    //second.forEachIndexed { idx, item -> println("    $idx  $item")}
    if (first.isEmpty() && second.isNotEmpty()) {
        //println("First list is empty. Second is not $second")
        return 1
    }
    first.forEachIndexed { idx, item ->
        //println("forEachIndexed $idx first: $item, second: $second")
        if (idx >= second.size) {
            //println("First list is bigger: ${first.size} vs ${second.size}")
            //println("  $first")
            //println("  $second")
            return 0 // first list is bigger
        }
        val secondItem = second[idx]
        //println("Comparing $idx first: $item, second: $secondItem")
        if (item is Int && secondItem is Int) {
            if (item < secondItem ) {
                //println("$item is less than $secondItem")
                return 1
            } else if (item > secondItem) {
                //println("$item is greater than $secondItem")
                return 0
            } else {
                //println("$item and $secondItem are equal -- keep going")
            }
        } else if (item !is Int || secondItem !is Int) {
            //println("At least one item is a list")
            val firstAsList = if (item is List<*>) item else listOf(item)
            val secondAsList = if (secondItem is List<*>) secondItem else listOf(secondItem)
            val ret = isListInOrder(firstAsList as List<Any>, secondAsList as List<Any>)
            if (ret == 0 || ret == 1) {
                return ret
            }
            // need to handle the case where a sublist is "in order"
            // which means stop and return true vs just all elements equal
            // which means continue processing
        }
    }
    if (first.size < second.size) { // first and second are equal, but second has more elements
        return 1
    }
    return 2 // lists are equal, keep processing
}

fun parse(lines:List<String>):List<Pair<List<Any>, List<Any>>> {
    val list = mutableListOf<Pair<List<Any>, List<Any>>>()

    var index = 0
    while (index < lines.size - 1) {
        list.add(Pair(parsePacket(lines[index]), parsePacket(lines[index + 1])))
        index += 3
    }

    return list
}

fun parsePacket(str:String): List<Any> {
    //println("New Packet: $str")
    return parseList(1, str).second
}

fun parseList(index:Int, str:String):Pair<Int, List<Any>> {
    var idx = index
    val list = mutableListOf<Any>()
    while (idx < str.length) {
        if (str[idx] == '[') {
            //println("Begin List $idx ${str[idx + 1]}")
            val ret = parseList(idx + 1, str)
            idx = ret.first
            list.add(ret.second)
        } else if (str[idx] == ']') {
            //println("End List $idx")
            return Pair(idx + 1, list)
        } else if (str[idx] == ',') {
            idx++
        } else { // must be a digit
            val numDigits = if (Character.isDigit(str[idx + 1])) 2 else 1
            val digitStr = str.substring(idx, idx + numDigits)
            //println("Digit $digitStr, numDigits $numDigits idx $idx")
            list.add(digitStr.toInt())
            idx += numDigits
        }
    }
    //println("End Packet")
    return Pair(idx, list)
}

