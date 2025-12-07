package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day14 : Day<Map<String, Triple<Int, Int, Int>>, Int> {
    override fun runPartOne(): Int = compete().first

    override fun runPartTwo(): Int = compete().second

    override fun getInput(): Map<String, Triple<Int, Int, Int>> = AOCUtils.getDayInput(2015, 14).associate {
        val groups = "([A-Za-z]+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds"
            .toRegex().find(it)!!.groupValues
        groups[1] to Triple(groups[2].toInt(), groups[3].toInt(), groups[4].toInt())
    }

    private fun compete(): Pair<Int, Int> {
        val reindeers = getInput()
        var resting = mutableMapOf<String, Int>()
        var flying = reindeers.entries.associate { e -> e.key to e.value.second }.toMutableMap()
        val distance = reindeers.keys.associateWith { 0 }.toMutableMap()
        val scores = reindeers.keys.associateWith { 0 }.toMutableMap()
        repeat(2503) {
            flying.forEach { (t, u) ->
                if (u > 0) {
                    flying[t] = flying[t]!!.minus(1)
                    distance[t] = distance[t]!!.plus(reindeers[t]!!.first)
                }
            }
            resting.forEach { (t, u) ->
                if (u > 0) {
                    resting[t] = resting[t]!!.minus(1)
                }
            }
            flying.filterValues { v -> v == 0 }.forEach { (t, _) ->
                resting[t] = reindeers[t]!!.third
            }
            resting.filterValues { v -> v == 0 }.forEach { (t, _) ->
                flying[t] = reindeers[t]!!.second
            }
            resting = resting.filter { e -> e.value != 0 }.toMutableMap()
            flying = flying.filter { e -> e.value != 0 }.toMutableMap()
            val ahead = distance.maxByOrNull { e -> e.value }!!.key
            scores[ahead] = scores[ahead]!!.plus(1)
        }
        return Pair(distance.maxOf { it.value }, scores.maxOf { it.value })
    }
}

fun main() {
    val day = Day14()
    println(day.runPartOne())
    println(day.runPartTwo())
}
