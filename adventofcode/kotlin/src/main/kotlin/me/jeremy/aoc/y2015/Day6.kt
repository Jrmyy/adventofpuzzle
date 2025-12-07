package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max

class Day6 : Day<List<Triple<String, Pair<Int, Int>, Pair<Int, Int>>>, Int> {
    override fun runPartOne(): Int {
        val lights = (0..999).map {
            (0..999).map { false }.toMutableList()
        }
        val instructions = getInput()
        instructions.forEach {
            val (op, from, to) = it
            when (op) {
                "turn on" -> {
                    (from.first..to.first).forEach { y ->
                        (from.second..to.second).forEach { x ->
                            lights[y][x] = true
                        }
                    }
                }
                "turn off" -> {
                    (from.first..to.first).forEach { y ->
                        (from.second..to.second).forEach { x ->
                            lights[y][x] = false
                        }
                    }
                }
                "toggle" -> {
                    (from.first..to.first).forEach { y ->
                        (from.second..to.second).forEach { x ->
                            lights[y][x] = !lights[y][x]
                        }
                    }
                }
            }
        }
        return lights.flatten().count { it }
    }

    override fun runPartTwo(): Int {
        val lights = (0..999).map {
            (0..999).map { 0 }.toMutableList()
        }
        val instructions = getInput()
        instructions.forEach {
            val (op, from, to) = it
            when (op) {
                "turn on" -> {
                    (from.first..to.first).forEach { y ->
                        (from.second..to.second).forEach { x ->
                            lights[y][x] += 1
                        }
                    }
                }
                "turn off" -> {
                    (from.first..to.first).forEach { y ->
                        (from.second..to.second).forEach { x ->
                            lights[y][x] = max(lights[y][x] - 1, 0)
                        }
                    }
                }
                "toggle" -> {
                    (from.first..to.first).forEach { y ->
                        (from.second..to.second).forEach { x ->
                            lights[y][x] += 2
                        }
                    }
                }
            }
        }
        return lights.flatten().sum()
    }

    override fun getInput(): List<Triple<String, Pair<Int, Int>, Pair<Int, Int>>> =
        AOCUtils.getDayInput(2015, 6).map {
            val match = "([\\w ]+) (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex().find(it)
                ?: throw Exception("Wrong line : $it")
            val groups = match.groupValues
            Triple(
                groups[1],
                Pair(groups[2].toInt(), groups[3].toInt()),
                Pair(groups[4].toInt(), groups[5].toInt())
            )
        }
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
