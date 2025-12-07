package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day6 : Day<List<List<String>>, String> {
    override fun runPartOne(): String =
        getInput().let { ipt ->
            (0 until ipt.first().size).joinToString("") {
                ipt.map { l -> l[it] }.groupingBy { c -> c }.eachCount().maxBy { e -> e.value }.key
            }
        }

    override fun runPartTwo(): String =
        getInput().let { ipt ->
            (0 until ipt.first().size).joinToString("") {
                ipt.map { l -> l[it] }.groupingBy { c -> c }.eachCount().minBy { e -> e.value }.key
            }
        }

    override fun getInput(): List<List<String>> = AOCUtils.getDayInput(2016, 6).map { it.split("") }
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
