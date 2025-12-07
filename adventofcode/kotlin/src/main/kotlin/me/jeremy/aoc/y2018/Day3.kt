package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day3 : Day<List<Day3.Claim>, Int> {

    data class Claim(
        val id: Int,
        val x: Int,
        val y: Int,
        val w: Int,
        val h: Int
    )

    override fun runPartOne(): Int =
        computeFabric().flatten().count { it.size >= 2 }

    override fun runPartTwo(): Int {
        val fabric = computeFabric()
        val atLeastOneInchSingle = fabric
            .flatten()
            .filter { it.size == 1 }
            .flatten()
            .distinct()
        val withOverlaps = fabric
            .flatten()
            .filter { it.size >= 2 }
            .flatten()
            .distinct()
        return atLeastOneInchSingle.first {
            it !in withOverlaps
        }
    }

    override fun getInput(): List<Claim> = AOCUtils.getDayInput(2018, 3).map {
        val regex = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")
        val values = regex.find(it)!!.groupValues.drop(1).map { p -> p.toInt() }
        Claim(
            values[0],
            values[1],
            values[2],
            values[3],
            values[4],
        )
    }

    private fun computeFabric(): List<List<List<Int>>> {
        val fabric = (1..1000).map {
            (1..1000).map {
                mutableListOf<Int>()
            }
        }
        getInput().forEach {
            (it.y until it.y + it.h).forEach { r ->
                (it.x until it.x + it.w).forEach { c ->
                    fabric[r][c].add(it.id)
                }
            }
        }
        return fabric
    }
}

fun main() {
    val day = Day3()
    println(day.runPartOne())
    println(day.runPartTwo())
}
