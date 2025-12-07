package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day3 : Day<List<List<Int>>, Int> {

    override fun runPartOne(): Int {
        val inputs = getInput()
        val commons = (0 until inputs.first().size).map {
            val row = inputs.map { l -> l[it] }
            val eachCount = row.groupingBy { i -> i }.eachCount()
            if (eachCount[0]!! > eachCount[1]!!) {
                Pair(1, 0)
            } else {
                Pair(0, 1)
            }
        }
        val binaries = commons.fold(Pair("", "")) { acc, pair ->
            Pair(acc.first.plus(pair.first), acc.second.plus(pair.second))
        }
        return Integer.parseInt(binaries.first, 2) * Integer.parseInt(binaries.second, 2)
    }

    override fun runPartTwo(): Int {
        val inputs = getInput()

        var currentIdx = 0
        var carbon = inputs.toList()
        while (carbon.size > 1) {
            val row = carbon.map { l -> l[currentIdx] }
            val eachCount = row.groupingBy { i -> i }.eachCount()
            val mostCommon = if (eachCount[0]!! > eachCount[1]!!) 0 else 1
            carbon = carbon.filter { it[currentIdx] == mostCommon }
            currentIdx += 1
        }

        currentIdx = 0
        var oxygen = inputs.toList()
        while (oxygen.size > 1) {
            val row = oxygen.map { l -> l[currentIdx] }
            val eachCount = row.groupingBy { i -> i }.eachCount()
            val leastCommon = if (eachCount[1]!! >= eachCount[0]!!) 0 else 1
            oxygen = oxygen.filter { it[currentIdx] == leastCommon }
            currentIdx += 1
        }

        return Integer.parseInt(
            carbon[0].joinToString(""), 2
        ) * Integer.parseInt(
            oxygen[0].joinToString(""), 2
        )
    }

    override fun getInput(): List<List<Int>> = AOCUtils.getDayInput(2021, 3).map {
        it.toList().map { i -> i.toString().toInt() }
    }
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
