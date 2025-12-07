package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day11 : Day<Int, String> {
    override fun runPartOne(): String {
        val fuelGrid = getFuelGrid()
        return (0 until 298).flatMap { y ->
            (0 until 298).map { x ->
                Pair(
                    listOf(x + 1, y + 1),
                    fuelGrid[y].subList(x, x + 3).sum() +
                        fuelGrid[y + 1].subList(x, x + 3).sum() +
                        fuelGrid[y + 2].subList(x, x + 3).sum()
                )
            }
        }.maxByOrNull { it.second }!!.first.joinToString(",")
    }

    override fun runPartTwo(): String {
        val fuelGrid = getFuelGrid()
        val dimensions = hashMapOf<List<Int>, Int>()
        (1 until 300).forEach { n ->
            (0 until 300 - n).forEach { y ->
                (0 until 300 - n).forEach { x ->
                    if (n > 1) {
                        var count = dimensions[listOf(x + 1, y + 1, n - 1)]!!
                        repeat(n) {
                            count += fuelGrid[y + n - 1][x + it]
                        }
                        repeat(n - 1) {
                            count += fuelGrid[y + it][x + n - 1]
                        }
                        dimensions[listOf(x + 1, y + 1, n)] = count
                    } else {
                        dimensions[listOf(x + 1, y + 1, n)] = fuelGrid[y][x]
                    }
                }
            }
        }
        return dimensions.maxByOrNull { it.value }!!.key.joinToString(",")
    }

    override fun getInput(): Int = AOCUtils.getDayInput(2018, 11).first().toInt()

    private fun getFuelGrid(): ArrayDeque<ArrayDeque<Int>> {
        val serialNumber = getInput()
        return ArrayDeque(
            (0 until 300).map { y ->
                ArrayDeque(
                    (0 until 300).map { x ->
                        val rackId = (x + 1 + 10)
                        (((rackId * (y + 1) + serialNumber) * rackId) / 100).toString().last().toString().toInt() - 5
                    }
                )
            }
        )
    }
}

fun main() {
    val day = Day11()
    println(day.runPartOne())
    println(day.runPartTwo())
}
