package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day2 : Day<List<List<Char>>, String> {

    override fun runPartOne(): String = getCode(
        mutableMapOf(
            Pair(0, 0) to '1',
            Pair(0, 1) to '2',
            Pair(0, 2) to '3',
            Pair(1, 0) to '4',
            Pair(1, 1) to '5',
            Pair(1, 2) to '6',
            Pair(2, 0) to '7',
            Pair(2, 1) to '8',
            Pair(2, 2) to '9',
        )
    )

    override fun runPartTwo(): String = getCode(
        mutableMapOf(
            Pair(0, 2) to '1',
            Pair(1, 1) to '2',
            Pair(1, 2) to '3',
            Pair(1, 3) to '4',
            Pair(2, 0) to '5',
            Pair(2, 1) to '6',
            Pair(2, 2) to '7',
            Pair(2, 3) to '8',
            Pair(2, 4) to '9',
            Pair(3, 1) to 'A',
            Pair(3, 2) to 'B',
            Pair(3, 3) to 'C',
            Pair(4, 2) to 'D',
        )
    )

    override fun getInput(): List<List<Char>> = AOCUtils.getDayInput(2016, 2).map { it.toList() }

    private fun getCode(pad: Map<Pair<Int, Int>, Char>): String {
        var position = Pair(1, 1)
        val code = mutableListOf<Char>()
        getInput().map {
            it.forEach { c ->
                val newPosition = when (c) {
                    'U' -> Pair(position.first - 1, position.second)
                    'D' -> Pair(position.first + 1, position.second)
                    'R' -> Pair(position.first, position.second + 1)
                    else -> Pair(position.first, position.second - 1)
                }
                if (pad.contains(newPosition)) {
                    position = newPosition
                }
            }
            code.add(pad[position]!!)
        }
        return code.joinToString("")
    }
}

fun main() {
    val day = Day2()
    println(day.runPartOne())
    println(day.runPartTwo())
}
