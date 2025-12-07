package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/2025/05/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) (internal.InventorySystem, []int) {
	var system internal.InventorySystem
	idx := 0
	for _, line := range lines {
		if strings.TrimSpace(line) == "" {
			break
		}
		parts := strings.Split(line, "-")
		system = append(
			system,
			internal.FreshIngredientRange{aocutils.MustStringToInt(parts[0]), aocutils.MustStringToInt(parts[1])},
		)
		idx++
	}

	var ingredients []int
	for _, line := range lines[idx+1:] {
		ingredients = append(ingredients, aocutils.MustStringToInt(line))
	}

	return system, ingredients
}

func runPartOne(system internal.InventorySystem, ingredients []int) int {
	return system.CountAvailableFreshIngredients(ingredients)
}

func runPartTwo(system internal.InventorySystem) int {
	return system.CountAllPossibleFreshIngredientsInSystem()
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	system, ingredients := parseInput(ipt)
	fmt.Println(runPartOne(system, ingredients))
	fmt.Println(runPartTwo(system))
}
