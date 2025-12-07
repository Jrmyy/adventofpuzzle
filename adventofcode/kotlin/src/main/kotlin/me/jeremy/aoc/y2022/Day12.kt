package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day12 : Day<List<MutableList<Char>>, Int> {

    override fun runPartOne(): Int {
        val grid = getInput()
        val toY = grid.indexOfFirst { it.contains('E') }
        val toX = grid[toY].indexOfFirst { it == 'E' }
        val fromY = grid.indexOfFirst { it.contains('S') }
        val fromX = grid[fromY].indexOfFirst { it == 'S' }
        grid[fromY][fromX] = 'a'
        grid[toY][toX] = 'z'
        return getMinStep(grid, Pair(fromX, fromY), Pair(toX, toY))
    }

    override fun runPartTwo(): Int {
        val grid = getInput()
        val toY = grid.indexOfFirst { r -> r.contains('E') }
        val toX = grid[toY].indexOfFirst { c -> c == 'E' }
        val fromY = grid.indexOfFirst { it.contains('S') }
        val fromX = grid[fromY].indexOfFirst { it == 'S' }
        grid[fromY][fromX] = 'a'
        grid[toY][toX] = 'z'
        return grid.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                if (c == 'a') Pair(x, y) else null
            }
        }.flatten().map {
            getMinStep(grid, Pair(it.first, it.second), Pair(toX, toY))
        }.filter { it > 0 }.min()
    }

    override fun getInput(): List<MutableList<Char>> = AOCUtils.getDayInput(2022, 12)
        .map { it.toMutableList() }.toMutableList()

    private fun getMinStep(
        grid: List<List<Char>>,
        from: Pair<Int, Int>,
        to: Pair<Int, Int>
    ): Int {
        // https://en.wikipedia.org/wiki/Breadth-first_search
        val queue: ArrayDeque<Pair<Int, Int>> = ArrayDeque(listOf(from))
        val checked = grid.map { it.map { false }.toMutableList() }
        val dist = grid.map { it.map { 0 }.toMutableList() }
        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()
            listOf(
                Pair(curr.first - 1, curr.second),
                Pair(curr.first + 1, curr.second),
                Pair(curr.first, curr.second - 1),
                Pair(curr.first, curr.second + 1),
            )
                .filter {
                    it.first in grid.first().indices &&
                        it.second in grid.indices &&
                        !checked[it.second][it.first] &&
                        grid[it.second][it.first].code - grid[curr.second][curr.first].code <= 1
                }
                .forEach {
                    dist[it.second][it.first] = dist[curr.second][curr.first] + 1
                    checked[it.second][it.first] = true
                    queue.add(Pair(it.first, it.second))
                }
        }
        return dist[to.second][to.first]
    }
}

fun main() {
    val day = Day12()
    println(day.runPartOne())
    println(day.runPartTwo())
}
