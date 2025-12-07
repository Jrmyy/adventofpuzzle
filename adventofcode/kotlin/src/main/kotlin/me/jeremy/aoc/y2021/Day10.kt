package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day10 : Day<List<List<Char>>, Long> {
    override fun runPartOne(): Long =
        mapOf(
            ')' to 3L,
            ']' to 57L,
            '}' to 1197L,
            '>' to 25137L
        ).let { scores ->
            getInput().sumOf {
                val (_, unMatchCharacter) = parseExpression(it)
                if (unMatchCharacter == null) 0L else scores[unMatchCharacter]!!
            }
        }

    override fun runPartTwo(): Long =
        mapOf(
            ')' to 1L,
            ']' to 2L,
            '}' to 3L,
            '>' to 4L
        ).let { scores ->
            getInput().mapNotNull {
                val (toClose, unMatchCharacter) = parseExpression(it)
                if (unMatchCharacter == null) {
                    toClose.map { c -> REVERSE_CHARACTERS[c] }.fold(0L) { acc, c ->
                        acc * 5L + scores[c]!!
                    }
                } else {
                    null
                }
            }.sorted().let { it[it.size / 2] }
        }

    private fun parseExpression(chars: List<Char>): Pair<List<Char>, Char?> {
        val toClose = mutableListOf<Char>()
        var unMatchCharacter: Char? = null
        for (c in chars) {
            if (REVERSE_CHARACTERS.keys.contains(c)) {
                toClose.add(0, c)
            } else {
                val opened = toClose.first()
                if (c == REVERSE_CHARACTERS[opened]) {
                    toClose.removeFirst()
                } else {
                    unMatchCharacter = c
                    break
                }
            }
        }
        return Pair(toClose, unMatchCharacter)
    }

    override fun getInput(): List<List<Char>> = AOCUtils.getDayInput(2021, 10).map { it.toList() }

    companion object {
        val REVERSE_CHARACTERS = mapOf(
            '(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>'
        )
    }
}

fun main() {
    val day = Day10()
    println(day.runPartOne())
    println(day.runPartTwo())
}
