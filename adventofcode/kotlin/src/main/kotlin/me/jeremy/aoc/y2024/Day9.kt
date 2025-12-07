package me.jeremy.aoc.y2024

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day9 : Day<MutableList<Int>, Long> {
    override fun runPartOne(): Long {
        val blocks = getInput()
        while (true) {
            val fileBlock = blocks.indexOfLast { it != -1 }
            val freeSpace = blocks.indexOfFirst { it == -1 }
            if (freeSpace > fileBlock) {
                break
            }
            blocks[freeSpace] = blocks[fileBlock]
            blocks[fileBlock] = -1
        }
        return blocks.mapIndexed { idx, c -> if (c == -1) 0 else idx * c.toLong() }.sum()
    }

    override fun runPartTwo(): Long {
        val blocks = getInput()
        var toMove = blocks.max()
        val sizes = blocks.groupBy { it }.mapValues { it.value.size }
        val freeSpaces = mutableListOf<Pair<Int, Int>>()
        var i = 0
        while (i <= blocks.size - 1) {
            if (blocks[i] != -1) {
                i++
                continue
            }
            val p = Pair(i, i + blocks.subList(i, blocks.size).takeWhile { j -> j == -1 }.size)
            freeSpaces.add(p)
            i = p.second
        }
        while (toMove >= 0) {
            val toFit = sizes[toMove]!!
            // First we check if we can find a free space big enough for this file
            val freeSpaceIdx = freeSpaces.indexOfFirst { toFit <= it.second - it.first }
            if (freeSpaceIdx == -1) {
                toMove--
                continue
            }

            val freeSpace = freeSpaces[freeSpaceIdx]
            val startFileIdx = blocks.indexOfFirst { it == toMove }
            // Then we check that the file will be moved left
            if (startFileIdx < freeSpace.first) {
                toMove--
                continue
            }

            // We book the space necessary for the file and update the free space size accordingly
            if (freeSpace.first + toFit < freeSpace.second) {
                freeSpaces[freeSpaceIdx] = Pair(freeSpace.first + toFit, freeSpace.second)
            } else {
                freeSpaces.removeAt(freeSpaceIdx)
            }

            // We update the blocks
            for (j in (freeSpace.first until freeSpace.first + toFit)) {
                if (blocks[j] != -1) {
                    throw Exception("Cannot replace block file by an other block file")
                }
                blocks[j] = toMove
            }
            for (j in (startFileIdx until startFileIdx + toFit)) {
                blocks[j] = -1
            }
            toMove--
        }
        return blocks.mapIndexed { idx, c -> if (c == -1) 0 else idx * c.toLong() }.sum()
    }

    override fun getInput(): MutableList<Int> = AOCUtils.getDayInput(2024, 9).first().let {
        var fileIdx = 0
        it.flatMapIndexed { idx, c ->
            val i = c.toString().toInt()
            val nc = if (idx % 2 == 1) {
                -1
            } else {
                fileIdx
            }
            if (idx % 2 == 0) {
                fileIdx++
            }
            List(i) { nc }
        }.toMutableList()
    }
}

fun main() {
    val day = Day9()
    println(day.runPartOne())
    println(day.runPartTwo())
}
