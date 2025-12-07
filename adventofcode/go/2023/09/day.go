package main

import (
	"embed"
	"fmt"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func predictSensor(sensor [][]int, extrapolateFunc func(steps [][]int) int) int {
	sum := 0
	for _, sv := range sensor {
		l := slices.Clone(sv)
		var steps = [][]int{l}
		for slices.ContainsFunc(
			l, func(i int) bool {
				return i != 0
			},
		) {
			newL := make([]int, len(l)-1)
			for i := range newL {
				newL[i] = l[i+1] - l[i]
			}
			l = newL
			steps = append(steps, l)
		}
		sum += extrapolateFunc(steps)
	}
	return sum
}

func runPartOne(sensor [][]int) int {
	return predictSensor(
		sensor, func(steps [][]int) int {
			sl := len(steps) - 1
			steps[sl] = append(steps[sl], 0)
			for i := len(steps) - 2; i >= 0; i-- {
				prevLastValue := steps[i+1][len(steps[i+1])-1]
				currentLastValue := steps[i][len(steps[i])-1]
				steps[i] = append(steps[i], prevLastValue+currentLastValue)
			}
			return steps[0][len(steps[0])-1]
		},
	)
}

func runPartTwo(sensor [][]int) int {
	return predictSensor(
		sensor, func(steps [][]int) int {
			sl := len(steps) - 1
			steps[sl] = append([]int{0}, steps[sl]...)
			for i := len(steps) - 2; i >= 0; i-- {
				nextFirstValue := steps[i+1][0]
				currentFirstValue := steps[i][0]
				steps[i] = append([]int{currentFirstValue - nextFirstValue}, steps[i]...)
			}
			return steps[0][0]
		},
	)
}

func parseInput() [][]int {
	lines := aocutils.MustGetDayInput(inputFile)
	sensor := make([][]int, len(lines))
	for idx, line := range lines {
		parts := strings.Split(line, " ")
		l := make([]int, len(parts))
		for i, j := range parts {
			l[i] = aocutils.MustStringToInt(j)
		}
		sensor[idx] = l
	}
	return sensor
}

func main() {
	sensor := parseInput()
	fmt.Println(runPartOne(sensor))
	fmt.Println(runPartTwo(sensor))
}
