package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day1 : Day<List<Int>, Int> {
    override fun runPartOne(): Int = getInput().max()

    override fun runPartTwo(): Int = getInput()
        .sortedDescending()
        .take(3)
        .sum()

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2022, 1)
        .fold(mutableListOf(0)) { acc, s ->
            if (s == "") {
                acc.add(0)
            } else {
                acc[acc.size - 1] += s.toInt()
            }
            acc
        }
}

fun main() {
    val day = Day1()
    println(day.runPartOne())
    println(day.runPartTwo())
}
