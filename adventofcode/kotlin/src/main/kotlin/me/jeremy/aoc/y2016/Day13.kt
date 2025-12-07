package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day13 : Day<Int, Int> {

    override fun runPartOne(): Int = getMinStep(Pair(1, 1), Pair(31, 39), getInput())

    override fun runPartTwo(): Int =
        (0..50).flatMap { y ->
            (0..50).map { x -> Pair(x, y) }
        }.count { getMinStep(Pair(1, 1), it, getInput()) in (1..50) }

    override fun getInput(): Int = AOCUtils.getDayInput(2016, 13).first().toInt()

    private fun getMinStep(
        from: Pair<Int, Int>,
        to: Pair<Int, Int>,
        favoriteNumber: Int
    ): Int {
        val grid = (0..60).map { (0..60).map { '.' } }
        val queue: ArrayDeque<Pair<Int, Int>> = ArrayDeque(listOf(from))
        val checked = grid.map { it.map { false }.toMutableList() }
        val dist = grid.map { it.map { 0 }.toMutableList() }
        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()
            listOf(
                Pair(curr.first - 1, curr.second),
                Pair(curr.first + 1, curr.second),
                Pair(curr.first, curr.second - 1),
                Pair(curr.first, curr.second + 1),
            )
                .filter { (x, y) ->
                    val res = (x * x + 3 * x + 2 * x * y + y + y * y + favoriteNumber).toString(2).count { it == '1' }
                    x in grid.first().indices &&
                        y in grid.indices &&
                        !checked[y][x] &&
                        res % 2 == 0
                }
                .forEach {
                    dist[it.second][it.first] = dist[curr.second][curr.first] + 1
                    checked[it.second][it.first] = true
                    queue.add(Pair(it.first, it.second))
                }
        }
        return dist[to.second][to.first]
    }
}

fun main() {
    val day = Day13()
    println(day.runPartOne())
    println(day.runPartTwo())
}
