package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day1 : Day<List<Int>, Int> {

    override fun runPartOne(): Int = checkIfIncreased(2)

    override fun runPartTwo(): Int = checkIfIncreased(4)

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2021, 1).map { it.toInt() }

    private fun checkIfIncreased(windowSize: Int): Int =
        getInput()
            .windowed(windowSize, 1)
            .count { it.last() > it.first() }
}

fun main() {
    val day = Day1()
    println(day.runPartOne())
    println(day.runPartTwo())
}
