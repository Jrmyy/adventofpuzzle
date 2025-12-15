package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var directions = map[rune]aocutils.Point{
	'>': {X: 1, Y: 0},
	'<': {X: -1, Y: 0},
	'^': {X: 0, Y: -1},
	'v': {X: 0, Y: 1},
}

func deliverPresents(allPath string, distributorsCount int) int {
	currentPositions := make([]aocutils.Point, distributorsCount)

	uniquePositions := aocutils.Set[aocutils.Point]{}
	uniquePositions.Add(aocutils.Point{})

	for idx, direction := range allPath {
		distributorIdx := idx % distributorsCount
		currentPositions[distributorIdx] = currentPositions[distributorIdx].Add(directions[direction])
		uniquePositions.Add(currentPositions[distributorIdx])
	}

	return len(uniquePositions)
}

func runPartOne(path string) int {
	return deliverPresents(path, 1)
}

func runPartTwo(path string) int {
	return deliverPresents(path, 2)
}

func main() {
	path := aocutils.MustGetDayInput(inputFile)[0]
	fmt.Println(runPartOne(path))
	fmt.Println(runPartTwo(path))
}
