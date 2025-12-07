package me.jeremy.aoc.y2017

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day6 : Day<MutableList<Int>, Int> {

    override fun runPartOne(): Int {
        val banks = getInput()
        val seen = mutableSetOf(banks)
        while (true) {
            val newBanks = banks.toMutableList()
            val (idx, count) = banks.withIndex().maxBy { it.value }
            newBanks[idx] = 0
            var i = 1
            while (i <= count) {
                newBanks[(idx + i) % newBanks.size] = newBanks[(idx + i) % newBanks.size] + 1
                i++
            }
            if (!seen.add(newBanks)) {
                break
            }
            banks.clear()
            banks.addAll(newBanks)
        }
        return seen.size
    }

    override fun runPartTwo(): Int {
        val banks = getInput()
        val seen = mutableListOf(banks)
        var found = -1
        while (found == -1) {
            val newBanks = banks.toMutableList()
            val (idx, count) = banks.withIndex().maxBy { it.value }
            newBanks[idx] = 0
            var i = 1
            while (i <= count) {
                newBanks[(idx + i) % newBanks.size] = newBanks[(idx + i) % newBanks.size] + 1
                i++
            }
            found = seen.indexOf(newBanks)
            seen.add(newBanks)
            banks.clear()
            banks.addAll(newBanks)
        }
        return seen.size - 1 - found
    }

    override fun getInput(): MutableList<Int> =
        "(\\d+)".toRegex().findAll(
            AOCUtils.getDayInput(2017, 6).first()
        ).map { it.value.toInt() }.toMutableList()
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
