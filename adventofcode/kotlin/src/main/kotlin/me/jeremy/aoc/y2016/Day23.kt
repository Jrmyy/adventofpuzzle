package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day23 : Day<MutableList<String>, Int> {

    override fun runPartOne(): Int = AssembunnyInterpreter.compute(getInput(), mutableMapOf("a" to 7))

    override fun runPartTwo(): Int =
        /**
         * How the program works :
         * 1. It does factorial of a (meaning 12)
         * 2. At the end of each step of the factorial, the tgl update from the bottom even lines
         * 3. The jnz with the big number is converted to a cpy
         * 4. It does a mult of the 2 big numbers and it is added to a
         */
        (1..12).reduce { acc, i -> acc * i } + 84 * 71

    override fun getInput(): MutableList<String> = AOCUtils.getDayInput(2016, 23).toMutableList()
}

fun main() {
    val day = Day23()
    println(day.runPartOne())
    println(day.runPartTwo())
}
