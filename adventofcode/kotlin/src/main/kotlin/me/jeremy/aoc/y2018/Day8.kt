package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day8 : Day<Day8.NavigationSystemNode, Int> {

    data class NavigationSystemNode(
        val metadata: List<Int>,
        val children: List<NavigationSystemNode>
    ) {
        fun sumAllMetadata(): Int =
            metadata.sum() + children.sumOf { it.sumAllMetadata() }

        fun sumIndexedMetadata(): Int =
            if (children.isEmpty()) {
                metadata.sum()
            } else {
                metadata.sumOf {
                    if (children.size >= it) {
                        children[it - 1].sumIndexedMetadata()
                    } else {
                        0
                    }
                }
            }
    }

    override fun runPartOne(): Int = getInput().sumAllMetadata()

    override fun runPartTwo(): Int = getInput().sumIndexedMetadata()

    override fun getInput(): NavigationSystemNode {
        val figures = AOCUtils.getDayInput(2018, 8)[0]
            .split(" ")
            .map { it.toInt() }
            .toMutableList()
        return buildNode(figures)
    }

    private fun buildNode(figures: MutableList<Int>): NavigationSystemNode {
        val childrenCount = figures.removeAt(0)
        val metadataCount = figures.removeAt(0)
        val children = mutableListOf<NavigationSystemNode>()
        val metadata = mutableListOf<Int>()
        repeat(childrenCount) { children.add(buildNode(figures)) }
        repeat(metadataCount) { metadata.add(figures.removeAt(0)) }
        return NavigationSystemNode(metadata, children)
    }
}

fun main() {
    val day = Day8()
    println(day.runPartOne())
    println(day.runPartTwo())
}
