package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day8 : Day<List<Pair<String, Int>>, Int> {
    override fun runPartOne(): Int = run(getInput()).first

    override fun runPartTwo(): Int {
        val instructions = getInput()
        val idxToChange = instructions.mapIndexed { idx, it ->
            if (it.first == "acc") {
                null
            } else {
                Pair(idx, it)
            }
        }.filterNotNull()
        return idxToChange.mapNotNull {
            val newInstructions = instructions.toMutableList()
            newInstructions[it.first] = Pair(
                if (it.second.first == "jmp") "nop" else "jmp",
                it.second.second
            )
            val (acc, hasCompleted) = run(newInstructions.toList())
            if (hasCompleted) {
                acc
            } else {
                null
            }
        }.getOrNull(0) ?: throw RuntimeException("No completed programs")
    }

    override fun getInput(): List<Pair<String, Int>> = AOCUtils.getDayInput(2020, 8)
        .map {
            val (op, value) = it.split(" ")
            Pair(op, value.toInt())
        }

    private fun run(instructions: List<Pair<String, Int>>): Pair<Int, Boolean> {
        var hasRepeated = false
        val visited = mutableListOf(0)
        var acc = 0
        var currentIdx = 0
        while (!hasRepeated && currentIdx < instructions.size) {
            val op = instructions[currentIdx]
            when (op.first) {
                "nop" -> {
                    currentIdx += 1
                }
                "acc" -> {
                    acc += op.second
                    currentIdx += 1
                }
                "jmp" -> {
                    currentIdx += op.second
                }
                else -> throw RuntimeException("Code not found ${op.first}")
            }
            visited.add(currentIdx)
            hasRepeated = visited.groupingBy { it }.eachCount().filterValues { it == 2 }.isNotEmpty()
        }
        return Pair(acc, currentIdx == instructions.size)
    }
}

fun main() {
    val day = Day8()
    println(day.runPartOne())
    println(day.runPartTwo())
}
