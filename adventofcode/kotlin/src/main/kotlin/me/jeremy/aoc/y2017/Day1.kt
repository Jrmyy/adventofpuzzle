package me.jeremy.aoc.y2017

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day1 : Day<String, Int> {

    override fun runPartOne(): Int =
        getInput().let { s ->
            (s.toList() + listOf(s.first())).windowed(2, 1).sumOf {
                if (it.first() == it.last()) it.first().toString().toInt() else 0
            }
        }

    override fun runPartTwo(): Int =
        getInput().let { s ->
            s.toList().let { l ->
                l.mapIndexed { idx, c -> listOf(c, l[(idx + l.size / 2) % l.size]) }
                    .sumOf {
                        if (it.first() == it.last()) it.first().toString().toInt() else 0
                    }
            }
        }

    override fun getInput(): String = AOCUtils.getDayInput(2017, 1).first()
}

fun main() {
    val day = Day1()
    println(day.runPartOne())
    println(day.runPartTwo())
}
