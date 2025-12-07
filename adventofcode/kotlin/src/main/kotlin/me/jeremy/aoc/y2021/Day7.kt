package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day7 : Day<List<Int>, Int> {
    override fun runPartOne(): Int {
        val crabsPositions = getInput().sorted()
        val median = if (crabsPositions.size % 2 == 0) {
            (crabsPositions[crabsPositions.size / 2] + crabsPositions[crabsPositions.size / 2 - 1]) / 2
        } else {
            crabsPositions[crabsPositions.size / 2]
        }
        return crabsPositions.sumOf { abs(it - median) }
    }

    override fun runPartTwo(): Int {
        val crabsPositions = getInput()
        val average = crabsPositions.sum() / crabsPositions.size
        return crabsPositions.sumOf {
            (max(it, average) - min(it, average)).let { n -> n * (n + 1) / 2 }
        }
    }

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2021, 7)[0]
        .split(",")
        .map { it.toInt() }
}

fun main() {
    val day = Day7()
    println(day.runPartOne())
    println(day.runPartTwo())
}
