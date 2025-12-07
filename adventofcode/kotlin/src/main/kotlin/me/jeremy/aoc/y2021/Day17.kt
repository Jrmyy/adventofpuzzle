package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs

class Day17 : Day<Pair<Pair<Int, Int>, Pair<Int, Int>>, Int> {
    override fun runPartOne(): Int = launchProbe().maxOrNull()!!

    override fun runPartTwo(): Int = launchProbe().size

    override fun getInput(): Pair<Pair<Int, Int>, Pair<Int, Int>> =
        AOCUtils.getDayInput(2021, 17).first().replace("target area: ", "")
            .split(", ")
            .let { parts ->
                val pParts = parts.map { s ->
                    s.split("..").let { p -> Pair(p.first().removeRange(0, 2).toInt(), p.last().toInt()) }
                }
                Pair(pParts.first(), pParts.last())
            }

    private fun reachTargetArea(position: Pair<Int, Int>, area: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean =
        position.first in (area.first.first..area.first.second) &&
            position.second in (area.second.first..area.second.second)

    private fun canReachTargetArea(position: Pair<Int, Int>, area: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean =
        position.first <= maxOf(area.first.first, area.first.second) &&
            position.second >= minOf(area.second.first, area.second.second)

    private fun launchProbe(): List<Int> = getInput().let { targetArea ->
        val minY = minOf(targetArea.second.first, targetArea.second.second)
        (0..maxOf(targetArea.first.first, targetArea.first.second)).flatMap { vx ->
            (minY..abs(minY)).mapNotNull { vy ->
                var position = Pair(0, 0)
                var velocity = Pair(vx, vy)
                val vys = mutableListOf(0)
                while (!reachTargetArea(position, targetArea) && canReachTargetArea(position, targetArea)) {
                    position = Pair(position.first + velocity.first, position.second + velocity.second)
                    velocity = Pair(
                        velocity.first.let { if (it == 0) it else if (it > 0) it - 1 else it + 1 },
                        velocity.second - 1
                    )
                    vys.add(position.second)
                }
                if (canReachTargetArea(position, targetArea)) {
                    vys.maxOrNull()!!
                } else {
                    null
                }
            }
        }
    }
}

fun main() {
    val day = Day17()
    println(day.runPartOne())
    println(day.runPartTwo())
}
