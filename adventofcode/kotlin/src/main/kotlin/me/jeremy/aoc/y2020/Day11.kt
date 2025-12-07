package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Plane(private val coordinates: List<MutableList<Char>>) {

    private fun getSeatsCountInAdjacentDirections(x: Int, y: Int, state: List<MutableList<Char>>): Int =
        listOf(
            Pair(-1, -1),
            Pair(0, -1),
            Pair(1, -1),
            Pair(-1, 0),
            Pair(1, 0),
            Pair(-1, 1),
            Pair(0, 1),
            Pair(1, 1)
        ).mapNotNull {
            var currentX = x + it.first
            var currentY = y + it.second
            var hasFoundBeforeTerminating = false
            while (currentX in state[0].indices && currentY in state.indices) {
                if (state[currentY][currentX] != '.') {
                    hasFoundBeforeTerminating = true
                    break
                }
                currentX += it.first
                currentY += it.second
            }
            if (hasFoundBeforeTerminating) {
                state[currentY][currentX]
            } else {
                null
            }
        }.count { it == '#' }

    fun copy() = Plane(coordinates.map { it.toMutableList() }.toList())

    fun updatePlane(wideSearch: Boolean, seatsCountToSwitchToEmpty: Int) {
        val initialState = copy()
        coordinates.forEachIndexed { y: Int, mutableList: MutableList<Char> ->
            mutableList.forEachIndexed { x, c ->
                val count = if (wideSearch) {
                    getSeatsCountInAdjacentDirections(x, y, initialState.coordinates)
                } else {
                    AOCUtils.getAdjacentPositions(initialState.coordinates, y, x, true).count {
                        initialState.coordinates[it.second][it.first] == '#'
                    }
                }
                if (c == '#' && count >= seatsCountToSwitchToEmpty) {
                    coordinates[y][x] = 'L'
                } else if (c == 'L' && count == 0) {
                    coordinates[y][x] = '#'
                }
            }
        }
    }

    fun getOccupiedSeatsCount(): Int = coordinates.flatten().count { it == '#' }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Plane) return false
        return this.coordinates == other.coordinates
    }

    override fun hashCode(): Int {
        return coordinates.hashCode()
    }
}

class Day11 : Day<Plane, Int> {
    override fun runPartOne(): Int = doRun()

    override fun runPartTwo(): Int = doRun(true, 5)

    override fun getInput(): Plane = Plane(
        AOCUtils.getDayInput(2020, 11).map { it.toList().toMutableList() }
    )

    private fun doRun(wideSearch: Boolean = false, seatsCountToSwitchToEmpty: Int = 4): Int {
        val map = getInput()
        var lastPlane = map.copy()
        var hasDone = false
        while (!hasDone) {
            val beforePlane = map.copy()
            map.updatePlane(wideSearch, seatsCountToSwitchToEmpty)
            lastPlane = map.copy()
            hasDone = lastPlane == beforePlane
        }
        return lastPlane.getOccupiedSeatsCount()
    }
}

fun main() {
    val day = Day11()
    println(day.runPartOne())
    println(day.runPartTwo())
}
