package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs
import kotlin.math.max

class Day3 : Day<List<List<Day3.Instruction>>, Int> {

    data class Instruction(
        val direction: Char,
        val distance: Int
    )

    override fun runPartOne(): Int {
        val wirePaths = getPaths()
        return wirePaths.second
            .intersect(wirePaths.third)
            .minOfOrNull { abs(it.first - wirePaths.first.first) + abs(it.second - wirePaths.first.second) }
            ?: throw RuntimeException("No cross :(")
    }

    override fun runPartTwo(): Int {
        val wirePaths = getPaths()
        return wirePaths.second
            .intersect(wirePaths.third)
            .minOfOrNull { (wirePaths.second.indexOf(it) + 1) + (wirePaths.third.indexOf(it) + 1) }
            ?: throw RuntimeException("No cross :(")
    }

    override fun getInput(): List<List<Instruction>> = AOCUtils.getDayInput(2019, 3).map {
        it.split(",").map { that -> Instruction(that[0], that.drop(1).toInt()) }
    }

    private fun getPaths(): Triple<Pair<Int, Int>, List<Pair<Int, Int>>, List<Pair<Int, Int>>> {
        val wiresInstructions = getInput()
        val firstWire = wiresInstructions[0]
        val secondWire = wiresInstructions[1]
        val maxSize = max(
            firstWire.maxOf { it.distance },
            secondWire.maxOf { it.distance }
        )
        val initialPos = Pair(maxSize, maxSize)
        var currentFirstPos = Pair(maxSize, maxSize)
        var currentSecondPos = Pair(maxSize, maxSize)
        val firstPositions = firstWire.flatMap {
            val subPath = doMove(currentFirstPos, it)
            currentFirstPos = subPath.last()
            subPath
        }
        val secondPositions = secondWire.flatMap {
            val subPath = doMove(currentSecondPos, it)
            currentSecondPos = subPath.last()
            subPath
        }
        return Triple(initialPos, firstPositions, secondPositions)
    }

    private fun doMove(pos: Pair<Int, Int>, instruction: Instruction): List<Pair<Int, Int>> =
        when (instruction.direction) {
            'R' -> (1..instruction.distance).map { Pair(pos.first + it, pos.second) }
            'L' -> (1..instruction.distance).map { Pair(pos.first - it, pos.second) }
            'U' -> (1..instruction.distance).map { Pair(pos.first, pos.second - it) }
            'D' -> (1..instruction.distance).map { Pair(pos.first, pos.second + it) }
            else -> throw RuntimeException("Wrong direction : ${instruction.direction}")
        }
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
