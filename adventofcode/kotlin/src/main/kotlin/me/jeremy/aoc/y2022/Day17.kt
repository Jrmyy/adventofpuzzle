package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day17 : Day<List<Char>, Long> {

    override fun runPartOne(): Long = tetris(2022)

    override fun runPartTwo(): Long = tetris(1_000_000_000_000)

    override fun getInput(): List<Char> = AOCUtils.getDayInput(2022, 17).first().toList()

    private fun tetris(steps: Long): Long {
        val jets = getInput()
        var jetIdx = 0
        var rockIdx = 0
        val cave = ".".repeat(7 * 4).chunked(7).map { it.toMutableList() }.toMutableList()
        cave.add("-".repeat(7).toMutableList())
        val states = mutableMapOf<Triple<Int, Int, List<List<Char>>>, Pair<Long, Int>>()
        var j = 0L
        var cycleFound = false
        var heightFromCycleRepeat = 0L
        while (j < steps) {
            if (cave.size >= 20) {
                val maxY = cave.indexOfFirst { it.contains('#') }
                // State is computed using current rock, current jet and the top n rows (20 here as a start)
                val state = Triple(rockIdx, jetIdx, cave.take(20).map { it.toList() }.toList())
                if (!cycleFound && states.contains(state)) {
                    cycleFound = true
                    // We get the first time we saw this state
                    val (appearedAt, heightAtThatPoint) = states[state] ?: error("It contains")
                    // We get the duration of the cycle
                    val duration = (j - appearedAt).toInt()
                    // We get how much the cycle increase size
                    val cycleHeightChange = cave.size - 1 - maxY - heightAtThatPoint
                    // We get the number of cycles we can put before reaching the steps
                    val cyclesCount = (steps - j) / duration
                    // We set the heightFromCycleRepeat as the height change from one cycle times the number of cycles
                    heightFromCycleRepeat = cycleHeightChange * cyclesCount
                    // We skip a lot of rock failing, the duration of a cycle times the number of cycles
                    j += cyclesCount * duration
                } else {
                    states[state] = Pair(j, cave.size - 1 - maxY)
                }
            }
            val rock = ROCKS[rockIdx]
            var x = 2
            val lastFreeLine = cave.indexOfLast { it.toSet().size == 1 && it.first() == '.' }
            var y = lastFreeLine - 3 - (rock.size - 1)
            if (y < 0) {
                for (yi in (0 until -y)) {
                    cave.add(0, ".".repeat(7).toMutableList())
                }
                y = 0
            }
            rockFall@ while (true) {
                val jet = jets[jetIdx]
                if (jet == '<' && x > 0) {
                    var canMove = true
                    rock.forEachIndexed { yi, r ->
                        r.forEachIndexed { xi, c ->
                            if (c == '#' && cave[y + yi][x + xi - 1] == '#') {
                                canMove = false
                            }
                        }
                    }
                    if (canMove) {
                        x--
                    }
                } else if (jet == '>' && (rock.first().size + x) < 7) {
                    var canMove = true
                    rock.forEachIndexed { yi, r ->
                        r.forEachIndexed { xi, c ->
                            if (c == '#' && cave[y + yi][x + xi + 1] == '#') {
                                canMove = false
                            }
                        }
                    }
                    if (canMove) {
                        x++
                    }
                }
                jetIdx = (jetIdx + 1) % jets.size
                if (y + rock.size == cave.size - 1) {
                    break
                }
                var canGoDown = true
                rock.forEachIndexed { yi, r ->
                    r.forEachIndexed { xi, c ->
                        if (c == '#' && cave[y + 1 + yi][x + xi] == '#') {
                            canGoDown = false
                        }
                    }
                }
                if (!canGoDown) {
                    y += rock.size - 1
                    break@rockFall
                }
                y++
            }
            rock.reversed().forEachIndexed { yi, row ->
                row.forEachIndexed { xi, c ->
                    if (cave[y - yi][x + xi] == '#') {
                        if (c == '#') {
                            error("Should not be a block")
                        }
                    } else {
                        cave[y - yi][x + xi] = c
                    }
                }
            }
            rockIdx = (rockIdx + 1) % ROCKS.size
            j++
        }
        val maxY = cave.indexOfFirst { it.contains('#') }
        // At the end we return the height from the cycle repeat + what we calculated before finding the cycle and
        // the remaining all the cycles to the number of required steps.
        return cave.size - 1 - maxY + heightFromCycleRepeat
    }

    companion object {
        val ROCKS = listOf(
            "####",
            """
                .#.
                ###
                .#.
            """.trimIndent(),
            """
                ..#
                ..#
                ###
            """.trimIndent(),
            """
                #
                #
                #
                #
            """.trimIndent(),
            """
                ##
                ##
            """.trimIndent(),
        ).map { it.split("\n").map { s -> s.toList() } }
    }
}

fun main() {
    val day = Day17()
    println(day.runPartOne())
    println(day.runPartTwo())
}
