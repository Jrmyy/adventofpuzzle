package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day14 : Day<String, Int> {

    override fun runPartOne(): Int = find64thKey()

    override fun runPartTwo(): Int = find64thKey(2016)

    override fun getInput(): String = AOCUtils.getDayInput(2016, 14).first()

    private fun find64thKey(numHashes: Int = 0): Int {
        var idx = 0
        val salt = getInput()
        var found = 0
        val cache = mutableListOf<String>()
        whiled@ while (found < 64) {
            val s = if (cache.getOrNull(idx) != null) {
                cache[idx]
            } else {
                val md = chainedMd5("$salt$idx", numHashes)
                cache.add(md)
                md
            }
            var foundChar: Char? = null
            for (i in 0 until s.length - 2) {
                if (s.substring(i, i + 3).toSet().size == 1) {
                    foundChar = s[i]
                    break
                }
            }
            if (foundChar != null) {
                val seq = foundChar.toString().repeat(5)
                for (i in 1 until 1001) {
                    val ss = if (cache.getOrNull(idx + i) != null) {
                        cache[idx + i]
                    } else {
                        val md = chainedMd5("$salt${idx + i}", numHashes)
                        cache.add(md)
                        md
                    }
                    if (seq in ss) {
                        found++
                        if (found == 64) {
                            break@whiled
                        }
                        break
                    }
                }
            }
            idx++
        }
        return idx
    }

    private fun chainedMd5(o: String, numHashes: Int): String {
        var s = AOCUtils.md5(o).lowercase()
        (0 until numHashes).forEach { _ ->
            s = AOCUtils.md5(s).lowercase()
        }
        return s
    }
}

fun main() {
    val day = Day14()
    println(day.runPartOne())
    println(day.runPartTwo())
}
