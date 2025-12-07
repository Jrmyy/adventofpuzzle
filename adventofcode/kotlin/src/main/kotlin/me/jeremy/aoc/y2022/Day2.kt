package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day2 : Day<List<List<String>>, Int> {

    override fun runPartOne(): Int =
        getInput().sumOf {
            val (opponent, me) = it
            val idxOfOpp = PLAYS.indexOf(opponent)
            val idxOfMe = PLAYS.indexOf(HAND_MAPPING[me]!!)
            when (idxOfMe) {
                (idxOfOpp + 1) % 3 -> 6
                idxOfOpp -> 3
                else -> 0
            } + idxOfMe + 1
        }

    override fun runPartTwo(): Int =
        getInput().sumOf {
            val (opponent, strategy) = it
            when (strategy) {
                "X" -> (PLAYS.indexOf(opponent) - 1).mod(3)
                "Y" -> 3 + PLAYS.indexOf(opponent)
                else -> 6 + (PLAYS.indexOf(opponent) + 1).mod(3)
            } + 1
        }

    override fun getInput(): List<List<String>> =
        AOCUtils.getDayInput(2022, 2).map { it.split(" ") }

    companion object {
        val PLAYS = listOf("A", "B", "C")
        val HAND_MAPPING = mapOf("X" to "A", "Y" to "B", "Z" to "C")
    }
}

fun main() {
    val day = Day2()
    println(day.runPartOne())
    println(day.runPartTwo())
}
