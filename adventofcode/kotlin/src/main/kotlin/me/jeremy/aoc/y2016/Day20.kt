package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.lang.Long.max
import java.lang.Long.min

class Day20 : Day<List<List<Long>>, Long> {

    override fun runPartOne(): Long {
        var min = 0L
        val limitations = getInput()
        while (true) {
            var newMin = min
            for (limitation in limitations) {
                val (imin, imax) = limitation
                if (newMin in (imin..imax)) {
                    newMin = imax + 1
                }
            }
            if (newMin == min) {
                break
            }
            min = newMin
        }
        return min
    }

    override fun runPartTwo(): Long {
        val limitations = getInput().sortedBy { it.first() }
        val sections = mutableListOf<Pair<Long, Long>>()
        while (true) {
            val oldSections = sections.toMutableList()
            for (limitation in limitations) {
                val withinIdx = sections.indexOfFirst {
                    limitation.first() in (it.first..it.second) ||
                        limitation.last() in (it.first..it.second)
                }
                if (withinIdx >= 0) {
                    sections[withinIdx] = Pair(
                        min(sections[withinIdx].first, limitation.first()),
                        max(sections[withinIdx].second, limitation.last()),
                    )
                } else {
                    sections.add(Pair(limitation.first(), limitation.last()))
                }
            }
            if (oldSections.sortedBy { it.first } == sections.sortedBy { it.first }) {
                break
            }
        }
        var num = 0L
        for (i in 0 until sections.size - 1) {
            num += (sections[i + 1].first - sections[i].second) - 1
        }
        return num
    }

    override fun getInput(): List<List<Long>> = AOCUtils.getDayInput(2016, 20).map {
        it.split("-").map { i -> i.toLong() }
    }
}

fun main() {
    val day = Day20()
    println(day.runPartOne())
    println(day.runPartTwo())
}
