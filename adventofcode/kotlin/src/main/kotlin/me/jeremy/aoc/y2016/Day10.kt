package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day10 : Day<List<String>, Int> {

    override fun runPartOne(): Int {
        val (chips, _) = handleBotInstructions()
        return chips.filter { e -> e.value.sorted() == mutableListOf(17, 61) }.keys.first()
    }

    override fun runPartTwo(): Int {
        val (_, outputs) = handleBotInstructions()
        return outputs.filter { it.first <= 2 }.fold(1) { acc, pair -> acc * pair.second }
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2016, 10)

    private fun handleBotInstructions(): Pair<MutableMap<Int, MutableList<Int>>, MutableList<Pair<Int, Int>>> {
        val chips = mutableMapOf<Int, MutableList<Int>>()
        val todo = mutableListOf<String>()
        val outputs = mutableListOf<Pair<Int, Int>>()
        val instructions = getInput()
        instructions.forEach {
            if (it.startsWith("bot")) {
                try {
                    handleInstruction(it, chips, outputs)
                } catch (_: UnsupportedOperationException) {
                    todo.add(it)
                }
            } else {
                val (value, bot) = "value (\\d+) goes to bot (\\d+)".toRegex().find(it)!!
                    .groupValues
                    .drop(1)
                    .map { s -> s.toInt() }
                chips.putIfAbsent(bot, mutableListOf())
                chips[bot]!!.add(value)
            }
        }
        while (todo.isNotEmpty()) {
            val instruction = todo.removeFirst()
            try {
                handleInstruction(instruction, chips, outputs)
            } catch (_: UnsupportedOperationException) {
                todo.add(instruction)
            }
        }
        return Pair(chips, outputs)
    }

    private fun handleInstruction(
        instruction: String,
        chips: MutableMap<Int, MutableList<Int>>,
        outputs: MutableList<Pair<Int, Int>>
    ) {
        val (bot, lowType, lowTypeId, highType, highTypeId) =
            "bot (\\d+) gives low to (output|bot) (\\d+) and high to (output|bot) (\\d+)".toRegex().find(instruction)!!
                .groupValues
                .drop(1)
        val botChip = chips[bot.toInt()]
        if (botChip?.size == 2) {
            val parsedLowTypeId = lowTypeId.toInt()
            if (lowType == "output") {
                outputs.add(Pair(parsedLowTypeId, botChip.min()))
            } else {
                chips.putIfAbsent(parsedLowTypeId, mutableListOf())
                chips[parsedLowTypeId]!!.add(botChip.min())
            }
            val parsedHighTypeId = highTypeId.toInt()
            if (highType == "output") {
                outputs.add(Pair(parsedHighTypeId, botChip.max()))
            } else {
                chips.putIfAbsent(parsedHighTypeId, mutableListOf())
                chips[parsedHighTypeId]!!.add(botChip.max())
            }
        } else {
            throw UnsupportedOperationException()
        }
    }
}

fun main() {
    val day = Day10()
    println(day.runPartOne())
    println(day.runPartTwo())
}
