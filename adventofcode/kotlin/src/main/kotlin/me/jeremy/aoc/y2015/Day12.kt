package me.jeremy.aoc.y2015

import com.google.gson.Gson
import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day12 : Day<String, Int> {
    override fun runPartOne(): Int {
        val json = getInput()
        val numbers = "((-|)\\d+)".toRegex().findAll(json)
        return numbers.sumOf { it.value.toInt() }
    }

    override fun runPartTwo(): Int {
        val str = getInput()
        val gson = Gson()
        val json = gson.fromJson(str, Map::class.java)
        return computeSumWithoutRed(Pair(json, null))
    }

    private fun computeSumWithoutRed(obj: Pair<Map<*, *>?, List<*>?>): Int {
        if (obj.first != null && obj.first!!.containsValue("red")) {
            return 0
        }
        val values: List<Any?> = if (obj.first != null) obj.first!!.values.toList() else obj.second!!
        return values.sumOf { v ->
            when (v) {
                is Number -> v.toInt()
                is Map<*, *> -> computeSumWithoutRed(Pair(v, null))
                is List<*> -> computeSumWithoutRed(Pair(null, v))
                else -> 0
            }
        }
    }

    override fun getInput(): String = AOCUtils.getDayInput(2015, 12).first()
}

fun main() {
    val day = Day12()
    println(day.runPartOne())
    println(day.runPartTwo())
}
