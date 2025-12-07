package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.util.PriorityQueue

class Day15 : Day<List<List<Int>>, Int> {

    override fun runPartOne(): Int = findLowestRisk(1)

    override fun runPartTwo(): Int = findLowestRisk(5)

    private fun findLowestRisk(scale: Int): Int {
        val cavern = getInput()
        val rows = cavern.size
        val columns = cavern.first().size
        val d: List<MutableList<Int>> = (0 until scale * rows).map {
            (0 until scale * columns).map { Int.MAX_VALUE }.toMutableList()
        }
        val q = PriorityQueue(
            compareBy<Triple<Int, Int, Int>> { it.first }.thenBy { it.second }.thenBy { it.third }
        )
        q.add(Triple(0, 0, 0))

        while (q.isNotEmpty()) {
            val (dist, y, x) = q.remove()
            val value = (
                cavern[y % rows][x % columns] + (y.toDouble() / rows).toInt() + (x.toDouble() / columns).toInt() - 1
                ) % 9 + 1

            val cost = dist + value
            if (cost < d[y][x]) {
                d[y][x] = cost
            } else {
                continue
            }

            if (y == scale * rows - 1 && x == scale * columns - 1) {
                break
            }

            q.addAll(AOCUtils.getAdjacentPositions(d, y, x).map { axy -> Triple(d[y][x], axy.second, axy.first) })
        }
        return d.last().last() - cavern.first().first()
    }

    override fun getInput(): List<List<Int>> = AOCUtils.getDayInput(2021, 15).map {
        it.toList().map { c -> c.toString().toInt() }
    }
}

fun main() {
    val day = Day15()
    println(day.runPartOne())
    println(day.runPartTwo())
}
