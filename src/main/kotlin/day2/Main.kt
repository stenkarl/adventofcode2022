package day2

import java.io.File

enum class Result(val points:Int, val code:Char) {
    WIN(6, 'Z'),
    DRAW(3, 'Y'),
    LOSE(0, 'X')

}

enum class Play(val points:Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3)
}

fun main(args:Array<String>) {
    println("Day 2")

    val lines = File("/Users/sten/dev/adventofcode2022/src/main/kotlin/day2/input.txt").readLines()

    part2(lines.map {i -> Pair(i[0], i[2])})
}

fun part1(rounds:List<Pair<Char, Char>>) {
    val total = rounds.sumOf { r -> score(r) }

    println("Part 1 Total Score $total")
}

fun part2(rounds:List<Pair<Char, Char>>) {
    val total = rounds.sumOf { r -> part2Score(r) }

    println("Part 2 Total Score $total")
}

fun score(round:Pair<Char, Char>):Int =
    hand(round.second) + if (round.first == 'A') { // rock
        when (round.second) {
            'X' -> Result.DRAW.points
            'Y' -> Result.WIN.points
            else -> Result.LOSE.points
        }
    } else if (round.first == 'B') { // paper
        when (round.second) {
            'X' -> Result.LOSE.points
            'Y' -> Result.DRAW.points
            else -> Result.WIN.points
        }
    } else {
        when (round.second) { // scissors
            'X' -> Result.WIN.points
            'Y' -> Result.LOSE.points
            else -> Result.DRAW.points
        }
    }

fun part2Score(round:Pair<Char, Char>):Int {
    val result = charToResult(round.second)
    return result.points + if (round.first == 'A') { // rock
        when (result) {
            Result.WIN -> Play.PAPER.points
            Result.LOSE -> Play.SCISSORS.points
            else -> Play.ROCK.points
        }
    } else if (round.first == 'B') { // paper
        when (result) {
            Result.WIN -> Play.SCISSORS.points
            Result.LOSE -> Play.ROCK.points
            else -> Play.PAPER.points
        }
    } else {
        when (result) {
            Result.WIN -> Play.ROCK.points
            Result.LOSE -> Play.PAPER.points
            else -> Play.SCISSORS.points
        }
    }
}

fun hand(h:Char) =
    when (h) {
        'X' -> 1
        'Y' -> 2
        else -> 3
    }

fun charToResult(c:Char):Result =
    when (c) {
        'X' -> Result.LOSE
        'Y' -> Result.DRAW
        else -> Result.WIN
    }
