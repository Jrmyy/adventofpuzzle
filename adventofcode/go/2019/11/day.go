package main

import (
	"embed"
	"errors"
	"fmt"
	"math"
	"strings"

	"adventofcode/pkg/aocutils"
	shared2019 "adventofcode/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne() int {
	tiles := paintPanel(0)
	return len(tiles)
}

func runPartTwo() int64 {
	tiles := paintPanel(1)
	minX, maxX, minY, maxY := math.MaxInt, math.MinInt, math.MaxInt, math.MinInt
	for point := range tiles {
		minX = min(minX, point.X)
		maxX = max(maxX, point.X)
		minY = min(minY, point.Y)
		maxY = max(maxY, point.Y)
	}
	for y := minY; y <= maxY; y++ {
		current := ""
		for x := maxX; x >= minX; x-- {
			point := aocutils.Point{X: x, Y: y}
			if tiles[point] == 1 {
				current += "#"
			} else {
				current += " "
			}
		}
		fmt.Println(current)
	}
	return 0
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

func paintPanel(startPanelColor int64) map[aocutils.Point]int64 {
	direction := aocutils.Point{X: 0, Y: -1}
	position := aocutils.Point{X: 0, Y: 0}
	tiles := map[aocutils.Point]int64{}
	program := shared2019.NewIntcodeProgram(parseInput(), []int64{startPanelColor})
	for {
		err := program.Run(2)
		if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
			panic(err)
		}
		outputs := program.Outputs
		if len(outputs) == 0 {
			break
		}

		tiles[position] = outputs[0]

		if outputs[1] == 0 {
			direction = direction.TurnLeft2D()
		} else if outputs[1] == 1 {
			direction = direction.TurnRight2D()
		} else {
			panic("unexpected turn output")
		}

		position = position.Add(direction)
		program.AddInputs([]int64{tiles[position]})
		program.ClearOutputs()
	}
	return tiles
}

func main() {
	fmt.Println(runPartOne())
	fmt.Println(runPartTwo())
}
