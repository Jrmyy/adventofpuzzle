package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day5 : Day<Pair<MutableList<MutableList<String>>, List<List<Int>>>, String> {

    override fun runPartOne(): String = moveCrates(false)

    override fun runPartTwo(): String = moveCrates(true)

    override fun getInput(): Pair<MutableList<MutableList<String>>, List<List<Int>>> = AOCUtils.getDayInput(2022, 5)
        .let {
            val initial = it.takeWhile { s -> s.trim() != "" }.map { l ->
                l.chunked(4).map { c -> c.trim().replace("[", "").replace("]", "") }
            }.dropLast(1)
            val stacks = mutableListOf<MutableList<String>>()
            (0 until initial.maxOf { r -> r.size }).forEach { idx ->
                stacks.add(initial.map { r -> r.getOrNull(idx)?.trim() ?: "" }.toMutableList())
            }
            Pair(
                stacks,
                it.takeLastWhile { s -> s.trim() != "" }.map { ins ->
                    "move (\\d+) from (\\d+) to (\\d+)".toRegex().find(ins)!!.groupValues
                        .drop(1)
                        .map { i -> i.toInt() }
                }
            )
        }

    private fun moveCrates(multiple: Boolean): String {
        val (stacks, instructions) = getInput()
        for (inst in instructions) {
            val (amount, from, to) = inst
            val fromLastEmptyIdx = stacks[from - 1].lastIndexOf("")
            val toTake = stacks[from - 1].filter { it != "" }.take(amount).toList().let {
                if (multiple) it.reversed() else it
            }
            (0 until amount).forEach {
                stacks[from - 1][fromLastEmptyIdx + 1 + it] = ""
            }
            val toLastEmptyIdx = stacks[to - 1].lastIndexOf("")
            for ((idx, toPlace) in toTake.withIndex()) {
                if (toLastEmptyIdx - idx < 0) {
                    stacks[to - 1].add(0, toPlace)
                } else {
                    stacks[to - 1][toLastEmptyIdx - idx] = toPlace
                }
            }
        }
        return stacks.joinToString("") { it.first { s -> s != "" } }
    }
}

fun main() {
    val day = Day5()
    println(day.runPartOne())
    println(day.runPartTwo())
}
