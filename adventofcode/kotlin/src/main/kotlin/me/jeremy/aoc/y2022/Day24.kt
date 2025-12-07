package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.min

typealias Valley = MutableList<List<MutableList<Char>>>

class Day24 : Day<Valley, Int> {

    override fun runPartOne(): Int {
        val map = getInput()
        val x = map[0].indexOf(mutableListOf('.'))
        val destX = map[map.size - 1].indexOfFirst { it == listOf('.') }
        return computeMinimumMinutes(map, Pair(x, 0), Pair(destX, map.size - 1)).second
    }

    override fun runPartTwo(): Int {
        val map = getInput()
        val x = map[0].indexOf(mutableListOf('.'))
        val destX = map[map.size - 1].indexOfFirst { it == listOf('.') }
        val dest = Pair(destX, map.size - 1)
        val start = Pair(x, 0)
        val (firstMap, firstSteps) = computeMinimumMinutes(map, start, dest)
        val (secondMap, secondSteps) = computeMinimumMinutes(firstMap, dest, start)
        val (_, thirdSteps) = computeMinimumMinutes(secondMap, start, dest)
        return firstSteps + secondSteps + thirdSteps
    }

    override fun getInput(): Valley =
        AOCUtils.getDayInput(2022, 24).map { r ->
            r.toList().map { c -> if (c == '.' || c == '#') mutableListOf(c) else mutableListOf(c, '.') }
        }.toMutableList()

    private fun computeMinimumMinutes(
        map: Valley,
        start: Pair<Int, Int>,
        dest: Pair<Int, Int>,
    ): Pair<Valley, Int> {
        val minutesPerLocation = mutableMapOf(start to 0)
        while (!minutesPerLocation.contains(dest)) {
            val newMap = map.map { it.map { mutableListOf<Char>() } }
            map.forEachIndexed { y, r ->
                r.forEachIndexed { x, cs ->
                    cs.forEach { c ->
                        when (c) {
                            '#' -> newMap[y][x].add('#')
                            '.' -> newMap[y][x].add('.')
                            '>' -> {
                                val nx = x + 1
                                if (map[y][nx].contains('#')) {
                                    newMap[y][1].add(c)
                                } else {
                                    newMap[y][nx].add(c)
                                }
                            }
                            '<' -> {
                                val nx = x - 1
                                if (map[y][nx].contains('#')) {
                                    newMap[y][map.first().size - 2].add(c)
                                } else {
                                    newMap[y][nx].add(c)
                                }
                            }
                            '^' -> {
                                val ny = y - 1
                                if (map[ny][x].contains('#')) {
                                    newMap[map.size - 2][x].add(c)
                                } else {
                                    newMap[ny][x].add(c)
                                }
                            }
                            'v' -> {
                                val ny = y + 1
                                if (map[ny][x].contains('#')) {
                                    newMap[1][x].add(c)
                                } else {
                                    newMap[ny][x].add(c)
                                }
                            }
                        }
                    }
                }
            }
            map.clear()
            map.addAll(newMap)
            val newMinutesPerLocation = mutableMapOf<Pair<Int, Int>, Int>()
            // We store for each location the number of minutes minimum to reach this position. At each turn
            // We make the blizzard move, and then we calculate the new min for each location
            // When we reach our destination, it means we found the min steps
            minutesPerLocation.forEach { (pos, steps) ->
                val (x, y) = pos
                ACTIONS.forEach { (dx, dy) ->
                    val newE = Pair(x + dx, y + dy)
                    if (
                        (newE.second in map.indices && newE.first in map.first().indices) &&
                        '#' !in newMap[newE.second][newE.first] &&
                        newMap[newE.second][newE.first].none { c -> c in listOf('^', 'v', '>', '<') }
                    ) {
                        newMinutesPerLocation.merge(newE, steps + 1) { a, b -> min(a, b) }
                    }
                }
            }

            minutesPerLocation.clear()
            minutesPerLocation.putAll(newMinutesPerLocation)
        }

        return Pair(map, minutesPerLocation[dest]!!)
    }

    companion object {
        val ACTIONS = listOf(
            Pair(1, 0),
            Pair(0, 1),
            Pair(-1, 0),
            Pair(0, -1),
            Pair(0, 0),
        )
    }
}

fun main() {
    val day = Day24()
    println(day.runPartOne())
    println(day.runPartTwo())
}
