package internal

import (
	"errors"
	"fmt"

	"adventofcode/pkg/aocutils"
	shared2019 "adventofcode/pkg/shared/2019"
)

var directions = map[rune]aocutils.Point{
	'^': {X: 0, Y: -1},
	'v': {X: 0, Y: 1},
	'<': {X: -1, Y: 0},
	'>': {X: 1, Y: 0},
}

var turns = map[aocutils.Point]map[aocutils.Point]string{
	{X: 0, Y: -1}: { // up
		{X: -1, Y: 0}: "L",
		{X: 1, Y: 0}:  "R",
	},
	{X: 0, Y: 1}: { // down
		{X: 1, Y: 0}:  "L",
		{X: -1, Y: 0}: "R",
	},
	{X: -1, Y: 0}: { // left
		{X: 0, Y: 1}:  "L",
		{X: 0, Y: -1}: "R",
	},
	{X: 1, Y: 0}: { // right
		{X: 0, Y: -1}: "L",
		{X: 0, Y: 1}:  "R",
	},
}

func BuildMap(program *shared2019.IntcodeProgram) (map[aocutils.Point]rune, aocutils.Point, aocutils.Point) {
	if err := program.Run(-1); err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
		panic(err)
	}

	y := 0
	x := 0
	space := map[aocutils.Point]rune{}
	position := aocutils.Point{}
	var line []rune
	var direction aocutils.Point
	isMapBuilt := false
	for _, output := range program.Outputs {
		if output == 10 {
			y++
			if x == 0 {
				// It means an empty line, we are done reading the map
				isMapBuilt = true
			}
			x = 0
			fmt.Println(string(line))
			line = []rune{}
		} else {
			character := rune(output)
			line = append(line, character)
			if !isMapBuilt {
				point := aocutils.Point{X: x, Y: y}
				space[point] = character
				if space[point] != '.' && space[point] != '#' {
					position = point
					direction = directions[space[point]]
				}
			}
			x++
		}
	}

	return space, position, direction
}

func BuildPath(space map[aocutils.Point]rune, position aocutils.Point, direction aocutils.Point) []string {
	seen := map[aocutils.Point]int{}
	var path []string
	currentStepCount := 0
	var turn string
	for {
		seen[position]++
		previousPos := position
		nextPos := position.Add(direction)
		// We accept to go on already seen positions in the current direction (this is when we will have loops)
		if space[nextPos] == '#' {
			position = nextPos
			currentStepCount++
			continue
		}

		for _, newDirection := range directions {
			if newDirection == direction {
				continue
			}
			neighbour := position.Add(newDirection)
			// turn to new direction, the seen condition prevents from going back and infinitely turning
			if space[neighbour] == '#' && seen[neighbour] == 0 {
				if currentStepCount > 0 {
					path = append(path, fmt.Sprintf("%s%d", turn, currentStepCount))
				}
				turn = turns[direction][newDirection]
				direction = newDirection
				position = neighbour
				currentStepCount = 1
				break
			}
		}

		if position == previousPos {
			break
		}
	}

	path = append(path, fmt.Sprintf("%s%d", turn, currentStepCount))

	return path
}

func CalculateAlignmentParameters(space map[aocutils.Point]rune) int {
	alignmentSum := 0
	for point, char := range space {
		if char == '#' && isIntersection(space, point) {
			alignmentSum += point.X * point.Y
		}
	}
	return alignmentSum
}

func isIntersection(space map[aocutils.Point]rune, point aocutils.Point) bool {
	for _, d := range directions {
		if space[point.Add(d)] != '#' {
			return false
		}
	}
	return true
}
