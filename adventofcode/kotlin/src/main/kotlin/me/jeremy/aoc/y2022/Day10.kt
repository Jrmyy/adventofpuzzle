package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day10 : Day<List<String>, Int> {

    override fun runPartOne(): Int {
        val values = mutableListOf<Int>()
        var x = 1
        val instructions = getInput()
        var idx = 0
        var remaining = 0
        var toAdd = 0
        for (c in 0..220) {
            if (listOf(20, 60, 100, 140, 180, 220).contains(c)) {
                values.add(x * c)
            }
            if (remaining == 0) {
                x += toAdd
                toAdd = 0
                val instruction = instructions[idx]
                if (instruction != "noop") {
                    toAdd = instruction.replace("addx ", "").toInt()
                    remaining = 1
                }
                idx++
            } else {
                remaining--
            }
        }
        return values.sum()
    }

    override fun runPartTwo(): Int {
        var x = 1
        val instructions = getInput()
        var idx = 0
        var remaining = 0
        var toAdd = 0
        var spritePosition = (1..3).map { '#' } + (1..37).map { '.' }
        val rows = mutableListOf<MutableList<Char>>()
        for (c in 0..239) {
            if (remaining == 0) {
                x += toAdd
                spritePosition = (0 until x - 1).map { '.' } + (x - 1..x + 1).map { '#' } + (x + 2..39).map { '.' }
                toAdd = 0
                val instruction = instructions[idx]
                if (instruction != "noop") {
                    toAdd = instruction.replace("addx ", "").toInt()
                    remaining = 1
                }
                idx++
            } else {
                remaining--
            }
            if (c % 40 == 0) {
                rows.add(mutableListOf())
            }
            rows[rows.size - 1].add(spritePosition[c % 40])
        }
        println(rows.joinToString("\n") { it.joinToString("") })
        return 0
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2022, 10)
}

fun main() {
    val day = Day10()
    println(day.runPartOne())
    println(day.runPartTwo())
}
