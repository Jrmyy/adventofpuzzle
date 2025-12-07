package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
	shared2019 "adventofcode/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne() int {
	impacted := 0
	for y := 0; y <= 49; y++ {
		for x := 0; x <= 49; x++ {
			if isBeamActive(x, y) {
				impacted++
			}
		}
	}
	return impacted
}

func runPartTwo() int {
	y := 0
	squareSize := 100
	for {
		x := findLeftEdge(y)
		if isBeamActive(x+squareSize-1, y-squareSize+1) {
			return x*10000 + y - squareSize + 1
		}
		y++
	}
}

func findLeftEdge(y int) int {
	left, right := 0, y
	for left < right {
		mid := (left + right) / 2
		if isBeamActive(mid, y) {
			right = mid
		} else {
			left = mid + 1
		}
	}
	return left
}

func isBeamActive(x, y int) bool {
	program := shared2019.NewIntcodeProgram(parseInput(), []int64{int64(x), int64(y)})
	err := program.Run(1)
	if err != nil {
		panic(err)
	}
	return program.Outputs[0] == 1
}

func parseInput() map[int]int64 {
	line := aocutils.MustGetDayInput(inputFile)[0]
	stringValues := strings.Split(line, ",")
	values := map[int]int64{}
	for idx := range stringValues {
		values[idx] = aocutils.MustStringToInt64(stringValues[idx])
	}
	return values
}

func main() {
	fmt.Println(runPartOne())
	fmt.Println(runPartTwo())
}
