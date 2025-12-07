package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day24 : Day<List<String>, Long> {
    override fun runPartOne(): Long {
        // Done by hand
        return 92967699949891
    }

    override fun runPartTwo(): Long {
        // Done by hand
        return 91411143612181
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2021, 24)
}

fun main() {
    val day = Day24()
    println(day.runPartOne())
    println(day.runPartTwo())
}
