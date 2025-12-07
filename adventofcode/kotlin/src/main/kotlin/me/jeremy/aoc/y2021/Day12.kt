package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.util.Locale

class Day12 : Day<List<Pair<String, String>>, Int> {
    override fun runPartOne(): Int =
        countPathsToEnd(getInput(), listOf("start"), 1)

    override fun runPartTwo(): Int =
        countPathsToEnd(getInput(), listOf("start"), 2)

    override fun getInput(): List<Pair<String, String>> =
        AOCUtils.getDayInput(2021, 12).map {
            it.split("-").let { s -> Pair(s.first(), s.last()) }
        }

    private fun countPathsToEnd(
        connexions: List<Pair<String, String>>,
        beginning: List<String>,
        maxSmallCavesVisit: Int
    ): Int =
        if (beginning.contains("end")) {
            1
        } else {
            connexions.mapNotNull {
                val otherPart = if (
                    it.first == beginning.last()
                ) it.second else if (
                    it.second == beginning.last()
                ) it.first else null
                if (otherPart == "start") null else otherPart
            }.sumOf {
                val newBeginning = beginning + listOf(it)
                val countPerSmallCaves = newBeginning.filter { s ->
                    s.lowercase(Locale.getDefault()) == s && s != "end" && s != "start"
                }.groupingBy { s -> s }.eachCount()
                val maxOfSmallCaves = countPerSmallCaves.maxOfOrNull { e -> e.value }
                val numOfMax = if (maxOfSmallCaves == null) {
                    0
                } else {
                    countPerSmallCaves.count { e -> e.value == maxOfSmallCaves }
                }
                if (
                    (maxOfSmallCaves != null && maxOfSmallCaves > maxSmallCavesVisit) ||
                    (maxOfSmallCaves == maxSmallCavesVisit && maxSmallCavesVisit > 1 && numOfMax > 1)
                ) {
                    0
                } else {
                    countPathsToEnd(connexions, newBeginning, maxSmallCavesVisit)
                }
            }
        }
}

fun main() {
    val day = Day12()
    println(day.runPartOne())
    println(day.runPartTwo())
}
