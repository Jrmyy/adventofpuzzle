package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day19 : Day<List<Day19.Blueprint>, Int> {

    data class Robot(val costs: Map<String, Int>)

    data class Blueprint(
        val id: Int,
        val possibleRobots: Map<String, Robot>
    )

    data class State(
        val robots: MutableMap<String, Int>,
        val resources: MutableMap<String, Int>,
        val time: Int,
    )

    override fun runPartOne(): Int =
        getInput().sumOf {
            it.id * findMaxGeodes(it)
        }

    override fun runPartTwo(): Int = getInput().take(3).fold(1) { acc, b ->
        acc * findMaxGeodes(b, 32)
    }

    override fun getInput(): List<Blueprint> = AOCUtils.getDayInput(2022, 19).map {
        val (blueprint, robots) = it.split(":")
        Blueprint(
            blueprint.replace("Blueprint ", "").toInt(),
            robots.split(" Each ").drop(1).associate { robot ->
                robot.split(" ").first() to
                    Robot(
                        robot.split(" costs ").last().split(" and ").associate { p ->
                            val (cost, type) = p.split(" ")
                            Pair(type.replace(".", ""), cost.toInt())
                        }
                    )
            }
        )
    }

    private fun findMaxGeodes(
        blueprint: Blueprint,
        simTime: Int = 24
    ): Int {
        val stack = ArrayDeque(
            listOf(State(mutableMapOf("ore" to 1), mutableMapOf(), simTime))
        )
        val seen = mutableSetOf<State>()
        var max = 0
        main@ while (stack.isNotEmpty()) {
            val state = stack.removeFirst()
            if (state.time == 0) {
                val geodes = state.resources.getOrDefault("geode", 0)
                if (max < geodes) {
                    max = geodes
                }
                continue
            }

            val maxes = blueprint.possibleRobots.filter { it.key != "geode" }.mapValues {
                blueprint.possibleRobots.maxOf { r -> r.value.costs.getOrDefault(it.key, 0) }
            }

            // If we have a state in which we have more robots than the max amount we can spend at each minute, this
            // state is equivalent to the number of robots for this resources set to the max
            for (it in maxes) {
                if (state.robots.getOrDefault(it.key, 0) > it.value) {
                    state.robots[it.key] = it.value
                }
            }

            // If we have a state which will have too much of resources, meaning that the current amount of any
            // resource is bigger than what we can consume at most for the remaining of times minus what will be
            // actually produced, we need to change this states as it is equivalent of the remaining. It will just
            // drop the excess you have
            for (it in maxes) {
                val remainingIfConsumedAtEachTurn = (
                    state.time * maxes.getOrDefault(it.key, 0) -
                        state.robots.getOrDefault(it.key, 0) * (state.time - 1)
                    )
                if (state.resources.getOrDefault(it.key, 0) >= remainingIfConsumedAtEachTurn) {
                    state.resources[it.key] = remainingIfConsumedAtEachTurn
                }
            }

            if (state in seen) {
                continue
            }

            seen.add(state)

            blueprint.possibleRobots.filterValues { robot ->
                robot.costs.all { c -> state.resources.getOrDefault(c.key, 0) >= c.value }
            }.forEach { (key, robot) ->
                val newRobots = state.robots.toMutableMap()
                val newResources = state.resources.toMutableMap()
                robot.costs.forEach {
                    newResources[it.key] = newResources[it.key]!! - it.value
                }
                state.robots.forEach {
                    newResources[it.key] = newResources.getOrDefault(it.key, 0) + it.value
                }
                newRobots[key] = newRobots.getOrDefault(key, 0) + 1
                stack.addFirst(
                    State(
                        time = state.time - 1,
                        robots = newRobots,
                        resources = newResources
                    )
                )
            }

            val newResources = state.resources.toMutableMap()
            state.robots.forEach {
                newResources[it.key] = newResources.getOrDefault(it.key, 0) + it.value
            }
            stack.addFirst(
                State(
                    robots = state.robots.toMutableMap(),
                    resources = newResources,
                    time = state.time - 1
                )
            )
        }
        return max
    }
}

fun main() {
    val day = Day19()
    println(day.runPartOne())
    println(day.runPartTwo())
}
