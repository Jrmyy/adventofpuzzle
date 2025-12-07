package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day4 : Day<Pair<List<Int>, MutableList<List<List<Int>>>>, Int> {

    override fun runPartOne(): Int {
        val (drawnNumbers, boards) = getInput()
        return playBingo(drawnNumbers, boards, 1)
    }

    override fun runPartTwo(): Int {
        val (drawnNumbers, boards) = getInput()
        return playBingo(drawnNumbers, boards, boards.size)
    }

    override fun getInput(): Pair<List<Int>, MutableList<List<List<Int>>>> {
        val lines = AOCUtils.getDayInput(2021, 4)
        val drawnNumbers = lines.first().split(",").map { it.toInt() }
        val boards = mutableListOf<List<List<Int>>>()
        var currentBoard = mutableListOf<List<Int>>()
        lines.subList(2, lines.size).forEach {
            if (it == "") {
                boards.add(currentBoard)
                currentBoard = mutableListOf()
            } else {
                currentBoard.add(it.split(" ").filter { d -> d.isNotBlank() }.map { d -> d.toInt() })
            }
        }
        boards.add(currentBoard)
        return Pair(drawnNumbers, boards)
    }

    private fun playBingo(
        drawnNumbers: List<Int>,
        boards: MutableList<List<List<Int>>>,
        winningBoardsCount: Int
    ): Int {
        val boardsInitialSize = boards.size
        var currentIdx = 0
        var remainingNumbersOnWinningBoard: List<Int>? = null
        while (remainingNumbersOnWinningBoard == null) {
            val drawnNumbersUntilNow = drawnNumbers.subList(0, currentIdx + 1)
            val matchingBoards = boards.mapNotNull { b ->
                val matchingRows = b.mapNotNull { r ->
                    if (r.intersect(drawnNumbersUntilNow).toList() == r) r else null
                }
                val matchingCols = b.indices.map { i -> b.map { r -> r[i] } }.mapNotNull { c ->
                    if (c.intersect(drawnNumbersUntilNow).toList() == c) c else null
                }
                if (matchingRows.isNotEmpty() || matchingCols.isNotEmpty()) {
                    b
                } else {
                    null
                }
            }
            boards.removeAll(matchingBoards)
            if (boards.size == boardsInitialSize - winningBoardsCount) {
                remainingNumbersOnWinningBoard = matchingBoards.first()
                    .flatten()
                    .filter { !drawnNumbersUntilNow.contains(it) }
            } else {
                currentIdx++
            }
        }
        return remainingNumbersOnWinningBoard.sum() * drawnNumbers[currentIdx]
    }
}

fun main() {
    val day = Day4()
    println(day.runPartOne())
    println(day.runPartTwo())
}
