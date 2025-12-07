package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day4 : Day<List<List<List<Int>>>, Int> {

    override fun runPartOne(): Int =
        getInput().count {
            val (f, s) = it
            f.first() >= s.first() && f.last() <= s.last() || s.first() >= f.first() && s.last() <= f.last()
        }

    override fun runPartTwo(): Int =
        getInput().count {
            val (f, s) = it
            f.last() in (s.first()..s.last()) || s.last() in (f.first()..f.last())
        }

    override fun getInput(): List<List<List<Int>>> = AOCUtils.getDayInput(2022, 4).map {
        it.split(",").map { s -> s.split("-").map { i -> i.toInt() } }
    }
}

fun main() {
    val day = Day4()
    println(day.runPartOne())
    println(day.runPartTwo())
}
