package main

import (
	"embed"
	"fmt"

	"adventofcode/2015/06/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

const gridSize = 1000

func parseInput(lines []string) []internal.Instruction {
	instructions := make([]internal.Instruction, len(lines))
	for idx, line := range lines {
		instructions[idx] = internal.NewInstruction(line)
	}
	return instructions
}

func prepareGrid() [][]int {
	grid := make([][]int, gridSize)
	for y := range grid {
		grid[y] = make([]int, gridSize)
	}
	return grid
}

func countTotalResult(grid [][]int) int {
	res := 0
	for _, row := range grid {
		for _, i := range row {
			res += i
		}
	}
	return res
}

func applyInstructions(
	instructions []internal.Instruction,
	unitaryApplyFunc func(instruction internal.Instruction, grid [][]int),
) int {
	grid := prepareGrid()
	for _, instruction := range instructions {
		unitaryApplyFunc(instruction, grid)
	}
	return countTotalResult(grid)
}

func runPartOne(instructions []internal.Instruction) int {
	return applyInstructions(
		instructions,
		func(instruction internal.Instruction, grid [][]int) {
			instruction.ApplyLightOperation(grid)
		},
	)
}

func runPartTwo(instructions []internal.Instruction) int {
	return applyInstructions(
		instructions,
		func(instruction internal.Instruction, grid [][]int) {
			instruction.ApplyBrightnessOperation(grid)
		},
	)
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	instructions := parseInput(ipt)
	fmt.Println(runPartOne(instructions))
	fmt.Println(runPartTwo(instructions))
}
