package me.jeremy.aoc.y2017

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day2 : Day<List<List<Int>>, Int> {

    override fun runPartOne(): Int = getInput().sumOf { it.max() - it.min() }

    override fun runPartTwo(): Int = getInput().sumOf {
        it.firstNotNullOf { i ->
            it.filter { j -> j != i }.firstNotNullOfOrNull { j -> if (i % j == 0) i / j else null }
        }
    }

    override fun getInput(): List<List<Int>> = AOCUtils.getDayInput(2017, 2).map {
        it.split("\t").map { i -> i.trim().toInt() }
    }
}

fun main() {
    val day = Day2()
    println(day.runPartOne())
    println(day.runPartTwo())
}
