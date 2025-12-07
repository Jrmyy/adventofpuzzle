package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day11 : Day<List<Day11.Monkey>, Long> {

    data class Monkey(
        val items: MutableList<Long>,
        val operation: (old: Long) -> Long,
        val divTest: Int,
        val monkeyIdIfTrue: Int,
        val monkeyIdIfFalse: Int,
    ) {
        companion object {
            fun of(def: List<String>): Monkey = Monkey(
                def.first().trim().replace("Starting items: ", "")
                    .split(", ")
                    .map { i -> i.toLong() }.toMutableList(),
                def[1].trim().replace("Operation: new = ", "").let { op ->
                    val parts = op.split(" ")
                    val operation = { old: Long ->
                        val first = parts.first().let { p -> if (p == "old") "$old" else p }
                        val second = parts.last().let { p -> if (p == "old") "$old" else p }
                        if (parts[1] == "*") {
                            first.toLong() * second.toLong()
                        } else {
                            first.toLong() + second.toLong()
                        }
                    }
                    operation
                },
                def[2].trim().replace("Test: divisible by ", "").toInt(),
                def[3].trim().replace("If true: throw to monkey ", "").toInt(),
                def[4].trim().replace("If false: throw to monkey ", "").toInt()
            )
        }
    }

    override fun runPartOne(): Long = monkeyBusiness(20, 3)

    override fun runPartTwo(): Long = monkeyBusiness(10_000, 1)

    override fun getInput(): List<Monkey> =
        AOCUtils.getDayInput(2022, 11).chunked(7).map {
            Monkey.of(
                it.drop(1).let { m -> if (m.size == 7) m.dropLast(1) else m }
            )
        }

    private fun monkeyBusiness(turns: Int, relief: Int = 1): Long {
        val monkeys = getInput()
        // We reduce the size of the worry by taking the modulo of all the dividers (i.e. if it goes to all the monkeys
        // worry will be reset)
        val modulo = monkeys.map { it.divTest }.reduce { acc, i -> acc * i }
        val itemsPerMonkey = monkeys.indices.map { 0L }.toMutableList()
        repeat(turns) {
            monkeys.forEachIndexed { idx, m ->
                while (m.items.isNotEmpty()) {
                    val item = m.items.removeFirst()
                    val worry = m.operation(item) / relief
                    if (worry % m.divTest == 0L) {
                        monkeys[m.monkeyIdIfTrue].items.add(worry % modulo)
                    } else {
                        monkeys[m.monkeyIdIfFalse].items.add(worry % modulo)
                    }
                    itemsPerMonkey[idx]++
                }
            }
        }
        return itemsPerMonkey.sortedDescending().take(2).reduce { acc, i -> acc * i }
    }
}

fun main() {
    val day = Day11()
    println(day.runPartOne())
    println(day.runPartTwo())
}
