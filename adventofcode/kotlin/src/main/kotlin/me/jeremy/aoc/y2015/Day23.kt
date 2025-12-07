package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day23 : Day<List<String>, Int> {
    override fun runPartOne(): Int = simulate(0)

    override fun runPartTwo(): Int = simulate(1)

    override fun getInput(): List<String> = AOCUtils.getDayInput(2015, 23)

    private fun simulate(ia: Int): Int {
        var a = ia
        var b = 0
        var i = 0
        val instructions = getInput()
        while (i in instructions.indices) {
            val instruction = instructions[i]
            when {
                instruction.startsWith("hlf") -> {
                    val variable = instruction.replace("hlf ", "")
                    if (variable == "a") a /= 2 else b /= 2
                    i++
                }
                instruction.startsWith("tpl") -> {
                    val variable = instruction.replace("tpl ", "")
                    if (variable == "a") a *= 3 else b *= 3
                    i++
                }
                instruction.startsWith("inc") -> {
                    val variable = instruction.replace("inc ", "")
                    if (variable == "a") a += 1 else b += 1
                    i++
                }
                instruction.startsWith("jmp") -> {
                    val offset = instruction.replace("jmp ", "").toInt()
                    i += offset
                }
                instruction.startsWith("jie") -> {
                    val variable = instruction.split(", ").first().replace("jie ", "")
                    val value = if (variable == "a") a else b
                    if (value % 2 == 0) {
                        val offset = instruction.split(", ").last().toInt()
                        i += offset
                    } else {
                        i++
                    }
                }
                instruction.startsWith("jio") -> {
                    val variable = instruction.split(", ").first().replace("jio ", "")
                    val value = if (variable == "a") a else b
                    if (value == 1) {
                        val offset = instruction.split(", ").last().toInt()
                        i += offset
                    } else {
                        i++
                    }
                }
            }
        }
        return b
    }
}

fun main() {
    val day = Day23()
    println(day.runPartOne())
    println(day.runPartTwo())
}
