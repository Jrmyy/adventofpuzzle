package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import me.jeremy.aoc.Point3D
import kotlin.math.abs

class Day19 : Day<MutableList<List<Point3D>>, Int> {

    override fun runPartOne(): Int {
        val remaining = getInput().toMutableList()
        val determined = mutableListOf(remaining.removeFirst())
        val scanners = mutableListOf(Point3D(0.0, 0.0, 0.0))

        while (remaining.isNotEmpty()) {
            mainLoop@ for (sci in remaining.withIndex()) {
                for (dsc in determined) {
                    val absolutePositionsAndScanner = getAbsoluteScannerAndBeaconsPositions(dsc, sci.value)
                    if (absolutePositionsAndScanner != null) {
                        val (beacons, scanner) = absolutePositionsAndScanner
                        determined.add(beacons)
                        remaining.removeAt(sci.index)
                        scanners.add(scanner)
                        break@mainLoop
                    }
                }
            }
        }

        // Part one
        println(determined.flatten().toSet().size)

        // Part two
        return scanners.flatMapIndexed { i, s ->
            scanners.filterIndexed { si, _ -> si != i }.map {
                abs(s.x - it.x) + abs(s.y - it.y) + abs(s.z - it.z)
            }
        }.maxOrNull()!!.toInt()
    }

    override fun runPartTwo(): Int {
        // Implemented in part one since it takes too much time
        return 0
    }

    override fun getInput(): MutableList<List<Point3D>> =
        AOCUtils.getDayInput(2021, 19).let { lines ->
            val scanners = mutableListOf<MutableList<Point3D>>(mutableListOf())
            for (line in lines.subList(1, lines.size)) {
                if (line.contains("scanner")) {
                    scanners.add(mutableListOf())
                } else if (line != "") {
                    scanners.last().add(
                        line.split(",").let {
                            Point3D(it.first().toDouble(), it[1].toDouble(), it.last().toDouble())
                        }
                    )
                }
            }
            scanners.map { it.toList() }.toMutableList()
        }

    private fun getAbsoluteSecondScannerPosition(fsc: List<Point3D>, pssc: List<Point3D>): Point3D? {
        for (i in fsc.indices) {
            for (j in 0 until i) {
                // We try to check, for every pair of positions in first and second scanner,
                // if in fact this is the same position, but seen relatively by the two scanners.
                // To do so, we try to check if the diff between this pair of positions can be the scanner position
                // For instance, in the example in 2D, (0, 2) - (-5, 0) gives (5,2), and so we check the number
                // of shared positions between first scanner (in absolute, because of the algorithm) and second scanner
                // in absolute by adding to all permuted positions the scanner position
                val scanner = fsc[i] - pssc[j]
                val uniques = mutableSetOf<Point3D>()
                uniques.addAll(fsc)
                uniques.addAll(pssc.map { it + scanner })
                // If there is 12 or more share positions, we found our scanner
                if (fsc.size + pssc.size - uniques.size >= 12) {
                    return scanner
                }
            }
        }
        return null
    }

    private fun getAbsoluteScannerAndBeaconsPositions(
        fsc: List<Point3D>,
        ssc: List<Point3D>
    ): Pair<List<Point3D>, Point3D>? {
        // We permute all the positions and return a list of size 24 with each element being the list of positions
        // of the beacon permuted with the same transformation
        val permutedPoints = ssc.map { permute(it) }.let { permutationsPerPoints ->
            (0 until permutationsPerPoints.first().size).map {
                permutationsPerPoints.map { l -> l[it] }
            }
        }
        // Then we check that the permutation of second scanner positions share positions with first scanner
        for (permute in permutedPoints) {
            val scanner = getAbsoluteSecondScannerPosition(fsc, permute)
            if (scanner != null) {
                // We return the absolute coordinates, meaning the permuted coordinates with the scanner position added
                // (It is absolute coordinates because we start with first scanner position at (0,0,0)
                // like in the example
                return Pair(permute.map { it + scanner }, scanner)
            }
        }
        return null
    }

    private fun permute(point: Point3D): List<Point3D> =
        listOf(
            Point3D(point.x, point.y, point.z),
            Point3D(point.x, point.z, -point.y),
            Point3D(-point.z, point.x, -point.y),
            Point3D(-point.x, -point.z, -point.y),
            Point3D(point.z, -point.x, -point.y),
            Point3D(point.z, -point.y, point.x),
            Point3D(point.y, point.z, point.x),
            Point3D(-point.z, point.y, point.x),
            Point3D(-point.y, -point.z, point.x),
            Point3D(-point.y, point.x, point.z),
            Point3D(-point.x, -point.y, point.z),
            Point3D(point.y, -point.x, point.z),
            Point3D(-point.z, -point.x, point.y),
            Point3D(point.x, -point.z, point.y),
            Point3D(point.z, point.x, point.y),
            Point3D(-point.x, point.z, point.y),
            Point3D(-point.x, point.y, -point.z),
            Point3D(-point.y, -point.x, -point.z),
            Point3D(point.x, -point.y, -point.z),
            Point3D(point.y, point.x, -point.z),
            Point3D(point.y, -point.z, -point.x),
            Point3D(point.z, point.y, -point.x),
            Point3D(-point.y, point.z, -point.x),
            Point3D(-point.z, -point.y, -point.x)
        )
}

fun main() {
    val day = Day19()
    println(day.runPartOne())
}
