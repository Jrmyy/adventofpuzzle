package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day7 : Day<Map<String, List<String>>, Int> {
    override fun runPartOne(): Int {
        val taskAndDependencies = getInput()
        val tasksCount = taskAndDependencies.size
        val doneTasks = mutableListOf<String>()
        while (doneTasks.size != tasksCount) {
            val task = taskAndDependencies
                .keys
                .filter { taskAndDependencies[it]!!.isEmpty() }
                .minOrNull() ?: error("No task available")
            doneTasks.add(task)
            taskAndDependencies.remove(task)
            taskAndDependencies.forEach { it.value.remove(task) }
        }
        println(doneTasks.joinToString(""))
        return 0
    }

    override fun runPartTwo(): Int {
        val taskAndDependencies = getInput()
        val tasksCount = taskAndDependencies.size
        val doneTasks = mutableListOf<String>()
        var totalDuration = 0
        val workers = mutableListOf(
            Pair("", 0),
            Pair("", 0),
            Pair("", 0),
            Pair("", 0),
            Pair("", 0)
        )
        while (doneTasks.size != tasksCount) {
            val tasks = taskAndDependencies
                .keys
                .filter {
                    taskAndDependencies[it]!!.isEmpty() && it !in workers.map { t -> t.first }
                }.sorted()
            tasks.forEach {
                val freeWorker = workers.indexOfFirst { w -> w.second == 0 }
                if (freeWorker >= 0) {
                    workers[freeWorker] = Pair(it, 60 + it.toCharArray()[0].code - 64)
                }
            }
            workers.forEachIndexed { idx, it ->
                if (it.second > 0) {
                    workers[idx] = Pair(it.first, it.second - 1)
                }
            }
            workers.forEachIndexed { idx, it ->
                if (it.second == 0 && it.first != "") {
                    doneTasks.add(it.first)
                    taskAndDependencies.remove(it.first)
                    taskAndDependencies.forEach { m -> m.value.remove(it.first) }
                    workers[idx] = Pair("", 0)
                }
            }
            totalDuration += 1
        }
        return totalDuration
    }

    override fun getInput(): MutableMap<String, MutableList<String>> {
        val taskAndDependencies = AOCUtils.getDayInput(2018, 7).map {
            val (parent, child) = Regex("Step (.+) must be finished before step (.+) can begin.")
                .find(it)!!.groupValues
                .drop(1)
            Pair(parent, child)
        }.fold(mutableMapOf<String, MutableList<String>>()) { acc, pair ->
            acc.putIfAbsent(pair.second, mutableListOf())
            acc[pair.second] = (acc[pair.second]!! + listOf(pair.first)).toMutableList()
            acc
        }
        taskAndDependencies.values.flatten().distinct().forEach {
            if (it !in taskAndDependencies) {
                taskAndDependencies[it] = mutableListOf()
            }
        }
        return taskAndDependencies
    }
}

fun main() {
    val day = Day7()
    println(day.runPartOne())
    println(day.runPartTwo())
}
