package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day6 : Day<List<List<List<Char>>>, Int> {
    override fun runPartOne(): Int =
        getInput()
            .sumOf {
                it.flatten().distinct().size
            }

    override fun runPartTwo(): Int =
        getInput().sumOf {
            it.flatten()
                .groupingBy { that -> that }
                .eachCount()
                .filterValues { that -> that == it.size }
                .count()
        }

    override fun getInput(): List<List<List<Char>>> {
        val lines = AOCUtils.getDayInput(2020, 6)
        val currentGroup = mutableListOf<String>()
        val groups = mutableListOf<List<List<Char>>>()
        for (line in lines) {
            if (line == "") {
                groups.add(
                    currentGroup.map {
                        it.toList()
                    }
                )
                currentGroup.clear()
            } else {
                currentGroup.add(line)
            }
        }
        if (currentGroup.size > 0) {
            groups.add(
                currentGroup.map {
                    it.toList()
                }
            )
        }
        return groups
    }
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
