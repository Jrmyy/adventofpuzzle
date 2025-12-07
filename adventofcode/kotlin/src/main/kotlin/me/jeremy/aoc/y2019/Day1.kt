package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max

class Day1 : Day<List<Int>, Int> {
    override fun runPartOne(): Int =
        getInput().sumOf {
            Math.floorDiv(it, 3) - 2
        }

    override fun runPartTwo(): Int =
        getInput().sumOf {
            var remaining = it
            val fuels = mutableListOf<Int>()
            while (remaining > 0) {
                remaining = max(Math.floorDiv(remaining, 3) - 2, 0)
                fuels.add(remaining)
            }
            fuels.sum()
        }

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2019, 1).map { it.toInt() }
}

fun main() {
    val day = Day1()
    println(day.runPartOne())
    println(day.runPartTwo())
}
