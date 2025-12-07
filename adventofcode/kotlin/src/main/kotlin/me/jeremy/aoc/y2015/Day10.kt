package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day10 : Day<String, Int> {
    override fun runPartOne(): Int = lookAndSay(40)

    override fun runPartTwo(): Int = lookAndSay(50)

    override fun getInput(): String = AOCUtils.getDayInput(2015, 10).first()

    private fun lookAndSay(i: Int): Int {
        val re = "((\\d)\\2*)".toRegex()
        var lk = getInput()
        repeat(i) {
            lk = re.findAll(lk).map {
                val v = it.groupValues[1]
                "${v.length}${v[0]}"
            }.joinToString("")
        }
        return lk.length
    }
}

fun main() {
    val day = Day10()
    println(day.runPartOne())
    println(day.runPartTwo())
}
