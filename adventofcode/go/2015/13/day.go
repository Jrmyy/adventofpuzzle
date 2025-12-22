package main

import (
	"embed"
	"fmt"

	"adventofcode/2015/13/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) internal.SeatingArrangementAlgorithm {
	return internal.NewSeatingArrangementAlgorithm(lines)
}

func runPartOne(algorithm internal.SeatingArrangementAlgorithm) int {
	return algorithm.Optimize()
}

func runPartTwo(algorithm internal.SeatingArrangementAlgorithm) int {
	algorithm.AddNeutralGuest("me")
	return algorithm.Optimize()
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	algorithm := parseInput(lines)
	fmt.Println(runPartOne(algorithm))
	fmt.Println(runPartTwo(algorithm))
}
