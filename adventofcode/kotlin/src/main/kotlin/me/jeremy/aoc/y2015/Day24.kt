package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day24 : Day<List<Int>, Long> {
    override fun runPartOne(): Long {
        val packages = getInput().reversed()
        val perSlot = packages.sum() / 3
        return minQuantum(packages, perSlot)
    }

    override fun runPartTwo(): Long {
        val packages = getInput().reversed()
        val perSlot = packages.sum() / 4
        return minQuantum(packages, perSlot)
    }

    private fun minQuantum(packages: List<Int>, perSlot: Int): Long =
        combinations(packages, perSlot).let { cmb ->
            val m = cmb.minOf { l -> l.size }
            cmb.filter { it.size == m }.minOfOrNull { l ->
                l.fold(1L) { acc, i -> acc * i }
            } ?: throw Exception("Must have at least one")
        }

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2015, 24).map {
        it.toInt()
    }

    private fun combinations(
        numbers: List<Int>,
        target: Int,
        found: MutableSet<Set<Int>> = mutableSetOf(),
        quantums: MutableMap<Set<Int>, Long> = mutableMapOf(),
        noop: MutableSet<Set<Int>> = mutableSetOf()
    ): List<List<Int>> {
        val used = getInput().minus(numbers.toSet()).toSet()
        if (used in found) {
            return listOf()
        }
        if (used in noop) {
            return listOf()
        }
        if (used.size > (found.minOfOrNull { it.size } ?: Int.MAX_VALUE)) {
            noop.add(used)
            return listOf()
        }
        if (target == 0) {
            val quantum = used.fold(1L) { acc, i -> acc * i }
            quantums[used] = quantum
            if (quantum > (quantums.minOfOrNull { it.value } ?: Long.MAX_VALUE)) {
                noop.add(used)
                return listOf()
            }
            found.add(used)
            return listOf(used.toList())
        }
        return numbers.filter { it <= target }.flatMap {
            val nc = numbers.toMutableList()
            nc.remove(it)
            combinations(nc, target - it, found, quantums, noop)
        }
    }
}

fun main() {
    val day = Day24()
    println(day.runPartTwo())
}
