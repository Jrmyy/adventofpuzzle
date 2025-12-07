package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day8 : Day<List<List<Int>>, Int> {

    override fun runPartOne(): Int = getInput().let { trees ->
        trees.mapIndexed { y, row ->
            row.mapIndexed { x, tree ->
                if (y == 0 || x == 0 || y == trees.size - 1 || x == row.size - 1) {
                    true
                } else {
                    val leftTrees = row.subList(0, x)
                    val rightTrees = row.subList(x + 1, row.size)
                    val topTrees = trees.subList(0, y).map { r -> r[x] }
                    val bottomTrees = trees.subList(y + 1, trees.size).map { r -> r[x] }
                    listOf(topTrees, leftTrees, rightTrees, bottomTrees).any {
                        it.all { i -> i < tree }
                    }
                }
            }
        }.flatten().count { it }
    }

    override fun runPartTwo(): Int = getInput().let { trees ->
        trees.mapIndexed { y, row ->
            row.mapIndexed { x, tree ->
                val leftTrees = row.subList(0, x).reversed()
                val rightTrees = row.subList(x + 1, row.size)
                val topTrees = trees.subList(0, y).map { r -> r[x] }.reversed()
                val bottomTrees = trees.subList(y + 1, trees.size).map { r -> r[x] }
                listOf(leftTrees, rightTrees, topTrees, bottomTrees).map {
                    if (it.all { i -> i < tree }) {
                        it.size
                    } else {
                        it.takeWhile { i -> i < tree }.size + 1
                    }
                }.reduce { acc, size -> acc * size }
            }
        }.flatten().max()
    }

    override fun getInput(): List<List<Int>> = AOCUtils.getDayInput(2022, 8).map {
        it.toList().map { i -> i.toString().toInt() }
    }
}

fun main() {
    val day = Day8()
    println(day.runPartOne())
    println(day.runPartTwo())
}
