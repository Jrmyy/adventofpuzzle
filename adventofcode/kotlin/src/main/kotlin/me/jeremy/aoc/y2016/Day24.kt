package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day24 : Day<List<MutableList<Char>>, Int> {

    data class Interest(
        val position: Pair<Int, Int>,
        val name: Char,
        val priority: Int = 1
    )

    data class Maze(
        private val start: Pair<Int, Int>,
        private val interests: Set<Interest>,
        private val openSpaces: Set<Pair<Int, Int>>
    ) {

        fun computeMinimumSteps(
            from: Pair<Int, Int> = start,
            visitedInterests: Set<Interest> = mutableSetOf(),
            cache: MutableMap<Pair<Pair<Int, Int>, Set<Interest>>, Int> = mutableMapOf()
        ): Int {
            val state = Pair(from, visitedInterests)

            if (state in cache) return cache.getValue(state)

            val minStepsFrom = findReachableInterestFromPoint(from, visitedInterests).map { alternative ->
                val (to, dist) = alternative.value
                dist + computeMinimumSteps(
                    to,
                    visitedInterests + alternative.key,
                    cache
                )
            }.minOrNull() ?: 0
            cache[state] = minStepsFrom
            return minStepsFrom
        }

        private fun findReachableInterestFromPoint(
            from: Pair<Int, Int>,
            visited: Set<Interest> = mutableSetOf()
        ): Map<Interest, Pair<Pair<Int, Int>, Int>> {
            val queue = ArrayDeque<Pair<Int, Int>>().apply { add(from) }
            val distance = mutableMapOf(from to 0)
            val interestDistance = mutableMapOf<Interest, Pair<Pair<Int, Int>, Int>>()
            val priorityFilter = interests.filter { it !in visited }.maxOfOrNull { it.priority } ?: 0

            while (queue.isNotEmpty()) {
                val next = queue.removeFirst()
                getNeighbors(next)
                    .filter { it in openSpaces }
                    .filterNot { it in distance }
                    .forEach { point ->
                        distance[point] = distance[next]!! + 1
                        val interest = interests.firstOrNull { it.position == point }
                        if (interest != null && interest !in visited && interest.priority >= priorityFilter) {
                            interestDistance[interest] = point to distance[point]!!
                        } else {
                            queue.add(point)
                        }
                    }
            }
            return interestDistance
        }

        private fun getNeighbors(position: Pair<Int, Int>): List<Pair<Int, Int>> = listOf(
            position.first to position.second - 1,
            position.first to position.second + 1,
            position.first + 1 to position.second,
            position.first - 1 to position.second
        )

        companion object {
            fun from(lines: List<List<Char>>, goBack: Boolean = false): Maze {
                var start: Pair<Int, Int>? = null
                val interests = mutableSetOf<Interest>()
                val openSpaces = mutableSetOf<Pair<Int, Int>>()

                lines.forEachIndexed { y, row ->
                    row.forEachIndexed { x, c ->
                        val place = x to y
                        if (c == '0') {
                            start = place
                            if (goBack) {
                                interests.add(Interest(place, c, 0))
                            }
                        }
                        if (c != '#') openSpaces.add(place)
                        if (c in ('1'..'9')) interests.add(Interest(place, c))
                    }
                }
                return Maze(start!!, interests, openSpaces)
            }
        }
    }

    override fun runPartOne(): Int = Maze.from(getInput()).computeMinimumSteps()

    override fun runPartTwo(): Int = Maze.from(getInput(), true).computeMinimumSteps()

    override fun getInput(): List<MutableList<Char>> = AOCUtils.getDayInput(2016, 24).map { it.toMutableList() }
}

fun main() {
    val day = Day24()
    println(day.runPartOne())
    println(day.runPartTwo())
}
