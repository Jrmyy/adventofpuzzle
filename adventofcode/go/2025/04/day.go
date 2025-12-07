package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type printingDepartment map[aocutils.Point]rune

func (department printingDepartment) GetAccessibleRolls() []aocutils.Point {
	accessiblePositions := make([]aocutils.Point, 0, len(department))
	for position, state := range department {
		if state != '@' {
			continue
		}
		cnt := 0
		for _, neighbour := range position.Neighbours2D(true) {
			if department[neighbour] == '@' {
				cnt++
			}
		}
		if cnt < 4 {
			accessiblePositions = append(accessiblePositions, position)
		}
	}
	return accessiblePositions
}

func (department printingDepartment) RemoveAccessibleRolls() int {
	accessible := department.GetAccessibleRolls()
	for _, position := range accessible {
		department[position] = '.'
	}
	return len(accessible)
}

func parseInput(lines []string) printingDepartment {
	space := printingDepartment{}
	for y, line := range lines {
		for x, r := range line {
			space[aocutils.Point{X: x, Y: y}] = r
		}
	}
	return space
}

func runPartOne(space printingDepartment) int {
	return len(space.GetAccessibleRolls())
}

func runPartTwo(space printingDepartment) int {
	totalRemoved := 0
	for {
		removed := space.RemoveAccessibleRolls()
		totalRemoved += removed
		if removed == 0 {
			break
		}
	}
	return totalRemoved
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	space := parseInput(ipt)
	fmt.Println(runPartOne(space))
	fmt.Println(runPartTwo(space))
}
