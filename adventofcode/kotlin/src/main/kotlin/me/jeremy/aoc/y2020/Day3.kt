package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day3 : Day<List<List<Char>>, Long> {
    override fun runPartOne(): Long =
        getTreesNumber(getInput(), 3, 1)

    override fun runPartTwo(): Long {
        val slopes = getInput()
        return getTreesNumber(slopes, 1, 1) *
            getTreesNumber(slopes, 3, 1) *
            getTreesNumber(slopes, 5, 1) *
            getTreesNumber(slopes, 7, 1) *
            getTreesNumber(slopes, 1, 2)
    }

    override fun getInput(): List<List<Char>> =
        AOCUtils.getDayInput(2020, 3).map { it.toList() }

    private fun getTreesNumber(slopes: List<List<Char>>, right: Int, down: Int): Long =
        slopes.mapIndexed { index, list ->
            if (index % down == 0) {
                list[(right * index / down) % list.size] == '#'
            } else {
                null
            }
        }.count { it != null && it }.toLong()
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
