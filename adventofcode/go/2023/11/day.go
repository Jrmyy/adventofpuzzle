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

func getGalaxiesDistances(ipt [][]string, emptySizeCount int) int {
	var emptyY []int
	var emptyX []int
	for y := range ipt {
		if !slices.Contains(ipt[y], "#") {
			emptyY = append(emptyY, y)
		}
	}
	for x := range ipt[0] {
		col := make([]string, len(ipt[0]))
		for y := range ipt {
			col = append(col, ipt[y][x])
		}
		if !slices.Contains(col, "#") {
			emptyX = append(emptyX, x)
		}
	}
	s := float64(0)
	var galaxies [][]int
	for y := range ipt {
		for x := range ipt[y] {
			if ipt[y][x] == "#" {
				galaxies = append(galaxies, []int{x, y})
			}
		}
	}
	for gi, g := range galaxies {
		for _, gg := range galaxies[gi+1:] {
			dx := math.Abs(float64(gg[0] - g[0]))
			dy := math.Abs(float64(gg[1] - g[1]))
			empty := 0
			for x := min(gg[0], g[0]); x <= max(gg[0], g[0]); x++ {
				if slices.Contains(emptyX, x) {
					empty++
				}
			}
			for y := min(gg[1], g[1]); y <= max(gg[1], g[1]); y++ {
				if slices.Contains(emptyY, y) {
					empty++
				}
			}
			s += dx + dy + float64((emptySizeCount-1)*empty)

		}
	}
	return int(s)
}

func runPartOne(ipt [][]string) int {
	return getGalaxiesDistances(ipt, 2)
}

func runPartTwo(ipt [][]string) int {
	return getGalaxiesDistances(ipt, 1_000_000)
}

func parseInput() [][]string {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([][]string, len(lines))
	for idx, line := range lines {
		sp := strings.Split(line, "")
		ipt[idx] = sp
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
