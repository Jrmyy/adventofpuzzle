package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day10 : Day<List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, Int> {
    override fun runPartOne(): Int {
        val stars = getInput()
        val minSecond = getSecondForMinBoundaries(stars)
        val translatedStars = stars.map {
            Pair(
                it.first.first + minSecond * it.second.first,
                it.first.second + minSecond * it.second.second,
            )
        }
        /* ktlint-disable */
        val space = (translatedStars.minOf { it.second }..translatedStars.maxOf { it.second }).map { y ->
            (translatedStars.minOf { it.first }..translatedStars.maxOf { it.first }).map { x ->
                if (translatedStars.firstOrNull { p -> p == Pair(x, y) } != null) '#' else ' '
            }
        }
        /* ktlint-enable */
        println(space.joinToString("\n") { it.joinToString("") })
        return 0
    }

    override fun runPartTwo(): Int = getSecondForMinBoundaries(getInput())

    override fun getInput(): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> =
        AOCUtils.getDayInput(2018, 10).map {
            val matches = Regex("position=<([- ]\\d+), ([- ]\\d+)> velocity=<([- ]\\d+), ([- ]\\d+)>")
                .find(it)!!.groupValues.drop(1).map { v ->
                v.trim().toInt()
            }
            Pair(Pair(matches[0], matches[1]), Pair(matches[2], matches[3]))
        }

    private fun getSecondForMinBoundaries(stars: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int =
        (
            (0..20000).map { n ->
                val minX = stars.minOf { it.first.first + n * it.second.first }
                val minY = stars.minOf { it.first.second + n * it.second.second }
                val maxX = stars.maxOf { it.first.first + n * it.second.first }
                val maxY = stars.maxOf { it.first.second + n * it.second.second }
                Pair(n, maxX - minX + maxY - minY)
            }
                .minByOrNull { it.second } ?: error("No data")
            )
            .first
}

fun main() {
    val day = Day10()
    day.runPartOne()
    println(day.runPartTwo())
}
