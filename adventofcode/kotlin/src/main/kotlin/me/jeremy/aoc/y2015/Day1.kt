package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day1 : Day<String, Int> {
    override fun runPartOne(): Int =
        getInput().let {
            it.count { c -> c == '(' } - it.count { c -> c == ')' }
        }

    override fun runPartTwo(): Int = getInput().let { s ->
        var floors = 0
        var i = 0
        while (floors >= 0) {
            if (s[i] == '(') floors++ else floors--
            i++
        }
        i
    }

    override fun getInput(): String = AOCUtils.getDayInput(2015, 1).first()
}

fun main() {
    val day = Day1()
    println(day.runPartOne())
    println(day.runPartTwo())
}
