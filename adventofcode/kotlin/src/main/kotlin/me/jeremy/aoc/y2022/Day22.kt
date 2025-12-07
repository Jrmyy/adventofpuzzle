package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day22 : Day<Pair<List<List<Char>>, List<String>>, Int> {

    override fun runPartOne(): Int {
        val (map, instructions) = getInput()
        val y = map.indexOfFirst { ',' in it || '#' in it }
        val x = map[y].indexOfFirst { it == '.' }
        var curr = Pair(x, y)
        var direction = 0
        for (ins in instructions) {
            if ("\\d+".toRegex().matches(ins)) {
                var i = 1
                val dir = DIRECTIONS[direction]
                while (i <= ins.toInt()) {
                    val new = Pair(
                        (curr.first + dir.first).mod(map.first().size),
                        (curr.second + dir.second).mod(map.size)
                    )
                    if (map[new.second][new.first] == '.') {
                        curr = new
                    } else if (map[new.second][new.first] == '#') {
                        break
                    } else {
                        var void = new
                        while (map[void.second][void.first] == ' ') {
                            void = Pair(
                                (void.first + dir.first).mod(map[void.second].size),
                                (void.second + dir.second).mod(map.size)
                            )
                        }
                        if (map[void.second][void.first] == '.') {
                            curr = void
                        } else if (map[void.second][void.first] == '#') {
                            break
                        } else {
                            error("Should not happen")
                        }
                    }
                    i++
                }
            } else {
                direction = when (ins) {
                    "R" -> (direction + 1).mod(DIRECTIONS.size)
                    "L" -> (direction - 1).mod(DIRECTIONS.size)
                    else -> error("Should not be possible to have : $ins")
                }
            }
        }
        return (curr.second + 1) * 1000 + (curr.first + 1) * 4 + direction
    }

    override fun runPartTwo(): Int {
        val (map, instructions) = getInput()
        val y = map.indexOfFirst { ',' in it || '#' in it }
        val x = map[y].indexOfFirst { it == '.' }
        var curr = Pair(x, y)
        var direction = 0
        /**
         * Fully hardcoded solution, only works on this kind of flatten dice
         *  64
         *  2
         * 31
         * 5
         */
        val faces = mapOf(
            6 to Pair(Pair(50, 0), Pair(99, 49)),
            4 to Pair(Pair(100, 0), Pair(149, 49)),
            2 to Pair(Pair(50, 50), Pair(99, 99)),
            1 to Pair(Pair(50, 100), Pair(99, 149)),
            3 to Pair(Pair(0, 100), Pair(49, 149)),
            5 to Pair(Pair(0, 150), Pair(49, 199)),
        )
        for (ins in instructions) {
            if ("\\d+".toRegex().matches(ins)) {
                var i = 1
                var dir = DIRECTIONS[direction]
                while (i <= ins.toInt()) {
                    val new = Pair(
                        (curr.first + dir.first).mod(map.first().size),
                        (curr.second + dir.second).mod(map.size)
                    )
                    if (map[new.second][new.first] == '.') {
                        curr = new
                    } else if (map[new.second][new.first] == '#') {
                        break
                    } else {
                        val currFaces = faces.filterValues { (tli, bri) ->
                            curr.first in (tli.first..bri.first) && curr.second in (tli.second..bri.second)
                        }.keys
                        assert(currFaces.size == 1)
                        val currFace = currFaces.first()
                        val (dest, newDir) = if (currFace == 1) {
                            val bounds = faces[1]!!
                            if (dir == Pair(0, 1)) {
                                val nextBounds = faces[5]!!
                                val diff = curr.first - bounds.first.first
                                Pair(
                                    Pair(nextBounds.second.first, nextBounds.first.second + diff),
                                    Pair(-1, 0)
                                )
                            } else {
                                val nextBounds = faces[4]!!
                                val diff = curr.second - bounds.first.second
                                Pair(
                                    Pair(nextBounds.second.first, nextBounds.second.second - diff),
                                    Pair(-1, 0)
                                )
                            }
                        } else if (currFace == 2) {
                            val bounds = faces[2]!!
                            if (dir == Pair(-1, 0)) {
                                val nextBounds = faces[3]!!
                                val diff = curr.second - bounds.first.second
                                Pair(
                                    Pair(nextBounds.first.first + diff, nextBounds.first.second),
                                    Pair(0, 1)
                                )
                            } else {
                                val nextBounds = faces[4]!!
                                val diff = curr.second - bounds.first.second
                                Pair(
                                    Pair(nextBounds.first.first + diff, nextBounds.second.second),
                                    Pair(0, -1)
                                )
                            }
                        } else if (currFace == 3) {
                            val bounds = faces[3]!!
                            if (dir == Pair(-1, 0)) {
                                val nextBounds = faces[6]!!
                                val diff = curr.second - bounds.first.second
                                Pair(
                                    Pair(nextBounds.first.first, nextBounds.second.second - diff),
                                    Pair(1, 0)
                                )
                            } else {
                                val nextBounds = faces[2]!!
                                val diff = curr.first - bounds.first.first
                                Pair(
                                    Pair(nextBounds.first.first, nextBounds.first.second + diff),
                                    Pair(1, 0)
                                )
                            }
                        } else if (currFace == 4) {
                            val bounds = faces[4]!!
                            when (dir) {
                                Pair(0, 1) -> {
                                    val nextBounds = faces[2]!!
                                    val diff = curr.first - bounds.first.first
                                    Pair(
                                        Pair(nextBounds.second.first, nextBounds.first.second + diff),
                                        Pair(-1, 0)
                                    )
                                }
                                Pair(1, 0) -> {
                                    val nextBounds = faces[1]!!
                                    val diff = bounds.second.second - curr.second
                                    Pair(
                                        Pair(nextBounds.second.first, nextBounds.first.second + diff),
                                        Pair(-1, 0)
                                    )
                                }
                                else -> {
                                    val nextBounds = faces[5]!!
                                    val diff = curr.first - bounds.first.first
                                    Pair(
                                        Pair(nextBounds.first.first + diff, nextBounds.second.second),
                                        Pair(0, -1)
                                    )
                                }
                            }
                        } else if (currFace == 5) {
                            val bounds = faces[5]!!
                            when (dir) {
                                Pair(1, 0) -> {
                                    val nextBounds = faces[1]!!
                                    val diff = curr.second - bounds.first.second
                                    Pair(
                                        Pair(nextBounds.first.first + diff, nextBounds.second.second),
                                        Pair(0, -1)
                                    )
                                }
                                Pair(0, 1) -> {
                                    val nextBounds = faces[4]!!
                                    val diff = curr.first - bounds.first.first
                                    Pair(
                                        Pair(nextBounds.first.first + diff, nextBounds.first.second),
                                        Pair(0, 1)
                                    )
                                }
                                else -> {
                                    val nextBounds = faces[6]!!
                                    val diff = curr.second - bounds.first.second
                                    Pair(
                                        Pair(nextBounds.first.first + diff, nextBounds.first.second),
                                        Pair(0, 1)
                                    )
                                }
                            }
                        } else {
                            val bounds = faces[6]!!
                            if (dir == Pair(-1, 0)) {
                                val nextBounds = faces[3]!!
                                val diff = bounds.second.second - curr.second
                                Pair(
                                    Pair(nextBounds.first.first, nextBounds.first.second + diff),
                                    Pair(1, 0)
                                )
                            } else {
                                val nextBounds = faces[5]!!
                                val diff = curr.first - bounds.first.first
                                Pair(
                                    Pair(nextBounds.first.first, nextBounds.first.second + diff),
                                    Pair(1, 0)
                                )
                            }
                        }
                        if (map[dest.second][dest.first] == '.') {
                            curr = dest
                            dir = newDir
                            direction = DIRECTIONS.indexOf(newDir)
                        } else if (map[dest.second][dest.first] == '#') {
                            break
                        } else {
                            error("Should not happen")
                        }
                    }
                    i++
                }
            } else {
                direction = when (ins) {
                    "R" -> (direction + 1).mod(DIRECTIONS.size)
                    "L" -> (direction - 1).mod(DIRECTIONS.size)
                    else -> error("Should not be possible to have : $ins")
                }
            }
        }
        return (curr.second + 1) * 1000 + (curr.first + 1) * 4 + direction
    }

    override fun getInput(): Pair<List<List<Char>>, List<String>> =
        AOCUtils.getDayInput(2022, 22).let { lines ->
            val map = mutableListOf<MutableList<Char>>()
            for (line in lines) {
                if (line.trim() == "") {
                    break
                }
                map.add(line.toMutableList())
            }
            val maxSize = map.maxOf { it.size }
            map.map {
                if (it.size < maxSize) {
                    it.addAll(" ".repeat(maxSize - it.size).toList())
                }
            }
            Pair(
                map,
                "(\\d+)([RL])?".toRegex().findAll(lines.last())
                    .flatMap { m -> m.groupValues.drop(1) }
                    .toList().dropLast(1)
            )
        }

    companion object {
        val DIRECTIONS = listOf(
            Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1)
        )
    }
}

fun main() {
    val day = Day22()
    println(day.runPartOne())
    println(day.runPartTwo())
}
