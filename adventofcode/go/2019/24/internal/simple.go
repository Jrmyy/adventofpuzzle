package internal

import (
	"maps"
	"math"

	"adventofcode/pkg/aocutils"
)

type SimpleSpace struct {
	grid Grid
}

func NewSimpleSpace(initialGrid Grid) *SimpleSpace {
	return &SimpleSpace{grid: maps.Clone(initialGrid)}
}

func (simpleSpace *SimpleSpace) Simulate() int {
	seen := map[string]bool{}
	for {
		hash := simpleSpace.hash()
		if _, ok := seen[hash]; ok {
			return simpleSpace.biodiversityRate(hash)
		}
		seen[hash] = true
		simpleSpace.grid = simulate(simpleSpace)
	}
}

func (simpleSpace *SimpleSpace) getGrid() Grid {
	return simpleSpace.grid
}

func (simpleSpace *SimpleSpace) getNeighbours(p aocutils.Point) []aocutils.Point {
	return p.Neighbours2D(false)
}

func (simpleSpace *SimpleSpace) hash() string {
	spaceArray := make([][]rune, maxY)
	for y := 0; y < maxY; y++ {
		spaceArray[y] = make([]rune, maxX)
	}
	hash := ""
	for point, state := range simpleSpace.grid {
		spaceArray[point.Y][point.X] = state
	}
	for _, row := range spaceArray {
		hash += string(row)
	}
	return hash
}

func (simpleSpace *SimpleSpace) biodiversityRate(repr string) int {
	rate := 0
	for x, r := range repr {
		if r == '#' {
			rate += int(math.Pow(2, float64(x)))
		}
	}
	return rate
}
