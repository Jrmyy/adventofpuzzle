package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.math.BigInteger
import kotlin.math.abs

data class Moon(
    val position: MutableList<Int>,
    val velocity: MutableList<Int>
) {
    fun calculateEnergy(): Int = position.map { abs(it) }.sum() * velocity.map { abs(it) }.sum()
}

class Day12 : Day<List<Moon>, BigInteger> {
    override fun runPartOne(): BigInteger {
        val moons = getInput()
        for (i in 1..1000) {
            runStep(moons)
        }
        return moons.sumOf { it.calculateEnergy() }.toBigInteger()
    }

    override fun runPartTwo(): BigInteger {
        val moons = getInput().toMutableList()
        val currentMoons = getInput().toMutableList()
        var i = 1
        val leastCommonMultiplier = mutableMapOf<Int, BigInteger>()
        while (leastCommonMultiplier.size < 3) {
            runStep(currentMoons)
            for (j in 0..2) {
                if (
                    currentMoons.map { it.position[j] } == moons.map { it.position[j] } &&
                    currentMoons.count { it.velocity[j] == 0 } == 4 &&
                    !leastCommonMultiplier.containsKey(j)
                ) {
                    leastCommonMultiplier[j] = i.toBigInteger()
                }
            }
            i += 1
        }
        println(leastCommonMultiplier)
        val firstLcm = leastCommonMultiplier[0]!!.multiply(leastCommonMultiplier[1]).divide(
            leastCommonMultiplier[0]!!.gcd(leastCommonMultiplier[1])
        )
        return leastCommonMultiplier[2]!!.multiply(firstLcm).divide(leastCommonMultiplier[2]!!.gcd(firstLcm))
    }

    override fun getInput(): List<Moon> = AOCUtils.getDayInput(2019, 12).map {
        val res = Regex("<x=((-|)\\d+), y=((-|)\\d+), z=((-|)\\d+)>").find(it)
        val groups = res!!.groups
        Moon(
            mutableListOf(groups[1]!!.value.toInt(), groups[3]!!.value.toInt(), groups[5]!!.value.toInt()),
            mutableListOf(0, 0, 0)
        )
    }

    private fun runStep(moons: List<Moon>) {
        moons.forEachIndexed { moonIdx, moon ->
            moons.forEachIndexed { otherIdx, other ->
                if (otherIdx > moonIdx) {
                    for (i in 0..2) {
                        if (moon.position[i] > other.position[i]) {
                            other.velocity[i] += 1
                            moon.velocity[i] -= 1
                        } else if (moon.position[i] < other.position[i]) {
                            moon.velocity[i] += 1
                            other.velocity[i] -= 1
                        }
                    }
                }
            }
        }
        moons.forEach {
            for (i in 0..2) {
                it.position[i] += it.velocity[i]
            }
        }
    }
}

fun main() {
    val day = Day12()
    println(day.runPartOne())
    println(day.runPartTwo())
}
