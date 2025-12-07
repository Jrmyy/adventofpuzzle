package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day25 : Day<Pair<Int, Int>, Long> {
    override fun runPartOne(): Long {
        val (r, c) = getInput()
        val grid = (0..r + c).map {
            (0..r + c).map { 0L }.toMutableList()
        }
        grid[0][0] = 20151125
        var x = 0
        var y = 1
        var ny = 2
        var px = 0
        var py = 0
        while (y < r || x < c) {
            grid[y][x] = (grid[py][px] * 252533) % 33554393
            px = x
            py = y
            if (y == 0) {
                y = ny
                ny++
                x = 0
            } else {
                y -= 1
                x += 1
            }
        }
        return grid[r - 1][c - 1]
    }

    override fun runPartTwo(): Long {
        TODO("Not yet implemented")
    }

    override fun getInput(): Pair<Int, Int> = AOCUtils.getDayInput(2015, 25).first().let {
        val w = it.replace("To continue, please consult the code grid in the manual.  Enter the code at ", "")
            .split(", ")
        Pair(
            w.first().split(" ").last().toInt(),
            w.last().split(" ").last().replace(".", "").toInt()
        )
    }
}

fun main() {
    val day = Day25()
    println(day.runPartOne())
}
