package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day20 : Day<List<Int>, Long> {

    override fun runPartOne(): Long = decrypt()

    override fun runPartTwo(): Long = decrypt(811_589_153, 10)

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2022, 20).map { it.toInt() }

    private fun decrypt(decryptKey: Int = 1, mixTimes: Int = 1): Long {
        val original = getInput().mapIndexed { index, i -> Pair(index, i.toLong() * decryptKey) }
        val moved = original.toMutableList()
        repeat(mixTimes) {
            original.forEach { p ->
                val idx = moved.indexOf(p)
                moved.removeAt(idx)
                moved.add((idx + p.second).mod(moved.size), p)
            }
        }
        return moved.map { it.second }.let {
            val idx0 = it.indexOf(0)
            (1..3).sumOf { i -> it[(i * 1000 + idx0) % it.size] }
        }
    }
}

fun main() {
    val day = Day20()
    println(day.runPartOne())
    println(day.runPartTwo())
}
