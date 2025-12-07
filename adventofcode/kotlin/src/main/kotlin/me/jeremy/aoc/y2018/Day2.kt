package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day2 : Day<List<String>, Int> {
    override fun runPartOne(): Int =
        getInput()
            .map {
                val parts = it.toList().groupingBy { c -> c }.eachCount()
                Pair(
                    parts.any { e -> e.value == 2 }.compareTo(false),
                    parts.any { e -> e.value == 3 }.compareTo(false)
                )
            }
            .reduce { acc, pair ->
                Pair(acc.first + pair.first, acc.second + pair.second)
            }
            .toList()
            .reduce { acc, i -> acc * i }

    override fun runPartTwo(): Int {
        val ids = getInput()
        println(
            ids
                .flatMap { id ->
                    ids.map { other ->
                        Pair(id, listDifferences(id, other))
                    }
                }
                .first { it.second.size == 1 }
                .let {
                    val chars = it.first.toList().toMutableList()
                    chars.removeAt(it.second.first())
                    chars.joinToString("")
                }
        )
        return 0
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2018, 2)

    private fun listDifferences(id: String, other: String): List<Int> =
        id.toList().indices.mapNotNull { n ->
            if (id[n] == other[n]) {
                null
            } else {
                n
            }
        }
}

fun main() {
    val day = Day2()
    println(day.runPartOne())
    println(day.runPartTwo())
}
