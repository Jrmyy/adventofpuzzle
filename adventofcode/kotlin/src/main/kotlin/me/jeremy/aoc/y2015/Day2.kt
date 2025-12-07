package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day2 : Day<List<Triple<Int, Int, Int>>, Int> {
    override fun runPartOne(): Int =
        getInput().sumOf {
            listOf(
                it.first * it.second,
                it.first * it.third,
                it.second * it.third
            ).let { l ->
                l.sumOf { p -> 2 * p } + l.minOrNull()!!
            }
        }

    override fun runPartTwo(): Int =
        getInput().sumOf {
            listOf(
                it.first + it.second,
                it.first + it.third,
                it.second + it.third
            ).minOf { p -> 2 * p } + it.first * it.second * it.third
        }

    override fun getInput(): List<Triple<Int, Int, Int>> = AOCUtils.getDayInput(2015, 2).map {
        it.split("x").map { s -> s.toInt() }.let { p -> Triple(p[0], p[1], p[2]) }
    }
}

fun main() {
    val day = Day2()
    println(day.runPartOne())
    println(day.runPartTwo())
}
