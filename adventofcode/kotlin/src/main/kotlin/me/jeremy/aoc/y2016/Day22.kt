package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day22 : Day<List<Triple<String, Int, Int>>, Int> {

    override fun runPartOne(): Int {
        val nodes = getInput()
        return nodes.mapIndexed { index, node ->
            if (node.second == 0) {
                0
            } else {
                val cpNodes = nodes.toMutableList()
                cpNodes.removeAt(index)
                cpNodes.count { node.second < it.third }
            }
        }.sum()
    }

    override fun runPartTwo(): Int {
        val nodes = getInput()
        val grid = mutableListOf<MutableList<Pair<Int, Int>>>()
        for (node in nodes) {
            val (_, y) = node.first.replace("/dev/grid/node-", "")
                .split("-").map { it.drop(1).toInt() }
            if (y == grid.size) {
                grid.add(mutableListOf(Pair(node.second, node.second + node.third)))
            } else {
                grid[y].add(Pair(node.second, node.second + node.third))
            }
        }
        println(grid.joinToString("\n") { it.joinToString("-") })
        // After printing carefully the grid, every server can contain another server except for a small part
        // that forms a wall. We need to get the min distance from the empty server (there is only one)
        // to the final server of the first
        val fromFree = 45 // calculated by hand
        // At this point the last element on the row is free, so to move one to the left, we need 5 moves
        return fromFree + (grid.first().size - 2) * 5
    }

    override fun getInput(): List<Triple<String, Int, Int>> = AOCUtils.getDayInput(2016, 22).drop(2).map {
        val match = "(.*) +\\d+T +(\\d+)T +(\\d+)T +\\d+%".toRegex().find(it)!!.groupValues
        Triple(match[1].trim(), match[2].toInt(), match[3].toInt())
    }
}

fun main() {
    val day = Day22()
    println(day.runPartTwo())
}
