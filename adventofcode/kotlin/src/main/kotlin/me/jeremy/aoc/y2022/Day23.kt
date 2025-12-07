package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.util.*

class Day23 : Day<MutableList<MutableList<Char>>, Int> {

    override fun runPartOne(): Int = simulate()

    override fun runPartTwo(): Int = simulate(Int.MAX_VALUE)

    override fun getInput(): MutableList<MutableList<Char>> = AOCUtils.getDayInput(2022, 23).map {
        it.toMutableList()
    }.toMutableList()

    private fun simulate(maxRounds: Int = 10): Int {
        val grid = getInput()
        var turn = 0
        while (turn < maxRounds) {
            val dir = turn % DIRECTIONS.size
            val moves = mutableMapOf<Pair<Int, Int>, List<Pair<Int, Int>>>()
            if (grid.first().contains('#')) {
                grid.add(0, grid.first().indices.map { '.' }.toMutableList())
            }
            if (grid.last().contains('#')) {
                grid.add(grid.first().indices.map { '.' }.toMutableList())
            }
            if (grid.any { r -> r.first() == '#' }) {
                grid.forEach { r -> r.add(0, '.') }
            }
            if (grid.any { r -> r.last() == '#' }) {
                grid.forEach { r -> r.add('.') }
            }
            grid.forEachIndexed { y, r ->
                r.forEachIndexed { x, c ->
                    if (c == '#') {
                        val nD = listOf(Pair(x, y - 1), Pair(x - 1, y - 1), Pair(x + 1, y - 1))
                        val sD = listOf(Pair(x, y + 1), Pair(x - 1, y + 1), Pair(x + 1, y + 1))
                        val wD = listOf(Pair(x - 1, y), Pair(x - 1, y - 1), Pair(x - 1, y + 1))
                        val eD = listOf(Pair(x + 1, y), Pair(x + 1, y - 1), Pair(x + 1, y + 1))
                        val toConsider = listOf(nD, sD, wD, eD)
                        Collections.rotate(toConsider, -dir)
                        if (toConsider.flatten().toSet().filter { p ->
                            p.first in grid.first().indices && p.second in grid.indices
                        }.any { p -> grid[p.second][p.first] == '#' }
                        ) {
                            var j = 0
                            while (j < DIRECTIONS.size) {
                                val alt = toConsider[j]
                                if (alt.filter { p ->
                                    p.first in grid.first().indices && p.second in grid.indices
                                }.all { p ->
                                        grid.getOrElse(p.second) { listOf() }.getOrElse(p.first) { '.' } == '.'
                                    }
                                ) {
                                    val moveTo = Pair(
                                        x + DIRECTIONS[(dir + j) % 4].first,
                                        y + DIRECTIONS[(dir + j) % 4].second
                                    )
                                    moves[moveTo] = moves.getOrDefault(moveTo, listOf()) + listOf(Pair(x, y))
                                    break
                                }
                                j++
                            }
                        }
                    }
                }
            }
            val filteredMoves = moves.filterValues { e -> e.size == 1 }
            if (filteredMoves.isEmpty()) {
                return turn + 1
            }
            filteredMoves.forEach { e ->
                val new = e.key
                val old = e.value.first()
                grid[new.second][new.first] = '#'
                grid[old.second][old.first] = '.'
            }
            turn++
        }
        val minY = grid.indexOfFirst { it.contains('#') }
        val maxY = grid.indexOfLast { it.contains('#') }
        val minX = grid.filter { it.contains('#') }.minOf { it.indexOf('#') }
        val maxX = grid.maxOf { it.indexOfLast { c -> c == '#' } }
        return (maxX - minX + 1) * (maxY - minY + 1) - grid.flatten().count { c -> c == '#' }
    }

    companion object {
        val DIRECTIONS = listOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0))
    }
}

fun main() {
    val day = Day23()
    println(day.runPartOne())
    println(day.runPartTwo())
}
