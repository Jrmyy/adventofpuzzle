package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day2 : Day<List<Triple<Pair<Int, Int>, Char, String>>, Int> {
    override fun runPartOne(): Int =
        getInput().count {
            it.third.count { that ->
                that == it.second
            } in it.first.first until it.first.second + 1
        }

    override fun runPartTwo(): Int =
        getInput().count {
            val isAtFirst = it.third[it.first.first - 1] == it.second
            val isAtSecond = it.third[it.first.second - 1] == it.second
            (isAtFirst && !isAtSecond) || (!isAtFirst && isAtSecond)
        }

    override fun getInput(): List<Triple<Pair<Int, Int>, Char, String>> =
        AOCUtils.getDayInput(2020, 2).map {
            val split = it.split(" ")
            val minMax = split[0].split("-").map { that -> that.toInt() }
            Triple(Pair(minMax[0], minMax[1]), split[1][0], split[2])
        }
}

fun main() {
    val day = Day2()
    println(day.runPartOne())
    println(day.runPartTwo())
}
