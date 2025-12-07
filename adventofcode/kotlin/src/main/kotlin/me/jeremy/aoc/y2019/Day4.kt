package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day4 : Day<Pair<Int, Int>, Int> {
    override fun runPartOne(): Int {
        val minMax = getInput()
        return (minMax.first..minMax.second).count {
            val figures = it.toString().toList().map { that -> that.toString().toInt() }
            figures.sorted() == figures && figures.toSortedSet().toList() != figures
        }
    }

    override fun runPartTwo(): Int {
        val minMax = getInput()
        return (minMax.first..minMax.second).count {
            val figures = it.toString().toList().map { that -> that.toString().toInt() }
            figures.sorted() == figures &&
                figures.toSortedSet().toList() != figures &&
                figures.groupingBy { that -> that }.eachCount().filterValues { that -> that == 2 }.count() > 0
        }
    }

    override fun getInput(): Pair<Int, Int> {
        val parts = AOCUtils.getDayInput(2019, 4)[0].split("-").map { it.toInt() }
        return Pair(parts[0], parts[1])
    }
}

fun main() {
    val day = Day4()
    println(day.runPartOne())
    println(day.runPartTwo())
}
