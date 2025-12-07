package me.jeremy.aoc.y2017

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs

class Day3 : Day<Int, Int> {

    override fun runPartOne(): Int {
        val memory = mutableMapOf(1 to Pair(0, 0))
        val until = getInput()
        var i = 2
        var s = 1
        while (i <= until) {
            for (yi in -s + 1..s) {
                memory[i] = Pair(s, yi)
                i++
            }
            for (xi in s - 1 downTo -s) {
                memory[i] = Pair(xi, s)
                i++
            }
            for (yi in s - 1 downTo -s) {
                memory[i] = Pair(-s, yi)
                i++
            }
            for (xi in -s + 1..s) {
                memory[i] = Pair(xi, -s)
                i++
            }
            s++
        }
        return (memory[until] ?: error("Should exist")).let { m -> abs(m.first) + abs(m.second) }
    }

    override fun runPartTwo(): Int {
        val memory = mutableMapOf(Pair(0, 0) to 1)
        val until = getInput()
        var i: Int
        var s = 1
        main@ while (true) {
            for (yi in -s + 1..s) {
                val p = Pair(s, yi)
                val j = sumAdj(memory, p)
                memory[p] = j
                i = j
                if (i > until) {
                    break@main
                }
            }
            for (xi in s - 1 downTo -s) {
                val p = Pair(xi, s)
                val j = sumAdj(memory, p)
                memory[p] = j
                i = j
                if (i > until) {
                    break@main
                }
            }
            for (yi in s - 1 downTo -s) {
                val p = Pair(-s, yi)
                val j = sumAdj(memory, p)
                memory[p] = j
                i = j
                if (i > until) {
                    break@main
                }
            }
            for (xi in -s + 1..s) {
                val p = Pair(xi, -s)
                val j = sumAdj(memory, p)
                memory[p] = j
                i = j
                if (i > until) {
                    break@main
                }
            }
            s++
        }
        return i
    }

    override fun getInput(): Int = AOCUtils.getDayInput(2017, 3).first().toInt()

    private fun sumAdj(map: Map<Pair<Int, Int>, Int>, p: Pair<Int, Int>): Int =
        map.filterKeys { pp ->
            pp.first in (p.first - 1..p.first + 1) && pp.second in (p.second - 1..p.second + 1)
        }.values.sum()
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
