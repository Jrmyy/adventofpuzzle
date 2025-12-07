package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day18 : Day<List<Char>, Int> {

    override fun runPartOne(): Int = getSafeTilesCount(40)

    override fun runPartTwo(): Int = getSafeTilesCount(400000)

    override fun getInput(): List<Char> = AOCUtils.getDayInput(2016, 18).first().toList()

    private fun getSafeTilesCount(rows: Int): Int {
        val tiles = mutableListOf(getInput())
        while (tiles.size != rows) {
            val last = tiles.last()
            tiles.add(
                last.indices.map { index ->
                    val prevConf = listOf(
                        last.getOrNull(index - 1) ?: '.',
                        last[index],
                        last.getOrNull(index + 1) ?: '.',
                    )
                    if (prevConf in TRAP_CONFIGURATIONS || prevConf.reversed() in TRAP_CONFIGURATIONS) {
                        '^'
                    } else {
                        '.'
                    }
                }
            )
        }
        return tiles.flatten().count { it == '.' }
    }

    companion object {
        val TRAP_CONFIGURATIONS = listOf(
            listOf('^', '^', '.'),
            listOf('^', '.', '.'),
        )
    }
}

fun main() {
    val day = Day18()
    println(day.runPartOne())
    println(day.runPartTwo())
}
