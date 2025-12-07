package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.lang.RuntimeException

class Day4 : Day<List<String>, Int> {
    override fun runPartOne(): Int =
        getInput().sumOf {
            val groups = "([a-z\\-]+)-(\\d+)\\[([a-z]+)]".toRegex().find(it)!!.groupValues
            val checksum = groups[3]
            val sectorId = groups[2]
            val encrypted = groups[1].filter { c -> c != '-' }
            val eachCount = encrypted.groupingBy { c -> c }.eachCount()
            val topFiveValues = eachCount.values.sortedDescending().take(5)
            if (checksum.mapIndexed { idx, c -> topFiveValues[idx] == eachCount[c] }.all { b -> b }) {
                sectorId.toInt()
            } else {
                0
            }
        }

    override fun runPartTwo(): Int {
        for (s in getInput()) {
            val groups = "([a-z\\-]+)-(\\d+)\\[([a-z]+)]".toRegex().find(s)!!.groupValues
            val sectorId = groups[2].toInt()
            val encrypted = groups[1].map { c ->
                if (c != '-') {
                    (((c.code - 'a'.code) + sectorId) % 26 + 'a'.code).toChar()
                } else {
                    '-'
                }
            }.joinToString("")
            if (encrypted.contains("north")) {
                return sectorId
            }
        }
        throw RuntimeException("Can't locate north pole storage")
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2016, 4)
}

fun main() {
    val day = Day4()
    println(day.runPartOne())
    println(day.runPartTwo())
}
