package me.jeremy.aoc.y2017

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day4 : Day<List<String>, Int> {

    override fun runPartOne(): Int = getInput().count {
        it.split(" ").let { words -> words.toSet().size == words.size }
    }

    override fun runPartTwo(): Int = getInput().count {
        it.split(" ").let { words ->
            words.withIndex().all { (idx, word) ->
                words.drop(idx + 1).all { other -> word.toSortedSet() != other.toSortedSet() }
            }
        }
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2017, 4)
}

fun main() {
    val day = Day4()
    println(day.runPartOne())
    println(day.runPartTwo())
}
