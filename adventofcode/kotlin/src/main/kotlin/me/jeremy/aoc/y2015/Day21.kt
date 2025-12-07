package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max

class Day21 : Day<Triple<Int, Int, Int>, Int> {

    data class Item(
        val cost: Int,
        val damage: Int,
        val armor: Int
    )

    override fun runPartOne(): Int {
        val boss = getInput()
        val wins = mutableSetOf<Int>()
        WEAPONS.forEach { w ->
            ARMORS.forEach { a ->
                RINGS.forEach { r1 ->
                    RINGS.filter { r2 -> r2 != r1 }.forEach { r2 ->
                        val player = Triple(
                            100,
                            w.damage + a.damage + r1.damage + r2.damage,
                            w.armor + a.armor + r1.armor + r2.armor
                        )
                        if (wins(player, boss)) {
                            wins.add(w.cost + a.cost + r1.cost + r2.cost)
                        }
                    }
                }
            }
        }
        return wins.minOrNull() ?: throw Exception("There is at least one win")
    }

    override fun runPartTwo(): Int {
        val boss = getInput()
        val loss = mutableSetOf<Int>()
        WEAPONS.forEach { w ->
            ARMORS.forEach { a ->
                RINGS.forEach { r1 ->
                    RINGS.filter { r2 -> r2 != r1 }.forEach { r2 ->
                        val player = Triple(
                            100,
                            w.damage + a.damage + r1.damage + r2.damage,
                            w.armor + a.armor + r1.armor + r2.armor
                        )
                        if (!wins(player, boss)) {
                            loss.add(w.cost + a.cost + r1.cost + r2.cost)
                        }
                    }
                }
            }
        }
        return loss.maxOrNull() ?: throw Exception("There is at least one loss")
    }

    override fun getInput(): Triple<Int, Int, Int> = AOCUtils.getDayInput(2015, 21).let { lines ->
        Triple(
            lines[0].split(": ").last().toInt(),
            lines[1].split(": ").last().toInt(),
            lines[2].split(": ").last().toInt()
        )
    }

    private fun wins(player: Triple<Int, Int, Int>, boss: Triple<Int, Int, Int>): Boolean {
        var p1 = player
        var p2 = boss
        var current = 1
        while (p1.first > 0 && p2.first > 0) {
            if (current == 1) {
                val dealt = max(p1.second - p2.third, 1)
                p2 = Triple(p2.first - dealt, p2.second, p2.third)
            } else {
                val dealt = max(p2.second - p1.third, 1)
                p1 = Triple(p1.first - dealt, p1.second, p1.third)
            }
            current = if (current == 1) 2 else 1
        }
        return p1.first > 0
    }

    companion object {
        val WEAPONS = listOf(
            Item(8, 4, 0),
            Item(10, 5, 0),
            Item(25, 6, 0),
            Item(40, 7, 0),
            Item(74, 8, 0)
        )

        val ARMORS = listOf(
            Item(0, 0, 0),
            Item(13, 0, 1),
            Item(31, 0, 2),
            Item(53, 0, 3),
            Item(75, 0, 4),
            Item(102, 0, 5)
        )

        val RINGS = listOf(
            Item(0, 0, 0),
            Item(0, 0, 0),
            Item(25, 1, 0),
            Item(50, 2, 0),
            Item(100, 3, 0),
            Item(20, 0, 1),
            Item(40, 0, 2),
            Item(80, 0, 3)
        )
    }
}

fun main() {
    val day = Day21()
    println(day.runPartOne())
    println(day.runPartTwo())
}
