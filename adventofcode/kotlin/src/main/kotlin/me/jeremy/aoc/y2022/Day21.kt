package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day21 : Day<Map<String, String>, Long> {

    override fun runPartOne(): Long = getMonkeyRoot(getInput())

    override fun runPartTwo(): Long {
        val monkeys = getInput().toMutableMap()
        monkeys["root"] = monkeys["root"]!!.split(" ").let { "${it.first()} = ${it.last()}" }

        // After investigating it seems that the biggest human is the lowest the left part of root decreased
        // The second part is not impacted by human

        var min = 0L
        var max = 0L
        var i = 1L

        mainWhile@ while (true) {
            monkeys["humn"] = "$i"
            val diff = getMonkeyRootDiff(monkeys)
            if (diff < 0) break@mainWhile
            min = i
            i *= 10
            max = i
        }

        while (max - min > 1) {
            val median = min + (max - min) / 2
            monkeys["humn"] = "$median"
            val medianDiff = getMonkeyRootDiff(monkeys)
            if (medianDiff > 0) {
                min = median
            } else {
                max = median
            }
        }

        return max
    }

    override fun getInput(): Map<String, String> = AOCUtils.getDayInput(2022, 21).associate {
        it.split(": ").let { p -> Pair(p.first(), p.last()) }
    }

    private fun getMonkeyRoot(monkeys: Map<String, String>): Long {
        val values = mutableMapOf<String, Long>()
        val toCheck = monkeys.keys.toMutableList()
        while (toCheck.isNotEmpty()) {
            val monkey = toCheck.removeFirst()
            val job = monkeys[monkey] ?: error("Should exist")
            if ("-?\\d+".toRegex().matches(job)) {
                values[monkey] = job.toLong()
            } else {
                val (first, op, second) = job.split(" ")
                if (values.contains(first) && values.contains(second)) {
                    val f = values[first] ?: error("Should exist")
                    val s = values[second] ?: error("Should exist")
                    values[monkey] = when (op) {
                        "+" -> f + s
                        "-" -> f - s
                        "*" -> f * s
                        else -> f / s
                    }
                } else {
                    toCheck.add(monkey)
                }
            }
        }
        return values["root"] ?: error("Should exist")
    }

    private fun getMonkeyRootDiff(monkeys: Map<String, String>): Long {
        val values = mutableMapOf<String, Long>()
        val toCheck = monkeys.keys.toMutableList()
        while (toCheck.isNotEmpty()) {
            val monkey = toCheck.removeFirst()
            val job = monkeys[monkey] ?: error("Should exist")
            if ("-?\\d+".toRegex().matches(job)) {
                values[monkey] = job.toLong()
            } else {
                val (first, op, second) = job.split(" ")
                if (values.contains(first) && values.contains(second)) {
                    val f = values[first] ?: error("Should exist")
                    val s = values[second] ?: error("Should exist")
                    if (monkey == "root") {
                        return f - s
                    }
                    values[monkey] = when (op) {
                        "+" -> f + s
                        "-" -> f - s
                        "*" -> f * s
                        "/" -> f / s
                        else -> if (f == s) 1 else 0
                    }
                } else {
                    toCheck.add(monkey)
                }
            }
        }
        error("Should have found")
    }
}

fun main() {
    val day = Day21()
    println(day.runPartOne())
    println(day.runPartTwo())
}
