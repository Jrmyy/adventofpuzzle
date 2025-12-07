package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import me.jeremy.aoc.Point3D

class Day18 : Day<Day18.LavaDroplet, Int> {

    data class LavaDroplet(val cubes: Set<Point3D>) {

        private val boundingBox: Pair<Point3D, Point3D> =
            Point3D(
                cubes.minOf { it.x }, cubes.minOf { it.y }, cubes.minOf { it.z }
            ) to Point3D(
                cubes.maxOf { it.x }, cubes.maxOf { it.y }, cubes.maxOf { it.z }
            )

        fun countSidesInLavaDropletBoundaries(from: Point3D): Int =
            FACES.count {
                val face = from + it
                if (cubes.contains(face)) false else isPartOfBoundary(face)
            }

        private fun isOutside(point: Point3D): Boolean {
            val (min, max) = boundingBox
            return point.x < min.x ||
                point.y < min.y ||
                point.z < min.z ||
                point.x > max.x ||
                point.y > max.y ||
                point.z > max.z
        }

        private fun isPartOfBoundary(face: Point3D): Boolean {
            val visited = (cubes + face).toMutableSet()
            var toVisit = setOf(face)
            while (toVisit.isNotEmpty()) {
                visited += toVisit
                // If we are out of the bounding box, this face is part of the boundaries of the LavaDroplet
                if (toVisit.any { isOutside(it) }) {
                    return true
                } else {
                    // Check if next faces are external sides
                    toVisit = toVisit.flatMap { p -> FACES.map { p + it } }
                        .filter { !visited.contains(it) }
                        .toSet()
                }
            }
            return false
        }
    }

    override fun runPartOne(): Int = getInput().let { LavaDroplet ->
        LavaDroplet.cubes.sumOf { point ->
            FACES.count { !LavaDroplet.cubes.contains(point + it) }
        }
    }

    override fun runPartTwo(): Int = getInput().let { LavaDroplet ->
        LavaDroplet.cubes.sumOf { LavaDroplet.countSidesInLavaDropletBoundaries(it) }
    }

    override fun getInput(): LavaDroplet = LavaDroplet(
        AOCUtils.getDayInput(2022, 18).map {
            it.split(",").let { l -> Point3D(l[0].toDouble(), l[1].toDouble(), l[2].toDouble()) }
        }.toSet()
    )

    companion object {
        val FACES = listOf(
            Triple(-1, 0, 0),
            Triple(1, 0, 0),
            Triple(0, -1, 0),
            Triple(0, 1, 0),
            Triple(0, 0, -1),
            Triple(0, 0, 1),
        ).map { (x, y, z) -> Point3D(x.toDouble(), y.toDouble(), z.toDouble()) }
    }
}

fun main() {
    val day = Day18()
    println(day.runPartOne())
    println(day.runPartTwo())
}
