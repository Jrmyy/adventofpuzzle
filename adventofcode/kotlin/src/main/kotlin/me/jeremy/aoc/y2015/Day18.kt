package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day18 : Day<List<MutableList<Char>>, Int> {
    override fun runPartOne(): Int = simulate()

    override fun runPartTwo(): Int {
        val grid = getInput()
        grid[0][0] = '#'
        grid[grid.size - 1][0] = '#'
        grid[0][grid.first().size - 1] = '#'
        grid[grid.size - 1][grid.first().size - 1] = '#'
        return simulate(
            grid,
            listOf(
                Pair(0, 0),
                Pair(0, grid.first().size - 1),
                Pair(grid.size - 1, 0),
                Pair(grid.size - 1, grid.first().size - 1)
            )
        )
    }

    private fun simulate(
        initial: List<List<Char>> = getInput(),
        untouchable: List<Pair<Int, Int>> = listOf()
    ): Int {
        var grid = initial
        repeat(100) {
            val newGrid = grid.map { l -> l.toMutableList() }.toList()
            newGrid.forEachIndexed { y, r ->
                r.forEachIndexed { x, c ->
                    if (Pair(x, y) !in untouchable) {
                        val adjacent = AOCUtils.getAdjacentPositions(grid, y, x, true)
                            .count { p -> grid[p.second][p.first] == '#' }
                        if (c == '#' && adjacent !in (2..3)) {
                            newGrid[y][x] = '.'
                        } else if (c == '.' && adjacent == 3) {
                            newGrid[y][x] = '#'
                        }
                    }
                }
            }
            grid = newGrid
        }
        return grid.flatten().count { it == '#' }
    }

    override fun getInput(): List<MutableList<Char>> = AOCUtils.getDayInput(2015, 18)
        .map { it.toMutableList() }
}

fun main() {
    val day = Day18()
    println(day.runPartOne())
    println(day.runPartTwo())
}
