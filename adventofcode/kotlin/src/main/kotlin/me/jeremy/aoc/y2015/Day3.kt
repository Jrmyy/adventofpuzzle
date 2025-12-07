package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day3 : Day<List<Char>, Int> {
    override fun runPartOne(): Int {
        val instructions = getInput()
        var position = Pair(0, 0)
        val positions = mutableSetOf(position)
        instructions.forEach {
            position = updatePosition(it, position)
            positions.add(position)
        }
        return positions.size
    }

    override fun runPartTwo(): Int {
        val instructions = getInput()
        var santaPosition = Pair(0, 0)
        var roboSantaPosition = Pair(0, 0)
        val positions = mutableSetOf(santaPosition)
        instructions.forEachIndexed { i, it ->
            if (i % 2 == 0) {
                santaPosition = updatePosition(it, santaPosition)
                positions.add(santaPosition)
            } else {
                roboSantaPosition = updatePosition(it, roboSantaPosition)
                positions.add(roboSantaPosition)
            }
        }
        return positions.size
    }

    private fun updatePosition(char: Char, position: Pair<Int, Int>): Pair<Int, Int> =
        when (char) {
            '>' -> Pair(position.first + 1, position.second)
            '<' -> Pair(position.first - 1, position.second)
            '^' -> Pair(position.first, position.second + 1)
            'v' -> Pair(position.first, position.second - 1)
            else -> throw Exception("Wrong char")
        }

    override fun getInput(): List<Char> = AOCUtils.getDayInput(2015, 3).first().toList()
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
