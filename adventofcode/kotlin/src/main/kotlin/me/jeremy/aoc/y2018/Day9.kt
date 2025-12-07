package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

fun <T> ArrayDeque<T>.rotate(num: Int) {
    if (num > 0) {
        repeat(num) { addFirst(removeLast()) }
    } else if (num < 0) {
        repeat(-num) { addLast(removeFirst()) }
    }
}

class Day9 : Day<Pair<Int, Int>, Long> {
    override fun runPartOne(): Long {
        val (playersCount, lastMarblePoints) = getInput()
        return runGame(playersCount, lastMarblePoints)
    }

    override fun runPartTwo(): Long {
        val (playersCount, lastMarblePoints) = getInput()
        return runGame(playersCount, lastMarblePoints * 100)
    }

    override fun getInput(): Pair<Int, Int> {
        val parts = Regex("(\\d+) players; last marble is worth (\\d+) points").find(
            AOCUtils.getDayInput(2018, 9)[0]
        )!!.groupValues
        return Pair(parts[1].toInt(), parts[2].toInt())
    }

    private fun runGame(playersCount: Int, lastMarblePoints: Int): Long {
        val marbles = ArrayDeque(listOf(0))
        val playersScore = (1..playersCount).map { 0L }.toMutableList()
        (1..lastMarblePoints).forEach {
            if (it % 23 == 0) {
                marbles.rotate(-7)
                playersScore[it % playersCount] += (it + marbles.removeLast()).toLong()
            } else {
                marbles.rotate(2)
                marbles.addLast(it)
            }
        }
        return playersScore.maxOf { it }
    }
}

fun main() {
    val day = Day9()
    println(day.runPartOne())
    println(day.runPartTwo())
}
