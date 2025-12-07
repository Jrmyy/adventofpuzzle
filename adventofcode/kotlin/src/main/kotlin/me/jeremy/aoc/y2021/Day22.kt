package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day22 : Day<List<Pair<String, Day22.Cuboid>>, Long> {

    data class Cuboid(
        val x: Pair<Int, Int>,
        val y: Pair<Int, Int>,
        val z: Pair<Int, Int>
    ) {
        fun subtract(other: Cuboid): List<Cuboid> {
            if (other.contains(this)) {
                return listOf()
            }
            if (!intersects(other)) {
                return listOf(this)
            }

            val xs = listOf(x.first) + listOf(other.x.first, other.x.second).filter {
                it > x.first && it < x.second
            } + listOf(x.second)
            val ys = listOf(y.first) + listOf(other.y.first, other.y.second).filter {
                it > y.first && it < y.second
            } + listOf(y.second)
            val zs = listOf(z.first) + listOf(other.z.first, other.z.second).filter {
                it > z.first && it < z.second
            } + listOf(z.second)

            return (0 until xs.size - 1).flatMap { xi ->
                (0 until ys.size - 1).flatMap { yi ->
                    (0 until zs.size - 1).map { zi ->
                        Cuboid(
                            Pair(xs[xi], xs[xi + 1]),
                            Pair(ys[yi], ys[yi + 1]),
                            Pair(zs[zi], zs[zi + 1])
                        )
                    }
                }
            }.filter { c -> !other.contains(c) }
        }

        fun contains(o: Cuboid): Boolean =
            x.first <= o.x.first && x.second >= o.x.second &&
                y.first <= o.y.first && y.second >= o.y.second &&
                z.first <= o.z.first && z.second >= o.z.second

        private fun intersects(o: Cuboid): Boolean =
            x.first <= o.x.second && x.second >= o.x.first &&
                y.first <= o.y.second && y.second >= o.y.first &&
                z.first <= o.z.second && z.second >= o.z.first

        fun volume(): Long = (x.second - x.first).toLong() *
            (y.second - y.first).toLong() *
            (z.second - z.first).toLong()
    }

    override fun runPartOne(): Long = run {
        Cuboid(Pair(-50, 50), Pair(-50, 50), Pair(-50, 50)).contains(it.second)
    }.sumOf { it.volume() }

    override fun runPartTwo(): Long = run().sumOf { it.volume() }

    override fun getInput(): List<Pair<String, Cuboid>> =
        AOCUtils.getDayInput(2021, 22).map {
            it.split(" ").let { p ->
                val status = p.first()
                val ranges = p.last().split(",").map { s ->
                    s.substring(2).split("..").let { r ->
                        Pair(r.first().toInt(), r.last().toInt() + 1)
                    }
                }
                Pair(status, Cuboid(ranges[0], ranges[1], ranges[2]))
            }
        }

    private fun run(fn: (Pair<String, Cuboid>) -> Boolean = { true }): List<Cuboid> {
        return getInput().filter { fn(it) }.fold(mutableListOf()) { acc, (s, c) ->
            (acc.flatMap { cc -> cc.subtract(c) } + if (s == "on") listOf(c) else listOf()).toMutableList()
        }
    }
}

fun main() {
    val day = Day22()
    println(day.runPartOne())
    println(day.runPartTwo())
}
