package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type point struct {
	X int
	Y int
}

type trail struct {
	Visited bool
	Type    byte
}

var directions = []point{
	{0, -1},
	{0, 1},
	{1, 0},
	{-1, 0},
}

var currMax int

func computeMaximumSteps(grid map[point]*trail, start point, end point, visited int) {
	var directionsToInspect []point
	switch grid[start].Type {
	case '.':
		directionsToInspect = directions
	case 'v':
		directionsToInspect = []point{{X: 0, Y: 1}}
	case '>':
		directionsToInspect = []point{{X: 1, Y: 0}}
	default:
		panic("should not happen")
	}
	grid[start].Visited = true
	for _, dir := range directionsToInspect {
		ns := point{X: start.X + dir.X, Y: start.Y + dir.Y}
		if ns == end {
			if visited > currMax {
				fmt.Println(visited)
			}
			currMax = max(visited, currMax)
			grid[start].Visited = false
			return
		}

		v, ok := grid[ns]
		if !ok || v.Visited {
			continue
		}

		if v.Type != '#' {
			computeMaximumSteps(grid, ns, end, visited+1)
		}
	}
	grid[start].Visited = false
}

func findLongestHike(ipt map[point]*trail, my int) int {
	currMax = 0
	start := point{Y: 0}
	end := point{Y: my - 1}
	for p, t := range ipt {
		if p.Y == 0 && t.Type == '.' {
			start.X = p.X
		}
		if p.Y == my-1 && t.Type == '.' {
			end.X = p.X
		}
	}
	computeMaximumSteps(ipt, start, end, 1)
	return currMax
}

func runPartOne(ipt map[point]*trail, my int) int {
	return findLongestHike(ipt, my)
}

func runPartTwo(ipt map[point]*trail, my int) int {
	for _, v := range ipt {
		if v.Type == 'v' || v.Type == '>' {
			v.Type = '.'
		}
	}
	return findLongestHike(ipt, my)
}

func parseInput() (map[point]*trail, int) {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = map[point]*trail{}
	for y, line := range lines {
		for x, b := range []byte(line) {
			ipt[point{X: x, Y: y}] = &trail{
				Visited: false,
				Type:    b,
			}
		}
	}
	return ipt, len(lines)
}

func main() {
	ipt, my := parseInput()
	fmt.Println(runPartOne(ipt, my))
	fmt.Println(runPartTwo(ipt, my))
}
