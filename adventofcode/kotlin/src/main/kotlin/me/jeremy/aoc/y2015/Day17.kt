package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day17 : Day<List<Int>, Int> {
    override fun runPartOne(): Int = combinations(150).count()

    override fun runPartTwo(): Int {
        val combinations = combinations(150)
        val min = combinations.minOf { it.size }
        return combinations.count { it.size == min }
    }

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2015, 17).map { it.toInt() }

    private fun combinations(
        n: Int,
        containers: List<Pair<Int, Int>> = getInput().mapIndexed { index, i -> Pair(index, i) },
        matched: MutableSet<List<Pair<Int, Int>>> = mutableSetOf()
    ): List<List<Int>> =
        if (n < 0) {
            listOf()
        } else if (n == 0) {
            if (matched.contains(containers)) {
                listOf()
            } else {
                matched.add(containers)
                listOf(getInput().filterIndexed { index, _ -> !containers.map { it.first }.contains(index) })
            }
        } else {
            containers.withIndex().flatMap {
                val nc = containers.toMutableList()
                nc.removeAt(it.index)
                combinations(n - it.value.second, nc, matched)
            }
        }
}

fun main() {
    val day = Day17()
    println(day.runPartOne())
    println(day.runPartTwo())
}
