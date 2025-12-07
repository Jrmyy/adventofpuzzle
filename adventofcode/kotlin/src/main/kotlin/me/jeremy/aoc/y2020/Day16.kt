package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day16 : Day<Day16.TicketsSystem, Long> {

    data class TicketsSystem(
        val rules: List<TicketRule>,
        val userTicket: List<Int>,
        val otherTickets: List<List<Int>>
    )

    data class TicketRule(
        val field: String,
        val ranges: List<Pair<Int, Int>>
    )

    override fun runPartOne(): Long {
        val system = getInput()
        val ranges = system.rules.flatMap { it.ranges }
        return system.otherTickets.flatMap {
            it.filter { that ->
                ranges.all { r -> that < r.first || that > r.second }
            }
        }.sum().toLong()
    }

    override fun runPartTwo(): Long {
        val system = getInput()
        val ranges = system.rules.flatMap { it.ranges }
        val validTickets = system.otherTickets.filter {
            it.none { that ->
                ranges.all { r -> that < r.first || that > r.second }
            }
        }
        val ticketSize = system.userTicket.size
        val validTicketsSeparatedByIndex = (0 until ticketSize).map { idx ->
            validTickets.map { it[idx] }
        }
        val rulesPossibleIdx = system.rules.map { rule ->
            Pair(
                rule.field,
                validTicketsSeparatedByIndex.mapIndexedNotNull { idx, it ->
                    val res = it.all { i -> rule.ranges.any { r -> i >= r.first && i <= r.second } }
                    if (res) {
                        idx
                    } else {
                        null
                    }
                }
            )
        }.sortedBy { it.second.size }
        val foundIdx = mutableMapOf<String, Int>()
        // This works because when watching the data, one field had 1 alternative, an other one had 2 alternatives, etc...
        rulesPossibleIdx.forEach {
            foundIdx[it.first] = it.second.first { idx -> !foundIdx.values.contains(idx) }
        }
        return foundIdx
            .filterKeys { it.startsWith("departure") }
            .values
            .map { system.userTicket[it].toLong() }
            .reduce { acc, i -> acc * i }
    }

    override fun getInput(): TicketsSystem {
        val lines = AOCUtils.getDayInput(2020, 16)
        val emptyLines = lines.mapIndexedNotNull { index, s -> if (s == "") index else null }
        val ruleRegex = Regex("([a-z ]+): (\\d+-\\d+) or (\\d+-\\d+)")
        val rules = lines.subList(0, emptyLines[0]).map {
            val groups = ruleRegex.find(it)!!.groups
            TicketRule(
                groups[1]!!.value,
                listOf(groups[2]!!.value, groups[3]!!.value).map { that ->
                    val split = that.split("-")
                    Pair(split[0].toInt(), split[1].toInt())
                }
            )
        }
        val userTicket = lines[emptyLines[1] - 1].split(",").map { it.toInt() }
        val otherTickets = lines.subList(emptyLines[1] + 2, lines.size).map {
            it.split(",").map { that -> that.toInt() }
        }
        return TicketsSystem(rules, userTicket, otherTickets)
    }
}

fun main() {
    val day = Day16()
    println(day.runPartOne())
    println(day.runPartTwo())
}
