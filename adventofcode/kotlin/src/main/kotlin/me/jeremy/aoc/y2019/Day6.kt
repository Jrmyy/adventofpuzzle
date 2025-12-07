package me.jeremy.aoc.y2019

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day6 : Day<List<Pair<String, String>>, Int> {
    override fun runPartOne(): Int = getAllPaths().sumOf { it.split("/").size - 1 }

    override fun runPartTwo(): Int {
        val allPaths = getAllPaths()
        val you = allPaths.first { it.split("/").last() == "YOU" }.split("/").reversed()
        val san = allPaths.first { it.split("/").last() == "SAN" }.split("/").reversed()
        var i = 0
        var commonParent: String? = null
        while (commonParent == null) {
            if (san.contains(you[i])) {
                commonParent = you[i]
            }
            i++
        }
        return you.indexOf(commonParent) - 1 + san.indexOf(commonParent) - 1
    }

    override fun getInput(): List<Pair<String, String>> = AOCUtils.getDayInput(2019, 6).map {
        val parts = it.split(")")
        Pair(parts[0], parts[1])
    }

    private fun getAllPaths(): List<String> {
        val associations = getInput()
        val comAssociation = associations.first { it.first == "COM" }
        val pathsToExplore = mutableListOf("${comAssociation.first}/${comAssociation.second}")
        val paths = mutableListOf<String>()
        while (pathsToExplore.size > 0) {
            val pathToExplore = pathsToExplore.removeAt(0)
            val parent = pathToExplore.split("/").last()
            pathsToExplore.addAll(
                associations.filter { it.first == parent }.map { "$pathToExplore/${it.second}" }
            )
            paths.add(pathToExplore)
        }
        return paths
    }
}

fun main() {
    val day = Day6()
    println(day.runPartOne())
    println(day.runPartTwo())
}
