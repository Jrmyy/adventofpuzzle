package main

import (
	"embed"
	"fmt"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var directions = [][]int{
	{1, 0},
	{0, 1},
	{-1, 0},
	{0, -1},
}

type state struct {
	X    int
	Y    int
	Dx   int
	Dy   int
	Heat int
}

func computeMinimalHeat(ipt [][]int, minMoves int, maxMoves int) int {
	q := []state{
		{X: 0, Y: 0, Dx: 0, Dy: 0, Heat: 0},
	}
	tx := len(ipt[0]) - 1
	ty := len(ipt) - 1
	var seen []state
	for len(q) > 0 {
		s := q[len(q)-1]
		q = q[:len(q)-1]
		if s.X == tx && s.Y == ty {
			return s.Heat
		}
		ss := state{X: s.X, Y: s.Y, Dx: s.Dx, Dy: s.Dy, Heat: 0}
		if slices.Contains(seen, ss) {
			continue
		}
		seen = append(seen, ss)
		for _, dir := range directions {
			if dir[0] == s.Dx && dir[1] == s.Dy {
				continue
			}
			if dir[0] == -s.Dx && dir[1] == -s.Dy {
				continue
			}
			xx, yy, hh := s.X, s.Y, s.Heat
			for i := 1; i <= maxMoves; i++ {
				xx = xx + dir[0]
				yy = yy + dir[1]
				if xx >= 0 && xx <= tx && yy >= 0 && yy <= ty {
					hh += ipt[yy][xx]
					if i >= minMoves {
						q = append(q, state{X: xx, Y: yy, Dx: dir[0], Dy: dir[1], Heat: hh})
					}
				}
			}
		}
		// Replace by priority queue
		slices.SortFunc(
			q, func(a, b state) int {
				return b.Heat - a.Heat
			},
		)
	}
	panic("should not happen")
}

func runPartOne(ipt [][]int) int {
	return computeMinimalHeat(ipt, 1, 3)
}

func runPartTwo(ipt [][]int) int {
	return computeMinimalHeat(ipt, 4, 10)
}

func parseInput() [][]int {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([][]int, len(lines))
	for li, line := range lines {
		parts := strings.Split(line, "")
		blocks := make([]int, len(parts))
		for pi, p := range parts {
			blocks[pi] = aocutils.MustStringToInt(p)
		}
		ipt[li] = blocks
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
