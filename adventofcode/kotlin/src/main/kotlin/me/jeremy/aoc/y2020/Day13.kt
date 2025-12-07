package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day13 : Day<Pair<Int, List<String>>, Long> {
    override fun runPartOne(): Long {
        val (departTimestamp, buses) = getInput()
        val minBus = buses.mapNotNull {
            if (it != "x") {
                it.toInt()
            } else {
                null
            }
        }.map {
            val busesRound = departTimestamp / it
            val res = busesRound * it - departTimestamp
            Pair(
                it,
                if (res < 0) {
                    res + it
                } else {
                    res
                }
            )
        }.minByOrNull { it.second } ?: throw RuntimeException("No bus")
        return (minBus.first * minBus.second).toLong()
    }

    override fun runPartTwo(): Long {
        // Chinese remainder theorem
        val (_, buses) = getInput()
        val indexedBuses = buses.mapIndexedNotNull { idx, it ->
            if (it != "x") {
                Pair(it.toInt(), Math.floorMod(-idx, it.toInt()))
            } else {
                null
            }
        }
        val bis = indexedBuses.map { it.second }
        val res = indexedBuses.fold(1L) { acc, pair ->
            acc * pair.first.toLong()
        }
        val nis = indexedBuses.map {
            res / it.first
        }
        val xis = nis.mapIndexed { index, l ->
            val mod = indexedBuses[index].first
            val realMultiplier = (l % mod).toInt()
            (0 until mod).mapNotNull {
                if ((realMultiplier * it) % mod == 1) {
                    it
                } else {
                    null
                }
            }.firstOrNull()!!
        }
        val sum = bis.indices.fold(0L) { acc, i ->
            acc + bis[i] * nis[i] * xis[i]
        }
        return sum % res
    }

    override fun getInput(): Pair<Int, List<String>> {
        val (departTimestamp, buses) = AOCUtils.getDayInput(2020, 13)
        return Pair(departTimestamp.toInt(), buses.split(","))
    }
}

fun main() {
    val day = Day13()
    println(day.runPartOne())
    println(day.runPartTwo())
}
