package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day12 : Day<Pair<List<Char>, List<Pair<List<Char>, Char>>>, Long> {
    override fun runPartOne(): Long = run(20).second
    override fun runPartTwo(): Long {
        val (diffs, count) = run(2000)
        val diffBetweenStep = diffs.maxByOrNull { it.value.size }!!.key
        val maxIter = diffs[diffBetweenStep]!!.maxOf { it }
        return (50_000_000_000 - (maxIter + 1)) * diffBetweenStep + count
    }

    override fun getInput(): Pair<MutableList<Char>, List<Pair<List<Char>, Char>>> {
        val lines = AOCUtils.getDayInput(2018, 12)
        val initialState = lines[0].removePrefix("initial state: ").toMutableList()
        val rules = lines.subList(2, lines.size).map {
            val parts = it.split(" => ")
            Pair(parts[0].toList(), parts[1][0])
        }
        return Pair(initialState, rules)
    }

    private fun run(numIter: Long): Pair<HashMap<Long, List<Long>>, Long> {
        val (state, rules) = getInput()
        val mapState = hashMapOf(*(state.mapIndexed { index, c -> Pair(index.toLong(), c) }.toTypedArray()))
        var prev = mapState.filter { it.value == '#' }.map { it.key }.sum()
        val diffs = hashMapOf<Long, List<Long>>()
        for (n in 0 until numIter) {
            val maxOf = mapState.maxOf { it.key }
            repeat(4) { x ->
                mapState[maxOf + (x + 1)] = '.'
                mapState[-(x + 1).toLong() - (4 * n)] = '.'
            }
            val newMaxOf = mapState.maxOf { it.key }
            val minOf = mapState.minOf { it.key }
            val copy = hashMapOf(*(mapState.toList().toTypedArray()))
            (minOf + 2..newMaxOf - 2).forEach { idx ->
                val sub = listOf(
                    copy[idx - 2]!!,
                    copy[idx - 1]!!,
                    copy[idx]!!,
                    copy[idx + 1]!!,
                    copy[idx + 2]!!,
                )
                val foundRule = rules.firstOrNull { r -> r.first == sub }
                if (foundRule != null) {
                    mapState[idx] = foundRule.second
                } else {
                    mapState[idx] = '.'
                }
            }
            val diff = mapState.filter { it.value == '#' }.map { it.key }.sum() - prev
            prev += diff
            diffs[diff] = diffs.getOrDefault(diff, listOf()) + listOf(n)
            if (diffs[diff]!!.size > 20) {
                return Pair(diffs, mapState.filter { it.value == '#' }.map { it.key }.sum())
            }
        }
        return Pair(diffs, mapState.filter { it.value == '#' }.map { it.key }.sum())
    }
}

fun main() {
    val day = Day12()
    println(day.runPartOne())
    println(day.runPartTwo())
}
