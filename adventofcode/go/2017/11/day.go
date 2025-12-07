package main

import (
	"embed"
	"fmt"
	"math"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func move(m string) (float64, float64) {
	switch m {
	case "n":
		return 0, -1
	case "s":
		return 0, 1
	case "nw":
		return -0.5, -0.5
	case "ne":
		return 0.5, -0.5
	case "sw":
		return -0.5, 0.5
	default:
		return 0.5, 0.5
	}
}

func runPartOne(ipt []string) int {
	x, y := 0.0, 0.0
	for _, m := range ipt {
		xx, yy := move(m)
		x += xx
		y += yy
	}
	res := math.Abs(x) + math.Abs(y)
	if math.Floor(res) != res {
		panic("Should be an int")
	}
	return int(res)
}

func runPartTwo(ipt []string) float64 {
	x, y, maxDist := 0.0, 0.0, 0.0
	for _, m := range ipt {
		xx, yy := move(m)
		x += xx
		y += yy
		maxDist = max(maxDist, math.Abs(x)+math.Abs(y))
	}
	return maxDist
}

func parseInput() []string {
	line := aocutils.MustGetDayInput(inputFile)[0]
	return strings.Split(line, ",")
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(fmt.Sprintf("%v", runPartTwo(ipt)))
}
