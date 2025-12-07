package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day12 : Day<MutableList<String>, Int> {

    override fun runPartOne(): Int = AssembunnyInterpreter.compute(getInput())

    override fun runPartTwo(): Int = AssembunnyInterpreter.compute(getInput(), mutableMapOf("c" to 1))

    override fun getInput(): MutableList<String> = AOCUtils.getDayInput(2016, 12).toMutableList()
}

fun main() {
    val day = Day12()
    println(day.runPartOne())
    println(day.runPartTwo())
}
