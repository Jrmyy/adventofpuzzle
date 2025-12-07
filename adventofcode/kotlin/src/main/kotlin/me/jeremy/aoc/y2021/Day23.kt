package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day23 : Day<Day23.Maze, Long> {

    data class Maze(
        val hallway: Map<Int, Char>,
        val rooms: Map<Char, List<Char>>,
    ) {

        fun computeMinEnergyToOrder(
            cache: MutableMap<Maze, Long> = mutableMapOf(),
            moves: MutableSet<Pair<Pair<Char, Int>, Pair<Char, Int>>> = mutableSetOf()
        ): Long {
            if (this in cache) return cache.getValue(this)
            val movable = findMovableAmphipods()
            val minEnergy = if (movable.isNotEmpty()) {
                movable.minOfOrNull { alternative ->
                    val (toMove, moveInstruction, energy) = alternative
                    if (moves.contains(moveInstruction)) {
                        Int.MAX_VALUE.toLong()
                    } else {
                        val (from, to) = moveInstruction
                        val nh = hallway.toMutableMap()
                        val nr = rooms.mapValues { it.value.toMutableList() }.toMutableMap()
                        val newMaze = if (from.first == 'H') {
                            nh[from.second] = '.'
                            nr[to.first]!![to.second] = toMove
                            copy(hallway = nh, rooms = nr)
                        } else {
                            nh[to.second] = toMove
                            nr[from.first]!![from.second] = '.'
                            copy(hallway = nh, rooms = nr)
                        }
                        val nm = moves.toMutableSet()
                        nm.add(moveInstruction)
                        energy + newMaze.computeMinEnergyToOrder(cache, nm)
                    }
                } ?: 0L
            } else {
                if (!rooms.all { e -> e.value.all { it == e.key } }) {
                    Int.MAX_VALUE.toLong()
                } else {
                    0
                }
            }
            cache[this] = minEnergy
            return minEnergy
        }

        private fun findMovableAmphipods(): List<Triple<Char, Pair<Pair<Char, Int>, Pair<Char, Int>>, Int>> =
            rooms.keys.flatMap { findMovableInRoom(it) } + findMovableInHallway()

        private fun findMovableInHallway(): List<Triple<Char, Pair<Pair<Char, Int>, Pair<Char, Int>>, Int>> {
            val movable = hallway.filterValues { it != '.' }
            val movableToRoom = movable.filter {
                val roomPosition = ROOMS_POSITIONS[it.value]!!
                movable.filterKeys { k ->
                    k > min(it.key, roomPosition) && k < max(it.key, roomPosition)
                }.all { e -> e.value == '.' }
            }
            return movableToRoom.mapNotNull {
                val room = rooms[it.value]!!
                val toInRoom = room.indexOfLast { c -> c == '.' }
                if (toInRoom >= 0 && room.all { c -> c == it.value || c == '.' }) {
                    Triple(
                        it.value,
                        Pair(Pair('H', it.key), Pair(it.value, toInRoom)),
                        ENERGY_FACTORS[it.value]!! * (abs(ROOMS_POSITIONS[it.value]!! - it.key) + toInRoom + 1)
                    )
                } else {
                    null
                }
            }
        }

        private fun findMovableInRoom(letter: Char): List<Triple<Char, Pair<Pair<Char, Int>, Pair<Char, Int>>, Int>> {
            val room = rooms[letter]!!
            val unwantedCharacters = room.filter { it != '.' && it != letter }
            val amphipod = if (unwantedCharacters.isEmpty()) {
                null
            } else {
                val idx = room.indexOfFirst { it != '.' }
                if (idx >= 0) {
                    Pair(room[idx], idx + 1)
                } else {
                    null
                }
            }
            if (amphipod == null) {
                return listOf()
            }
            val roomPosition = ROOMS_POSITIONS[letter]!!
            val before = hallway.filterKeys { it < roomPosition }.keys.reversed().takeWhile {
                hallway[it] == '.'
            }
            val after = hallway.filterKeys { it > roomPosition }.keys.takeWhile {
                hallway[it] == '.'
            }
            return (before + after).map {
                Triple(
                    amphipod.first,
                    Pair(Pair(letter, amphipod.second - 1), Pair('H', it)),
                    ENERGY_FACTORS[amphipod.first]!! * (amphipod.second + abs(roomPosition - it))
                )
            }
        }

        companion object {

            val ENERGY_FACTORS = mapOf(
                'A' to 1,
                'B' to 10,
                'C' to 100,
                'D' to 1000
            )

            val ROOMS_POSITIONS = mapOf(
                'A' to 2,
                'B' to 4,
                'C' to 6,
                'D' to 8
            )

            fun unfoldRooms(maze: Maze): Maze {
                val toAdd = mapOf(
                    'A' to listOf('D', 'D'),
                    'B' to listOf('C', 'B'),
                    'C' to listOf('B', 'A'),
                    'D' to listOf('A', 'C'),
                )
                return maze.copy(
                    rooms = maze.rooms.mapValues { e ->
                        listOf(e.value.first()) + toAdd[e.key]!! + listOf(e.value.last())
                    }
                )
            }

            fun from(lines: List<String>): Maze =
                Maze(
                    mapOf(
                        0 to '.',
                        1 to '.',
                        3 to '.',
                        5 to '.',
                        7 to '.',
                        9 to '.',
                        10 to '.'
                    ),
                    mapOf(
                        'A' to listOf(lines[2][3], lines[3][3]),
                        'B' to listOf(lines[2][5], lines[3][5]),
                        'C' to listOf(lines[2][7], lines[3][7]),
                        'D' to listOf(lines[2][9], lines[3][9])
                    )
                )
        }
    }

    override fun runPartOne(): Long = getInput().computeMinEnergyToOrder()

    override fun runPartTwo(): Long = Maze.unfoldRooms(getInput()).computeMinEnergyToOrder()

    override fun getInput(): Maze = Maze.from(AOCUtils.getDayInput(2021, 23))
}

fun main() {
    val day = Day23()
    println(day.runPartOne())
    println(day.runPartTwo())
}
