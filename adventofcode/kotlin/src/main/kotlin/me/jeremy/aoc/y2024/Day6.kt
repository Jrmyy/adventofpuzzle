package me.jeremy.aoc.y2024

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day6 : Day<List<MutableList<Char>>, Int> {

    class StuckInLoopException : Exception()

    private val directions = listOf(
        Pair(0, -1),
        Pair(1, 0),
        Pair(0, 1),
        Pair(-1, 0),
    )

    override fun runPartOne(): Int = patrol(getInput())

    override fun runPartTwo(): Int {
        val map = getInput()
        var loops = 0
        for (y in map.indices) {
            for (x in map.indices) {
                val newMap = map.map { it.toMutableList() }.toList()
                newMap[y][x] = '#'
                try {
                    patrol(newMap)
                } catch (e: StuckInLoopException) {
                    loops++
                }
            }
        }
        return loops
    }

    override fun getInput(): List<MutableList<Char>> = AOCUtils.getDayInput(2024, 6).map {
        it.toList().toMutableList()
    }

    private fun patrol(map: List<MutableList<Char>>): Int {
        var curr = Pair(-1, -1)
        var dirIdx = 0
        val visited = mutableSetOf<Pair<Int, Int>>()
        // Approximation to prevent from finding loops .... 😬
        val maxSteps = 20_000

        mainLoop@ for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == '^') {
                    curr = Pair(x, y)
                    map[y][x] = '.'
                    break@mainLoop
                }
            }
        }

        visited.add(curr)
        var steps = 0

        while (steps < maxSteps) {
            val dir = directions[dirIdx]
            val next = Pair(curr.first + dir.first, curr.second + dir.second)
            if (next.first !in map[0].indices || next.second !in map.indices) {
                break
            }
            val c = map[next.second][next.first]
            if (c == '#') {
                dirIdx = (dirIdx + 1) % 4
            } else {
                curr = next
            }
            visited.add(curr)
            steps++
        }

        if (steps == maxSteps) {
            throw StuckInLoopException()
        }

        return visited.size
    }
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
