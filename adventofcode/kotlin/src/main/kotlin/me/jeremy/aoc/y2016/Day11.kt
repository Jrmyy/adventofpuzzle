package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day11 : Day<Day11.State, Int> {

    enum class ElementType(val value: Int) {
        CHIP(1),
        GENERATOR(-1),
    }

    data class Element(
        val name: Char,
        val type: ElementType
    ) {
        fun hash(): Int {
            return name.code * type.value
        }
    }

    data class State(
        val elevatorLevel: Int,
        val floors: List<MutableSet<Element>>
    ) {
        fun hash(): String {
            return "elevation=$elevatorLevel;floors=${floors.joinToString("|") {
                it.let { s -> "(${s.size},${s.count { e -> e.hash() < 0 }})" }
            }}"
        }
    }

    override fun runPartOne(): Int = getMinSteps()

    override fun runPartTwo(): Int {
        val state = getInput()
        state.floors[0].addAll(
            listOf(
                Element('D', ElementType.CHIP),
                Element('D', ElementType.GENERATOR),
                Element('E', ElementType.CHIP),
                Element('E', ElementType.GENERATOR),
            )
        )
        return getMinSteps(state)
    }

    override fun getInput(): State =
        State(
            0,
            AOCUtils.getDayInput(2016, 11).map { l ->
                if (l.contains("nothing")) {
                    mutableSetOf()
                } else {
                    l.split(" contains ").last().removeSuffix(".").let { c ->
                        val split = if (c.contains(",")) {
                            c.split(", ")
                        } else if (c.contains("and")) {
                            c.split(" and ")
                        } else {
                            listOf(c)
                        }
                        split.map { s ->
                            s.replace("and ", "").replace("a ", "").let { ss ->
                                Element(
                                    ss.first().uppercaseChar(),
                                    if (ss.contains("microchip")) ElementType.CHIP else ElementType.GENERATOR
                                )
                            }
                        }.toMutableSet()
                    }
                }
            }
        )

    private fun getMinSteps(initialState: State = getInput()): Int {
        /**
         * States are unique per hash, the hash is made of
         * - the elevator level
         * - a hashesentation of each floor : a floor is hashesented by the number of elements
         *   in it and the number of generators in it
         *
         * With this hashesentation, it enables to filter all "equivalent" states, i.e. states
         * with the same number of elements in all floors and with the same number fo generators
         * per floor, i.e. permutations, or states obtained by moving pairs.
         *
         * We can move 1 element if :
         *  - it is a microchip and
         *      - there no generator or
         *      - all generators are linked to their microchip
         *      - a generator exists on the next floor for this
         * - it is a generator and
         *      - there no microchip or
         *      - all microchips are linked to their generator
         *      - a microchip exists on the next floor for this
         *
         * We can move 2 elements if :
         *  - it is a couple microchip & generator (first char equal)
         *  - it is 2 microchips and
         *      - there is no generator or
         *      - all generators have their chip or
         *      - both generators are present
         * - it is 2 generators and
         *      - there is no chip or
         *      - all chips have their generator or
         *      - both chips are present
         *
         * To optimize we :
         *  - we don't move one element to top if we can move 2
         *  - we don't move two elements to bottom if we can move 1
         */
        val states = ArrayDeque(listOf(Pair(0, initialState)))
        val seen = mutableMapOf<String, Int>()
        var min = Int.MAX_VALUE
        while (states.isNotEmpty()) {
            val (steps, currentState) = states.removeFirst()
            if (
                currentState.floors.last().isNotEmpty() &&
                currentState.floors.take(3).all { it.isEmpty() } &&
                steps < min
            ) {
                min = steps
            }
            if (steps >= min) {
                continue
            }
            val possibilities = listOf(currentState.elevatorLevel - 1, currentState.elevatorLevel + 1)
                .filter { it in currentState.floors.indices }
            for (newLevel in possibilities) {
                val newFloor = currentState.floors[newLevel]
                val currentFloor = currentState.floors[currentState.elevatorLevel]
                val alternatives = mutableSetOf<Set<Element>>()
                for (element in currentFloor) {
                    for (other in currentFloor) {
                        alternatives.add(setOf(element, other))
                    }
                }
                val groupedAlternatives = alternatives.groupBy { it.size }.toList().let {
                    if (newLevel > currentState.elevatorLevel)
                        it.sortedByDescending { p -> p.first }
                    else
                        it.sortedBy { p -> p.first }
                }.map { it.second }

                for (group in groupedAlternatives) {
                    var hasFoundForGroup = false
                    for (alternative in group) {
                        if (alternative.size == 2) {
                            val (element, other) = alternative.toList()
                            val isCouple = (element.name == other.name && element.type != other.type)
                            val isSameType = element.type == other.type && element.name != other.name
                            val canMoveBoth = if (isCouple) {
                                true
                            } else if (isSameType) {
                                val nextOthers = newFloor.filter { it.type != element.type }
                                nextOthers.isEmpty() ||
                                    nextOthers.all { o -> newFloor.any { it.name == o.name && it.type != o.type } } ||
                                    alternative.all { a -> nextOthers.any { it.name == a.name && it.type != a.type } }
                            } else {
                                false
                            }
                            if (canMoveBoth) {
                                val newFloors = currentState.floors.toList().map { it.toMutableList() }
                                newFloors[currentState.elevatorLevel].remove(element)
                                newFloors[currentState.elevatorLevel].remove(other)
                                newFloors[newLevel].add(element)
                                newFloors[newLevel].add(other)
                                val newState = currentState.copy(
                                    elevatorLevel = newLevel,
                                    floors = newFloors.map { it.toMutableSet() }
                                )
                                val hash = newState.hash()
                                if (
                                    seen.getOrDefault(hash, Int.MAX_VALUE) > steps + 1 &&
                                    seen.getOrDefault(hash, 0) <= min
                                ) {
                                    states.removeIf {
                                        it.first >= steps + 1 && it.second.hash() == hash
                                    }
                                    states.addFirst(Pair(steps + 1, newState))
                                    seen[hash] = steps + 1
                                    hasFoundForGroup = true
                                }
                            }
                        } else {
                            val element = alternative.first()
                            val nextOthers = newFloor.filter { it.type != element.type }
                            if (
                                nextOthers.isEmpty() ||
                                nextOthers.all { o -> newFloor.any { it.name == o.name && it.type != o.type } } ||
                                nextOthers.any { it.name == element.name && it.type != element.type }
                            ) {
                                val newFloors = currentState.floors.toList().map { it.toMutableList() }
                                newFloors[currentState.elevatorLevel].remove(element)
                                newFloors[newLevel].add(element)
                                val newState = currentState.copy(
                                    elevatorLevel = newLevel,
                                    floors = newFloors.map { it.toMutableSet() }
                                )
                                val hash = newState.hash()
                                if (
                                    seen.getOrDefault(hash, Int.MAX_VALUE) > steps + 1 &&
                                    seen.getOrDefault(hash, 0) <= min
                                ) {
                                    states.removeIf {
                                        it.first >= steps + 1 && it.second.hash() == hash
                                    }
                                    states.addFirst(Pair(steps + 1, newState))
                                    seen[hash] = steps + 1
                                    hasFoundForGroup = true
                                }
                            }
                        }
                    }
                    if (hasFoundForGroup) {
                        break
                    }
                }
            }
        }
        return min
    }
}

fun main() {
    val day = Day11()
    println(day.runPartOne())
    println(day.runPartTwo())
}
