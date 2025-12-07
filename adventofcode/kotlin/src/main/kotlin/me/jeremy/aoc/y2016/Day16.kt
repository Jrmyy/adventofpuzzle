package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day16 : Day<String, String> {

    override fun runPartOne(): String = getChecksum(272)

    override fun runPartTwo(): String = getChecksum(35651584)

    override fun getInput(): String = AOCUtils.getDayInput(2016, 16).first()

    private fun getChecksum(size: Int): String {
        var state = getInput()
        while (state.length < size) {
            val b = state.toList().reversed().map { c -> if (c == '0') '1' else '0' }.joinToString("")
            state = "${state}0$b"
        }
        state = state.substring(0, size)
        while (state.length % 2 == 0) {
            state = state.chunked(2).map { if (it.toSet().size == 1) '1' else '0' }.joinToString("")
        }
        return state
    }
}

fun main() {
    val day = Day16()
    println(day.runPartOne())
    println(day.runPartTwo())
}
