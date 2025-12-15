package main

import (
	"embed"
	"fmt"

	"adventofcode/2015/02/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func computeRequiredResources(presents []internal.Present, getResource func(present internal.Present) int) int {
	totalResources := 0
	for _, p := range presents {
		totalResources += getResource(p)
	}
	return totalResources
}

func parseInput(lines []string) []internal.Present {
	presents := make([]internal.Present, len(lines))
	for idx, line := range lines {
		presents[idx] = internal.NewPresent(line)
	}
	return presents
}

func runPartOne(presents []internal.Present) int {
	return computeRequiredResources(
		presents,
		func(present internal.Present) int {
			return present.WrappingPaper()
		},
	)
}

func runPartTwo(presents []internal.Present) int {
	return computeRequiredResources(
		presents,
		func(present internal.Present) int {
			return present.Ribbon()
		},
	)
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	presents := parseInput(ipt)
	fmt.Println(runPartOne(presents))
	fmt.Println(runPartTwo(presents))
}
