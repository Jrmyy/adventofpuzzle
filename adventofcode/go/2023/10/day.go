package main

import (
	"embed"
	"fmt"
	"math"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var directions = map[string]string{
	"0,-1": "N",
	"0,1":  "S",
	"1,0":  "E",
	"-1,0": "W",
}

func repr(x, y int) string {
	return fmt.Sprintf("%v,%v", x, y)
}

func isConnectable(b, adj, direction string) bool {
	if adj == "S" {
		return true
	}
	if adj == "." {
		return false
	}
	switch b {
	case "|":
		switch direction {
		case "N":
			return slices.Contains([]string{"|", "F", "7"}, adj)
		case "S":
			return slices.Contains([]string{"|", "L", "J"}, adj)
		default:
			return false
		}
	case "-":
		switch direction {
		case "W":
			return slices.Contains([]string{"-", "L", "F"}, adj)
		case "E":
			return slices.Contains([]string{"-", "J", "7"}, adj)
		default:
			return false
		}
	case "7":
		switch direction {
		case "W":
			return slices.Contains([]string{"-", "L", "F"}, adj)
		case "S":
			return slices.Contains([]string{"|", "L", "J"}, adj)
		default:
			return false
		}
	case "J":
		switch direction {
		case "W":
			return slices.Contains([]string{"-", "L", "F"}, adj)
		case "N":
			return slices.Contains([]string{"|", "F", "7"}, adj)
		default:
			return false
		}
	case "F":
		switch direction {
		case "E":
			return slices.Contains([]string{"-", "J", "7"}, adj)
		case "S":
			return slices.Contains([]string{"|", "J", "L"}, adj)
		default:
			return false
		}
	case "L":
		switch direction {
		case "E":
			return slices.Contains([]string{"-", "J", "7"}, adj)
		case "N":
			return slices.Contains([]string{"|", "F", "7"}, adj)
		default:
			return false
		}
	default:
		panic("Should not happen")
	}
}

func findAdjacent(pipes [][]string, x, y int, loop []string) [][]int {
	alternatives := [][]int{
		{1, 0},
		{-1, 0},
		{0, 1},
		{0, -1},
	}
	adjacent := make([][]int, 0, 4)
	for _, alt := range alternatives {
		xx, yy := x+alt[0], y+alt[1]
		if !slices.Contains(
			loop,
			repr(xx, yy),
		) && yy >= 0 && yy < len(pipes) && xx >= 0 && xx < len(pipes[0]) && isConnectable(
			pipes[y][x],
			pipes[yy][xx],
			directions[repr(alt[0], alt[1])],
		) {
			adjacent = append(adjacent, []int{xx, yy})
		}
	}
	return adjacent
}

func findLoop(pipes [][]string) []string {
	start := []int{-1, -1}
	for y := range pipes {
		for x := range pipes[y] {
			if pipes[y][x] == "S" {
				start = []int{x, y}
			}
		}
	}
	for _, c := range []string{"7", "J", "F", "L", "|", "-"} {
		pipes[start[1]][start[0]] = c
		adjacent := findAdjacent(pipes, start[0], start[1], []string{})
		if len(adjacent) == 2 {
			break
		}
	}
	loop := []string{repr(start[0], start[1])}
	curr := slices.Clone(start)
	for true {
		adjacent := findAdjacent(pipes, curr[0], curr[1], loop)
		if len(adjacent) == 0 {
			break
		}
		if len(adjacent) >= 1 {
			curr = adjacent[0]
			loop = append(loop, repr(curr[0], curr[1]))
		}
	}
	return loop
}

func runPartOne(ipt [][]string) int {
	loop := findLoop(ipt)
	return int(math.Ceil(float64(len(loop)) / 2))
}

func runPartTwo(ipt [][]string) int {
	// Basically what is inside odd "|" are inside parts
	// But first we need to clean up the lines meaning
	// What is not on the loop is a dot (.)
	// | => |
	// - => _
	// L7 => |_
	// FJ => _|
	// After that alone L or J => |
	// After that alone F or 7 => _
	// Then we just remove double || and _ and we get the number of dots
	loop := findLoop(ipt)
	for y := range ipt {
		for x := range ipt[y] {
			r := repr(x, y)
			if !slices.Contains(loop, r) {
				ipt[y][x] = "."
			}
		}
	}
	boundaries := map[int][]int{}
	for _, l := range loop {
		parts := strings.Split(l, ",")
		x := aocutils.MustStringToInt(parts[0])
		y := aocutils.MustStringToInt(parts[1])
		_, ok := boundaries[y]
		if !ok {
			boundaries[y] = []int{len(ipt[y]), 0}
		}
		if x < boundaries[y][0] {
			boundaries[y][0] = x
		}
		if x > boundaries[y][1] {
			boundaries[y][1] = x
		}
	}
	ins := 0
	for y, bx := range boundaries {
		miX, maX := bx[0], bx[1]
		l := strings.Join(ipt[y][miX:maX+1], "")
		l = strings.Replace(l, "-", "_", -1)
		l = strings.Replace(l, "L7", "|_", -1)
		l = strings.Replace(l, "FJ", "_|", -1)
		l = strings.Replace(l, "L", "|", -1)
		l = strings.Replace(l, "J", "|", -1)
		l = strings.Replace(l, "7", "_", -1)
		l = strings.Replace(l, "F", "_", -1)
		l = strings.Replace(l, "_", "", -1)
		l = strings.Replace(l, "||", "", -1)
		vert := 0
		for _, c := range strings.Split(l, "") {
			if c == "|" {
				vert++
			} else if c == "." && vert%2 == 1 {
				ins++
			}
		}
	}
	return ins
}

func parseInput() [][]string {
	lines := aocutils.MustGetDayInput(inputFile)
	grid := make([][]string, len(lines))
	for idx, line := range lines {
		grid[idx] = strings.Split(line, "")
	}
	return grid
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	ipt = parseInput()
	fmt.Println(runPartTwo(ipt))
}
