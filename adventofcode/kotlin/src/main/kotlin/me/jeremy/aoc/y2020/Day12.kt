package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs

class Day12 : Day<List<Pair<Char, Int>>, Int> {
    override fun runPartOne(): Int {
        var currentDirection = 'E'
        var currentPosition = Pair(0, 0)
        getInput().forEach {
            val (instruction, amount) = it
            val nextPosition = getNextPosition(currentPosition, instruction, amount)
            if (nextPosition != null) {
                currentPosition = nextPosition
            } else {
                when (instruction) {
                    'F' -> currentPosition = getNextPosition(currentPosition, currentDirection, amount)!!
                    else -> currentDirection = changeDirection(currentDirection, instruction, amount)
                }
            }
        }
        return abs(currentPosition.first) + abs(currentPosition.second)
    }

    override fun runPartTwo(): Int {
        var currentWaypointPosition = Pair(10, -1)
        var currentShipPosition = Pair(0, 0)
        getInput().forEach {
            val (instruction, amount) = it
            val newWaypointPosition = getNextPosition(currentWaypointPosition, instruction, amount)
            if (newWaypointPosition != null) {
                currentWaypointPosition = newWaypointPosition
            } else {
                when (instruction) {
                    'F' -> currentShipPosition = Pair(
                        currentShipPosition.first + amount * currentWaypointPosition.first,
                        currentShipPosition.second + amount * currentWaypointPosition.second,
                    )
                    else -> currentWaypointPosition = turnWaypoint(currentWaypointPosition, instruction, amount)
                }
            }
        }
        return abs(currentShipPosition.first) + abs(currentShipPosition.second)
    }

    override fun getInput(): List<Pair<Char, Int>> = AOCUtils.getDayInput(2020, 12).map {
        Pair(it[0], it.substring(1).toInt())
    }

    private fun getNextPosition(currentPosition: Pair<Int, Int>, direction: Char, amount: Int): Pair<Int, Int>? =
        when (direction) {
            'N' -> Pair(currentPosition.first, currentPosition.second - amount)
            'S' -> Pair(currentPosition.first, currentPosition.second + amount)
            'W' -> Pair(currentPosition.first - amount, currentPosition.second)
            'E' -> Pair(currentPosition.first + amount, currentPosition.second)
            else -> null
        }

    private fun changeDirection(direction: Char, turningInstruction: Char, angle: Int): Char {
        val turningOrder = if (turningInstruction == 'L') {
            listOf('N', 'W', 'S', 'E')
        } else {
            listOf('N', 'E', 'S', 'W')
        }
        val idxOf = turningOrder.indexOf(direction)
        val jumpIdx = angle / 90
        return turningOrder[(idxOf + jumpIdx) % 4]
    }

    private fun turnWaypoint(position: Pair<Int, Int>, turningInstruction: Char, angle: Int): Pair<Int, Int> =
        if (turningInstruction == 'R') {
            when ((angle / 90) % 4) {
                1 -> Pair(-position.second, position.first)
                2 -> Pair(-position.first, -position.second)
                3 -> Pair(position.second, -position.first)
                else -> throw RuntimeException("can't execute $turningInstruction $angle")
            }
        } else {
            when ((angle / 90) % 4) {
                1 -> Pair(position.second, -position.first)
                2 -> Pair(-position.first, -position.second)
                3 -> Pair(-position.second, position.first)
                else -> throw RuntimeException("can't execute $turningInstruction $angle")
            }
        }
}

fun main() {
    val day = Day12()
    println(day.runPartOne())
    println(day.runPartTwo())
}
