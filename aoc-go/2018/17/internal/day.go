package internal

import "adventofcode-go/pkg/aocutils"

type Ground struct {
	Grid [][]rune
	MinX int
	MaxX int
	MinY int
	MaxY int
}

func (ground Ground) Flow(from aocutils.Point, direction int) int {
	// It means above it is water, so we convert to flowing water
	if ground.Grid[from.Y][from.X] == '.' {
		ground.Grid[from.Y][from.X] = '|'
	}

	// We reached the end of the visible ground, we stop the recursion
	if from.Y == ground.MaxY {
		return -1
	}

	// We reached a clay, we stop the recursion
	if ground.Grid[from.Y][from.X] == '#' {
		return from.X
	}

	// If below it is also sand, we flow to that position until we reach a clay/settled water or the max depth
	if ground.Grid[from.Y+1][from.X] == '.' {
		ground.Flow(aocutils.Point{X: from.X, Y: from.Y + 1}, 0)
	}

	// If below is settled water or clay
	if ground.Grid[from.Y+1][from.X] == '~' || ground.Grid[from.Y+1][from.X] == '#' {
		// If we are already flowing to the left or right, we keep flowing until we reach a clay
		if direction != 0 {
			return ground.Flow(aocutils.Point{X: from.X + direction, Y: from.Y}, direction)
		}

		// Otherwise we try to expand to the left and right and settle the water
		leftX := ground.Flow(aocutils.Point{X: from.X - 1, Y: from.Y}, -1)
		rightX := ground.Flow(aocutils.Point{X: from.X + 1, Y: from.Y}, 1)
		if ground.Grid[from.Y][leftX] == '#' && ground.Grid[from.Y][rightX] == '#' {
			for fillX := leftX + 1; fillX < rightX; fillX++ {
				ground.Grid[from.Y][fillX] = '~'
			}
		}

		// We stop the recursion
		return -1
	}

	return from.X
}

func (ground Ground) CountFlooded() int {
	flooded := 0
	for y := ground.MinY; y <= ground.MaxY; y++ {
		for x := 0; x < len(ground.Grid[0]); x++ {
			state := ground.Grid[y][x]
			if state == '|' || state == '~' {
				flooded++
			}
		}
	}
	return flooded
}

func (ground Ground) CountSettled() int {
	settled := 0
	for y := ground.MinY; y <= ground.MaxY; y++ {
		for x := 0; x < len(ground.Grid[0]); x++ {
			if ground.Grid[y][x] == '~' {
				settled++
			}
		}
	}
	return settled
}
