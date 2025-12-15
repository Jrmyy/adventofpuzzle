package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var instructionMapping = map[rune]int{
	'(': 1,
	')': -1,
}

func moveAroundFloors(instructions string, stopCondition func(currentFloor int) bool) int {
	currentFloor := 0
	for idx, instruction := range instructions {
		currentFloor += instructionMapping[instruction]
		if stopCondition(currentFloor) {
			return idx + 1
		}
	}
	return currentFloor
}

func runPartOne(instructions string) int {
	return moveAroundFloors(
		instructions,
		func(currentFloor int) bool {
			return false
		},
	)
}

func runPartTwo(instructions string) int {
	return moveAroundFloors(
		instructions,
		func(currentFloor int) bool {
			return currentFloor == -1
		},
	)
}

func main() {
	instructions := aocutils.MustGetDayInput(inputFile)[0]
	fmt.Println(runPartOne(instructions))
	fmt.Println(runPartTwo(instructions))
}
