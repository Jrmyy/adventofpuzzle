package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day3 : Day<List<List<Int>>, Int> {
    override fun runPartOne(): Int = getValidTriangles(getInput())

    override fun runPartTwo(): Int = getValidTriangles(
        getInput().chunked(3).map {
            listOf(
                listOf(it[0][0], it[1][0], it[2][0]),
                listOf(it[0][1], it[1][1], it[2][1]),
                listOf(it[0][2], it[1][2], it[2][2]),
            )
        }.flatten()
    )

    override fun getInput(): List<List<Int>> = AOCUtils.getDayInput(2016, 3).map {
        "(?: +)(\\d+)".toRegex().findAll(it).toList().map { g -> g.value.trim().toInt() }
    }

    private fun getValidTriangles(triangles: List<List<Int>>): Int = triangles.count {
        it.mapIndexed { idx, i -> i < it.filterIndexed { itIdx, _ -> itIdx != idx }.sum() }.all { b -> b }
    }
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
