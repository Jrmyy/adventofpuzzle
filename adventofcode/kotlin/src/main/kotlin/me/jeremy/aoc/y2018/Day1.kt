package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day1 : Day<List<Int>, Int> {
    override fun runPartOne(): Int =
        getInput().reduce { acc, i -> acc + i }

    override fun runPartTwo(): Int {
        val frequencies = mutableListOf<Int>()
        val changes = getInput()
        var currentFrequency = 0
        var i = 0
        while (currentFrequency !in frequencies) {
            frequencies.add(currentFrequency)
            currentFrequency += changes[i % changes.size]
            i++
        }
        return currentFrequency
    }

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2018, 1).map {
        val value = it.drop(1).toInt()
        if (it.startsWith("-")) {
            -value
        } else {
            value
        }
    }
}

fun main() {
    val day = Day1()
    println(day.runPartOne())
    println(day.runPartTwo())
}
