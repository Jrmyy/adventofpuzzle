package me.jeremy.aoc.y2024

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day7 : Day<List<Pair<Long, List<Long>>>, Long> {
    override fun runPartOne(): Long =
        getInput()
            .filter { isValid(it.first, it.second) }
            .sumOf { it.first }

    override fun runPartTwo(): Long =
        getInput()
            .filter { isValid(it.first, it.second, true) }
            .sumOf { it.first }

    override fun getInput(): List<Pair<Long, List<Long>>> =
        AOCUtils.getDayInput(2024, 7)
            .map {
                val (res, parts) = it.split(": ")
                Pair(res.toLong(), parts.split(" ").map { i -> i.toLong() })
            }

    private fun isValid(res: Long, parts: List<Long>, allowConcat: Boolean = false, curr: Long = 0): Boolean {
        if (curr > res) {
            return false
        }
        if (parts.isEmpty()) {
            return res == curr
        }
        val newParts = parts.drop(1)
        val valid = isValid(res, newParts, allowConcat, curr + parts.first()) ||
            isValid(res, newParts, allowConcat, curr * parts.first())
        if (allowConcat) {
            return valid || isValid(res, newParts, true, "${curr}${parts.first()}".toLong())
        }
        return valid
    }
}

fun main() {
    val day = Day7()
    println(day.runPartOne())
    println(day.runPartTwo())
}
