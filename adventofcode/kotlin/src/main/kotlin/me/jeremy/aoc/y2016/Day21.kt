package me.jeremy.aoc.y2016

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.util.*

class Day21 : Day<List<String>, String> {

    override fun runPartOne(): String {
        val password = "abcdefgh".toMutableList()
        val instructions = getInput()
        scramble(password, instructions)
        return password.joinToString("")
    }

    override fun runPartTwo(): String {
        val password = "fbgdceah".toMutableList()
        val instructions = getInput()
        val permutations = getAllPermutations(password)
        for (permutation in permutations) {
            val copy = permutation.toMutableList()
            scramble(copy, instructions)
            if (copy == password) {
                return permutation.joinToString("")
            }
        }
        error("Should have found solution")
    }

    override fun getInput(): List<String> = AOCUtils.getDayInput(2016, 21)

    private fun <T> getAllPermutations(input: List<T>): List<List<T>> {
        val permutations = mutableListOf<List<T>>()
        if (input.size == 1) {
            permutations.add(listOf(input.first()))
        } else {
            for ((idx, el) in input.withIndex()) {
                permutations.addAll(
                    getAllPermutations(
                        input.subList(0, idx) + input.subList(idx + 1, input.size)
                    ).map {
                        listOf(el) + it
                    }
                )
            }
        }
        return permutations
    }

    private fun scramble(password: MutableList<Char>, instructions: List<String>) {
        for (instruction in instructions) {
            if (instruction.startsWith("swap position")) {
                val (x, y) = "swap position (\\d+) with position (\\d+)".toRegex().find(instruction)!!.groupValues
                    .drop(1)
                    .map { it.toInt() }
                val tmp = password[x]
                password[x] = password[y]
                password[y] = tmp
            } else if (instruction.startsWith("swap letter")) {
                val (x, y) = "swap letter ([a-z]) with letter ([a-z])".toRegex().find(instruction)!!.groupValues
                    .drop(1)
                    .map { it.first() }
                val xC = password.mapIndexedNotNull { idx, c -> if (c == x) idx else null }
                val yC = password.mapIndexedNotNull { idx, c -> if (c == y) idx else null }
                for (xx in xC) password[xx] = y
                for (yy in yC) password[yy] = x
            } else if (instruction.startsWith("rotate based")) {
                val letter = instruction.replace("rotate based on position of letter ", "").first()
                val idxOf = password.indexOf(letter)
                Collections.rotate(password, 1 + idxOf + (if (idxOf >= 4) 1 else 0))
            } else if (instruction.startsWith("rotate")) {
                val (direction, steps) = "rotate (left|right) (\\d+) step".toRegex().find(instruction)!!.groupValues
                    .drop(1)
                Collections.rotate(password, steps.toInt() * if (direction == "left") -1 else 1)
            } else if (instruction.startsWith("reverse positions")) {
                val (x, y) = "reverse positions (\\d+) through (\\d+)".toRegex().find(instruction)!!.groupValues
                    .drop(1)
                    .map { it.toInt() }
                (0..(y - x) / 2).forEach {
                    val tmp = password[x + it]
                    password[x + it] = password[y - it]
                    password[y - it] = tmp
                }
            } else if (instruction.startsWith("move position")) {
                val (x, y) = "move position (\\d+) to position (\\d+)".toRegex().find(instruction)!!.groupValues
                    .drop(1)
                    .map { it.toInt() }
                password.add(y, password.removeAt(x))
            }
        }
    }
}

fun main() {
    val day = Day21()
    println(day.runPartOne())
    println(day.runPartTwo())
}
