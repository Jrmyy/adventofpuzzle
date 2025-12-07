package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day9 : Day<String, Long> {
    override fun runPartOne(): Long = uncompressOnce(getInput())

    override fun runPartTwo(): Long = uncompress(getInput())

    private fun uncompress(s: String): Long {
        var count = 0L
        var idx = 0
        while (idx < s.length) {
            if (s[idx] == '(') {
                val parenthesis = s.substring(idx).let { it.substring(0, it.indexOf(')') + 1) }
                val (chars, repeat) = parenthesis
                    .substring(1, parenthesis.length - 1)
                    .split("x")
                    .map { it.toInt() }
                count += uncompress(s.substring(idx + parenthesis.length, idx + parenthesis.length + chars)) * repeat
                idx += parenthesis.length + chars
            } else {
                count++
                idx++
            }
        }
        return count
    }

    private fun uncompressOnce(s: String): Long {
        var count = 0L
        var idx = 0
        while (idx < s.length) {
            if (s[idx] == '(') {
                val parenthesis = s.substring(idx).let { it.substring(0, it.indexOf(')') + 1) }
                val (chars, repeat) = parenthesis
                    .substring(1, parenthesis.length - 1)
                    .split("x")
                    .map { it.toInt() }
                count += chars * repeat
                idx += parenthesis.length + chars
            } else {
                count++
                idx++
            }
        }
        return count
    }

    override fun getInput(): String = AOCUtils.getDayInput(2016, 9).first()
}

fun main() {
    val day = Day9()
    println(day.runPartOne())
    println(day.runPartTwo())
}
