package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day19 : Day<Int, Int> {

    override fun runPartOne(): Int =
        // https://en.wikipedia.org/wiki/Josephus_problem#k=2
        getInput().let { 2 * (it - it.takeHighestOneBit()) + 1 }

    override fun runPartTwo(): Int {
        /**
         * Based on mega thread solutions.
         * Representation is like :
         *       1    5
         *     2        4
         *         3
         */
        val elvesCount = getInput()
        val left = ArrayDeque((1..elvesCount / 2).toList())
        val right = ArrayDeque((elvesCount / 2 + 1..elvesCount).reversed().toList())

        while (left.isNotEmpty() && right.isNotEmpty()) {
            // Stole the gift from the person directly facing in the circle or the one to the left if 2
            if (left.size > right.size) {
                left.removeLast()
            } else {
                right.removeLast()
            }

            // Rotation of the circle
            right.addFirst(left.removeFirst())
            left.addLast(right.removeLast())
        }

        // Return the king in the hill
        return left.firstOrNull() ?: right.first()
    }

    override fun getInput(): Int = AOCUtils.getDayInput(2016, 19).first().toInt()
}

fun main() {
    val day = Day19()
    println(day.runPartOne())
    println(day.runPartTwo())
}
