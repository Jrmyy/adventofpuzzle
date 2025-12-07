package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day7 : Day<List<String>, Int> {
    override fun runPartOne(): Int = getInput().count {
        val (hypernetSequences, supernetSequences) = parseIP(it)
        hypernetSequences.none { s -> hasABBA(s.substring(1, s.length - 1)) } &&
            supernetSequences.any { s -> hasABBA(s) }
    }

    override fun runPartTwo(): Int = getInput().count {
        val (hypernetSequences, supernetSequences) = parseIP(it)
        val abas = supernetSequences.flatMap { s -> getABAs(s) }
        hypernetSequences.any { s -> abas.any { aba -> s.contains("${aba[1]}${aba[0]}${aba[1]}") } }
    }

    private fun parseIP(it: String): Pair<List<String>, List<String>> {
        val hypernetSequencesGroups = "\\[([a-z]+)]".toRegex().findAll(it).toList()
        val hypernetSequences = hypernetSequencesGroups.map { g -> g.value }
        var idx = 0
        val supernetSequences = mutableListOf<String>()
        for (group in hypernetSequencesGroups) {
            supernetSequences.add(it.substring(idx, group.range.first))
            idx = group.range.last + 1
        }
        if (idx < it.length - 1) {
            supernetSequences.add(it.substring(idx))
        }
        return Pair(hypernetSequences, supernetSequences)
    }

    private fun hasABBA(s: String): Boolean {
        val chars = s.toList()
        for (idx in (0..chars.size - 4)) {
            val pattern = chars.subList(idx, idx + 2)
            if (pattern.toSet().size != pattern.size) {
                continue
            }
            if (chars.subList(idx + 2, idx + 4).reversed() == pattern) {
                return true
            }
        }
        return false
    }

    private fun getABAs(s: String): List<String> {
        val chars = s.toList()
        val abas = mutableListOf<String>()
        for (idx in (0..chars.size - 3)) {
            val pattern = chars.subList(idx, idx + 3)
            if (pattern.toSet().size == 1) {
                continue
            }
            if (pattern.first() == pattern.last()) {
                abas.add(pattern.joinToString(""))
            }
        }
        return abas
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2016, 7)
}

fun main() {
    val day = Day7()
    println(day.runPartOne())
    println(day.runPartTwo())
}
