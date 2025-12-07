package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day8 : Day<List<String>, Int> {

    override fun runPartOne(): Int = computeCode().flatten().count { c -> c == '#' }

    override fun runPartTwo(): Int {
        val screen = computeCode()
        println(screen.joinToString("\n") { it.joinToString("") })
        return 0
    }

    private fun computeCode(): List<List<Char>> {
        val rowSize = 50
        val colSize = 6
        val screen = (0 until colSize).map {
            (0 until rowSize).map { '.' }.toMutableList()
        }.toMutableList()
        getInput().forEach {
            if (it.startsWith("rect")) {
                val (x, y) = it.replace("rect ", "").split("x")
                    .map { c -> c.toInt() }
                (0 until y).forEach { cy ->
                    (0 until x).forEach { cx ->
                        screen[cy][cx] = '#'
                    }
                }
            } else if (it.startsWith("rotate row")) {
                val (rowNumber, rotateCount) = it.replace("rotate row y=", "").split(" by ")
                    .map { c -> c.toInt() }
                val toBeReplaced = screen[rowNumber].toMutableList()
                (0 until rowSize).forEach { idx ->
                    screen[rowNumber][idx] = toBeReplaced[(idx - rotateCount).mod(rowSize)]
                }
            } else if (it.startsWith("rotate column")) {
                val (colNumber, rotateCount) = it.replace("rotate column x=", "").split(" by ")
                    .map { c -> c.toInt() }
                val toBeReplaced = (0 until colSize).map { idx -> screen[idx][colNumber] }
                (0 until colSize).forEach { idx ->
                    screen[idx][colNumber] = toBeReplaced[(idx - rotateCount).mod(colSize)]
                }
            }
        }
        return screen
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2016, 8)
}

fun main() {
    val day = Day8()
    println(day.runPartOne())
    println(day.runPartTwo())
}
