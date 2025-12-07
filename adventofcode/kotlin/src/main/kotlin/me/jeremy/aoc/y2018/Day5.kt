package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day5 : Day<MutableList<Char>, Int> {
    override fun runPartOne(): Int = getReducedReactionSize(getInput())

    override fun runPartTwo(): Int {
        val initialReaction = getInput()
        return initialReaction
            .distinctBy { it.lowercaseChar() }
            .minOf {
                getReducedReactionSize(
                    initialReaction.filter { c -> !c.equals(it, ignoreCase = true) }
                )
            }
    }

    override fun getInput(): MutableList<Char> = AOCUtils.getDayInput(2018, 5).first().toMutableList()

    private fun getReducedReactionSize(initialReaction: List<Char>): Int {
        var canReduce = true
        var reaction = initialReaction.toMutableList()
        while (canReduce) {
            canReduce = false
            reaction = reaction.fold(mutableListOf()) { acc, c ->
                val last = acc.lastOrNull()
                if (
                    last != null &&
                    c.equals(last, ignoreCase = true) &&
                    c != last
                ) {
                    canReduce = true
                    acc.removeLast()
                } else {
                    acc.add(c)
                }
                acc
            }
        }
        return reaction.size
    }
}

fun main() {
    val day = Day5()
    println(day.runPartOne())
    println(day.runPartTwo())
}
