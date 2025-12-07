package me.jeremy.aoc.y2020

import me.jeremy.aoc.Day

class Day25 : Day<Pair<Long, Long>, Long> {
    override fun runPartOne(): Long {
        val (cardPk, doorPk) = getInput()
        var cardSecretLoop = 0
        var cardTempResult = 1L
        val subjectNumber = 7L
        while (cardTempResult != cardPk) {
            cardTempResult = transform(cardTempResult, subjectNumber)
            cardSecretLoop++
        }
        var encryptionKey = 1L
        repeat(cardSecretLoop) {
            encryptionKey = transform(encryptionKey, doorPk)
        }
        return encryptionKey
    }

    override fun runPartTwo(): Long {
        // No Part 2 this day
        return 0L
    }

    private fun transform(l: Long, subjectNumber: Long) = (l * subjectNumber) % 20201227

    override fun getInput(): Pair<Long, Long> = Pair(8987316L, 14681524L)
}

fun main() {
    val day = Day25()
    println(day.runPartOne())
    println(day.runPartTwo())
}
