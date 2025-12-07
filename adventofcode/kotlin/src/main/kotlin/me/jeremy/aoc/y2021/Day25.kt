package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day25 : Day<List<MutableList<Char>>, Int> {
    override fun runPartOne(): Int {
        var cucumbers = getInput()
        var steps = 0
        var hasMoved = true
        while (hasMoved) {
            val afterEast = cucumbers.map { it.toMutableList() }.toList()
            cucumbers.forEachIndexed { y, r ->
                r.forEachIndexed { x, c ->
                    if (c == '>') {
                        val nx = (x + 1) % r.size
                        if (r[nx] == '.') {
                            afterEast[y][nx] = '>'
                            afterEast[y][x] = '.'
                        }
                    }
                }
            }
            val afterSouth = afterEast.map { it.toMutableList() }.toList()
            afterEast.forEachIndexed { y, r ->
                r.forEachIndexed { x, c ->
                    if (c == 'v') {
                        val ny = (y + 1) % cucumbers.size
                        if (afterEast[ny][x] == '.') {
                            afterSouth[ny][x] = 'v'
                            afterSouth[y][x] = '.'
                        }
                    }
                }
            }
            steps++
            hasMoved = afterSouth != cucumbers
            cucumbers = afterSouth
        }
        return steps
    }

    override fun runPartTwo(): Int {
        // No Part 2 this day
        return 0
    }

    override fun getInput(): List<MutableList<Char>> = AOCUtils.getDayInput(2021, 25).map {
        it.toList().toMutableList()
    }
}

fun main() {
    val day = Day25()
    println(day.runPartOne())
}
