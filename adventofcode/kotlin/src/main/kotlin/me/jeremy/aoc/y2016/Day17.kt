package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day17 : Day<String, String> {

    override fun runPartOne(): String = findPath()!!

    override fun runPartTwo(): String = findPath(strategy = "max")!!

    override fun getInput(): String = AOCUtils.getDayInput(2016, 17).first()

    private fun findPath(
        position: Pair<Int, Int> = Pair(0, 0),
        path: String = "",
        workingPaths: MutableSet<String> = mutableSetOf(),
        strategy: String = "min"
    ): String? {
        val hash = getInput()
        val positions = AOCUtils.md5("$hash$path").take(4)
            .mapIndexedNotNull { idx, c ->
                if (c !in listOf('b', 'c', 'd', 'e', 'f')) {
                    null
                } else {
                    val op = OPERATIONS[idx]
                    val adjustedPosition = Pair(position.first + op.first, position.second + op.second)
                    if (adjustedPosition.toList().all { i -> i in 0..3 }) {
                        Pair(adjustedPosition, "${path}${DIRECTIONS[idx]}")
                    } else {
                        null
                    }
                }
            }
            .filter {
                (
                    it.second !in workingPaths &&
                        if (strategy == "min") {
                            it.second.length < (workingPaths.minOfOrNull { p -> p.length } ?: Int.MAX_VALUE)
                        } else {
                            true
                        }
                    )
            }
        val paths = mutableListOf<String>()
        for ((nextPos, newPath) in positions) {
            if (nextPos == Pair(3, 3)) {
                paths.add(newPath)
                workingPaths.add(newPath)
            } else {
                val short = findPath(nextPos, newPath, workingPaths, strategy)
                if (short != null) {
                    paths.add(short)
                }
            }
        }
        return if (strategy == "min") paths.minByOrNull { it.length } else paths.maxByOrNull { it.length }
    }

    companion object {
        val DIRECTIONS = listOf('U', 'D', 'L', 'R')
        val OPERATIONS = listOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0))
    }
}

fun main() {
    val day = Day17()
    println(day.runPartOne())
    println(day.runPartTwo().length)
}
