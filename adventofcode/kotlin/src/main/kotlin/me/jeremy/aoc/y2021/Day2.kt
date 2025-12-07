package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day2 : Day<List<Pair<String, Int>>, Int> {
    override fun runPartOne(): Int =
        getInput().fold(Pair(0, 0)) { acc, pair ->
            when (pair.first) {
                "forward" -> Pair(acc.first + pair.second, acc.second)
                "up" -> Pair(acc.first, acc.second - pair.second)
                else -> Pair(acc.first, acc.second + pair.second)
            }
        }.let { it.first * it.second }

    override fun runPartTwo(): Int =
        getInput().fold(Triple(0, 0, 0)) { acc, pair ->
            when (pair.first) {
                "forward" -> Triple(acc.first + pair.second, acc.second + pair.second * acc.third, acc.third)
                "up" -> Triple(acc.first, acc.second, acc.third - pair.second)
                else -> Triple(acc.first, acc.second, acc.third + pair.second)
            }
        }.let { it.first * it.second }

    override fun getInput(): List<Pair<String, Int>> = AOCUtils.getDayInput(2021, 2).map {
        it.split(" ").let { p -> Pair(p.first(), p.last().toInt()) }
    }
}

fun main() {
    val day = Day2()
    println(day.runPartOne())
    println(day.runPartTwo())
}
