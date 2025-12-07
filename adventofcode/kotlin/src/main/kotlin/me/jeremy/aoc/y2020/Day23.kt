package me.jeremy.aoc.y2020

import me.jeremy.aoc.Day
import java.util.TreeSet

class Day23 : Day<List<Int>, String> {

    data class Node(val data: Int) : Comparable<Node> {
        lateinit var next: Node
        override fun compareTo(other: Node): Int = data.compareTo(other.data)
    }

    override fun runPartOne(): String {
        val (initialHead, cups) = buildTreeSet(getInput())
        val first = runGame(cups, initialHead, 100).floor(Node(1))!!
        var next = first.next
        var res = ""
        repeat(cups.size - 1) {
            res += next.data
            next = next.next
        }
        return res
    }

    override fun runPartTwo(): String {
        val (initialHead, cups) = buildTreeSet(getInput() + (10..1_000_000).toList())
        val nodeOne = runGame(cups, initialHead, 10_000_000).floor(Node(1))!!
        return ((nodeOne.next.data).toLong() * (nodeOne.next.next.data).toLong()).toString()
    }

    override fun getInput(): List<Int> = "589174263".toList().map { it.toString().toInt() }

    private fun buildTreeSet(ints: List<Int>): Pair<Node, TreeSet<Node>> {
        val nodes = ints.map { Node(it) }
        nodes.dropLast(1).forEachIndexed { index, node ->
            node.next = nodes[index + 1]
        }
        nodes.last().next = nodes.first()
        return Pair(nodes.first(), TreeSet(nodes))
    }

    private fun runGame(cups: TreeSet<Node>, initialHead: Node, n: Int): TreeSet<Node> {
        var head = initialHead
        repeat(n) {
            val toMove = listOf(head.next, head.next.next, head.next.next.next)
            val next = head.next.next.next.next
            head.next = next
            cups.removeAll(toMove)
            val dest = cups.lower(head) ?: cups.last()
            val after = dest.next
            dest.next = toMove[0]
            toMove[2].next = after
            cups.addAll(toMove)
            head = next
        }
        return cups
    }
}

fun main() {
    val day = Day23()
    println(day.runPartOne())
    println(day.runPartTwo())
}
