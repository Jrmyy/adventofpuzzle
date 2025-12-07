package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.ceil

class Day14 : Day<Pair<MutableMap<Pair<Char, Char>, Long>, Map<Pair<Char, Char>, Char>>, Long> {
    override fun runPartOne(): Long = runPolymer(10)

    override fun runPartTwo(): Long = runPolymer(40)

    override fun getInput(): Pair<MutableMap<Pair<Char, Char>, Long>, Map<Pair<Char, Char>, Char>> =
        AOCUtils.getDayInput(2021, 14).let { lines ->
            Pair(
                lines.first().toList().windowed(2, 1).fold(mutableMapOf()) { acc, l ->
                    val pair = Pair(l.first(), l.last())
                    acc[pair] = acc.getOrDefault(pair, 0) + 1
                    acc
                },
                lines.subList(2, lines.size).associate {
                    it.split(" -> ").let { p ->
                        p.first().toList().let { l -> Pair(l.first(), l.last()) } to p.last().first()
                    }
                }
            )
        }

    private fun runPolymer(steps: Int): Long {
        val (polymer, rules) = getInput()
        repeat(steps) {
            val newPolymer = polymer.flatMap { (pattern, count) ->
                listOf(
                    Pair(Pair(pattern.first, rules[pattern]!!), count),
                    Pair(Pair(rules[pattern]!!, pattern.second), count)
                )
            }.fold(mutableMapOf<Pair<Char, Char>, Long>()) { acc, pair ->
                acc[pair.first] = (acc[pair.first] ?: 0).plus(pair.second)
                acc
            }
            polymer.clear()
            polymer.putAll(newPolymer)
        }
        // We are doing a window of size 2 and step 1 so all counts are doubled, so we need to divide by at the end
        // And take the upper long value (because first and last value of polymer are not counted in double)
        return polymer.toList().fold(mutableMapOf<Char, Long>()) { acc, pair ->
            val (pattern, count) = pair
            acc[pattern.first] = (acc[pattern.first] ?: 0L).plus(count)
            acc[pattern.second] = (acc[pattern.second] ?: 0L).plus(count)
            acc
        }.let {
            val max = it.maxOf { e -> e.value }
            val min = it.minOf { e -> e.value }
            ceil(((max - min) / 2).toDouble()).toLong()
        }
    }
}

fun main() {
    val day = Day14()
    println(day.runPartOne())
    println(day.runPartTwo())
}
