package main

import (
	"embed"
	"errors"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
	shared2019 "adventofcode/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne() int {
	tiles := map[aocutils.Point]int64{}
	program := shared2019.NewIntcodeProgram(parseInput(), []int64{})
	err := program.Run(-1)
	if err != nil {
		panic(err)
	}

	for i := 0; i < len(program.Outputs)/3; i++ {
		x := int(program.Outputs[3*i])
		y := int(program.Outputs[3*i+1])
		tileId := program.Outputs[3*i+2]
		tiles[aocutils.Point{X: x, Y: y}] = tileId
	}

	cntBlocks := 0
	for _, tileId := range tiles {
		if tileId == 2 {
			cntBlocks++
		}
	}
	return cntBlocks
}

func runPartTwo() int64 {
	memory := parseInput()
	memory[0] = 2 // set to free play

	var currentScore int64 = 0
	paddle := aocutils.Point{X: 0, Y: 0}
	ball := aocutils.Point{X: 0, Y: 0}

	program := shared2019.NewIntcodeProgram(memory, []int64{0})
	for {
		err := program.Run(3)
		if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
			panic(err)
		}

		outputs := program.Outputs
		if len(outputs) == 0 {
			break
		}

		if outputs[0] == -1 && outputs[1] == 0 {
			currentScore = outputs[2]
		} else if outputs[2] == 3 {
			paddle = aocutils.Point{X: int(outputs[0]), Y: int(outputs[1])}
		} else if outputs[2] == 4 {
			ball = aocutils.Point{X: int(outputs[0]), Y: int(outputs[1])}
		}

		if paddle.X > ball.X {
			program.ResetInputs([]int64{-1})
		} else if paddle.X < ball.X {
			program.ResetInputs([]int64{1})
		} else {
			program.ResetInputs([]int64{0})
		}
		program.ClearOutputs()
	}

	return currentScore
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
