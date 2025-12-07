package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day8 : Day<List<List<Int>>, Int> {
    override fun runPartOne(): Int {
        val minLine = (
            getInput()
                .minByOrNull { it.count { that -> that == 0 } }
                ?: throw RuntimeException("")
            )
        return minLine.count { it == 1 } * minLine.count { it == 2 }
    }

    override fun runPartTwo(): Int {
        val finalLayer = mutableListOf<Int>()
        val layers = getInput()
        for (i in 0 until 25 * 6) {
            val coordElements = layers.map { it[i] }
            var currentCoord = coordElements[0]
            var j = 0
            while (currentCoord == 2) {
                j += 1
                currentCoord = coordElements[j]
            }
            finalLayer.add(currentCoord)
        }
        display(finalLayer)
        return 0
    }

    private fun display(l: List<Int>) {
        l.chunked(25).forEach {
            println(
                it.joinToString("") { that ->
                    if (that == 0) {
                        " "
                    } else {
                        "."
                    }
                }
            )
        }
    }

    override fun getInput(): List<List<Int>> = AOCUtils.getDayInput(2019, 8)[0]
        .toList()
        .map { it.toString().toInt() }
        .chunked(25 * 6)
}

fun main() {
    val day = Day8()
    println(day.runPartOne())
    day.runPartTwo()
}
