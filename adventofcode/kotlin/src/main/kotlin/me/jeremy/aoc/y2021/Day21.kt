package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max
import kotlin.math.min

class Day21 : Day<MutableList<Int>, Long> {

    data class State(
        val p1: Int,
        val p2: Int,
        val s1: Int,
        val s2: Int
    )

    override fun runPartOne(): Long {
        val pos = getInput()
        val scores = mutableListOf(0L, 0L)
        var dice = 1
        var dl = 0
        var player = 0
        while (scores.all { it < 1000 }) {
            val rolls = listOf(dice, dice + 1, dice + 2).sumOf { (it - 1) % 100 + 1 }
            pos[player] = (pos[player] + rolls - 1) % 10 + 1
            scores[player] = scores[player] + pos[player].toLong()
            dice += 3
            dl += 3
            player = (player + 1) % 2
        }
        return dl * scores.minOrNull()!!
    }

    override fun runPartTwo(): Long {
        val pos = getInput()
        // We have one combination for 3 but 3 combinations for 4 for instance
        val rollsWeight = (1..3).flatMap { a ->
            (1..3).flatMap { b ->
                (1..3).map { c ->
                    a + b + c
                }
            }
        }.groupingBy { it }.eachCount().toMap()
        val universes = mutableMapOf(
            State(pos.first(), pos.last(), 0, 0) to 1L
        )
        (0..20).forEach { s1 ->
            (0..20).forEach { s2 ->
                (1..10).forEach { p1 ->
                    (1..10).forEach { p2 ->
                        val os = State(p1, p2, s1, s2)
                        rollsWeight.forEach { (r1, w1) ->
                            val np1 = (p1 + r1 - 1) % 10 + 1
                            val ns1 = min(s1 + np1, 21)
                            if (ns1 >= 21) {
                                val ns = State(np1, p2, ns1, s2)
                                universes[ns] = (universes[ns] ?: 0L) + universes.getOrDefault(os, 0) * w1
                            } else {
                                rollsWeight.forEach { (r2, w2) ->
                                    val np2 = (p2 + r2 - 1) % 10 + 1
                                    val ns2 = min(s2 + np2, 21)
                                    val ns = State(np1, np2, ns1, ns2)
                                    universes[ns] = (universes[ns] ?: 0L) +
                                        universes.getOrDefault(os, 0) * w1 * w2
                                }
                            }
                        }
                    }
                }
            }
        }
        return max(
            universes.filterKeys { it.s1 == 21 }.values.sum(),
            universes.filterKeys { it.s2 == 21 }.values.sum()
        )
    }

    override fun getInput(): MutableList<Int> = AOCUtils.getDayInput(2021, 21).map {
        it.split(":").last().trim().toInt()
    }.toMutableList()
}

fun main() {
    val day = Day21()
    println(day.runPartOne())
    println(day.runPartTwo())
}
