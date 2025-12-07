package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day10 : Day<List<Int>, Long> {
    override fun runPartOne(): Long {
        var oneDifferences = 0
        var threeDifferences = 0
        val adapters = getInput().sorted()
        var previousValue = 0
        adapters.forEach {
            val diff = it - previousValue
            if (diff > 3 || diff == 0) {
                throw RuntimeException("Can't chain all adapters")
            }
            previousValue = it
            if (diff == 1) {
                oneDifferences++
            }
            if (diff == 3) {
                threeDifferences++
            }
        }
        return oneDifferences * (threeDifferences + 1).toLong()
    }

    override fun runPartTwo(): Long {
        val adapters = getInput().sorted()
        val possibilitiesFromStart = adapters.map {
            Pair(it, adapters.filter { that -> that <= it + 3 && that > it })
        }.reversed()
        val cache = mutableMapOf(possibilitiesFromStart[0].first to 1L)
        possibilitiesFromStart.drop(1).forEach {
            cache[it.first] = it.second.map { that -> cache[that]!! }.sum()
        }
        return cache.filterKeys { it <= 3 }.toList().map { it.second }.sum()
    }

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2020, 10).map { it.toInt() }
}

fun main() {
    val day = Day10()
    println(day.runPartOne())
    println(day.runPartTwo())
}
