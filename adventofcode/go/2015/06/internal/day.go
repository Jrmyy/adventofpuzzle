package internal

import (
	"regexp"

	"adventofcode/pkg/aocutils"
)

type operation func(grid [][]int, x, y int)

var brightnessOperations = map[string]operation{
	"toggle": func(grid [][]int, x, y int) {
		grid[y][x] += 2
	},
	"turn on": func(grid [][]int, x, y int) {
		grid[y][x] += 1
	},
	"turn off": func(grid [][]int, x, y int) {
		grid[y][x] = max(grid[y][x]-1, 0)
	},
}

var lightOperations = map[string]operation{
	"toggle": func(grid [][]int, x, y int) {
		grid[y][x] = (grid[y][x] + 1) % 2
	},
	"turn on": func(grid [][]int, x, y int) {
		grid[y][x] = 1
	},
	"turn off": func(grid [][]int, x, y int) {
		grid[y][x] = 0
	},
}

var regex = regexp.MustCompile("^(toggle|turn on|turn off) (\\d+),(\\d+) through (\\d+),(\\d+)$")

type Instruction struct {
	startX int
	startY int
	endX   int
	endY   int
	opType string
}

func (instruction Instruction) ApplyLightOperation(grid [][]int) {
	instruction.operate(grid, lightOperations[instruction.opType])
}

func (instruction Instruction) ApplyBrightnessOperation(grid [][]int) {
	instruction.operate(grid, brightnessOperations[instruction.opType])
}

func (instruction Instruction) operate(grid [][]int, unitaryOperation operation) {
	for y := instruction.startY; y <= instruction.endY; y++ {
		for x := instruction.startX; x <= instruction.endX; x++ {
			unitaryOperation(grid, x, y)
		}
	}
}

func NewInstruction(line string) Instruction {
	matches := regex.FindStringSubmatch(line)[1:]
	return Instruction{
		startX: aocutils.MustStringToInt(matches[1]),
		startY: aocutils.MustStringToInt(matches[2]),
		endX:   aocutils.MustStringToInt(matches[3]),
		endY:   aocutils.MustStringToInt(matches[4]),
		opType: matches[0],
	}
}
