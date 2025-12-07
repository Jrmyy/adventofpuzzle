package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day5 : Day<List<String>, Int> {
    override fun runPartOne(): Int =
        getInput().count {
            !it.contains("(ab|cd|pq|xy)".toRegex()) &&
                it.count { c -> listOf('a', 'e', 'i', 'o', 'u').contains(c) } >= 3 &&
                it.windowed(2, 1).any { s -> s.first() == s.last() }
        }

    override fun runPartTwo(): Int =
        getInput().count {
            it.windowed(2, 1).mapIndexed { i, s ->
                Pair(s, Pair(i, i + 1))
            }.fold(mutableMapOf<String, List<Pair<Int, Int>>>()) { acc, p ->
                acc[p.first] = (acc[p.first] ?: listOf()) + listOf(p.second)
                acc
            }.values.firstOrNull { l ->
                l.flatMap { p ->
                    l.mapNotNull { op ->
                        if (op.first != p.second && op.second != p.first && op != p) true else null
                    }
                }.isNotEmpty()
            } != null &&
                it.windowed(3, 1).any { s -> s.first() == s.last() }
        }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2015, 5)
}

fun main() {
    val day = Day5()
    println(day.runPartOne())
    println(day.runPartTwo())
}
