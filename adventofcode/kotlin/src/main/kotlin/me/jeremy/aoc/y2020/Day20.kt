package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.sqrt

class Day20 : Day<List<Day20.Tile>, Long> {

    data class Tile(val idx: Long, val pixels: List<List<Char>>) {

        fun getWaterRoughness(): Long {
            val seaMonsterPositions = (0 until pixels.size - PATTERN.size).flatMap { r ->
                (0 until pixels.size - PATTERN[0].size).map { c ->
                    val sub = pixels.subList(r, r + PATTERN.size).map { row ->
                        row.subList(c, c + PATTERN[0].size)
                    }
                    val matchingChars = sub.flatten().filterIndexed { idx, char ->
                        if (FLATTEN_PATTERN[idx] == ' ') {
                            true
                        } else {
                            char == '#'
                        }
                    }
                    if (matchingChars.size == sub.flatten().size) {
                        Pair(r, c)
                    } else {
                        null
                    }
                }
            }.filterNotNull()
            return if (seaMonsterPositions.isEmpty()) {
                0L
            } else {
                pixels.flatMapIndexed { row, list ->
                    list.mapIndexed { col, c ->
                        val withinSeaMonster = seaMonsterPositions.firstOrNull {
                            row >= it.first && row <= it.first + PATTERN.size - 1 &&
                                col >= it.second && col <= it.second + PATTERN[0].size - 1
                        }
                        val isSeaMonster = if (withinSeaMonster != null) {
                            PATTERN[row - withinSeaMonster.first][col - withinSeaMonster.second] == '#'
                        } else {
                            false
                        }
                        if (c == '#' && !isSeaMonster) {
                            1
                        } else {
                            0
                        }
                    }
                }.sum().toLong()
            }
        }

        fun computeAllTransformations(): List<Tile> =
            listOf(
                this,
                flip('V'),
                flip('H'),
                rotate(90),
                rotate(90).flip('V'),
                rotate(90).flip('H'),
                rotate(180),
                rotate(180).flip('V'),
                rotate(180).flip('H'),
                rotate(270),
                rotate(270).flip('V'),
                rotate(270).flip('H'),
            )

        fun findAdjacentTiles(others: List<Tile>): List<Pair<Char, Tile>> =
            others.flatMap {
                it.computeAllTransformations().toList()
            }.mapNotNull {
                val commonBorder = getCommonBorder(it)
                if (commonBorder == null) {
                    null
                } else {
                    Pair(commonBorder, it)
                }
            }

        // Rotate by deg and return the corresponding tile
        private fun rotate(deg: Int): Tile =
            when (deg) {
                90 -> {
                    Tile(
                        idx,
                        pixels.indices.map {
                            pixels.map { l -> l[it] }.reversed().toList()
                        }.toList()
                    )
                }
                180 -> {
                    Tile(
                        idx,
                        pixels.toList().reversed().map {
                            it.toList().reversed()
                        }.toList()
                    )
                }
                270 -> {
                    Tile(
                        idx,
                        pixels[0].indices.map {
                            pixels.map { l -> l[pixels[0].size - 1 - it] }.toList()
                        }.toList()
                    )
                }
                else -> error("Wrong deg")
            }

        // Flip horizontally or vertically and return the corresponding tile
        private fun flip(direction: Char): Tile =
            when (direction) {
                'V' -> Tile(idx, pixels.map { it.reversed().toList() }.toList())
                'H' -> Tile(idx, pixels.reversed().map { it.toList() }.toList())
                else -> error("Wrong direction")
            }

        private fun getCommonBorder(other: Tile): Char? =
            when {
                pixels.last() == other.pixels.first() -> 'D'
                pixels.first() == other.pixels.last() -> 'U'
                pixels.map { it.first() } == other.pixels.map { it.last() } -> 'L'
                pixels.map { it.last() } == other.pixels.map { it.first() } -> 'R'
                else -> null
            }
    }

    override fun runPartOne(): Long {
        val tiles = getInput()
        val allPossibilities = tiles.map {
            Pair(
                it,
                it.computeAllTransformations().map { t ->
                    t.findAdjacentTiles(tiles.toList().filter { o -> o.idx != t.idx })
                }.map { l -> l.distinctBy { p -> p.first } }
            )
        }

        val corners = allPossibilities
            .filter { it.second.maxOf { l -> l.size } == 2 }

        if (corners.size != 4) {
            error("We need 4 corners")
        }
        return corners.map { it.first.idx }.reduce { acc, i -> acc * i }
    }

    override fun runPartTwo(): Long {
        val tiles = getInput()
        val allPossibilities = tiles.flatMap {
            it.computeAllTransformations().map { t ->
                Pair(t, t.findAdjacentTiles(tiles.toList().filter { o -> o.idx != t.idx }))
            }
        }.toMap()
        val image = mutableListOf<List<Tile>>()
        var lastTile: Tile? = null
        val dimension = sqrt(tiles.size.toDouble()).toInt()
        (0 until dimension).map { _ ->
            val row = mutableListOf<Tile>()
            (0 until dimension).map { c ->
                val lastRow = image.lastOrNull()
                if (lastRow == null) {
                    if (lastTile != null) {
                        val tileAndPossibilities =
                            allPossibilities[lastTile!!] ?: error("Missing possibilities for last tile")
                        lastTile = tileAndPossibilities.first {
                            it.first == 'R' && it.second.idx !in image.flatten().map { t -> t.idx }
                        }.second
                        row.add(lastTile!!)
                    } else {
                        // top left corner
                        val tile = allPossibilities
                            .filterValues { it.size == 2 && it.map { p -> p.first } == listOf('D', 'R') }
                            .toList().first()
                        lastTile = tile.first
                        row.add(tile.first)
                    }
                } else {
                    val lastCol = row.lastOrNull()
                    if (lastCol == null) {
                        val tileAndPossibilities =
                            allPossibilities[lastRow.first()] ?: error("Missing possibilities for last row first")
                        lastTile = tileAndPossibilities.first {
                            it.first == 'D' && it.second.idx !in image.flatten().map { t -> t.idx }
                        }.second
                        row.add(lastTile!!)
                    } else {
                        val leftPossibilities =
                            (allPossibilities[lastCol] ?: error("Missing possibilities for last col"))
                                .filter { it.first == 'R' && it.second.idx !in image.flatten().map { t -> t.idx } }
                                .map { it.second }
                        val topPossibilities = (
                            allPossibilities[lastRow[c]] ?: error("Missing possibilities for last row")
                            ).filter { it.first == 'D' && it.second.idx !in image.flatten().map { t -> t.idx } }
                            .map { it.second }
                        val matchingBoth = leftPossibilities.toSet().intersect(topPossibilities.toSet())
                        if (matchingBoth.size != 1) {
                            error("Should be only one match")
                        }
                        lastTile = matchingBoth.first()
                        row.add(lastTile!!)
                    }
                }
            }
            image.add(row)
        }

        val pixels = image
            .map { row ->
                row.map { tile ->
                    Tile(
                        tile.idx,
                        tile.pixels
                            .drop(1)
                            .dropLast(1)
                            .map { it.drop(1).dropLast(1) }
                    )
                }
            }
            .map { row ->
                row[0].pixels.indices.map { idx ->
                    row.map { tile -> tile.pixels[idx] }.flatten()
                }
            }
            .flatten()

        val tile = Tile(0, pixels)
        return tile.computeAllTransformations().map {
            it.getWaterRoughness()
        }.maxOrNull() ?: error("No image found with sea monsters")
    }

    override fun getInput(): List<Tile> {
        val lines = AOCUtils.getDayInput(2020, 20)
        val tiles = mutableListOf<Tile>()
        var currentTile = 0L
        val pixels = mutableListOf<List<Char>>()
        lines.forEach {
            when {
                it == "" -> {
                    tiles.add(Tile(currentTile, pixels.toList()))
                    pixels.clear()
                }
                it.startsWith("Tile") -> {
                    currentTile = Regex("Tile (\\d+):")
                        .find(it)!!.groups[1]!!.value
                        .toLong()
                }
                else -> {
                    pixels.add(it.toList())
                }
            }
        }
        tiles.add(Tile(currentTile, pixels))
        return tiles
    }

    companion object {
        val PATTERN = listOf(
            "                  # ".toList(),
            "#    ##    ##    ###".toList(),
            " #  #  #  #  #  #   ".toList()
        )
        val FLATTEN_PATTERN = PATTERN.flatten()
    }
}

fun main() {
    val day = Day20()
    println(day.runPartOne())
    println(day.runPartTwo())
}
