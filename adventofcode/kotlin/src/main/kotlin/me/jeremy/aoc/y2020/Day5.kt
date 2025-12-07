package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.ceil

class Day5 : Day<List<List<Char>>, Int> {

    data class Seat(
        val row: Int,
        val col: Int
    ) {
        fun getSeatId(): Int = (MAX_COL + 1) * row + col
    }

    override fun runPartOne(): Int =
        getInput()
            .map { getSeat(it).getSeatId() }
            .maxOrNull() ?: throw RuntimeException("(")

    override fun runPartTwo(): Int {
        val takenSeats = getInput().map { getSeat(it) }
        val plainRows = takenSeats
            .groupingBy { it.row }
            .eachCount()
            .filter { it.value == MAX_COL + 1 }
        val firstPlainRow = plainRows.minOf { it.key }
        val lastPlainRow = plainRows.maxOf { it.key }
        val allPlaces = (firstPlainRow + 1 until lastPlainRow - 1).flatMap {
            (0..MAX_COL).map { that ->
                Seat(it, that).getSeatId()
            }
        }
        val notTakenSeats = allPlaces.subtract(takenSeats.map { it.getSeatId() })
        if (notTakenSeats.size != 1) {
            throw RuntimeException("Too many seats not taken")
        }
        return notTakenSeats.first()
    }

    private fun getSeat(boardingPass: List<Char>): Seat {
        var currentRow = Pair(0, MAX_ROW)
        var currentCol = Pair(0, MAX_COL)
        boardingPass.forEach {
            when (it) {
                'F' -> {
                    currentRow = Pair(
                        currentRow.first,
                        currentRow.second - ceil((currentRow.second - currentRow.first).toDouble() / 2).toInt()
                    )
                }
                'B' -> {
                    currentRow = Pair(
                        currentRow.first + ceil((currentRow.second - currentRow.first).toDouble() / 2).toInt(),
                        currentRow.second
                    )
                }
                'L' -> {
                    currentCol = Pair(
                        currentCol.first,
                        currentCol.second - ceil((currentCol.second - currentCol.first).toDouble() / 2).toInt()
                    )
                }
                else -> {
                    currentCol = Pair(
                        currentCol.first + ceil((currentCol.second - currentCol.first).toDouble() / 2).toInt(),
                        currentCol.second
                    )
                }
            }
        }
        return Seat(currentRow.first, currentCol.first)
    }

    override fun getInput(): List<List<Char>> = AOCUtils.getDayInput(2020, 5).map { it.toList() }

    companion object {
        const val MAX_ROW = 127
        const val MAX_COL = 7
    }
}

fun main() {
    val day = Day5()
    println(day.runPartOne())
    println(day.runPartTwo())
}
