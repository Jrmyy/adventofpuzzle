package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.math.BigInteger
import kotlin.math.*

class Day10 : Day<List<Triple<Int, Int, Char>>, Int> {

    override fun runPartOne(): Int {
        val space = getInput()
        val asteroids = space.filter { it.third == '#' }
        return asteroids.associate {
            Pair(Pair(it.first, it.second), getVisible(it, space).size)
        }.maxByOrNull { it.value }?.value ?: throw RuntimeException(":(")
    }

    override fun runPartTwo(): Int {
        val space = getInput()
        val asteroids = space.filter { it.third == '#' }
        val maxAst = asteroids.map {
            Pair(Pair(it.first, it.second), getVisible(it, space))
        }.maxByOrNull { it.second.size } ?: throw RuntimeException(":(")
        val (coord, seenable) = maxAst
        val seenableWithAngles = seenable.map {
            Pair(it, getAngle(coord, it))
        }.fold(mutableMapOf<Double, MutableList<Pair<Int, Int>>>(), { acc, pair ->
            val content = acc.getOrDefault(pair.second, mutableListOf())
            content.add(pair.first)
            acc[pair.second] = content
            acc
        }).mapValues {
            it.value.sortedBy { that ->
                sqrt((that.first - coord.first).toDouble().pow(2) + (that.second - coord.second).toDouble().pow(2))
            }
        }.toList().sortedBy { it.first }.map { Pair(it.first, it.second.toMutableList()) }
        var j = 0
        for (i in 0 until 199) {
            seenableWithAngles[j].second.removeAt(0)
            j = (j + 1) % seenableWithAngles.size
        }
        val res = seenableWithAngles[j].second.removeAt(0)
        return 100 * res.first + res.second
    }

    override fun getInput(): List<Triple<Int, Int, Char>> =
        AOCUtils.getDayInput(2019, 10)
            .map { it.toList() }
            .flatMapIndexed { idx, chars ->
                chars.mapIndexed { charIdx, it ->
                    Triple(charIdx, idx, it)
                }
            }

    private fun getAngle(from: Pair<Int, Int>, coord: Pair<Int, Int>): Double {
        val res = atan2((coord.first - from.first).toDouble(), (from.second - coord.second).toDouble()) * 180 / PI
        return if (res < 0) {
            res + 360
        } else {
            res
        }
    }

    private fun getVisible(from: Triple<Int, Int, Char>, space: List<Triple<Int, Int, Char>>): List<Pair<Int, Int>> {
        return space.filter {
            if (it != from && it.third == '#') {
                val xDiff = abs(it.first - from.first)
                val yDiff = abs(it.second - from.second)
                val minX = min(it.first, from.first)
                val maxX = max(it.first, from.first)
                val minY = min(it.second, from.second)
                val maxY = max(it.second, from.second)
                if (xDiff != 0 && yDiff != 0) {
                    val gcd = BigInteger.valueOf(max(xDiff, yDiff).toLong())
                        .gcd(BigInteger.valueOf(min(xDiff, yDiff).toLong())).toInt()
                    if (gcd == 1) {
                        true
                    } else {
                        (1 until gcd).map { that ->
                            space.first { coord ->
                                if (from.first > it.first) {
                                    if (from.second > it.second) {
                                        coord.first == from.first - that * xDiff / gcd &&
                                            coord.second == from.second - that * yDiff / gcd
                                    } else {
                                        coord.first == from.first - that * xDiff / gcd &&
                                            coord.second == from.second + that * yDiff / gcd
                                    }
                                } else {
                                    if (from.second > it.second) {
                                        coord.first == from.first + that * xDiff / gcd &&
                                            coord.second == from.second - that * yDiff / gcd
                                    } else {
                                        coord.first == from.first + that * xDiff / gcd &&
                                            coord.second == from.second + that * yDiff / gcd
                                    }
                                }
                            }
                        }.none { that -> that.third == '#' }
                    }
                } else if (xDiff == 0) {
                    (minY + 1 until maxY).map { that ->
                        space.first { coord ->
                            coord.first == it.first && coord.second == that
                        }
                    }.none { that -> that.third == '#' }
                } else {
                    (minX + 1 until maxX).map { that ->
                        space.first { coord ->
                            coord.first == that && coord.second == it.second
                        }
                    }.none { that -> that.third == '#' }
                }
            } else {
                false
            }
        }.map { Pair(it.first, it.second) }
    }
}

fun main() {
    val day = Day10()
    println(day.runPartOne())
    println(day.runPartTwo())
}
