package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day16 : Day<List<Int>, String> {
    override fun runPartOne(): String {
        val basePattern = listOf(0, 1, 0, -1)
        var currentRun = 1
        var currentFigures = getInput().toList()
        while (currentRun <= 100) {
            val runRes = (1..currentFigures.size).map { step ->
                val currentPattern = basePattern.flatMap {
                    (1..step).map { _ -> it }
                }
                var currentIdx = 1
                currentFigures.map {
                    val multiplied = currentPattern[currentIdx] * it
                    currentIdx = (currentIdx + 1) % currentPattern.size
                    multiplied
                }.sum().toString().reversed()[0].toString().toInt()
            }
            currentFigures = runRes.toList()
            currentRun++
        }
        return currentFigures.subList(0, 8).joinToString("")
    }

    override fun runPartTwo(): String {
        val initialFigures = getInput()
        val augmentedInitialFigures = (1..10000).flatMap {
            initialFigures.toList()
        }
        val offset = initialFigures.subList(0, 7).joinToString("").toInt()
        // This solution only works if the offset is > size / 2 because we are in case we have only ones
        // Doing the full calculation is too long
        if (offset < augmentedInitialFigures.size / 2) {
            throw RuntimeException(":(")
        }
        var currentRun = 1
        var currentFigures = augmentedInitialFigures.toList().subList(offset, augmentedInitialFigures.size)
        while (currentRun <= 100) {
            var currentIdx = augmentedInitialFigures.size - 1
            val nextFigures = mutableListOf(currentFigures.last())
            while (currentIdx >= offset) {
                nextFigures.add((currentFigures[currentIdx - offset] + nextFigures.last()) % 10)
                currentIdx -= 1
            }
            currentFigures = nextFigures.reversed().toList()
            currentRun += 1
            nextFigures.clear()
        }
        return currentFigures.subList(0, 8).joinToString("")
    }

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2019, 16)[0].toList().map {
        it.toString().toInt()
    }
}

fun main() {
    val day = Day16()
    println(day.runPartOne())
    println(day.runPartTwo())
}
