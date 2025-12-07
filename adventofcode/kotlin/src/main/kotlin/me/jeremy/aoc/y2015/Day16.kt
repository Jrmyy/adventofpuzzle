package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day16 : Day<List<List<Pair<String, Int>>>, Int> {
    override fun runPartOne(): Int = getInput().indexOfFirst {
        it.intersect(CRITERIA) == it.toSet()
    } + 1

    override fun runPartTwo(): Int = getInput().indexOfFirst {
        CRITERIA.all { p ->
            val crit = it.firstOrNull { op -> op.first == p.first }
            if (crit == null) {
                true
            } else {
                when (p.first) {
                    "cats" -> p.second < crit.second
                    "trees" -> p.second < crit.second
                    "pomeranians" -> p.second > crit.second
                    "goldfish" -> p.second > crit.second
                    else -> p.second == crit.second
                }
            }
        }
    } + 1

    override fun getInput(): List<List<Pair<String, Int>>> = AOCUtils.getDayInput(2015, 16).map {
        it.replace("Sue \\d+:".toRegex(), "").split(", ").map { p ->
            Pair(p.split(": ").first().trim(), p.split(": ").last().toInt())
        }
    }

    companion object {
        val CRITERIA = setOf(
            Pair("children", 3),
            Pair("cats", 7),
            Pair("samoyeds", 2),
            Pair("pomeranians", 3),
            Pair("akitas", 0),
            Pair("vizslas", 0),
            Pair("goldfish", 5),
            Pair("trees", 3),
            Pair("cars", 2),
            Pair("perfumes", 1)
        )
    }
}

fun main() {
    val day = Day16()
    println(day.runPartOne())
    println(day.runPartTwo())
}
