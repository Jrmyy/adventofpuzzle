package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day14 : Day<List<List<List<Int>>>, Int> {

    override fun runPartOne(): Int {
        val rocksPaths = getInput()
        val sandsPosition = mutableSetOf<List<Int>>()
        all@ while (true) {
            var sand = listOf(500, 0)
            var numSteps = 0
            sandWhile@ while (true) {
                val nextPosition = listOf(
                    listOf(sand.first(), sand.last() + 1),
                    listOf(sand.first() - 1, sand.last() + 1),
                    listOf(sand.first() + 1, sand.last() + 1),
                ).firstOrNull { nextPosition ->
                    rocksPaths.none { rockPaths ->
                        rockPaths.windowed(2, 1).any { rockPath ->
                            if (
                                rockPath.first().first() == rockPath.last().first() &&
                                nextPosition.first() == rockPath.first().first()
                            ) {
                                val lasts = rockPath.map { it.last() }
                                nextPosition.last() in (lasts.min()..lasts.max())
                            } else if (
                                rockPath.first().last() == rockPath.last().last() &&
                                nextPosition.last() == rockPath.first().last()
                            ) {
                                val firsts = rockPath.map { it.first() }
                                nextPosition.first() in (firsts.min()..firsts.max())
                            } else {
                                false
                            }
                        }
                    } && nextPosition !in sandsPosition
                }
                if (nextPosition != null) {
                    sand = nextPosition
                    numSteps++
                    if (numSteps == 200) {
                        break@all
                    }
                } else {
                    sandsPosition.add(sand)
                    break@sandWhile
                }
            }
        }
        return sandsPosition.size
    }

    override fun runPartTwo(): Int {
        val rocksPaths = getInput()
        val maxLast = rocksPaths.flatMap { it.map { l -> l.last() } }.max() + 2
        val grid = (0 until maxLast).map {
            (-maxLast - 1..maxLast + 1).map { '.' }.toMutableList()
        }.toMutableList()
        grid.add((-maxLast - 1..maxLast + 1).map { '#' }.toMutableList())
        val middle = grid.first().size / 2
        for (rockPaths in rocksPaths) {
            rockPaths.windowed(2, 1).forEach { rockPath ->
                if (rockPath.first().first() == rockPath.last().first()) {
                    val lasts = rockPath.map { it.last() }
                    (lasts.min()..lasts.max()).forEach {
                        grid[it][middle - (500 - rockPath.first().first())] = '#'
                    }
                } else if (rockPath.first().last() == rockPath.last().last()) {
                    val firsts = rockPath.map { it.first() }
                    (firsts.min()..firsts.max()).forEach {
                        grid[rockPath.first().last()][middle - (500 - it)] = '#'
                    }
                }
            }
        }
        grid[0][middle] = 'o'
        for (y in 1 until maxLast) {
            for (xi in -y..y) {
                val x = middle + xi
                if (
                    grid[y][x] == '.' &&
                    listOf(grid[y - 1][x], grid[y - 1][x - 1], grid[y - 1][x + 1]).any { it == 'o' }
                ) {
                    grid[y][x] = 'o'
                }
            }
        }
        return grid.flatten().count { it == 'o' }
    }

    override fun getInput(): List<List<List<Int>>> = AOCUtils.getDayInput(2022, 14).map {
        it.split(" -> ").map { p -> p.split(",").map { i -> i.toInt() } }
    }
}

fun main() {
    val day = Day14()
    println(day.runPartOne())
    println(day.runPartTwo())
}
