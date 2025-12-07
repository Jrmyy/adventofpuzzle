package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day3 : Day<List<String>, Int> {

    override fun runPartOne(): Int = getInput().sumOf { getPriority(it.chunked(it.length / 2)) }

    override fun runPartTwo(): Int = getInput().chunked(3).sumOf { getPriority(it) }

    private fun getPriority(l: List<String>): Int {
        val inter = l.map { s -> s.toSet() }.reduce { acc, s -> acc.intersect(s) }
        assert(inter.size == 1)
        val char = inter.first()
        return if (char.isLowerCase()) {
            char.code - 'a'.code + 1
        } else {
            27 + char.code - 'A'.code
        }
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2022, 3)
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
