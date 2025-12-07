package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day13 : Day<Map<Pair<String, String>, Int>, Int> {
    override fun runPartOne(): Int = findBestSeats()

    override fun runPartTwo(): Int = findBestSeats(
        getInput().let { m ->
            m +
                m.keys.map { k -> k.first }.toSet().associate { k -> Pair(k, "Me") to 0 } +
                m.keys.map { k -> k.first }.toSet().associate { k -> Pair("Me", k) to 0 }
        }
    )

    override fun getInput(): Map<Pair<String, String>, Int> = AOCUtils.getDayInput(2015, 13).map {
        val parts = it.split(" ")
        val from = parts.first()
        val to = parts.last().removeSuffix(".")
        val h = if (it.contains("gain")) parts[3].toInt() else -parts[3].toInt()
        Pair(Pair(from, to), h)
    }.toMap()

    private fun findBestSeats(
        conditions: Map<Pair<String, String>, Int> = getInput(),
        cache: MutableMap<List<String>, Int> = mutableMapOf(),
        placed: List<String> = mutableListOf()
    ): Int {
        val persons = conditions.map { it.key.first }
        if (placed in cache) return cache.getValue(placed)
        val toSeat = persons.minus(placed.toSet())
        val maxHappiness = if (toSeat.isNotEmpty()) {
            persons.toSet().minus(placed.toSet()).maxOfOrNull { to ->
                val from = placed.lastOrNull()
                if (from == null) {
                    findBestSeats(conditions, cache, placed + to)
                } else {
                    conditions[Pair(from, to)]!! +
                        conditions[Pair(to, from)]!! +
                        findBestSeats(conditions, cache, placed + to)
                }
            } ?: 0
        } else {
            conditions[Pair(placed.last(), placed.first())]!! + conditions[Pair(placed.first(), placed.last())]!!
        }
        cache[placed] = maxHappiness
        return maxHappiness
    }
}

fun main() {
    val day = Day13()
    println(day.runPartOne())
    println(day.runPartTwo())
}
