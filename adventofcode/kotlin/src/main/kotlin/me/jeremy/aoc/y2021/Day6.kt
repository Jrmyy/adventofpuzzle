package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day6 : Day<List<Int>, Long> {

    override fun runPartOne(): Long = countReproductedLanternfish(80)

    override fun runPartTwo(): Long = countReproductedLanternfish(256)

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2021, 6)[0].split(",")
        .map { it.toInt() }

    private fun countReproductedLanternfish(nbIteration: Int): Long {
        val lanternfish = getInput().toMutableList()
        val reproductionTable = (1..nbIteration).map {
            if (it <= nbIteration - FIRST_REPRODUCTION_PERIOD) {
                1 + (nbIteration - (it + 9)) / REPRODUCTION_PERIOD
            } else {
                0
            }.toLong()
        }.toMutableList()
        for (i in reproductionTable.indices.reversed()) {
            if (i < nbIteration - FIRST_REPRODUCTION_PERIOD) {
                val nbAfter = (nbIteration - 1 - (i + FIRST_REPRODUCTION_PERIOD)) / REPRODUCTION_PERIOD
                val whenProduced = listOf(i + FIRST_REPRODUCTION_PERIOD) + (0 until nbAfter).map {
                    i + FIRST_REPRODUCTION_PERIOD + (it + 1) * REPRODUCTION_PERIOD
                }
                reproductionTable[i] = whenProduced.sumOf { reproductionTable[it] } + reproductionTable[i]
            }
        }
        val producedByInitial = lanternfish.indices.associateWith { 0L }.toMutableMap()
        repeat((1..nbIteration).count()) {
            lanternfish.forEachIndexed { index, i ->
                if (i == 0) {
                    lanternfish[index] = 6
                    producedByInitial[index] = producedByInitial[index]?.plus(reproductionTable[it] + 1L) ?: 0L
                } else {
                    lanternfish[index] = i - 1
                }
            }
        }
        return producedByInitial.values.sum() + lanternfish.size
    }

    companion object {
        const val FIRST_REPRODUCTION_PERIOD: Int = 9
        const val REPRODUCTION_PERIOD: Int = 7
    }
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
