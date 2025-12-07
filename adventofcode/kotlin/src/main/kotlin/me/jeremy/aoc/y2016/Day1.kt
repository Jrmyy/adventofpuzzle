package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs

class Day1 : Day<List<Pair<Char, Int>>, Int> {
    override fun runPartOne(): Int {
        var current = Pair(0, 0)
        var orientation = 'N'
        val orientations = listOf('N', 'E', 'S', 'W')
        getInput().forEach {
            val (direction, distance) = it
            orientation = orientations[
                (orientations.indexOf(orientation) + (if (direction == 'R') 1 else - 1)).mod(orientations.size)
            ]
            current = when (orientation) {
                'N' -> Pair(current.first - distance, current.second)
                'E' -> Pair(current.first, current.second + distance)
                'S' -> Pair(current.first + distance, current.second)
                else -> Pair(current.first, current.second - distance)
            }
        }
        return abs(current.first) + abs(current.second)
    }

    override fun runPartTwo(): Int {
        var current = Pair(0, 0)
        val instructions = getInput()
        val positions = mutableSetOf<Pair<Int, Int>>()
        var orientation = 'N'
        val orientations = listOf('N', 'E', 'S', 'W')
        var cameTwice = false
        var i = 0
        while (!cameTwice) {
            val (direction, distance) = instructions[i % instructions.size]
            orientation = orientations[
                (orientations.indexOf(orientation) + (if (direction == 'R') 1 else - 1)).mod(orientations.size)
            ]
            val path = when (orientation) {
                'N' -> (1..distance).map { Pair(current.first + it, current.second) }
                'E' -> (1..distance).map { Pair(current.first, current.second + it) }
                'S' -> (1..distance).map { Pair(current.first - it, current.second) }
                else -> (1..distance).map { Pair(current.first, current.second - it) }
            }
            i++
            for (position in path) {
                current = position
                cameTwice = !positions.add(position)
                if (cameTwice) {
                    break
                }
            }
        }
        return abs(current.first) + abs(current.second)
    }

    override fun getInput(): List<Pair<Char, Int>> = AOCUtils.getDayInput(2016, 1).first()
        .split(", ")
        .map { Pair(it.first(), it.substring(1).toInt()) }
}

fun main() {
    val day = Day1()
    println(day.runPartOne())
    println(day.runPartTwo())
}
