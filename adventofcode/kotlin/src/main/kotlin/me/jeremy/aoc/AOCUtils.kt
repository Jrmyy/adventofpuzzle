package me.jeremy.aoc

import java.math.BigInteger
import java.security.MessageDigest

object AOCUtils {

    fun getDayInput(year: Int, day: Int): List<String> =
        javaClass.classLoader.getResourceAsStream("$year/day$day.txt")!!
            .bufferedReader()
            .readLines()

    fun md5(s: String): String =
        BigInteger(1, MessageDigest.getInstance("MD5").digest(s.toByteArray()))
            .toString(16)
            .padStart(32, '0')

    fun getAdjacentPositions(l: List<List<Any>>, y: Int, x: Int, diagCount: Boolean = false): List<Pair<Int, Int>> =
        listOf(
            Pair(x + 1, y),
            Pair(x - 1, y),
            Pair(x, y - 1),
            Pair(x, y + 1),
        ).let {
            if (diagCount) it + listOf(
                Pair(x - 1, y - 1),
                Pair(x + 1, y - 1),
                Pair(x - 1, y + 1),
                Pair(x + 1, y + 1)
            ) else it
        }.filter {
            it.second in l.indices && it.first in l.first().indices
        }
}
