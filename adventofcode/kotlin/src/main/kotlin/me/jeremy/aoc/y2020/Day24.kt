package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.ceil
import kotlin.math.floor

class Day24 : Day<List<String>, Int> {

    override fun runPartOne(): Int = createInitialTiles().count { it.value }

    override fun runPartTwo(): Int {
        val tiles = createInitialTiles()
        repeat(100) {
            val copy = hashMapOf(*(tiles.toList().toTypedArray()))
            val minX = floor(copy.minOf { it.key.first } - 1).toInt()
            val minY = floor(copy.minOf { it.key.second } - 1).toInt()
            val maxX = ceil(copy.maxOf { it.key.first } + 1).toInt()
            val maxY = ceil(copy.maxOf { it.key.second } + 1).toInt()
            (minY..maxY).forEach { y ->
                (minX..maxX).forEach { x ->
                    updateTile(tiles, copy, x.toDouble(), y.toDouble())
                    updateTile(tiles, copy, x + 0.5, y.toDouble())
                    updateTile(tiles, copy, x.toDouble(), y + 0.5)
                    updateTile(tiles, copy, x + 0.5, y + 0.5)
                }
            }
        }
        return tiles.count { it.value }
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2020, 24)

    private fun createInitialTiles(): HashMap<Pair<Double, Double>, Boolean> {
        val tiles = hashMapOf<Pair<Double, Double>, Boolean>()
        val instructions = getInput()
        instructions.forEach {
            var instructionInProgress = it
            var currentPosition = Pair((0).toDouble(), (0).toDouble())
            while (instructionInProgress.isNotEmpty()) {
                val toRemove = if (listOf("se", "ne", "nw", "sw").any { d -> instructionInProgress.startsWith(d) }) {
                    2
                } else {
                    1
                }
                val direction = instructionInProgress.subSequence(0, toRemove)
                instructionInProgress = instructionInProgress.drop(toRemove)
                currentPosition = when (direction) {
                    "e" -> Pair(currentPosition.first + 1, currentPosition.second)
                    "w" -> Pair(currentPosition.first - 1, currentPosition.second)
                    "sw" -> Pair(currentPosition.first - 0.5, currentPosition.second + 0.5)
                    "nw" -> Pair(currentPosition.first - 0.5, currentPosition.second - 0.5)
                    "ne" -> Pair(currentPosition.first + 0.5, currentPosition.second - 0.5)
                    "se" -> Pair(currentPosition.first + 0.5, currentPosition.second + 0.5)
                    else -> error("Wrong direction $direction")
                }
            }
            tiles[currentPosition] = !tiles.getOrDefault(currentPosition, false)
        }
        return tiles
    }

    private fun updateTile(
        tiles: HashMap<Pair<Double, Double>, Boolean>,
        copy: HashMap<Pair<Double, Double>, Boolean>,
        x: Double,
        y: Double
    ) {
        val neighbours = listOf(
            copy.getOrDefault(Pair((x - 1), y), false),
            copy.getOrDefault(Pair((x + 1), y), false),
            copy.getOrDefault(Pair(x + 0.5, y + 0.5), false),
            copy.getOrDefault(Pair(x - 0.5, y - 0.5), false),
            copy.getOrDefault(Pair(x + 0.5, y - 0.5), false),
            copy.getOrDefault(Pair(x - 0.5, y + 0.5), false)
        )
        val current = copy.getOrDefault(Pair(x, y), false)
        val blackCount = neighbours.count { it }
        if (current && (blackCount == 0 || blackCount > 2)) {
            tiles[Pair(x, y)] = false
        } else if (!current && blackCount == 2) {
            tiles[Pair(x, y)] = true
        }
    }
}

fun main() {
    val day = Day24()
    println(day.runPartOne())
    println(day.runPartTwo())
}
