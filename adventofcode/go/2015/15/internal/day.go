package internal

import (
	"maps"
	"regexp"
	"slices"

	"adventofcode/pkg/aocutils"
)

var ingredientRegex = regexp.MustCompile("^(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)$")

type Ingredient struct {
	Name       string
	Capacity   int
	Durability int
	Flavor     int
	Texture    int
	Calories   int
}

var maxTeaspoons = 100

type Recipe struct {
	Proportions map[Ingredient]int
	TotalUsed   int
}

func (recipe Recipe) ComputeScore(shouldMatchCalories bool) int {
	totalUsed := 0
	totalCalories := 0

	totalCapacity := 0
	totalDurability := 0
	totalFlavor := 0
	totalTexture := 0

	for ingredient, teaspoons := range recipe.Proportions {
		totalUsed += teaspoons
		totalCalories += teaspoons * ingredient.Calories

		totalCapacity += teaspoons * ingredient.Capacity
		totalDurability += teaspoons * ingredient.Durability
		totalFlavor += teaspoons * ingredient.Flavor
		totalTexture += teaspoons * ingredient.Texture
	}

	if totalUsed != 100 {
		return 0
	}

	if totalCalories != 500 && shouldMatchCalories {
		return 0
	}

	if totalCapacity < 0 || totalDurability < 0 || totalFlavor < 0 || totalTexture < 0 {
		return 0
	}

	return totalCapacity * totalDurability * totalFlavor * totalTexture
}

func findMaximum(currentRecipe Recipe, remainingIngredients []Ingredient, shouldMatchCalories bool) int {
	if len(remainingIngredients) == 1 {
		currentRecipe.Proportions[remainingIngredients[0]] = maxTeaspoons - currentRecipe.TotalUsed
		return currentRecipe.ComputeScore(shouldMatchCalories)
	}

	maxValue := 0
	for i := 0; i <= maxTeaspoons-currentRecipe.TotalUsed; i++ {
		newProportions := maps.Clone(currentRecipe.Proportions)
		newProportions[remainingIngredients[0]] = i
		newRecipe := Recipe{
			Proportions: newProportions,
			TotalUsed:   currentRecipe.TotalUsed + i,
		}
		maxValue = max(findMaximum(newRecipe, remainingIngredients[1:], shouldMatchCalories), maxValue)
	}

	return maxValue
}

func FindOptimalRecipeScore(ingredients []Ingredient, shouldMatchCalories bool) int {
	return findMaximum(
		Recipe{Proportions: map[Ingredient]int{}, TotalUsed: 0},
		slices.Clone(ingredients),
		shouldMatchCalories,
	)
}

func NewIngredient(description string) Ingredient {
	matches := ingredientRegex.FindStringSubmatch(description)[1:]
	return Ingredient{
		Name:       matches[0],
		Capacity:   aocutils.MustStringToInt(matches[1]),
		Durability: aocutils.MustStringToInt(matches[2]),
		Flavor:     aocutils.MustStringToInt(matches[3]),
		Texture:    aocutils.MustStringToInt(matches[4]),
		Calories:   aocutils.MustStringToInt(matches[5]),
	}
}
