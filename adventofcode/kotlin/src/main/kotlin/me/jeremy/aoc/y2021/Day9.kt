package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day9 : Day<List<List<Int>>, Int> {
    override fun runPartOne(): Int = getDangerZones(getInput()).flatten().sum()

    override fun runPartTwo(): Int {
        val heightMap = getInput()
        val dangerZones = getDangerZones(heightMap)
        return dangerZones.asSequence().flatMapIndexed { y, r ->
            r.mapIndexedNotNull { x, d ->
                if (d > 0) Pair(x, y) else null
            }
        }.map {
            val basinPositions = mutableSetOf(it)
            val toVisit = mutableListOf(it)
            val visited = mutableListOf<Pair<Int, Int>>()
            while (toVisit.isNotEmpty()) {
                val (x, y) = toVisit.removeFirst()
                visited.add(Pair(x, y))
                val adjacent = AOCUtils.getAdjacentPositions(heightMap, y, x)
                val sameBasinAdjacentPositions = adjacent.filter { p -> heightMap[p.second][p.first] != 9 }
                basinPositions.addAll(sameBasinAdjacentPositions)
                toVisit.addAll(sameBasinAdjacentPositions.filter { p -> !visited.contains(p) })
            }
            basinPositions.size
        }.sortedDescending().take(3).reduce { acc, i -> acc * i }
    }

    override fun getInput(): List<List<Int>> = AOCUtils.getDayInput(2021, 9).map {
        it.split("").filter { d -> d.isNotBlank() }.map { d -> d.toInt() }
    }

    private fun getDangerZones(heightMap: List<List<Int>>): List<List<Int>> =
        heightMap.mapIndexed { y, r ->
            r.mapIndexed { x, d ->
                if (AOCUtils.getAdjacentPositions(heightMap, y, x).minOf { heightMap[it.second][it.first] } > d) {
                    d + 1
                } else {
                    0
                }
            }
        }
}

fun main() {
    val day = Day9()
    println(day.runPartOne())
    println(day.runPartTwo())
}
