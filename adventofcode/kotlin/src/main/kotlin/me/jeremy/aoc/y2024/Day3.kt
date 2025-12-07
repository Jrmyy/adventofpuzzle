package me.jeremy.aoc.y2024

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day3 : Day<String, Int> {

    val regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

    override fun runPartOne(): Int =
        regex.findAll(getInput()).sumOf {
            val (_, x, y) = it.groupValues
            Integer.parseInt(x) * Integer.parseInt(y)
        }

    override fun runPartTwo(): Int {
        val program = getInput()
        val conditions = "don't\\(\\)|do\\(\\)".toRegex().findAll(program).map { Pair(it.value, it.range.last) }
        return regex.findAll(program).sumOf {
            val (_, x, y) = it.groupValues
            val condition = conditions.findLast { c -> c.second < it.range.first }
            if (condition?.first == "do()") {
                Integer.parseInt(x) * Integer.parseInt(y)
            } else {
                0
            }
        }
    }

    override fun getInput(): String = AOCUtils.getDayInput(2024, 3).joinToString()
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
