package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.ceil

class Day14 : Day<List<Day14.Reaction>, Long> {

    data class ChemicalElement(
        val amount: Long,
        val name: String
    )

    data class Reaction(
        val inputs: List<ChemicalElement>,
        val output: ChemicalElement
    )

    override fun runPartOne(): Long = doRun(1)

    override fun runPartTwo(): Long {
        val maxOre = 1000000000000
        var minFuel = maxOre / doRun(1)
        var maxFuel = 10 * minFuel

        while (doRun(maxFuel) < maxOre) {
            minFuel = maxFuel
            maxFuel *= 10
        }

        while (maxFuel - minFuel > 1) {
            val medianFuel = minFuel + (maxFuel - minFuel) / 2
            val medianOre = doRun(medianFuel)
            if (medianOre > maxOre) {
                maxFuel = medianFuel
            } else {
                minFuel = medianFuel
            }
        }

        if (doRun(maxFuel) < maxOre) {
            return maxFuel
        }
        return minFuel
    }

    override fun getInput(): List<Reaction> = AOCUtils.getDayInput(2019, 14).map {
        val iptOpt = it.split(" => ")
        val groups = Regex("((\\d+ [A-Z]+)(,|))").findAll(iptOpt[0]).toList()
            .flatMap { that -> that.groups.toList() }
        if (groups.size % 4 != 0) {
            throw RuntimeException(":(")
        }
        val inputs = (0 until groups.size / 4).map { that ->
            val parts = groups[2 + 4 * that]!!.value.split(" ")
            ChemicalElement(parts[0].toLong(), parts[1])
        }
        val optParts = iptOpt[1].split(" ")
        Reaction(inputs, ChemicalElement(optParts[0].toLong(), optParts[1]))
    }

    private fun doRun(fuelAmount: Long): Long {
        val partsThatNeedOre = mutableListOf(ChemicalElement(fuelAmount, "FUEL"))
        val reactions = getInput()
        val tooMuch = mutableMapOf<String, Long>()
        var ore = 0L
        while (partsThatNeedOre.size > 0) {
            val currentStudied = partsThatNeedOre.removeAt(0)
            val reaction = reactions.firstOrNull { it.output.name == currentStudied.name }
                ?: throw RuntimeException("Impossible to produce ${currentStudied.name}")
            val reallyNeeded = currentStudied.amount - tooMuch.getOrDefault(currentStudied.name, 0)
            val multiplier = ceil(
                reallyNeeded.toDouble() / reaction.output.amount.toDouble()
            ).toInt()
            tooMuch[currentStudied.name] = 0
            val scaledReaction = reaction.inputs.map {
                ChemicalElement(multiplier * it.amount, it.name)
            }
            partsThatNeedOre.addAll(scaledReaction.filter { it.name != "ORE" })
            ore += scaledReaction.firstOrNull { it.name == "ORE" }?.amount ?: 0L
            tooMuch[currentStudied.name] = multiplier * reaction.output.amount - reallyNeeded
        }
        return ore
    }
}

fun main() {
    val day = Day14()
    println(day.runPartOne())
    println(day.runPartTwo())
}
