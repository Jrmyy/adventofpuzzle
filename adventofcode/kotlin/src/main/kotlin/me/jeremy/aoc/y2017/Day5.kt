package me.jeremy.aoc.y2017

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day5 : Day<MutableList<Int>, Int> {

    override fun runPartOne(): Int = getInput().let { offsets ->
        var curr = 0
        var steps = 0
        while (curr < offsets.size) {
            val jump = offsets[curr]
            val new = curr + jump
            offsets[curr] = offsets[curr] + 1
            curr = new
            steps++
        }
        steps
    }

    override fun runPartTwo(): Int = getInput().let { offsets ->
        var curr = 0
        var steps = 0
        while (curr < offsets.size) {
            val jump = offsets[curr]
            val new = curr + jump
            offsets[curr] = offsets[curr] + if (jump >= 3) -1 else 1
            curr = new
            steps++
        }
        steps
    }

    override fun getInput(): MutableList<Int> =
        AOCUtils.getDayInput(2017, 5).map { it.toInt() }.toMutableList()
}

fun main() {
    val day = Day5()
    println(day.runPartOne())
    println(day.runPartTwo())
}
