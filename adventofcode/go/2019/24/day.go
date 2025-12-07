package main

import (
	"embed"
	"fmt"

	"adventofcode/2019/24/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput() internal.Grid {
	lines := aocutils.MustGetDayInput(inputFile)
	space := map[aocutils.Point]rune{}
	for y, line := range lines {
		for x, r := range line {
			space[aocutils.Point{X: x, Y: y, Z: 0}] = r
		}
	}
	return space
}

func runPartOne(grid internal.Grid) int {
	space := internal.NewSimpleSpace(grid)
	return space.Simulate()
}

func runPartTwo(grid internal.Grid) int {
	space := internal.NewRecursiveSpace(grid, 200)
	return space.Simulate()
}

func main() {
	grid := parseInput()
	fmt.Println(runPartOne(grid))
	fmt.Println(runPartTwo(grid))
}
