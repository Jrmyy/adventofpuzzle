package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day25 : Day<MutableList<String>, Int> {

    override fun runPartOne(): Int {
        var a = 0
        while (true) {
            try {
                AssembunnyInterpreter.compute(getInput(), mutableMapOf("a" to a))
                break
            } catch (e: java.lang.IllegalStateException) {
                a++
            }
        }
        return a
    }

    override fun runPartTwo(): Int {
        TODO("Not yet implemented")
    }

    override fun getInput(): MutableList<String> = AOCUtils.getDayInput(2016, 25).toMutableList()
}

fun main() {
    val day = Day25()
    println(day.runPartOne())
}
