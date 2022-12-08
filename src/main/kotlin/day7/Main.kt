package day7

fun main(args:Array<String>) {
    println("Day 7")

    val lines = java.io.File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day7/input.txt").readLines()

    println(lines)

    val root = build(lines.subList(1, lines.size))
    root.print("")

    //println("Part 1 " + part1(root))
    println("Part 2 " + part2(root))

}

fun part1(root:Dir): Long {
    val dirs = root.find(100000)

    println(dirs)

    return dirs.sumOf { it.size() }
}

fun part2(root:Dir): Long {
    val used = root.size()
    val free = 70000000L - used
    val needed = 30000000L - free
    val dirs = root.findAtLeast(needed)

    println("Needed: $needed")

    val smallest = dirs.minByOrNull { it.size() }!!

    return smallest.size()
}

fun build(lines:List<String>): Dir {
    val root = Dir(null, "/")
    var current = root
    lines.forEach { str ->
        if (str.startsWith("$")) {
            println("cmd: $str")
            val tokens = str.split(" ")
            if (tokens[1] == "cd") {
                println("current $current, line $str")
                current = current.cd(tokens[2])!!
            }
        } else { // listing contents
            println("listing.. $str")
            current.add(buildItem(current, str))
        }
    }

    return root
}

fun buildItem(parent: Dir, str:String): FileElement {
    val tokens = str.split(" ")
    val item = if (tokens[0] == "dir") {
        Dir(parent, tokens[1])
    } else {
        File(tokens[1], tokens[0].toLong())
    }
    return item
}

abstract class FileElement(val name:String) {

    abstract fun size(): Long

    open fun print(indent: String) {
        println(indent + name + " (" + size() + ")")
    }

    override fun toString(): String {
        return name + " (" + size() + ")"
    }
}

class File(name:String, val size:Long): FileElement(name) {

    override fun size(): Long = size

}

class Dir(val parent:Dir?, name:String): FileElement(name) {

    private val contents = mutableListOf<FileElement>()

    override fun size(): Long =
        contents.sumOf { it.size() }

    fun add(element:FileElement) {
        contents.add(element)
    }

    fun cd(which:String): Dir? =
        if (which == "..") {
            parent
        } else {
            contents.find { it.name == which } as Dir?
        }

    override fun print(indent: String) {
        println(indent + name)
        contents.forEach { it.print("  $indent") }
    }

    fun find(threshold:Long):Set<Dir> {
        val results = mutableSetOf<Dir>()

        contents.forEach {
            if (it is Dir) {
                if (it.size() <= threshold) {
                    results.add(it)
                }
                results.addAll(it.find(threshold))
            }
        }

        return results
    }

    fun findAtLeast(threshold:Long):Set<Dir> {
        val results = mutableSetOf<Dir>()

        contents.forEach {
            if (it is Dir) {
                if (it.size() >= threshold) {
                    results.add(it)
                    results.addAll(it.findAtLeast(threshold))
                }

            }
        }

        return results
    }

}

 // cgcqpjpn (11133322)
 // cgcqpjpn (2948823)

// 2586708