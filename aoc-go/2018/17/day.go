package main

import (
	"embed"
	"fmt"
	"math"
	"regexp"

	"adventofcode-go/2018/17/internal"
	"adventofcode-go/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) (internal.Ground, aocutils.Point) {
	regex := regexp.MustCompile(`\d+`)

	clays := make([][4]int, len(lines))
	for idx, line := range lines {
		matches := regex.FindAllString(line, -1)
		fixed := aocutils.MustStringToInt(matches[0])
		minRange := aocutils.MustStringToInt(matches[1])
		maxRange := aocutils.MustStringToInt(matches[2])
		if line[0] == 'x' {
			clays[idx] = [4]int{fixed, fixed, minRange, maxRange}
		} else {
			clays[idx] = [4]int{minRange, maxRange, fixed, fixed}
		}
	}

	minX, maxX, minY, maxY := math.MaxInt, math.MinInt, math.MaxInt, math.MinInt
	for _, clay := range clays {
		minX = min(minX, clay[0])
		maxX = max(maxX, clay[1])
		minY = min(minY, clay[2])
		maxY = max(maxY, clay[3])
	}

	grid := make([][]rune, maxY+1)
	for y := range grid {
		grid[y] = make([]rune, maxX-minX+2)
		for x := range grid[y] {
			grid[y][x] = '.'
		}
	}

	for _, clay := range clays {
		for x := clay[0]; x <= clay[1]; x++ {
			for y := clay[2]; y <= clay[3]; y++ {
				grid[y][x-minX+1] = '#'
			}
		}
	}

	spring := aocutils.Point{X: 500 - minX + 1, Y: 0}
	grid[spring.Y][spring.X] = '+'
	return internal.Ground{
		Grid: grid,
		MinX: minX,
		MinY: minY,
		MaxX: maxX,
		MaxY: maxY,
	}, spring
}

func runPartOne(lines []string) int {
	ground, spring := parseInput(lines)
	ground.Flow(spring, 0)
	return ground.CountFlooded()
}

func runPartTwo(lines []string) int {
	ground, spring := parseInput(lines)
	ground.Flow(spring, 0)
	return ground.CountSettled()
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
