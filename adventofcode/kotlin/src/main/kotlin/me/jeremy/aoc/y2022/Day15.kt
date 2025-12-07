package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.abs

class Day15 : Day<List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, Long> {

    private data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int) {
        private val left = x
        private val right = x + width - 1
        private val top = y
        private val bottom = y + height - 1

        val corners = listOf(
            Pair(left, top),
            Pair(right, top),
            Pair(left, bottom),
            Pair(right, bottom),
        )

        fun split(): List<Rectangle> {
            val w0 = width / 2
            val w1 = width - w0
            val h0 = height / 2
            val h1 = height - h0
            return listOf(
                Rectangle(left, top, w0, h0),
                Rectangle(left + w0, top, w1, h0),
                Rectangle(left, top + h0, w0, h1),
                Rectangle(left + w0, top + h0, w1, h1),
            )
        }
    }

    override fun runPartOne(): Long {
        val state = getInput()
        val y = 2_000_000
        val noBeaconX = mutableSetOf<Int>()
        state.forEach {
            val (s, b) = it
            val dist = abs(s.first - b.first) + abs(s.second - b.second)
            if ((s.second >= y && s.second - dist <= y) || (s.second <= y && s.second + dist >= y)) {
                val diff = dist - abs(y - s.second)
                noBeaconX.addAll((-diff..diff).map { x -> s.first + x })
            }
        }
        return noBeaconX.size.toLong() - state.map { it.second }.filter { it.second == y }.toMutableSet().size
    }

    override fun runPartTwo(): Long =
        getUncoveredAreas(getInput(), Rectangle(0, 0, 4_000_000, 4_000_000)).first().let {
            r ->
            r.x * 4_000_000L + r.y
        }

    private fun getUncoveredAreas(state: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, rect: Rectangle): List<Rectangle> {
        // If a sensor contains all the corners, it does not have uncovered area
        for ((s, b) in state) {
            if (rect.corners.all { c ->
                val dc = abs(c.first - s.first) + abs(c.second - s.second)
                val db = abs(b.first - s.first) + abs(b.second - s.second)
                dc <= db
            }
            ) {
                return listOf()
            }
        }

        // If we arrive at the point in which we have a unique point therefore not covered by any beacon,
        // that is our answer
        if (rect.width == 1 && rect.height == 1) {
            return listOf(rect)
        }

        // Otherwise we split our rectangle in 4, and we try to find the uncovered area in those
        return rect.split().flatMap { cRect -> getUncoveredAreas(state, cRect) }
    }

    override fun getInput(): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> = AOCUtils.getDayInput(2022, 15).map {
        val (s, b) = it.split(": ")
        Pair(
            s.replace("Sensor at ", "").split(", ").map { c ->
                "(-?\\d+)".toRegex().find(c)!!.value.toInt()
            }.let { l -> Pair(l.first(), l.last()) },
            b.replace("closest beacon is at ", "").split(", ").map { c ->
                "(-?\\d+)".toRegex().find(c)!!.value.toInt()
            }.let { l -> Pair(l.first(), l.last()) }
        )
    }
}

fun main() {
    val day = Day15()
    println(day.runPartOne())
    println(day.runPartTwo())
}
