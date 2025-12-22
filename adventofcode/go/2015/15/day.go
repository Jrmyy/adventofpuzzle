package main

import (
	"embed"
	"fmt"

	"adventofcode/2015/15/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) []internal.Ingredient {
	ingredients := make([]internal.Ingredient, len(lines))
	for idx, line := range lines {
		ingredients[idx] = internal.NewIngredient(line)
	}
	return ingredients
}

func runPartOne(ingredients []internal.Ingredient) int {
	return internal.FindOptimalRecipeScore(ingredients, false)
}

func runPartTwo(ingredients []internal.Ingredient) int {
	return internal.FindOptimalRecipeScore(ingredients, true)
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	ingredients := parseInput(ipt)
	fmt.Println(runPartOne(ingredients))
	fmt.Println(runPartTwo(ingredients))
}
