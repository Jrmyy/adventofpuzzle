package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type instruction struct {
	Direction uint8
	Count     int
}

func (ins instruction) Execute(start int) (int, int) {
	direction := 1
	if ins.Direction == 'L' {
		direction = -1
	}
	value, passageByZero := start, 0
	for i := 1; i <= aocutils.Abs(ins.Count); i++ {
		value = (value + direction + 100) % 100
		if value == 0 {
			passageByZero++
		}
	}
	return value, passageByZero
}

func parseInput(lines []string) []instruction {
	instructions := make([]instruction, len(lines))
	for idx, line := range lines {
		instructions[idx] = instruction{Direction: line[0], Count: aocutils.MustStringToInt(line[1:])}
	}
	return instructions
}

func runPartOne(instructions []instruction) int {
	position := 50
	pointingAtZero := 0
	for _, ins := range instructions {
		position, _ = ins.Execute(position)
		if position == 0 {
			pointingAtZero++
		}
	}
	return pointingAtZero
}

func runPartTwo(instructions []instruction) int {
	position := 50
	stepsByZero := 0
	for _, ins := range instructions {
		newPosition, rotationStepsByZero := ins.Execute(position)
		stepsByZero += rotationStepsByZero
		position = newPosition
	}
	return stepsByZero
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	instructions := parseInput(ipt)
	fmt.Println(runPartOne(instructions))
	fmt.Println(runPartTwo(instructions))
}
