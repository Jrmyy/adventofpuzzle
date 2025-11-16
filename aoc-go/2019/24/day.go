package main

import (
	"embed"
	"fmt"
	"math"

	"adventofcode-go/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne() int {
	seen := map[string]bool{}
	space, maxX, maxY := parseInput()
	for {
		hash := toHash(space, maxX, maxY)
		if _, ok := seen[hash]; ok {
			fmt.Println(hash)
			return biodiversityRate(hash)
		}
		seen[hash] = true
		space = simulate(space)
	}
}

func parseInput() (map[aocutils.Point]rune, int, int) {
	lines := aocutils.MustGetDayInput(inputFile)
	space := map[aocutils.Point]rune{}
	for y, line := range lines {
		for x, r := range line {
			space[aocutils.Point{X: x, Y: y}] = r
		}
	}
	return space, len(lines[0]) - 1, len(lines) - 1
}

func toHash(space map[aocutils.Point]rune, maxX, maxY int) string {
	spaceArray := make([][]rune, maxY+1)
	for y := 0; y <= maxY; y++ {
		spaceArray[y] = make([]rune, maxX+1)
	}
	hash := ""
	for point, state := range space {
		spaceArray[point.Y][point.X] = state
	}
	for _, row := range spaceArray {
		hash += string(row)
	}
	return hash
}

func simulate(space map[aocutils.Point]rune) map[aocutils.Point]rune {
	newSpace := map[aocutils.Point]rune{}
	for point, state := range space {
		neighbors := point.Neighbours2D(false)
		numBugs := 0
		for _, neighbor := range neighbors {
			if space[neighbor] == '#' {
				numBugs++
			}
		}
		if state == '.' && (numBugs == 1 || numBugs == 2) {
			newSpace[point] = '#'
		} else if state == '#' && numBugs != 1 {
			newSpace[point] = '.'
		} else {
			newSpace[point] = state
		}
	}
	return newSpace
}

func biodiversityRate(repr string) int {
	rate := 0
	for x, r := range repr {
		if r == '#' {
			rate += int(math.Pow(2, float64(x)))
		}
	}
	return rate
}

func main() {
	fmt.Println(runPartOne())
}
