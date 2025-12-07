package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day22 : Day<Pair<MutableList<Int>, MutableList<Int>>, Int> {
    override fun runPartOne(): Int {
        val (first, second) = getInput()
        return score(combat(first, second))
    }

    override fun runPartTwo(): Int {
        val (first, second) = getInput()
        return score(recursiveCombat(first.toMutableList(), second.toMutableList()).second)
    }

    override fun getInput(): Pair<MutableList<Int>, MutableList<Int>> {
        val lines = AOCUtils.getDayInput(2020, 22)
        val idxOfEmpty = lines.indexOfFirst { it == "" }
        val first = lines.subList(1, idxOfEmpty)
        val second = lines.subList(idxOfEmpty + 2, lines.size)
        return Pair(
            first.map { it.toInt() }.toMutableList(),
            second.map { it.toInt() }.toMutableList(),
        )
    }

    private fun score(cards: List<Int>): Int =
        cards.reversed().reduceIndexed { index, acc, i -> acc + (index + 1) * i }

    private fun combat(first: MutableList<Int>, second: MutableList<Int>): List<Int> {
        while (first.isNotEmpty() && second.isNotEmpty()) {
            val firstPlay = first.removeFirst()
            val secondPlay = second.removeFirst()
            if (firstPlay < secondPlay) {
                second.add(secondPlay)
                second.add(firstPlay)
            } else {
                first.add(firstPlay)
                first.add(secondPlay)
            }
        }
        return if (first.isEmpty()) second else first
    }

    private fun recursiveCombat(first: MutableList<Int>, second: MutableList<Int>): Pair<Int, List<Int>> {
        val configs = mutableSetOf<String>()
        while (first.isNotEmpty() && second.isNotEmpty()) {
            val config = first.joinToString(" ") + " | " + second.joinToString(" ")
            if (!configs.add(config)) {
                return Pair(1, first.toMutableList())
            }
            val firstPlay = first.removeFirst()
            val secondPlay = second.removeFirst()
            if (firstPlay <= first.size && secondPlay <= second.size) {
                val winner = recursiveCombat(
                    first.take(firstPlay).toMutableList(),
                    second.take(secondPlay).toMutableList()
                )
                if (winner.first == 2) {
                    second.add(secondPlay)
                    second.add(firstPlay)
                } else {
                    first.add(firstPlay)
                    first.add(secondPlay)
                }
            } else {
                if (firstPlay < secondPlay) {
                    second.add(secondPlay)
                    second.add(firstPlay)
                } else {
                    first.add(firstPlay)
                    first.add(secondPlay)
                }
            }
        }
        return listOf(first.toMutableList(), second.toMutableList())
            .mapIndexed { index, it -> Pair(index + 1, it) }
            .first { it.second.isNotEmpty() }
    }
}

fun main() {
    val day = Day22()
    println(day.runPartOne())
    println(day.runPartTwo())
}
