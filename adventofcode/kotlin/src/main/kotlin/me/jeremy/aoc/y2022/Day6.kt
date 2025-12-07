package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day6 : Day<String, Int> {

    override fun runPartOne(): Int = findStartOfMessage(4)

    override fun runPartTwo(): Int = findStartOfMessage(14)

    override fun getInput(): String = AOCUtils.getDayInput(2022, 6).first()

    private fun findStartOfMessage(distinctChars: Int): Int =
        getInput().windowed(distinctChars, 1).indexOfFirst {
            it.toSet().size == distinctChars
        } + distinctChars
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
