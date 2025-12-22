package internal

import (
	"adventofcode/pkg/aocutils"
)

func initGrid(rawGrid []string) (grid map[aocutils.Point]bool, maxGridX, maxGridY int) {
	grid = map[aocutils.Point]bool{}
	for y, line := range rawGrid {
		for x, c := range line {
			grid[aocutils.Point{X: x, Y: y}] = c == '#'

		}
	}
	maxGridX = len(rawGrid[0]) - 1
	maxGridY = len(rawGrid) - 1
	return grid, maxGridX, maxGridY
}
