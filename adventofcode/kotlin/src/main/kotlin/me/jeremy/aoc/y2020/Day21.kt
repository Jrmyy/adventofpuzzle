package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day21 : Day<List<Day21.Food>, Int> {

    data class Food(
        val ingredients: List<String>,
        val allergens: List<String> = listOf()
    )

    override fun runPartOne(): Int {
        val foods = getInput()
        val allergenIngredientMapping = getAllergenIngredientMapping(foods)
        return foods
            .flatMap { it.ingredients }
            .toSet()
            .subtract(allergenIngredientMapping.values)
            .sumOf {
                foods.flatMap { it.ingredients }.count { ing -> ing == it }
            }
    }

    override fun runPartTwo(): Int {
        val foods = getInput()
        val allergenIngredientMapping = getAllergenIngredientMapping(foods)
        println(
            allergenIngredientMapping.toList()
                .sortedBy { it.first }
                .joinToString(",") { it.second }
        )
        return 0
    }

    override fun getInput(): List<Food> = AOCUtils.getDayInput(2020, 21).map {
        val parts = it.split(" (contains ")
        val ingredients = parts.first().split(" ")
        val allergens = if (parts.size == 2) {
            parts.last().dropLast(1).split(", ")
        } else {
            listOf()
        }
        Food(ingredients, allergens)
    }

    private fun getAllergenIngredientMapping(foods: List<Food>): Map<String, String> {
        val allergenIngredientMapping = mutableMapOf<String, MutableList<String>>()
        val allergensSize = foods.flatMap { it.allergens }.distinct().size
        while (
            allergenIngredientMapping.size != allergensSize ||
            allergenIngredientMapping.values.any { it.size > 1 }
        ) {
            foods.forEach { food ->
                if (food.allergens.isNotEmpty()) {
                    food.allergens.forEach { allergen ->
                        if (allergen !in allergenIngredientMapping) {
                            allergenIngredientMapping[allergen] = food.ingredients.toMutableList()
                        } else {
                            allergenIngredientMapping[allergen] = food.ingredients.toSet().intersect(
                                allergenIngredientMapping[allergen]!!
                            ).toList().toMutableList()
                            if (allergenIngredientMapping[allergen]!!.size == 1) {
                                allergenIngredientMapping.forEach { mapping ->
                                    if (mapping.key != allergen) {
                                        mapping.value.remove(allergenIngredientMapping[allergen]!![0])
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return allergenIngredientMapping.mapValues { it.value.first() }
    }
}

fun main() {
    val day = Day21()
    println(day.runPartOne())
    println(day.runPartTwo())
}
