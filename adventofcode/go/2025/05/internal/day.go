package internal

import "slices"

type FreshIngredientRange [2]int

func (ir FreshIngredientRange) Contains(i int) bool {
	return ir[0] <= i && ir[1] >= i
}

func (ir FreshIngredientRange) Merge(other FreshIngredientRange) FreshIngredientRange {
	return FreshIngredientRange{min(ir[0], other[0]), max(ir[1], other[1])}
}

func (ir FreshIngredientRange) Overlaps(other FreshIngredientRange) bool {
	return other.Contains(ir[0]) || ir.Contains(other[0]) || other.Contains(ir[1]) || ir.Contains(other[1])
}

func (ir FreshIngredientRange) Size() int {
	return ir[1] - ir[0] + 1
}

type InventorySystem []FreshIngredientRange

func (inventorySystem InventorySystem) CountAvailableFreshIngredients(ingredients []int) int {
	cnt := 0
	for _, ingredient := range ingredients {
		if inventorySystem.isSpoiled(ingredient) {
			cnt++
		}
	}
	return cnt
}

func (inventorySystem InventorySystem) CountAllPossibleFreshIngredientsInSystem() int {
	inventorySystem.sort()

	reducedInventory := InventorySystem{}
	i := 0
	for i < len(inventorySystem) {
		reducedInterval := inventorySystem[i]
		j := i + 1
		for j < len(inventorySystem) {
			prevInterval := inventorySystem[j]
			if !reducedInterval.Overlaps(prevInterval) {
				break
			}
			reducedInterval = reducedInterval.Merge(prevInterval)
			j++
		}
		reducedInventory = append(reducedInventory, reducedInterval)
		i = j
	}

	cnt := 0
	for _, interval := range reducedInventory {
		cnt += interval.Size()
	}
	return cnt
}

func (inventorySystem InventorySystem) sort() {
	slices.SortFunc(inventorySystem, func(r1, r2 FreshIngredientRange) int {
		return r1[0] - r2[0]
	})
}

func (inventorySystem InventorySystem) isSpoiled(ingredient int) bool {
	for _, freshIngredientRange := range inventorySystem {
		if freshIngredientRange.Contains(ingredient) {
			return true
		}
	}
	return false
}
