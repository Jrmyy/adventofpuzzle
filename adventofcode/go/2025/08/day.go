package main

import (
	"embed"
	"fmt"
	"math"
	"slices"
	"strings"

	"adventofcode/2025/08/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) *internal.Playground {
	boxes := make([]aocutils.Point, len(lines))
	for idx, line := range lines {
		parts := strings.Split(line, ",")
		boxes[idx] = aocutils.Point{
			X: aocutils.MustStringToInt(parts[0]),
			Y: aocutils.MustStringToInt(parts[1]),
			Z: aocutils.MustStringToInt(parts[2]),
		}
	}
	return internal.NewPlayground(boxes)
}

func runPartOne(playground *internal.Playground) int {
	circuits, _ := playground.CreateCircuits(playground.BoxesCount)

	circuitsPerSize := map[int]int{}
	for _, circuitIdx := range circuits {
		circuitsPerSize[circuitIdx]++
	}
	circuitsSize := make([]int, 0, len(circuitsPerSize))
	for _, circuitSize := range circuitsPerSize {
		circuitsSize = append(circuitsSize, circuitSize)
	}

	slices.SortFunc(circuitsSize, func(a, b int) int {
		return b - a
	})
	return circuitsSize[0] * circuitsSize[1] * circuitsSize[2]
}

func runPartTwo(playground *internal.Playground) int {
	_, lastMergedPair := playground.CreateCircuits(math.MaxInt)
	return lastMergedPair.First.X * lastMergedPair.Second.X
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	playground := parseInput(ipt)
	fmt.Println(runPartOne(playground))
	playground.ResetCircuits()
	fmt.Println(runPartTwo(playground))
}
