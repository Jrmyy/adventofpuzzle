package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs

class Day6 : Day<List<Pair<Int, Int>>, Int> {
    override fun runPartOne(): Int {
        val coordinates = getInput()
        val minX = coordinates.minOf { it.first }
        val maxX = coordinates.maxOf { it.first }
        val minY = coordinates.minOf { it.second }
        val maxY = coordinates.maxOf { it.second }
        val scaledCoordinates = coordinates.map {
            Pair(it.first - minX + 1, it.second - minY + 1)
        }
        val map: MutableList<MutableList<Int>> = (minY - 1..maxY + 1).map {
            (minX - 1..maxX + 1).map {
                -1
            }.toMutableList()
        }.toMutableList()
        coordinates.forEachIndexed { idx, it ->
            map[it.second - minY + 1][it.first - minX + 1] = idx
        }
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                val closestCoordinates = getClosestCoordinates(x, y, scaledCoordinates)
                if (closestCoordinates.size == 1 && c == -1) {
                    map[y][x] = closestCoordinates[0]
                }
            }
        }
        val finiteAreas = coordinates.mapIndexedNotNull { index, _ ->
            if (
                map.first().none { it == index } &&
                map.last().none { it == index } &&
                map.map { it.first() }.none { it == index } &&
                map.map { it.last() }.none { it == index }
            ) {
                index
            } else {
                null
            }
        }
        return finiteAreas.maxOf {
            map.flatten().count { i -> i == it }
        }
    }

    override fun runPartTwo(): Int {
        val epicenterDistance = 10000
        val coordinates = getInput()
        val minX = coordinates.minOf { it.first }
        val maxX = coordinates.maxOf { it.first }
        val minY = coordinates.minOf { it.second }
        val maxY = coordinates.maxOf { it.second }
        val scaledCoordinates = coordinates.map {
            Pair(it.first - minX + 1, it.second - minY + 1)
        }
        // We assume that with the number of coordinates, we only need to give a space of 100 from any coordinates
        val map: MutableList<MutableList<Int>> = (
            minY - epicenterDistance / 10 - 1..maxY + epicenterDistance / 10 + 1
            ).map {
            (minX - epicenterDistance / 10 - 1..maxX + epicenterDistance / 10 + 1).map {
                -1
            }.toMutableList()
        }.toMutableList()
        coordinates.forEachIndexed { idx, it ->
            map[it.second - minY + 1][it.first - minX + 1] = idx
        }
        return map.flatMapIndexed { y, row ->
            row.mapIndexed { x, _ ->
                if (sumAllDistances(x, y, scaledCoordinates) < epicenterDistance) {
                    1
                } else {
                    0
                }
            }
        }.sum()
    }

    override fun getInput(): List<Pair<Int, Int>> =
        AOCUtils.getDayInput(2018, 6).map {
            val (x, y) = it.split(", ").map { i -> i.toInt() }
            Pair(x, y)
        }

    private fun sumAllDistances(x: Int, y: Int, coordinates: List<Pair<Int, Int>>): Int =
        coordinates.sumOf { abs(x - it.first) + abs(y - it.second) }

    private fun getClosestCoordinates(x: Int, y: Int, coordinates: List<Pair<Int, Int>>): List<Int> {
        val distances = coordinates.mapIndexed { idx, it ->
            Pair(idx, abs(x - it.first) + abs(y - it.second))
        }
        val minDistance = (distances.minByOrNull { it.second } ?: error("No min")).second
        return distances.filter { it.second == minDistance }.map { it.first }
    }
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
