package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max

class Day16 : Day<Map<String, Day16.Valve>, Int> {

    data class Valve(
        val name: String,
        val flowRate: Int,
        var links: List<Valve> = listOf(),
        var paths: Map<String, Int> = mapOf(),
        var openedAt: Int = -1,
    ) {

        override fun equals(other: Any?): Boolean {
            if (other !is Valve) return false
            return name == other.name
        }

        override fun hashCode(): Int = name.hashCode()
    }

    override fun runPartOne(): Int {
        var maxFlow = 0
        val states = mutableListOf(Triple("AA", 30, getInput()))
        while (states.isNotEmpty()) {
            val (position, time, state) = states.removeFirst()
            maxFlow = max(
                maxFlow,
                state.values.filter { it.openedAt > 0 }.sumOf { it.flowRate * (it.openedAt - 1) }
            )

            if (time <= 0) continue

            val v = state[position] ?: error("Should be found")

            if (v.openedAt == -1 && v.flowRate > 0) {
                val newState = copyState(state)
                newState[position]!!.openedAt = time
                states.add(Triple(position, time - 1, newState))
            } else {
                states.addAll(
                    v.paths.filter { (k, d) ->
                        state[k]!!.openedAt == -1 && state[k]!!.flowRate > 0 && time - d > 0
                    }.map { (next, dist) -> Triple(next, time - dist, copyState(state)) }
                )
            }
        }
        return maxFlow
    }

    override fun runPartTwo(): Int {
        val bestStates = mutableMapOf<Set<String>, Int>()
        val allStates = mutableMapOf<Set<String>, Map<String, Valve>>()
        val states = mutableListOf(Triple("AA", 26, getInput()))

        // First we compute all the "best scenarios", i.e., for a set of opened valves, how much, at max, we can
        // have as pressure
        while (states.isNotEmpty()) {
            val (position, time, state) = states.removeFirst()
            if (time <= 0) continue
            val v = state[position] ?: error("Should be found")
            if (v.openedAt == -1 && v.flowRate > 0) {
                val newState = copyState(state)
                newState[position]!!.openedAt = time
                states.add(Triple(position, time - 1, newState))
                val opened = newState.values.filter { it.openedAt >= 0 }
                val openedSet = opened.map { it.name }.toSet()
                val maxStatePressure = opened.sumOf { it.flowRate * (it.openedAt - 1) }
                if (bestStates.getOrDefault(openedSet, 0) < maxStatePressure) {
                    bestStates[openedSet] = maxStatePressure
                    allStates[openedSet] = newState
                }
            } else {
                states.addAll(
                    v.paths.filter { (k, d) ->
                        state[k]!!.openedAt == -1 && state[k]!!.flowRate > 0 && time - d > 0
                    }.map { (next, dist) -> Triple(next, time - dist, copyState(state)) }
                )
            }
        }

        var maxFlow = 0
        // The human and the elephant evolve independently, so we need to cross join all the states, filter the one
        // that are not ok (i.e. valves touched by both human and elephant) and get the max
        for (human in allStates.values) {
            for (elephant in allStates.values) {
                val isGood = human.filterValues { it.openedAt >= 0 }.keys.toSet()
                    .intersect(elephant.filterValues { it.openedAt >= 0 }.keys.toSet())
                    .isEmpty()
                if (isGood) {
                    maxFlow = max(
                        maxFlow,
                        human.values.filter { it.openedAt >= 0 }.sumOf { it.flowRate * (it.openedAt - 1) } +
                            elephant.values.filter { it.openedAt >= 0 }.sumOf { it.flowRate * (it.openedAt - 1) }
                    )
                }
            }
        }
        return maxFlow
    }

    override fun getInput(): Map<String, Valve> =
        AOCUtils.getDayInput(2022, 16).associate {
            val matches = "Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? ([\\w, ]+)".toRegex().find(
                it
            )!!.groupValues
            matches[1] to Pair(Valve(matches[1], matches[2].toInt()), matches[3].split(", "))
        }.let { valves ->
            valves.mapValues { (k, v) ->
                val (valve, tunnels) = v
                valve.links = tunnels.map { t -> valves[t]!!.first }
                valve
            }.mapValues { (k, valve) ->
                valve.paths = valves.filter { it.key != k }.map { e -> e.key to distance(valve, e.value.first) }.toMap()
                valve
            }
        }

    private fun distance(start: Valve, to: Valve): Int {
        val seen = mutableSetOf(start)
        val toVisit = ArrayDeque<Pair<Valve, Int>>()
        toVisit.addAll(start.links.map { Pair(it, 1) })
        seen.addAll(start.links)
        while (toVisit.isNotEmpty()) {
            val (valve, steps) = toVisit.removeFirst()
            if (valve == to) {
                return steps
            }
            for (link in valve.links) {
                if (link !in seen) {
                    seen.add(link)
                    toVisit.add(Pair(link, steps + 1))
                }
            }
        }
        error("Should have found distance")
    }

    private fun copyState(state: Map<String, Valve>): Map<String, Valve> =
        state.mapValues { e -> e.value.copy() }.toMap()
}

fun main() {
    val day = Day16()
    println(day.runPartOne())
    println(day.runPartTwo())
}
