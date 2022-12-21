package day21

const val file = "input"

fun main() {
    println("Day 21")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day21/$file.txt").readLines()

    val root = parseInput(lines)
    println(root)

    val one = simplify(root as Equals)

    if (one != null) {
        println("After simplify $one ${one.second.eval()}")
    }
}

fun simplify(eq:Equals):Equals? {
    println("First ${eq.first} = ${eq.second.eval()}")

    if (eq.first is Divide) {
        return Equals("1)", eq.first.first, Times("Times",eq.second, eq.first.second))
    }
    return null
}

fun parseInput(lines:List<String>): Op {
    val ops = mutableMapOf<String, Op>()
    val tokenizedList = lines.map { it.split(": ")}
    while (ops.size != lines.size) {
        tokenizedList.forEach {
            //println(it)
            if (!ops.containsKey(it[0])) {
                if (it[1][0].isDigit()) {
                    ops[it[0]] = Value(it[0], it[1].toLong())
                } else if (it[1][0] == '?') {
                    ops[it[0]] = UnknownValue(it[0])
                }
                else {
                    val tokens = it[1].split(" ")
                    if (ops.containsKey(tokens[0]) && ops.containsKey(tokens[2])) {
                        val op = when (tokens[1]) {
                            "+" -> Plus(it[0], ops[tokens[0]]!!, ops[tokens[2]]!!)
                            "-" -> Minus(it[0], ops[tokens[0]]!!, ops[tokens[2]]!!)
                            "*" -> Times(it[0], ops[tokens[0]]!!, ops[tokens[2]]!!)
                            "/" -> Divide(it[0], ops[tokens[0]]!!, ops[tokens[2]]!!)
                            else -> Equals(it[0], ops[tokens[0]]!!, ops[tokens[2]]!!)
                        }
                        ops[it[0]] = op
                    }
                }
            }
        }
    }
    return ops["root"]!!
}

abstract class Op(val name:String) {

    abstract fun eval(): Long
}

class UnknownValue(name:String) : Op(name) {
    override fun eval(): Long = throw RuntimeException("Unknown Value can't be evaled")

    override fun toString(): String = "?"
}

class Value(name:String, val value:Long) : Op(name) {
    override fun eval(): Long = value

    override fun toString(): String = "$value"
}

abstract class BinOp(name:String, val first:Op, val second:Op) : Op(name) {

}

class Plus(name:String, first:Op, second:Op) : BinOp(name, first, second) {

    override fun eval(): Long = first.eval() + second.eval()

    override fun toString(): String = "($first + $second)"
}

class Minus(name:String, first:Op, second:Op) : BinOp(name, first, second) {
    override fun eval(): Long = first.eval() - second.eval()

    override fun toString(): String = "($first - $second)"
}

class Times(name:String, first:Op, second:Op) : BinOp(name, first, second) {
    override fun eval(): Long = first.eval() * second.eval()

    override fun toString(): String = "($first * $second)"
}

class Divide(name:String, first:Op, second:Op) : BinOp(name, first, second) {
    override fun eval(): Long = first.eval() / second.eval()

    override fun toString(): String = "($first / $second)"
}

class Equals(name:String, first:Op, second:Op) : BinOp(name, first, second) {
    override fun eval(): Long = if (first.eval() == second.eval()) 1 else 0

    override fun toString(): String = "($first == $second)"
}