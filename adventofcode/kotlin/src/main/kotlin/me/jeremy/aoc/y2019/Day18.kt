package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day18 : Day<List<MutableList<Char>>, Int> {

    data class Maze(
        private val starts: Set<Pair<Int, Int>>,
        private val keys: Map<Pair<Int, Int>, Char>,
        private val doors: Map<Pair<Int, Int>, Char>,
        private val openSpaces: Set<Pair<Int, Int>>
    ) {

        fun computeMinimumSteps(
            froms: Set<Pair<Int, Int>> = starts,
            collectedKeys: Set<Char> = mutableSetOf(),
            cache: MutableMap<Pair<Set<Pair<Int, Int>>, Set<Char>>, Int> = mutableMapOf()
        ): Int {
            val state = Pair(froms, collectedKeys)

            if (state in cache) return cache.getValue(state)

            val minStepsFrom = findReachableKeysFromPoints(froms, collectedKeys).map { alternative ->
                val (from, to, dist) = alternative.value
                dist + computeMinimumSteps(
                    (froms - from) + to,
                    collectedKeys + alternative.key,
                    cache
                )
            }.minOrNull() ?: 0
            cache[state] = minStepsFrom
            return minStepsFrom
        }

        private fun findReachableKeysFromPoints(
            froms: Set<Pair<Int, Int>>,
            haveKeys: Set<Char>
        ): Map<Char, Triple<Pair<Int, Int>, Pair<Int, Int>, Int>> =
            froms.flatMap { from ->
                findReachableKeysFromPoint(from, haveKeys).map { e ->
                    e.key to Triple(from, e.value.first, e.value.second)
                }
            }.toMap()

        private fun findReachableKeysFromPoint(
            from: Pair<Int, Int>,
            haveKeys: Set<Char> = mutableSetOf()
        ): Map<Char, Pair<Pair<Int, Int>, Int>> {
            val queue = ArrayDeque<Pair<Int, Int>>().apply { add(from) }
            val distance = mutableMapOf(from to 0)
            val keyDistance = mutableMapOf<Char, Pair<Pair<Int, Int>, Int>>()

            while (queue.isNotEmpty()) {
                val next = queue.removeFirst()
                getNeighbors(next)
                    .filter { it in openSpaces }
                    .filterNot { it in distance }
                    .forEach { point ->
                        distance[point] = distance[next]!! + 1
                        val door = doors[point]
                        val key = keys[point]
                        if (door == null || door.toLowerCase() in haveKeys) {
                            if (key != null && key !in haveKeys) {
                                keyDistance[key] = point to distance[point]!!
                            } else {
                                queue.add(point)
                            }
                        }
                    }
            }
            return keyDistance
        }

        private fun getNeighbors(position: Pair<Int, Int>): List<Pair<Int, Int>> = listOf(
            position.first to position.second - 1,
            position.first to position.second + 1,
            position.first + 1 to position.second,
            position.first - 1 to position.second
        )

        companion object {
            fun from(lines: List<List<Char>>): Maze {
                val starts = mutableSetOf<Pair<Int, Int>>()
                val keys = mutableMapOf<Pair<Int, Int>, Char>()
                val doors = mutableMapOf<Pair<Int, Int>, Char>()
                val openSpaces = mutableSetOf<Pair<Int, Int>>()

                lines.forEachIndexed { y, row ->
                    row.forEachIndexed { x, c ->
                        val place = x to y
                        if (c == '@') starts.add(place)
                        if (c != '#') openSpaces.add(place)
                        if (c in ('a'..'z')) keys[place] = c
                        if (c in ('A'..'Z')) doors[place] = c
                    }
                }
                return Maze(starts, keys, doors, openSpaces)
            }
        }
    }

    override fun runPartOne(): Int = Maze.from(getInput()).computeMinimumSteps()

    override fun runPartTwo(): Int {
        val rows = getInput().map { it.toMutableList() }
        val cy = rows.indexOfFirst { it.contains('@') }
        val cx = rows[cy].indexOfFirst { it == '@' }
        rows[cy][cx] = '#'
        rows[cy + 1][cx] = '#'
        rows[cy - 1][cx] = '#'
        rows[cy][cx + 1] = '#'
        rows[cy][cx - 1] = '#'
        rows[cy - 1][cx - 1] = '@'
        rows[cy + 1][cx - 1] = '@'
        rows[cy - 1][cx + 1] = '@'
        rows[cy + 1][cx + 1] = '@'
        return Maze.from(rows).computeMinimumSteps()
    }

    override fun getInput(): List<MutableList<Char>> = AOCUtils.getDayInput(2019, 18).map { it.toMutableList() }
}

fun main() {
    val day = Day18()
    println(day.runPartOne())
    println(day.runPartTwo())
}
