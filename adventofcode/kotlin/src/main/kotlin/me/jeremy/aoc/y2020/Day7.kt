package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day7 : Day<List<Day7.BagRule>, Int> {

    data class BagRule(
        val container: Bag,
        val contained: List<Bag>
    )

    data class Bag(
        val amount: Int,
        val description: String
    )

    override fun runPartOne(): Int {
        val rules = getInput()
        val toCheckCanContain = rules
            .filter { it.contained.any { that -> that.description == "shiny gold" } }
            .map { it.container }
            .toMutableList()
        val canContain = toCheckCanContain.toMutableList()
        while (toCheckCanContain.isNotEmpty()) {
            val studied = toCheckCanContain.removeAt(0)
            val contains = rules
                .filter { it.contained.any { that -> that.description == studied.description } }
                .map { it.container }
            toCheckCanContain.addAll(contains)
            canContain.addAll(contains)
        }
        return canContain.distinct().size
    }

    override fun runPartTwo(): Int {
        val rules = getInput()
        val contained = rules.filter {
            it.container.description == "shiny gold"
        }.flatMap {
            it.contained
        }.toMutableList()
        val bagsAmount = rules.first {
            it.container.description == contained[0].description
        }.contained.toMutableList()
        while (contained.isNotEmpty()) {
            val currentStudied = contained.removeAt(0)
            val rule = rules.first {
                it.container.description == currentStudied.description
            }
            val scaledRule = rule.contained.map {
                Bag(currentStudied.amount * it.amount, it.description)
            }
            contained.addAll(scaledRule)
            bagsAmount.addAll(scaledRule)
        }
        return bagsAmount.sumOf { it.amount }
    }

    override fun getInput(): List<BagRule> = AOCUtils.getDayInput(2020, 7).map {
        val (containerStr, containedStr) = it.split(" bags contain ")
        val containedRegex = Regex("(\\d+ (\\w+ )+)bag(?:s|)(?:, |.)")
        val groups = containedRegex.findAll(containedStr).toList().flatMap { that -> that.groups }
        if (groups.size % 3 != 0) {
            throw RuntimeException(":(")
        }
        val inputs = (0 until groups.size / 3).map { that ->
            val parts = groups[1 + 3 * that]!!.value.split(" ")
            Bag(parts[0].toInt(), parts.drop(1).joinToString(" ").trim())
        }
        BagRule(
            Bag(1, containerStr),
            inputs
        )
    }
}

fun main() {
    val day = Day7()
    println(day.runPartOne())
    println(day.runPartTwo())
}
