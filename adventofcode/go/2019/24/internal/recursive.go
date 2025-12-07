package internal

import (
	"maps"

	"adventofcode/pkg/aocutils"
)

type RecursiveSpace struct {
	grid    Grid
	minutes int
}

func NewRecursiveSpace(levelZeroInitialSpace Grid, minutes int) *RecursiveSpace {
	levelZeroGrid := maps.Clone(levelZeroInitialSpace)
	levelZeroGrid[aocutils.Point{X: 2, Y: 2, Z: 0}] = '?'

	maxZ := minutes

	grid := Grid{}

	// Prefill space
	for z := -(maxZ + 1); z <= maxZ+1; z++ {
		if z == 0 {
			maps.Insert(grid, maps.All(levelZeroGrid))
		} else {
			for y := 0; y < maxY; y++ {
				for x := 0; x < maxX; x++ {
					r := '.'
					if x == 2 && y == 2 {
						r = '?'
					}
					grid[aocutils.Point{X: x, Y: y, Z: z}] = r
				}
			}
		}
	}

	return &RecursiveSpace{minutes: minutes, grid: grid}
}

func (recursiveSpace *RecursiveSpace) Simulate() int {
	for m := 1; m <= recursiveSpace.minutes; m++ {
		recursiveSpace.grid = simulate(recursiveSpace)
	}

	return recursiveSpace.countBugs()
}

func (recursiveSpace *RecursiveSpace) getGrid() Grid {
	return recursiveSpace.grid
}

func (recursiveSpace *RecursiveSpace) getNeighbours(p aocutils.Point) []aocutils.Point {
	if p.X == 2 && p.Y == 2 {
		return []aocutils.Point{}
	}

	var neighbours []aocutils.Point

	// look left
	if p.X == 0 {
		// looking out
		neighbours = append(neighbours, aocutils.Point{X: 1, Y: 2, Z: p.Z - 1})
	} else if p.X == 3 && p.Y == 2 {
		// looking in
		for inY := 0; inY < maxY; inY++ {
			neighbours = append(neighbours, aocutils.Point{X: 4, Y: inY, Z: p.Z + 1})
		}
	} else {
		neighbours = append(neighbours, aocutils.Point{X: p.X - 1, Y: p.Y, Z: p.Z})
	}

	// look right
	if p.X == 4 {
		// looking out
		neighbours = append(neighbours, aocutils.Point{X: 3, Y: 2, Z: p.Z - 1})
	} else if p.X == 1 && p.Y == 2 {
		// looking in
		for inY := 0; inY < maxY; inY++ {
			neighbours = append(neighbours, aocutils.Point{X: 0, Y: inY, Z: p.Z + 1})
		}
	} else {
		neighbours = append(neighbours, aocutils.Point{X: p.X + 1, Y: p.Y, Z: p.Z})
	}

	// look up
	if p.Y == 0 {
		// looking out
		neighbours = append(neighbours, aocutils.Point{X: 2, Y: 1, Z: p.Z - 1})
	} else if p.X == 2 && p.Y == 3 {
		// looking in
		for inX := 0; inX < maxX; inX++ {
			neighbours = append(neighbours, aocutils.Point{X: inX, Y: 4, Z: p.Z + 1})
		}
	} else {
		neighbours = append(neighbours, aocutils.Point{X: p.X, Y: p.Y - 1, Z: p.Z})
	}

	// look down
	if p.Y == 4 {
		// looking out
		neighbours = append(neighbours, aocutils.Point{X: 2, Y: 3, Z: p.Z - 1})
	} else if p.X == 2 && p.Y == 1 {
		// looking in
		for inX := 0; inX < maxX; inX++ {
			neighbours = append(neighbours, aocutils.Point{X: inX, Y: 0, Z: p.Z + 1})
		}
	} else {
		neighbours = append(neighbours, aocutils.Point{X: p.X, Y: p.Y + 1, Z: p.Z})
	}

	return neighbours
}

func (recursiveSpace *RecursiveSpace) countBugs() int {
	count := 0
	for _, state := range recursiveSpace.grid {
		if state == '#' {
			count++
		}
	}
	return count
}
