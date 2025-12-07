package me.jeremy.aoc.y2022

import com.google.gson.Gson
import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max

class Day13 : Day<List<String>, Int> {

    override fun runPartOne(): Int = getInput().chunked(2).mapIndexedNotNull { index, packets ->
        val gson = Gson()
        val left = gson.fromJson(packets.first(), List::class.java)
        val right = gson.fromJson(packets.last(), List::class.java)
        if (inCorrectOrder(left, right)!!) {
            index + 1
        } else {
            null
        }
    }.sum()

    override fun runPartTwo(): Int {
        val packets = getInput().toMutableList()
        val dividers = listOf(2, 6).map { "[[$it]]" }
        packets.addAll(dividers)
        val gson = Gson()
        var isSorted = false
        while (!isSorted) {
            isSorted = true
            var idx = 0
            while (idx < packets.size - 1) {
                val left = gson.fromJson(packets[idx], List::class.java)
                val right = gson.fromJson(packets[idx + 1], List::class.java)
                if (!inCorrectOrder(left, right)!!) {
                    val tmp = packets[idx]
                    packets[idx] = packets[idx + 1]
                    packets[idx + 1] = tmp
                    isSorted = false
                }
                idx++
            }
        }
        return dividers.map { packets.indexOf(it) + 1 }.reduce { acc, i -> acc * i }
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2022, 13).filter { it != "" }

    private fun inCorrectOrder(left: List<*>, right: List<*>): Boolean? {
        var idx = 0
        while (idx < max(left.size, right.size)) {
            val f = left.getOrNull(idx)
            val s = right.getOrNull(idx)
            if (f == null) return true
            if (s == null) return false
            if (f is Double && s is Double) {
                if (f < s) return true
                if (s < f) return false
                idx++
            } else {
                val res = if (f is Double && s is List<*>) {
                    inCorrectOrder(listOf(f), s)
                } else if (f is List<*> && s is Double) {
                    inCorrectOrder(f, listOf(s))
                } else if (f is List<*> && s is List<*>) {
                    inCorrectOrder(f, s)
                } else {
                    error("Wrong type for $f and $s")
                }
                if (res != null) return res
                idx++
            }
        }
        return null
    }
}

fun main() {
    val day = Day13()
    println(day.runPartOne())
    println(day.runPartTwo())
}
