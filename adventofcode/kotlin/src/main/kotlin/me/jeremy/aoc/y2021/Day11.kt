package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day11 : Day<List<List<Int>>, Int> {
    override fun runPartOne(): Int {
        val octopuses = getInput()
        var flashes = 0
        repeat(100) {
            val (stepFlashes, _) = simulateStep(octopuses)
            flashes += stepFlashes
        }
        return flashes
    }

    override fun runPartTwo(): Int {
        val octopuses = getInput()
        var step = 0
        var hasAllFlashed = false
        while (!hasAllFlashed) {
            val (_, flashedCount) = simulateStep(octopuses)
            step++
            hasAllFlashed = flashedCount == octopuses.size * octopuses.first().size
        }
        return step
    }

    override fun getInput(): List<MutableList<Int>> = AOCUtils.getDayInput(2021, 11).map {
        it.toList().map { c -> c.toString().toInt() }.toMutableList()
    }

    private fun simulateStep(octopuses: List<MutableList<Int>>): Pair<Int, Int> {
        var stepFlashes = 0
        val flashed = mutableListOf<Pair<Int, Int>>()
        octopuses.forEachIndexed { y, row ->
            row.forEachIndexed { x, i ->
                if (i <= 9 && !flashed.contains(Pair(x, y))) {
                    octopuses[y][x] = i + 1
                }
                if (i >= 9) {
                    stepFlashes++
                    flashed.add(Pair(x, y))
                    octopuses[y][x] = 0
                    AOCUtils.getAdjacentPositions(octopuses, y, x, true).forEach { aXY ->
                        val (ax, ay) = aXY
                        if (octopuses[ay][ax] <= 9 && !flashed.contains(Pair(ax, ay))) {
                            octopuses[ay][ax] = octopuses[ay][ax] + 1
                        }
                    }
                }
            }
        }
        val toFlash = octopuses.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, i -> if (i == 10) Pair(x, y) else null }
        }.toMutableList()
        while (toFlash.isNotEmpty()) {
            val (tx, ty) = toFlash.removeFirst()
            octopuses[ty][tx] = 0
            stepFlashes++
            flashed.add(Pair(tx, ty))
            AOCUtils.getAdjacentPositions(octopuses, ty, tx, true).map { aXY ->
                val (ax, ay) = aXY
                if (octopuses[ay][ax] <= 9 && !flashed.contains(Pair(ax, ay))) {
                    octopuses[ay][ax] = octopuses[ay][ax] + 1
                    if (octopuses[ay][ax] == 10) {
                        toFlash.add(aXY)
                    }
                }
            }
        }
        return Pair(stepFlashes, flashed.size)
    }
}

fun main() {
    val day = Day11()
    println(day.runPartOne())
    println(day.runPartTwo())
}
