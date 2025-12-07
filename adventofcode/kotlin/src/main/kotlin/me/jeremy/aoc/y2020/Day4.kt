package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day4 : Day<List<Day4.RawPassport>, Int> {

    data class RawPassport(
        val byr: String? = null,
        val iyr: String? = null,
        val eyr: String? = null,
        val hgt: String? = null,
        val hcl: String? = null,
        val ecl: String? = null,
        val pid: String? = null,
        val cid: String? = null
    ) {

        fun isHeightMatching(): Boolean {
            val regex = Regex("^((1[5-9][0-9])cm|([5-7][0-9])in)$")
            if (hgt == null) {
                return false
            }
            val res = regex.find(hgt) ?: return false
            val isIn = hgt.endsWith("in")
            val idx = if (isIn) 3 else 2
            val group = res.groups[idx] ?: return false
            val height = group.value.toIntOrNull() ?: return false
            return (isIn && height in 59 until 77) ||
                (!isIn && height in 150 until 194)
        }

        fun isValidAtFirst(): Boolean =
            byr != null && iyr != null && eyr != null && hgt != null && hcl != null && ecl != null && pid != null

        fun isReallyValid(): Boolean =
            byr?.toIntOrNull() in 1920 until 2003 &&
                iyr?.toIntOrNull() in 2010 until 2021 &&
                eyr?.toIntOrNull() in 2020 until 2031 &&
                hcl != null && Regex("^#[0-9a-f]{6}$").matches(hcl) &&
                listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(ecl) &&
                isHeightMatching() &&
                pid != null && Regex("^[0-9]{9}$").matches(pid)
    }

    override fun runPartOne(): Int =
        getInput().count {
            it.isValidAtFirst()
        }

    override fun runPartTwo(): Int =
        getInput().count {
            it.isReallyValid()
        }

    override fun getInput(): List<RawPassport> {
        val lines = AOCUtils.getDayInput(2020, 4)
        val currentPassport = mutableListOf<String>()
        val passports = mutableListOf<RawPassport>()
        for (line in lines) {
            if (line == "") {
                passports.add(getPassport(currentPassport))
                currentPassport.clear()
            } else {
                currentPassport.add(line)
            }
        }
        if (currentPassport.size > 0) {
            passports.add(getPassport(currentPassport))
        }
        return passports
    }

    private fun getPassport(lines: MutableList<String>): RawPassport {
        val content = lines.joinToString(" ").split(" ").map {
            val keyValue = it.split(":")
            Pair(keyValue[0], keyValue[1])
        }.toMap()
        return RawPassport(
            content["byr"],
            content["iyr"],
            content["eyr"],
            content["hgt"],
            content["hcl"],
            content["ecl"],
            content["pid"],
            content["cid"]
        )
    }
}

fun main() {
    val day = Day4()
    println(day.runPartOne())
    println(day.runPartTwo())
}
