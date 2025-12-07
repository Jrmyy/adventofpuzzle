package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day20 : Day<Pair<List<Char>, MutableList<MutableList<Char>>>, Int> {
    override fun runPartOne(): Int = enhanceImage(2)

    override fun runPartTwo(): Int = enhanceImage(50)

    override fun getInput(): Pair<List<Char>, MutableList<MutableList<Char>>> {
        val lines = AOCUtils.getDayInput(2021, 20)
        val imageEnhancement = lines.first().toList()
        val iptImage = lines.subList(2, lines.size).map { it.toList().toMutableList() }.toMutableList()
        return Pair(imageEnhancement, iptImage)
    }

    private fun enhanceImage(steps: Int): Int {
        val ipt = getInput()
        val imageEnhancement = ipt.first
        var iptImage = ipt.second
        repeat(steps) { i ->
            val newIptImage = iptImage.map { it.toList().toMutableList() }.toMutableList()
            val toAddFirst = if (i == 0) (0 until iptImage.first().size).map { '.' } else
                newIptImage.first().toMutableList()
            val toAddLast = if (i == 0) (0 until iptImage.first().size).map { '.' } else
                newIptImage.last().toMutableList()
            newIptImage.addAll(
                0,
                listOf(
                    toAddFirst.toMutableList(),
                    toAddFirst.toMutableList(),
                    toAddFirst.toMutableList()
                )
            )
            newIptImage.addAll(
                listOf(
                    toAddLast.toMutableList(),
                    toAddLast.toMutableList(),
                    toAddLast.toMutableList()
                )
            )
            newIptImage.forEach { r ->
                r.addAll(0, (0 until 3).map { if (i == 0) '.' else r.first() })
                r.addAll((0 until 3).map { if (i == 0) '.' else r.last() })
            }
            val updatedNewIptImage = newIptImage.map { it.toMutableList() }.toMutableList()
            newIptImage.forEachIndexed { y, row ->
                row.forEachIndexed { x, _ ->
                    val window = listOf(
                        Pair(y - 1, x - 1),
                        Pair(y - 1, x),
                        Pair(y - 1, x + 1),
                        Pair(y, x - 1),
                        Pair(y, x),
                        Pair(y, x + 1),
                        Pair(y + 1, x - 1),
                        Pair(y + 1, x),
                        Pair(y + 1, x + 1)
                    ).map { yx ->
                        val value = if (yx == Pair(-1, -1)) {
                            Pair(0, 0)
                        } else if (yx == Pair(-1, row.size)) {
                            Pair(0, row.size - 1)
                        } else if (yx.first == -1) {
                            Pair(0, yx.second)
                        } else if (yx == Pair(newIptImage.size, -1)) {
                            Pair(newIptImage.size - 1, 0)
                        } else if (yx == Pair(newIptImage.size, row.size)) {
                            Pair(newIptImage.size - 1, row.size - 1)
                        } else if (yx.first == newIptImage.size) {
                            Pair(newIptImage.size - 1, yx.second)
                        } else if (yx.second == -1) {
                            Pair(yx.first, 0)
                        } else if (yx.second == row.size) {
                            Pair(yx.first, row.size - 1)
                        } else {
                            yx
                        }
                        if (newIptImage[value.first][value.second] == '.') 0 else 1
                    }.joinToString("")
                    val enhancementIdx = window.toLong(2).toInt()
                    updatedNewIptImage[y][x] = imageEnhancement[enhancementIdx]
                }
            }
            iptImage = updatedNewIptImage
        }
        return iptImage.flatten().count { it == '#' }
    }
}

fun main() {
    val day = Day20()
    println(day.runPartOne())
    println(day.runPartTwo())
}
