package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max

class Day15 : Day<List<Day15.Ingredient>, Long> {

    data class Ingredient(
        val cap: Int,
        val dur: Int,
        val fla: Int,
        val tex: Int,
        val cal: Int
    )

    override fun runPartOne(): Long = bestRecipe()

    override fun runPartTwo(): Long = bestRecipe(500)

    override fun getInput(): List<Ingredient> = AOCUtils.getDayInput(2015, 15).map {
        val amounts = it.split(", ").map { p -> p.split(" ").last().toInt() }
        Ingredient(amounts[0], amounts[1], amounts[2], amounts[3], amounts[4])
    }

    private fun bestRecipe(calCondition: Long? = null): Long {
        val ingredients = getInput()
        return combinations(100, ingredients.size).mapNotNull { qty ->
            val qpi = qty.mapIndexed { idx, q -> Pair(ingredients[idx], q) }.toMap()
            val cal = calculateIngredientProperty(qpi) { e -> e.key.cal }
            if (calCondition != null && cal != calCondition) {
                null
            } else {
                val cap = calculateIngredientProperty(qpi) { e -> e.key.cap }
                val dur = calculateIngredientProperty(qpi) { e -> e.key.dur }
                val fla = calculateIngredientProperty(qpi) { e -> e.key.fla }
                val tex = calculateIngredientProperty(qpi) { e -> e.key.tex }
                cap * dur * fla * tex
            }
        }.maxOrNull()!!
    }

    private fun calculateIngredientProperty(qpi: Map<Ingredient, Int>, fn: (Map.Entry<Ingredient, Int>) -> Int) =
        max(qpi.map { e -> fn(e) * e.value.toLong() }.sum(), 0)

    private fun combinations(n: Int, k: Int): List<List<Int>> {
        if (k == 1) {
            return listOf(listOf(n))
        }
        if (n == 1) {
            return listOf(
                (0 until k).mapIndexed { i, _ -> if (i == 0) n else 0 }
            )
        }
        return (0..n).flatMap {
            combinations(n - it, k - 1).map { l ->
                listOf(it) + l
            }
        }
    }
}

fun main() {
    val day = Day15()
    println(day.runPartOne())
    println(day.runPartTwo())
}
