package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.pow

class Day14 : Day<List<Pair<List<Char>, List<Pair<Long, Long>>>>, Long> {
    override fun runPartOne(): Long {
        val operations = getInput()
        val currentResults = mutableMapOf<Long, Long>()
        operations.forEach {
            val (mask, instructions) = it
            instructions.forEach { that ->
                val (idx, value) = that
                val bitsValueWithMask = passValueThroughMaskPartOne(value, mask)
                val parsed = convertToLong(bitsValueWithMask)
                currentResults[idx] = parsed
            }
        }
        return currentResults.toList().map { that -> that.second }.sum()
    }

    override fun runPartTwo(): Long {
        val operations = getInput()
        val currentResults = mutableMapOf<Long, Long>()
        operations.forEach {
            val (mask, instructions) = it
            instructions.forEach { that ->
                val (idx, value) = that
                val addresses = passValueThroughMaskPartTwo(idx, mask)
                addresses.forEach { ad ->
                    currentResults[convertToLong(ad)] = value
                }
            }
        }
        return currentResults.toList().map { that -> that.second }.sum()
    }

    override fun getInput(): List<Pair<List<Char>, List<Pair<Long, Long>>>> {
        val lines = AOCUtils.getDayInput(2020, 14)
        var currentMask = listOf<Char>()
        val currentInstructions = mutableListOf<Pair<Long, Long>>()
        val operations = mutableListOf<Pair<List<Char>, List<Pair<Long, Long>>>>()
        val memRegex = Regex("^mem\\[(\\d+)] = (\\d+)\$")
        for (line in lines) {
            if (line.startsWith("mask = ")) {
                if (currentMask.isNotEmpty()) {
                    operations.add(
                        Pair(currentMask, currentInstructions.toList())
                    )
                    currentInstructions.clear()
                }
                currentMask = line.replace("mask = ", "").toList()
            } else {
                val groups = memRegex.find(line)!!.groups
                currentInstructions.add(
                    Pair(
                        groups[1]!!.value.toLong(),
                        groups[2]!!.value.toLong()
                    )
                )
            }
        }
        if (currentMask.isNotEmpty()) {
            operations.add(
                Pair(currentMask, currentInstructions.toList())
            )
        }
        return operations
    }

    private fun passValueThroughMaskPartTwo(value: Long, mask: List<Char>): List<List<Char>> {
        val converted = convertToBits(value).reversed()
        val throughMask = mask.reversed().mapIndexed { index, c ->
            when (c) {
                '1' -> '1'
                '0' -> if (index < converted.size) {
                    converted[index]
                } else {
                    '0'
                }
                else -> 'X'
            }
        }.reversed()
        return computeAllPossibilities(throughMask)
    }

    private fun computeAllPossibilities(chars: List<Char>): List<List<Char>> {
        val firstXIdx = chars.indexOfFirst { it == 'X' }
        return if (firstXIdx >= 0) {
            computeAllPossibilities(chars.subList(firstXIdx + 1, chars.size)).map {
                listOf(
                    chars.subList(0, firstXIdx) + listOf('0') + it,
                    chars.subList(0, firstXIdx) + listOf('1') + it
                )
            }.flatten()
        } else {
            listOf(chars)
        }
    }

    private fun convertToBits(value: Long): List<Char> {
        val bits = mutableListOf<Char>()
        var currentValue = value
        while (currentValue > 0) {
            bits.add((currentValue % 2).toString()[0])
            currentValue /= 2
        }
        return bits.reversed()
    }

    private fun convertToLong(value: List<Char>): Long =
        value.reversed().mapIndexed { idx, it ->
            (it.toString().toInt() * 2.toDouble().pow(idx.toDouble())).toLong()
        }.sum()

    private fun passValueThroughMaskPartOne(value: Long, mask: List<Char>): List<Char> {
        val converted = convertToBits(value).reversed()
        return mask.reversed().mapIndexed { index, c ->
            when (c) {
                '1' -> '1'
                '0' -> '0'
                else -> {
                    if (index < converted.size) {
                        converted[index]
                    } else {
                        '0'
                    }
                }
            }
        }.reversed()
    }
}

fun main() {
    val day = Day14()
    println(day.runPartOne())
    println(day.runPartTwo())
}
