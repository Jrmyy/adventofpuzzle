package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.*

class Day25 : Day<List<String>, String> {

    override fun runPartOne(): String = toSNAFU(getInput().sumOf { toDecimal(it) })

    override fun runPartTwo(): String {
        TODO("Not yet implemented")
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2022, 25)

    private fun toSNAFU(l: Long): String {
        var maxPow = 0
        while (5.toDouble().pow(maxPow + 1) < l) {
            maxPow++
        }
        var i = maxPow
        var k = l.toDouble()
        var s = ""
        while (i >= 0) {
            val j = (k / 5.toDouble().pow(i)).roundToInt()
            k -= j * 5.toDouble().pow(i)
            s += DECIMAL_SNAFU[j]
            i--
        }
        return s
    }

    private fun toDecimal(snafu: String): Long =
        snafu.toList().reversed().mapIndexed { idx, c ->
            5.toDouble().pow(idx).toLong() * SNAFU_DECIMAL[c]!!
        }.sum()

    companion object {
        val DECIMAL_SNAFU = mapOf(
            -2 to '=',
            -1 to '-',
            0 to '0',
            1 to '1',
            2 to '2',
        )
        val SNAFU_DECIMAL = DECIMAL_SNAFU.map { it.value to it.key }.toMap()
    }
}

fun main() {
    val day = Day25()
    println(day.runPartOne())
}
