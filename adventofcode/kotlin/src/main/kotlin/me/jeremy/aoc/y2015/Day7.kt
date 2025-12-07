package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day7 : Day<List<String>, Int> {
    override fun runPartOne(): Int = runOps()["a"]!!

    override fun runPartTwo(): Int = runOps(mutableMapOf("b" to runOps()["a"]!!))["a"]!!

    override fun getInput(): List<String> = AOCUtils.getDayInput(2015, 7)

    private fun runOps(values: MutableMap<String, Int> = mutableMapOf()): Map<String, Int> {
        val instructions = getInput()
        var remainingInstructions = instructions.toMutableList()
        while (remainingInstructions.isNotEmpty()) {
            val newRemaining = mutableListOf<String>()
            remainingInstructions.forEach {
                val parts = it.split(" -> ")
                val opt = parts.last().trim()
                if (!values.contains(opt)) {
                    if (it.contains("AND")) {
                        val ipt = parts.first().split(" AND ").map { k ->
                            if (values.contains(k)) values[k].toString() else k
                        }
                        if (ipt.all { s -> "\\d+".toRegex().matches(s) }) {
                            values[opt] = ipt.first().toInt() and ipt.last().toInt()
                        } else {
                            newRemaining.add(it)
                        }
                    } else if (it.contains("NOT")) {
                        val ipt = parts.first().replace("NOT", "").trim().let { s ->
                            if (values.contains(s)) values[s].toString() else s
                        }
                        if ("\\d+".toRegex().matches(ipt)) {
                            val res = ipt.toInt().inv()
                            values[opt] = if (res >= 0) res else res + 65536
                        } else {
                            newRemaining.add(it)
                        }
                    } else if (it.contains("OR")) {
                        val ipt = parts.first().split(" OR ").map { k ->
                            if (values.contains(k)) values[k].toString() else k
                        }
                        if (ipt.all { s -> "\\d+".toRegex().matches(s) }) {
                            values[opt] = (ipt.first().toInt() or ipt.last().toInt())
                        } else {
                            newRemaining.add(it)
                        }
                    } else if (it.contains("RSHIFT")) {
                        val ipt = parts.first().split(" RSHIFT ").map { k ->
                            if (values.contains(k)) values[k].toString() else k
                        }
                        if (ipt.all { s -> "\\d+".toRegex().matches(s) }) {
                            values[opt] = ipt.first().toInt().shr(ipt.last().toInt())
                        } else {
                            newRemaining.add(it)
                        }
                    } else if (it.contains("LSHIFT")) {
                        val ipt = parts.first().split(" LSHIFT ").map { k ->
                            if (values.contains(k)) values[k].toString() else k
                        }
                        if (ipt.all { s -> "\\d+".toRegex().matches(s) }) {
                            values[opt] = ipt.first().toInt().shl(ipt.last().toInt())
                        } else {
                            newRemaining.add(it)
                        }
                    } else {
                        val ipt = parts.first().trim().let { s ->
                            if (values.contains(s)) values[s].toString() else s
                        }
                        if ("\\d+".toRegex().matches(ipt)) {
                            values[opt] = ipt.toInt()
                        } else {
                            newRemaining.add(it)
                        }
                    }
                }
            }
            remainingInstructions = newRemaining
        }
        return values
    }
}

fun main() {
    val day = Day7()
    println(day.runPartOne())
    println(day.runPartTwo())
}
