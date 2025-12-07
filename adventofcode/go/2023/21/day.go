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

var directions = [][]int{
	{0, 1},
	{0, -1},
	{-1, 0},
	{1, 0},
}

func mod(a, b int) int {
	if a < 0 {
		for a < 0 {
			a += b
		}
		return a
	}
	return a % b
}

func calculateGardenPlots(grid [][]byte, steps int) int {
	pos := map[point]int{}
	for y := range grid {
		for x := range grid[y] {
			if grid[y][x] == 'S' {
				pos[point{X: x, Y: y}] = 1
				grid[y][x] = '.'
			}
		}
	}
	mx, my := len(grid[0]), len(grid)
	for i := 1; i <= steps; i++ {
		newPos := map[point]int{}
		for p, _ := range pos {
			for _, dir := range directions {
				pp := point{X: p.X + dir[0], Y: p.Y + dir[1]}
				if grid[mod(pp.Y, my)][mod(pp.X, mx)] == '.' {
					newPos[pp] = 1
				}
			}
		}
		pos = newPos
	}
	cnt := 0
	for _, num := range pos {
		cnt += num
	}
	return cnt
}

func runPartOne() int {
	grid := parseInput()
	return calculateGardenPlots(grid, 64)
}

func runPartTwo() int {
	// We need 26501365 steps. Nevertheless, 26501365 / len(grid) (131) = 202300 with 65 as remainder
	// The starting point is the center of the grid
	// the row and column of the starting point have no obstacle
	// same goes for the diagonal of the midpoints of each edge
	// This means that it is going to grow as a diamond, therefore it can be defined as a quadratic formula
	// We need to calculate the solution for 65, 65 + 131 and 65 + 131 * 2, and then it makes the following formula
	// for 65 steps, f(0) = 3848
	// for 65 + 131 steps, f(1) = 34310
	// for 65 + 131*2 steps, f(2) = 95144
	// f(X) = 15186x^2 + 15276x + 3848
	// So now we only need to return f(202300)
	values := make([]int, 3)
	steps := 26501365
	grid := parseInput()
	x := steps / len(grid)
	r := steps % len(grid)
	for i := 0; i <= 2; i++ {
		grid = parseInput()
		values[i] = calculateGardenPlots(grid, r+i*len(grid))
	}
	return 15186*x*x + 15276*x + 3848
}

func parseInput() [][]byte {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([][]byte, len(lines))
	for idx, line := range lines {
		b := []byte(line)
		ipt[idx] = b
	}
	return ipt
}

func main() {
	fmt.Println(runPartOne())
	fmt.Println(runPartTwo())
}
