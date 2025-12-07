package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day15 : Day<List<Int>, Int> {
    override fun runPartOne(): Int = run(2020)

    override fun runPartTwo(): Int = run(30000000)

    override fun getInput(): List<Int> =
        AOCUtils.getDayInput(2020, 15)[0].split(",").map { it.toInt() }

    private fun run(to: Int): Int {
        val initialValues = getInput().toList()
        val valuesAndTurns = initialValues.dropLast(1).mapIndexed { index, i ->
            Pair(i, mutableListOf(index))
        }.toMap().toMutableMap()
        var lastNumber = initialValues.last()
        (initialValues.size - 1 until to - 1).forEach {
            if (valuesAndTurns.containsKey(lastNumber)) {
                valuesAndTurns[lastNumber]!!.add(it)
                val positions = valuesAndTurns[lastNumber]!!
                lastNumber = positions.last() - positions[positions.size - 2]
            } else {
                valuesAndTurns[lastNumber] = mutableListOf(it)
                lastNumber = 0
            }
        }
        return lastNumber
    }
}

fun main() {
    val day = Day15()
    println(day.runPartOne())
    println(day.runPartTwo())
}
