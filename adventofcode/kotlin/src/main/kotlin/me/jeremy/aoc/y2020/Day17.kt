package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day17 : Day<MutableList<MutableList<MutableList<Char>>>, Int> {

    class Infinite3DMap(private val coordinates: MutableList<MutableList<MutableList<Char>>>) {

        private fun getAdjacentCubesCount(
            x: Int,
            y: Int,
            z: Int,
            state: MutableList<MutableList<MutableList<Char>>>
        ): Int =
            listOf(
                listOf(z - 1, z, z + 1).flatMap {
                    listOf(
                        Triple(x - 1, y, it),
                        Triple(x - 1, y - 1, it),
                        Triple(x - 1, y + 1, it),
                        Triple(x + 1, y, it),
                        Triple(x + 1, y - 1, it),
                        Triple(x + 1, y + 1, it),
                        Triple(x, y, it),
                        Triple(x, y - 1, it),
                        Triple(x, y + 1, it)
                    )
                }
            ).flatten().filter {
                it.third in state.indices &&
                    it.second in state[0].indices &&
                    it.first in state[0][0].indices &&
                    it != Triple(x, y, z)
            }.count {
                state[it.third][it.second][it.first] == '#'
            }

        fun countTotalCubes() = coordinates.flatten().flatten().count { it == '#' }

        private fun copy() = Infinite3DMap(
            coordinates.map { it.map { that -> that.toMutableList() }.toMutableList() }.toMutableList()
        )

        private fun pushLimits() {
            // Add z
            val emptyCoordinates = coordinates[0][0].indices.map {
                '.'
            }.toMutableList()
            coordinates.add(
                0,
                coordinates[0].indices.map {
                    emptyCoordinates.toMutableList()
                }.toMutableList()
            )
            coordinates.add(
                coordinates[0].indices.map {
                    emptyCoordinates.toMutableList()
                }.toMutableList()
            )

            // Add y
            coordinates.forEach {
                it.add(0, emptyCoordinates.toMutableList())
                it.add(emptyCoordinates.toMutableList())
            }

            // Add x
            coordinates.forEach { layer ->
                layer.forEach {
                    it.add(0, '.')
                    it.add('.')
                }
            }
        }

        fun updateMap() {
            pushLimits()
            val initialState = copy()
            coordinates.forEachIndexed { z: Int, layer: List<MutableList<Char>> ->
                layer.forEachIndexed { y: Int, mutableList ->
                    mutableList.forEachIndexed { x: Int, c: Char ->
                        val count = getAdjacentCubesCount(x, y, z, initialState.coordinates)
                        if (c == '#' && count !in (2..3)) {
                            coordinates[z][y][x] = '.'
                        } else if (c == '.' && count == 3) {
                            coordinates[z][y][x] = '#'
                        }
                    }
                }
            }
        }
    }

    class Infinite4DMap(private val coordinates: MutableList<MutableList<MutableList<MutableList<Char>>>>) {

        private fun getAdjacentCubesCount(
            x: Int,
            y: Int,
            z: Int,
            w: Int,
            state: MutableList<MutableList<MutableList<MutableList<Char>>>>
        ): Int =
            listOf(
                listOf(w - 1, w, w + 1).flatMap {
                    listOf(z - 1, z, z + 1).flatMap { zi ->
                        listOf(
                            listOf(x - 1, y, zi, it),
                            listOf(x - 1, y - 1, zi, it),
                            listOf(x - 1, y + 1, zi, it),
                            listOf(x + 1, y, zi, it),
                            listOf(x + 1, y - 1, zi, it),
                            listOf(x + 1, y + 1, zi, it),
                            listOf(x, y, zi, it),
                            listOf(x, y - 1, zi, it),
                            listOf(x, y + 1, zi, it)
                        )
                    }
                }
            ).flatten().filter {
                it[3] in state.indices &&
                    it[2] in state[0].indices &&
                    it[1] in state[0][0].indices &&
                    it[0] in state[0][0][0].indices &&
                    it != listOf(x, y, z, w)
            }.count {
                state[it[3]][it[2]][it[1]][it[0]] == '#'
            }

        fun countTotalCubes() = coordinates.flatten().flatten().flatten().count { it == '#' }

        private fun copy() = Infinite4DMap(
            coordinates.map {
                it.map { that ->
                    that.map { other ->
                        other.toMutableList()
                    }.toMutableList()
                }.toMutableList()
            }.toMutableList()
        )

        private fun pushLimits() {
            // Add w
            val emptyLine = coordinates[0][0][0].indices.map {
                '.'
            }.toMutableList()
            val emptySquare = coordinates[0][0].indices.map {
                emptyLine.toMutableList()
            }.toMutableList()
            val emptyCube = coordinates[0].indices.map {
                emptySquare.toMutableList()
            }.toMutableList()
            coordinates.add(
                0,
                emptyCube.map { it.map { that -> that.toMutableList() }.toMutableList() }.toMutableList()
            )
            coordinates.add(
                emptyCube.map { it.map { that -> that.toMutableList() }.toMutableList() }.toMutableList()
            )

            // Add z
            coordinates.forEach { hypercube ->
                hypercube.add(0, emptySquare.map { it.toMutableList() }.toMutableList())
                hypercube.add(emptySquare.map { it.toMutableList() }.toMutableList())
            }

            // Add y
            coordinates.forEach { hypercube ->
                hypercube.forEach { layer ->
                    layer.add(0, emptyLine.toMutableList())
                    layer.add(emptyLine.toMutableList())
                }
            }

            // Add x
            coordinates.forEach { hypercube ->
                hypercube.forEach { layer ->
                    layer.forEach { mutableList ->
                        mutableList.add(0, '.')
                        mutableList.add('.')
                    }
                }
            }
        }

        fun updateMap() {
            pushLimits()
            val initialState = copy()
            coordinates.forEachIndexed { w: Int, hypercube: MutableList<MutableList<MutableList<Char>>> ->
                hypercube.forEachIndexed { z: Int, layer: MutableList<MutableList<Char>> ->
                    layer.forEachIndexed { y: Int, mutableList: MutableList<Char> ->
                        mutableList.forEachIndexed { x: Int, c: Char ->
                            val count = getAdjacentCubesCount(x, y, z, w, initialState.coordinates)
                            if (c == '#' && count !in (2..3)) {
                                coordinates[w][z][y][x] = '.'
                            } else if (c == '.' && count == 3) {
                                coordinates[w][z][y][x] = '#'
                            }
                        }
                    }
                }
            }
        }
    }

    override fun runPartOne(): Int {
        val map = Infinite3DMap(getInput().toMutableList())
        (1..6).forEach { _ ->
            map.updateMap()
        }
        return map.countTotalCubes()
    }

    override fun runPartTwo(): Int {
        val map = Infinite4DMap(
            mutableListOf(getInput().toMutableList())
        )
        (1..6).forEach { _ ->
            map.updateMap()
        }
        return map.countTotalCubes()
    }

    override fun getInput(): MutableList<MutableList<MutableList<Char>>> = mutableListOf(
        AOCUtils.getDayInput(2020, 17).map {
            it.toList().toMutableList()
        }.toMutableList()
    )
}

fun main() {
    val day = Day17()
    println(day.runPartOne())
    println(day.runPartTwo())
}
