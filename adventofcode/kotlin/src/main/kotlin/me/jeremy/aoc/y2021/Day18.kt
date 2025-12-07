package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.ceil
import kotlin.math.floor

class Day18 : Day<List<String>, Int> {
    override fun runPartOne(): Int {
        val numbers = getInput()
        var sum = numbers.first()
        numbers.subList(1, numbers.size).forEach {
            sum = sum(sum, it)
        }
        return magnitude(sum)
    }

    override fun runPartTwo(): Int {
        val numbers = getInput()
        return numbers.flatMapIndexed { i, n ->
            numbers.filterIndexed { j, _ -> i != j }.map { magnitude(sum(n, it)) }
        }.maxOrNull()!!
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2021, 18)

    private fun magnitude(s: String): Int {
        var numOpened = 0
        if (s.length == 1) {
            return s.toInt()
        }
        for (i in s.toList().indices) {
            if (s[i] == '[') {
                numOpened++
            } else if (s[i] == ']') {
                numOpened--
            }
            if (numOpened == 0) {
                if (i == s.length - 1 && s.length == 5) {
                    return 3 * s[1].toString().toInt() + 2 * s[3].toString().toInt()
                }
                if (i == s.length - 1) {
                    return magnitude(s.removePrefix("[").removeSuffix("]"))
                }
                return 3 * magnitude(s.substring(0, i + 1)) + 2 * magnitude(s.substring(i + 2))
            }
        }
        throw Exception("Should not happen")
    }
}

private fun sum(f: String, s: String): String {
    var m = "[$f,$s]"
    var hasOperations = true
    while (hasOperations) {
        var hasExploded = false
        var hasSplit = false
        var opened = 0
        for (ic in m.toList().withIndex()) {
            when (ic.value) {
                '[' -> opened++
                ']' -> opened--
            }
            if (opened == 5) {
                hasExploded = true
                val toExplodeStr =
                    m.substring(ic.index, m.substring(ic.index).indexOfFirst { it == ']' } + ic.index + 1)
                val end = ic.index + toExplodeStr.length
                val toExplode = toExplodeStr.removePrefix("[").removeSuffix("]")
                    .split(",")
                    .let { Pair(it.first().toInt(), it.last().toInt()) }
                val valueToAppendLeft = "(\\d+)".toRegex().find(m.substring(0, ic.index).reversed())
                val initialSize = m.length
                var afterLeftDelta = 0
                if (valueToAppendLeft != null) {
                    val group = valueToAppendLeft.groups.first()!!
                    m = m.substring(0, ic.index - 1 - group.range.last()) +
                        (group.value.reversed().toInt() + toExplode.first).toString() +
                        m.substring(ic.index - group.range.first())
                    afterLeftDelta = m.length - initialSize
                }
                val valueToAppendRight = "(\\d+)".toRegex().find(m.substring(end + afterLeftDelta))
                if (valueToAppendRight != null) {
                    val group = valueToAppendRight.groups.first()!!
                    m = m.substring(0, end + afterLeftDelta + group.range.first()) +
                        (group.value.toInt() + toExplode.second).toString() +
                        m.substring(end + afterLeftDelta + 1 + group.range.last())
                }
                m = m.substring(0, ic.index + afterLeftDelta) + "0" + m.substring(end + afterLeftDelta)
                break
            }
        }
        if (!hasExploded) {
            val toSplit = "(\\d{2,})".toRegex().find(m)
            if (toSplit != null) {
                hasSplit = true
                val group = toSplit.groups.first()!!
                val value = group.value.toInt()
                m = m.substring(0, group.range.first) +
                    "[${floor(value.toDouble() / 2).toInt()},${ceil(value.toDouble() / 2).toInt()}]" +
                    m.substring(group.range.last + 1)
            }
        }
        hasOperations = hasExploded || hasSplit
    }
    return m
}

fun main() {
    val day = Day18()
    println(day.runPartOne())
    println(day.runPartTwo())
}
