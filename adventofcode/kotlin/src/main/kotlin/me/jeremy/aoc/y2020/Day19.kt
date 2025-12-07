package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day19 : Day<Pair<List<Day19.Rule>, List<String>>, Int> {

    data class Rule(
        val idx: Int,
        val alternatives: Pair<String?, List<List<Int>>?>
    ) {

        fun generateRegex(reference: Map<Int, String>): String =
            "(${
            alternatives.second!!.joinToString("|") {
                "(${
                it.joinToString("") { i ->
                    reference[i] ?: error("Missing regex $i")
                }
                })"
            }
            })"
    }

    override fun runPartOne(): Int {
        val (rules, expressions) = getInput()
        val acceptedExpressions = computeAcceptedExpressions(rules)
        return expressions.count {
            Regex("^${acceptedExpressions[0]}$").matches(it)
        }
    }

    override fun runPartTwo(): Int {
        val (rules, expressions) = getInput()
        val acceptedExpressions = computeAcceptedExpressions(rules)
        var regex11 = ""
        val regex31 = acceptedExpressions[31] ?: error("No 31")
        val regex42 = acceptedExpressions[42] ?: error("No 42")
        repeat(12) {
            regex11 += "($regex42{${it + 1}}$regex31{${it + 1}})|"
        }
        regex11 = "(${regex11.dropLast(1)})"
        acceptedExpressions[0] = "($regex42+)$regex11"
        return expressions.count {
            Regex("^${acceptedExpressions[0]}$").matches(it)
        }
    }

    override fun getInput(): Pair<List<Rule>, List<String>> {
        val lines = AOCUtils.getDayInput(2020, 19)
        val emptyLine = lines.indexOfFirst { it == "" }
        val expressions = lines.subList(emptyLine + 1, lines.size)

        val rules = lines.subList(0, emptyLine)

        return Pair(
            rules.map {
                val idx = it.split(" ")[0].dropLast(1).toInt()
                val ruleContent = it.split(" ").drop(1).joinToString(" ")

                // It is a character
                val character = if (ruleContent.contains("\"")) {
                    ruleContent.replace("\"", "")
                } else {
                    null
                }

                Rule(
                    idx,
                    Pair(
                        character,
                        if (character == null) {
                            ruleContent
                                .split(" | ")
                                .map { p -> p.split(" ").map { i -> i.toInt() } }
                        } else {
                            null
                        }
                    )
                )
            },
            expressions
        )
    }

    private fun computeAcceptedExpressions(rules: List<Rule>): MutableMap<Int, String> {
        val acceptedExpressions = rules
            .filter { it.alternatives.first != null }
            .map { Pair(it.idx, it.alternatives.first!!) }
            .toMap()
            .toMutableMap()
        val containsLoop = rules.filter {
            it.alternatives.second != null && it.alternatives.second!!.flatten().contains(it.idx)
        }
        while (acceptedExpressions.size != rules.size - containsLoop.size) {
            val evaluableRules = rules.filter {
                it.alternatives.second != null &&
                    it.alternatives.second!!.flatten().toSet().subtract(acceptedExpressions.keys).isEmpty() &&
                    it.idx !in acceptedExpressions.keys &&
                    !it.alternatives.second!!.flatten().contains(it.idx)
            }
            if (evaluableRules.isEmpty()) {
                break
            }
            acceptedExpressions.putAll(
                evaluableRules.map {
                    Pair(
                        it.idx,
                        it.generateRegex(acceptedExpressions)
                    )
                }
            )
        }
        return acceptedExpressions
    }
}

fun main() {
    val day = Day19()
    println(day.runPartOne())
    println(day.runPartTwo())
}
