package internal

import (
	"adventofcode/pkg/aocutils"
)

const maxX = 5
const maxY = 5

type Grid map[aocutils.Point]rune
type Simulation interface {
	getNeighbours(p aocutils.Point) []aocutils.Point
	getGrid() Grid
	Simulate() int
}

func simulate[S Simulation](space S) Grid {
	newGrid := Grid{}
	grid := space.getGrid()
	for point, state := range grid {
		numBugs := 0
		for _, neighbour := range space.getNeighbours(point) {
			if grid[neighbour] == '#' {
				numBugs++
			}
		}
		if state == '.' && (numBugs == 1 || numBugs == 2) {
			newGrid[point] = '#'
		} else if state == '#' && numBugs != 1 {
			newGrid[point] = '.'
		} else {
			newGrid[point] = state
		}
	}
	return newGrid
}
