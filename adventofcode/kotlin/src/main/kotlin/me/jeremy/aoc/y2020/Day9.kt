package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day9 : Day<List<Long>, Long> {
    override fun runPartOne(): Long {
        val figures = getInput()
        val figuresToTest = figures.subList(25, figures.size)
        figuresToTest.mapIndexed { index, it ->
            val toCheck = figures.subList(index, index + 25)
            val res = toCheck.mapIndexed { toCheckIdx, _ ->
                isSumOfTwo(it, toCheck, toCheckIdx)
            }.any { it }
            if (!res) {
                return it
            }
        }
        throw RuntimeException("All numbers are a sum of 2 numbers in the first 25")
    }

    override fun runPartTwo(): Long {
        val weakness = runPartOne()
        val figures = getInput()
        var currentIdx = 0
        var currentSumIdx = 0
        var currentCount = 0L
        while (currentIdx < figures.size) {
            currentCount += figures[currentSumIdx]
            currentSumIdx += 1
            if (currentSumIdx == figures.size || currentCount > weakness) {
                currentIdx += 1
                currentSumIdx = currentIdx
                currentCount = 0
            }
            if (currentCount == weakness) {
                val contiguousSum = figures.subList(currentIdx, currentSumIdx + 1).sorted()
                return contiguousSum[0] + contiguousSum[contiguousSum.size - 1]
            }
        }
        throw RuntimeException("No contiguous sum")
    }

    private fun isSumOfTwo(fig: Long, figures: List<Long>, idxForCut: Int): Boolean {
        if (idxForCut == 0) {
            return false
        }
        val middle = figures[idxForCut]
        return figures.subList(0, idxForCut).contains(fig - middle) ||
            figures.subList(idxForCut, figures.size).contains(fig - middle)
    }

    override fun getInput(): List<Long> = AOCUtils.getDayInput(2020, 9).map { it.toLong() }
}

fun main() {
    val day = Day9()
    println(day.runPartOne())
    println(day.runPartTwo())
}
