package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day9 : Day<Map<Pair<String, String>, Int>, Int> {
    override fun runPartOne(): Int = findShortestPath()

    override fun runPartTwo(): Int = findLongestPath()

    override fun getInput(): Map<Pair<String, String>, Int> = AOCUtils.getDayInput(2015, 9).flatMap {
        val parts = it.split(" = ")
        val fromTo = parts.first().split(" to ")
        listOf(
            Pair(Pair(fromTo.first(), fromTo.last()), parts.last().toInt()),
            Pair(Pair(fromTo.last(), fromTo.first()), parts.last().toInt())
        )
    }.toMap()

    private fun findLongestPath(
        cache: MutableMap<List<String>, Int> = mutableMapOf(),
        visited: List<String> = mutableListOf()
    ): Int {
        val paths = getInput()
        val galaxies = paths.map { it.key.first }
        if (visited in cache) return cache.getValue(visited)
        val maxDistance = galaxies.minus(visited.toSet()).maxOfOrNull { to ->
            val from = visited.lastOrNull()
            if (from == null) {
                findLongestPath(cache, visited + to)
            } else {
                paths[Pair(from, to)]!! + findLongestPath(cache, visited + to)
            }
        } ?: 0
        cache[visited] = maxDistance
        return maxDistance
    }

    private fun findShortestPath(
        cache: MutableMap<List<String>, Int> = mutableMapOf(),
        visited: List<String> = mutableListOf()
    ): Int {
        val paths = getInput()
        val galaxies = paths.map { it.key.first }
        if (visited in cache) return cache.getValue(visited)
        val minDistance = galaxies.minus(visited.toSet()).minOfOrNull { to ->
            val from = visited.lastOrNull()
            if (from == null) {
                findShortestPath(cache, visited + to)
            } else {
                paths[Pair(from, to)]!! + findShortestPath(cache, visited + to)
            }
        } ?: 0
        cache[visited] = minDistance
        return minDistance
    }
}

fun main() {
    val day = Day9()
    println(day.runPartOne())
    println(day.runPartTwo())
}
