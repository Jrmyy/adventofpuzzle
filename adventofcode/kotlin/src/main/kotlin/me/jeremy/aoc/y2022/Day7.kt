package me.jeremy.aoc.y2022

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day7 : Day<List<String>, Int> {

    override fun runPartOne(): Int = getSystemTree().filter { it.value < 100_000 }.values.sum()

    override fun runPartTwo(): Int =
        getSystemTree().let {
            it.filter { e -> e.value >= (30_000_000 - (70_000_000 - it["/"]!!)) }.minOf { e -> e.value }
        }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2022, 7)

    private fun getSystemTree(): Map<String, Int> {
        val terminalOutput = getInput()
        var idx = 0
        var cwd = "/"
        val sizes = mutableMapOf<String, MutableList<Pair<String, Int?>>>()
        while (idx < terminalOutput.size) {
            val opt = terminalOutput[idx]
            if (opt.startsWith("$ cd")) {
                val newCwd = opt.replace("$ cd ", "")
                if (newCwd == "/") {
                    cwd = "/"
                } else if (newCwd == "..") {
                    cwd = cwd.split("/").dropLast(1).joinToString("/")
                    if (cwd == "") {
                        cwd = "/"
                    }
                } else {
                    cwd = if (cwd == "/") "/$newCwd" else "$cwd/$newCwd"
                }
                idx++
            } else if (opt.startsWith("$ ls")) {
                sizes.putIfAbsent(cwd, mutableListOf())
                var j = idx + 1
                while (j < terminalOutput.size && !terminalOutput[j].startsWith("$")) {
                    val lsOpt = terminalOutput[j]
                    if (lsOpt.startsWith("dir")) {
                        sizes[cwd]!!.add(
                            Pair(
                                "$cwd/${lsOpt.replace("dir ", "")}".replace("//", "/"),
                                null
                            )
                        )
                    } else {
                        val (size, fileName) = lsOpt.split(" ")
                        sizes[cwd]!!.add(Pair("$cwd/$fileName".replace("//", "/"), size.toInt()))
                    }
                    j++
                }
                idx = j
            } else {
                error("Should not be here")
            }
        }
        return sizes.mapValues { e -> computeSize(e.key, sizes) }
    }

    private fun computeSize(folder: String, tree: MutableMap<String, MutableList<Pair<String, Int?>>>): Int =
        tree[folder]!!.sumOf { if (it.second == null) computeSize(it.first, tree) else it.second!! }
}

fun main() {
    val day = Day7()
    println(day.runPartOne())
    println(day.runPartTwo())
}
