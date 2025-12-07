package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max
import kotlin.math.min

class Day5 : Day<List<Day5.VentLine>, Int> {

    data class Coordinates(val x: Int, val y: Int)

    data class VentLine(val from: Coordinates, val to: Coordinates)

    override fun runPartOne(): Int = computeOverlappingLines(
        getInput().filter { it.from.x == it.to.x || it.from.y == it.to.y }
    )

    override fun runPartTwo(): Int = computeOverlappingLines(getInput())

    override fun getInput(): List<VentLine> =
        AOCUtils.getDayInput(2021, 5).map {
            val split = it.split(" -> ")
            val first = split.first().split(",").map { d -> d.toInt() }
            val second = split.last().split(",").map { d -> d.toInt() }
            VentLine(Coordinates(first.first(), first.last()), Coordinates(second.first(), second.last()))
        }

    private fun computeOverlappingLines(ventsLines: List<VentLine>): Int =
        ventsLines
            .fold(mutableMapOf<Coordinates, Int>()) { acc, pair ->
                (
                    if (pair.from.x == pair.to.x) {
                        (min(pair.from.y, pair.to.y)..max(pair.from.y, pair.to.y)).map { y ->
                            Coordinates(pair.from.x, y)
                        }
                    } else if (pair.from.y == pair.to.y) {
                        (min(pair.from.x, pair.to.x)..max(pair.from.x, pair.to.x)).map { x ->
                            Coordinates(x, pair.from.y)
                        }
                    } else {
                        val delta = max(pair.from.y, pair.to.y) - min(pair.from.y, pair.to.y)
                        val xDirection = if (pair.from.x < pair.to.x) 1 else -1
                        val yDirection = if (pair.from.y < pair.to.y) 1 else -1
                        (0..delta).map { d ->
                            Coordinates(pair.from.x + d * xDirection, pair.from.y + d * yDirection)
                        }
                    }
                    ).forEach { c -> acc[c] = acc[c]?.plus(1) ?: 1 }
                acc
            }.count { e -> e.value > 1 }
}

fun main() {
    val day = Day5()
    println(day.runPartOne())
    println(day.runPartTwo())
}
