package me.jeremy.aoc.y2017

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day8 : Day<List<String>, Int> {

    override fun runPartOne(): Int = doOperations().last()

    override fun runPartTwo(): Int = doOperations().max()

    override fun getInput(): List<String> = AOCUtils.getDayInput(2017, 8)

    private fun doOperations(): List<Int> {
        val registers = mutableMapOf<String, Int>()
        val maxes = mutableListOf<Int>()
        getInput().forEach {
            val parts = it.split(" ")
            val first = parts.first()
            val op = parts[1]
            val second = parts[2].toInt()
            val cmp = parts[4]
            val cmpOp = parts[5]
            val cmpVal = parts[6].toInt()
            val cmpRes = when (cmpOp) {
                ">" -> registers.getOrDefault(cmp, 0) > cmpVal
                ">=" -> registers.getOrDefault(cmp, 0) >= cmpVal
                "<" -> registers.getOrDefault(cmp, 0) < cmpVal
                "<=" -> registers.getOrDefault(cmp, 0) <= cmpVal
                "==" -> registers.getOrDefault(cmp, 0) == cmpVal
                else -> registers.getOrDefault(cmp, 0) != cmpVal
            }
            if (cmpRes) {
                registers[first] = registers.getOrDefault(first, 0) + if (op == "inc") second else - second
            }
            maxes.add(registers.values.maxOrNull() ?: 0)
        }
        return maxes
    }
}

fun main() {
    val day = Day8()
    println(day.runPartOne())
    println(day.runPartTwo())
}
