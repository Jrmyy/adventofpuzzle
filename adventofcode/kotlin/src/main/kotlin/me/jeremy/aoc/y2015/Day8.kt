package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day8 : Day<List<String>, Int> {
    override fun runPartOne(): Int =
        getInput().sumOf {
            it.length - it.substring(1, it.length - 1)
                .replace("\\\\\\\\".toRegex(), "a")
                .replace("\\\\\"".toRegex(), "a")
                .replace("\\\\x[A-Za-z0-9]{2}".toRegex(), "a").length
        }

    override fun runPartTwo(): Int =
        getInput().sumOf {
            2 + it.replace("\\", "\\\\").replace("\"", "\\\"").length - it.length
        }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2015, 8)
}

fun main() {
    val day = Day8()
    println(day.runPartOne())
    println(day.runPartTwo())
}
