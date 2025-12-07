package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day15 : Day<List<Pair<Int, Int>>, Int> {

    override fun runPartOne(): Int = getMomentToPress(getInput())

    override fun runPartTwo(): Int =
        getInput().let {
            val mut = it.toMutableList()
            mut.add(Pair(11, 0))
            getMomentToPress(mut)
        }

    override fun getInput(): List<Pair<Int, Int>> = AOCUtils.getDayInput(2016, 15).map {
        "Disc #\\d has (\\d+) positions; at time=0, it is at position (\\d+)".toRegex().find(it)!!.groupValues.let {
            m ->
            Pair(m[1].toInt(), m[2].toInt())
        }
    }

    private fun getMomentToPress(discs: List<Pair<Int, Int>>): Int {
        var mDiscs = discs.toList()
        var time = 0
        while (true) {
            val newDiscs = mDiscs.toList().mapIndexed { idx, p -> Pair(p.first, (p.second + idx + 1) % p.first) }

            if (newDiscs.all { it.second == 0 })
                break

            mDiscs = mDiscs.toList().map { p -> Pair(p.first, p.second + 1) }
            time++
        }
        return time
    }
}

fun main() {
    val day = Day15()
    println(day.runPartOne())
    println(day.runPartTwo())
}
