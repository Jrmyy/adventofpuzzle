package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day8 : Day<List<Pair<List<String>, List<String>>>, Int> {

    data class DigitBorders(
        val t: Char,
        val tl: Char,
        val tr: Char,
        val m: Char,
        val bl: Char,
        val br: Char,
        val b: Char
    )

    data class DigitBordersPuzzle(
        val t: MutableList<Char> = mutableListOf(),
        val tl: MutableList<Char> = mutableListOf(),
        val tr: MutableList<Char> = mutableListOf(),
        val m: MutableList<Char> = mutableListOf(),
        val bl: MutableList<Char> = mutableListOf(),
        val br: MutableList<Char> = mutableListOf(),
        val b: MutableList<Char> = mutableListOf()
    ) {
        fun toDigitBorders(): DigitBorders =
            DigitBorders(
                t.first(),
                tl.first(),
                tr.first(),
                m.first(),
                bl.first(),
                br.first(),
                b.first()
            )
    }

    override fun runPartOne(): Int =
        getInput()
            .flatMap { it.second }
            .count { listOf(2, 3, 4, 7).contains(it.length) }

    override fun runPartTwo(): Int =
        getInput().sumOf { getOutput(it) }

    override fun getInput(): List<Pair<List<String>, List<String>>> =
        AOCUtils.getDayInput(2021, 8).map {
            it.split(" | ").let { p ->
                Pair(p.first().split(" "), p.last().split(" "))
            }
        }

    private fun getOutput(line: Pair<List<String>, List<String>>): Int {
        val possibleBorders = DigitBordersPuzzle()
        val (ipt, opt) = line
        val one = ipt.first { it.length == 2 }
        val seven = ipt.first { it.length == 3 }

        // Top border is obtained by diff between 7 and 1
        possibleBorders.t.add(seven.toSet().minus(one.toSet()).first())
        // We add the top right and bottom right alternatives
        possibleBorders.tr.addAll(one.toList())
        possibleBorders.br.addAll(one.toList())

        // top left and middle alternatives are obtained by diff between 4 and 7
        val four = ipt.first { it.length == 4 }
        possibleBorders.tl.addAll(four.toSet().minus(seven.toSet()))
        possibleBorders.m.addAll(four.toSet().minus(seven.toSet()))

        // bottom is obtained because only diff between 5 and 7
        val three = ipt.first { it.length == 5 && it.toList().containsAll(seven.toList()) }
        val b = three.toSet().minus(seven.toSet()).filter { !possibleBorders.m.contains(it) }
        assert(b.size == 1)
        possibleBorders.b.add(b.first())

        // Top left is obtained because it is the only diff between 9 and 3 (we can also find middle thanks to 4)
        val nine = ipt.first { it.length == 6 && it.toList().containsAll(three.toList()) }
        possibleBorders.tl.clear()
        possibleBorders.tl.add(nine.toSet().minus(three.toSet()).first())
        possibleBorders.m.remove(possibleBorders.tl.first())

        // Now that we have top, top left, middle and bottom we can find bottom right thanks to 5
        // (and also top right thanks to 1)
        val five = ipt.first {
            it.length == 5 && it.toList().containsAll(
                listOf(
                    possibleBorders.t.first(),
                    possibleBorders.tl.first(),
                    possibleBorders.m.first(),
                    possibleBorders.b.first()
                )
            )
        }
        val br = five.toList().toSet().minus(
            setOf(
                possibleBorders.t.first(),
                possibleBorders.tl.first(),
                possibleBorders.m.first(),
                possibleBorders.b.first()
            )
        )
        assert(br.size == 1)
        possibleBorders.br.clear()
        possibleBorders.br.add(br.first())
        possibleBorders.tr.remove(possibleBorders.br.first())

        // We are now only missing bottom right, so we use 8 for that
        val eight = ipt.first { it.length == 7 }
        val bl = eight.toList().toSet().minus(
            listOf(
                possibleBorders.t,
                possibleBorders.tl,
                possibleBorders.tr,
                possibleBorders.m,
                possibleBorders.br,
                possibleBorders.b
            ).flatten().toSet()
        )
        assert(bl.size == 1)
        possibleBorders.bl.add(bl.first())
        val digitBorders = possibleBorders.toDigitBorders()

        val possibleOutputs = mapOf(
            setOf(
                digitBorders.t,
                digitBorders.tl,
                digitBorders.tr,
                digitBorders.br,
                digitBorders.bl,
                digitBorders.b
            ) to 0,
            setOf(
                digitBorders.tr,
                digitBorders.br
            ) to 1,
            setOf(
                digitBorders.t,
                digitBorders.tr,
                digitBorders.m,
                digitBorders.bl,
                digitBorders.b
            ) to 2,
            setOf(
                digitBorders.t,
                digitBorders.tr,
                digitBorders.m,
                digitBorders.br,
                digitBorders.b
            ) to 3,
            setOf(
                digitBorders.tr,
                digitBorders.m,
                digitBorders.tl,
                digitBorders.br
            ) to 4,
            setOf(
                digitBorders.t,
                digitBorders.tl,
                digitBorders.m,
                digitBorders.br,
                digitBorders.b
            ) to 5,
            setOf(
                digitBorders.t,
                digitBorders.tl,
                digitBorders.m,
                digitBorders.br,
                digitBorders.bl,
                digitBorders.b
            ) to 6,
            setOf(
                digitBorders.t,
                digitBorders.tr,
                digitBorders.br
            ) to 7,
            setOf(
                digitBorders.t,
                digitBorders.tl,
                digitBorders.tr,
                digitBorders.m,
                digitBorders.br,
                digitBorders.bl,
                digitBorders.b
            ) to 8,
            setOf(
                digitBorders.t,
                digitBorders.tl,
                digitBorders.tr,
                digitBorders.m,
                digitBorders.br,
                digitBorders.b
            ) to 9,
        ).mapKeys { it.key.sorted() }

        return opt.map { possibleOutputs[it.toList().toSet().sorted()]!! }.joinToString("").toInt()
    }
}

fun main() {
    val day = Day8()
    println(day.runPartOne())
    println(day.runPartTwo())
}
