package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day20 : Day<Int, Int> {

    override fun runPartOne(): Int {
        val presents = getInput()
        var house = 100_000
        while ((1..presents / 10).filter { e -> house % e == 0 }.sumOf { e -> e * 10 } < presents) {
            house += 100
        }
        var min = house
        for (mh in house downTo 100_000) {
            if ((1..presents).filter { e -> mh % e == 0 }.sum() >= presents) {
                min = mh
            }
        }
        return min
    }

    override fun runPartTwo(): Int {
        val presents = getInput()
        var house = 100_000
        while ((1..presents / 11).filter { e -> house % e == 0 && house <= e * 50 }.sumOf { e -> e * 11 } < presents) {
            house += 100
        }
        var min = house
        for (mh in house downTo 100_000) {
            if ((1..presents).filter { e -> mh % e == 0 && house <= e * 50 }.sum() >= presents) {
                min = mh
            }
        }
        return min
    }

    override fun getInput(): Int = AOCUtils.getDayInput(2015, 20).first().toInt()
}

fun main() {
    val day = Day20()
    println(day.runPartOne())
    println(day.runPartTwo())
}
