package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.math.BigInteger
import java.security.MessageDigest

class Day5 : Day<String, String> {
    override fun runPartOne(): String {
        var i = 0
        val base = getInput()
        val password = mutableListOf<Char>()
        val md = MessageDigest.getInstance("MD5")
        while (password.size != 8) {
            val hash = BigInteger(1, md.digest("$base$i".toByteArray()))
                .toString(16)
                .padStart(32, '0')
            if (hash.startsWith("00000")) {
                password.add(hash[5])
            }
            i++
        }
        return password.joinToString("")
    }

    override fun runPartTwo(): String {
        var i = 0
        val base = getInput()
        val password = (0..7).associateWith { "" }.toMutableMap()
        val md = MessageDigest.getInstance("MD5")
        while (password.count { e -> e.value != "" } != 8) {
            val hash = BigInteger(1, md.digest("$base$i".toByteArray()))
                .toString(16)
                .padStart(32, '0')
            val potentialPosition = hash[5].toString()
            if (
                hash.startsWith("00000") &&
                potentialPosition in (0..7).map { it.toString() } &&
                password[potentialPosition.toInt()] == ""
            ) {
                password[potentialPosition.toInt()] = hash[6].toString()
            }
            i++
        }
        return password.values.joinToString("")
    }

    override fun getInput(): String = AOCUtils.getDayInput(2016, 5).first()
}

fun main() {
    val day = Day5()
    println(day.runPartOne())
    println(day.runPartTwo())
}
