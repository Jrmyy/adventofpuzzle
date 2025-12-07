package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day11 : Day<String, String> {
    override fun runPartOne(): String = findNextPassword()

    override fun runPartTwo(): String = findNextPassword(increment(findNextPassword()))

    override fun getInput(): String = AOCUtils.getDayInput(2015, 11).first()

    private fun findNextPassword(initial: String = getInput()): String {
        var password = initial
        while (!correct(password)) {
            password = increment(password)
        }
        return password
    }

    private fun increment(p: String): String {
        val chars = p.toMutableList()
        return if (chars.last() == 'z') {
            increment(chars.dropLast(1).joinToString("")) + "a"
        } else {
            chars[chars.size - 1] = ++chars[chars.size - 1]
            chars.joinToString("")
        }
    }

    private fun correct(p: String): Boolean =
        !p.contains("[iol]".toRegex()) && STRAIGHT_INCREASING.any { p.contains(it) } &&
            p.windowed(2, 1)
                .mapIndexedNotNull { i, s -> if (s.first() == s.last()) Pair(s.first(), listOf(i, i + 1)) else null }
                .fold(mutableMapOf<Char, MutableList<Int>>()) { acc, pa ->
                    if (!acc.containsKey(pa.first)) {
                        acc[pa.first] = mutableListOf()
                    }
                    acc[pa.first]!!.addAll(pa.second)
                    acc
                }.values.filter { it.isNotEmpty() }.let { m ->
                    m.size >= 2 && m.flatten().toSet().size >= 4
                }

    companion object {
        val STRAIGHT_INCREASING = "abcdefghijklmnopqrstuvwxyz".toList().let { l ->
            List(l.subList(0, l.size - 2).size) { i -> "${l[i]}${l[i + 1]}${l[i + 2]}" }
        }
    }
}

fun main() {
    val day = Day11()
    println(day.runPartOne())
    println(day.runPartTwo())
}
