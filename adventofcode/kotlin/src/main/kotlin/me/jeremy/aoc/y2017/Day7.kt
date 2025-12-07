package me.jeremy.aoc.y2017

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day7 : Day<Map<String, Day7.Node>, String> {

    data class Node(
        val value: Int,
        val childrenRef: List<String>
    )

    override fun runPartOne(): String = getRoot(getInput())

    override fun runPartTwo(): String {
        val tree = getInput()
        val unbalanced = mutableMapOf<String, Pair<Int, Int>>()
        tree.forEach { (ref, node) ->
            val weights = node.childrenRef.map { getWeight(tree, it) }
            if (weights.toSet().size > 1) {
                val uniqueWeights = weights.withIndex().groupingBy { it.value }.eachCount()
                val correct = uniqueWeights.filterValues { it > 1 }.keys.first()
                val incorrect = uniqueWeights.filterValues { it == 1 }.keys.first()
                unbalanced[ref] = Pair(
                    correct - incorrect,
                    weights.indexOf(incorrect)
                )
            }
        }
        var root = getRoot(tree)
        while (true) {
            val unb = unbalanced[root] ?: break
            root = tree[root]!!.childrenRef[unb.second]
        }
        return (tree[root]!!.value + unbalanced.values.first().first).toString()
    }

    override fun getInput(): Map<String, Node> =
        AOCUtils.getDayInput(2017, 7).associate { l ->
            val parts = l.split(" -> ")
            val node = parts.first()
            val children = parts.getOrNull(1)?.split(", ") ?: listOf()
            val (name, value) = node.split(" ")
            name to Node(value.drop(1).dropLast(1).toInt(), children)
        }

    private fun getRoot(tree: Map<String, Node>): String =
        tree.keys.first {
            tree.none { t -> it in t.value.childrenRef }
        }

    private fun getWeight(tree: Map<String, Node>, nodeStr: String): Int {
        val node = tree[nodeStr] ?: error("Should exist")
        if (node.childrenRef.isEmpty()) {
            return node.value
        }
        return node.childrenRef.sumOf { getWeight(tree, it) } + node.value
    }
}

fun main() {
    val day = Day7()
    println(day.runPartOne())
    println(day.runPartTwo())
}
