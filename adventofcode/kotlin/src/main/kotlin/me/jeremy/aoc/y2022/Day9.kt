package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs

class Day9 : Day<List<Pair<Char, Int>>, Int> {

    override fun runPartOne(): Int = moveRope(2)

    override fun runPartTwo(): Int = moveRope(10)

    override fun getInput(): List<Pair<Char, Int>> = AOCUtils.getDayInput(2022, 9)
        .map { it.split(" ").let { s -> Pair(s.first().first(), s.last().toInt()) } }

    private fun shouldMoveKnot(prevKnot: Pair<Int, Int>, nextKnot: Pair<Int, Int>): Boolean {
        val (x, y) = prevKnot
        return !listOf(
            Pair(x + 1, y),
            Pair(x - 1, y),
            Pair(x, y - 1),
            Pair(x, y + 1),
            Pair(x - 1, y - 1),
            Pair(x + 1, y - 1),
            Pair(x - 1, y + 1),
            Pair(x + 1, y + 1),
            Pair(x, y)
        ).contains(nextKnot)
    }

    private fun moveRope(knotsNumber: Int): Int {
        val knots = (1..knotsNumber).map { Pair(0, 0) }.toMutableList()
        val tailPositions = mutableSetOf(Pair(0, 0))
        getInput().forEach {
            val (direction, count) = it
            (1..count).forEach { _ ->
                knots[0] = when (direction) {
                    'U' -> knots[0].copy(second = knots[0].second + 1)
                    'D' -> knots[0].copy(second = knots[0].second - 1)
                    'L' -> knots[0].copy(first = knots[0].first - 1)
                    else -> knots[0].copy(first = knots[0].first + 1)
                }
                for (i in 1 until knots.size) {
                    if (shouldMoveKnot(knots[i - 1], knots[i])) {
                        val head = knots[i - 1]
                        val tail = knots[i]
                        knots[i] = if (head.first == tail.first) {
                            if (head.second > tail.second) {
                                tail.copy(second = tail.second + 1)
                            } else {
                                tail.copy(second = tail.second - 1)
                            }
                        } else if (head.second == tail.second) {
                            if (head.first > tail.first) {
                                tail.copy(first = tail.first + 1)
                            } else {
                                tail.copy(first = tail.first - 1)
                            }
                        } else {
                            listOf(
                                Pair(tail.first, tail.second + 1),
                                Pair(tail.first, tail.second - 1),
                                Pair(tail.first + 1, tail.second),
                                Pair(tail.first + 1, tail.second - 1),
                                Pair(tail.first + 1, tail.second + 1),
                                Pair(tail.first - 1, tail.second),
                                Pair(tail.first - 1, tail.second + 1),
                                Pair(tail.first - 1, tail.second - 1),
                            ).minBy { p -> abs(p.first - head.first) + abs(p.second - head.second) }
                        }
                        if (i == knots.size - 1) {
                            tailPositions.add(knots[i])
                        }
                    }
                }
            }
        }
        return tailPositions.size
    }
}

fun main() {
    val day = Day9()
    println(day.runPartOne())
    println(day.runPartTwo())
}
