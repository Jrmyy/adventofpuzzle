package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne(ipt []string) int {
	return countTrailheads(ipt, false)
}

func runPartTwo(ipt []string) int {
	return countTrailheads(ipt, true)
}

func countTrailheads(ipt []string, allowMultipleTrails bool) int {
	mountain, starts, summits := parseInput(ipt)
	cnt := 0
	for _, s := range starts {
		for _, d := range summits {
			cnt += countPaths(mountain, s, d, allowMultipleTrails)
		}
	}
	return cnt
}

func parseInput(ipt []string) (map[aocutils.Point]int, []aocutils.Point, []aocutils.Point) {
	mountain := map[aocutils.Point]int{}
	starts := make([]aocutils.Point, 0, len(ipt)*len(ipt))
	summits := make([]aocutils.Point, 0, len(ipt)*len(ipt))
	for y, l := range ipt {
		for x, c := range l {
			i := aocutils.MustStringToInt(string(c))
			p := aocutils.Point{X: x, Y: y}
			mountain[p] = i
			if i == 0 {
				starts = append(starts, p)
			}
			if i == 9 {
				summits = append(summits, p)
			}
		}
	}
	return mountain, starts, summits
}

func countPaths(mountain map[aocutils.Point]int, s, d aocutils.Point, allowMultipleTrails bool) int {
	visited := map[aocutils.Point]bool{}
	pathsCount := []int{0}
	countPathsRecursive(mountain, s, d, allowMultipleTrails, visited, pathsCount)
	return pathsCount[0]
}

func countPathsRecursive(
	mountain map[aocutils.Point]int,
	s aocutils.Point,
	d aocutils.Point,
	allowMultipleTrails bool,
	visited map[aocutils.Point]bool,
	currentCount []int,
) {
	visited[s] = true

	if s == d {
		currentCount[0]++
	}

	i := mountain[s]
	for _, n := range s.Neighbours2D(false) {
		if j, ok := mountain[n]; ok && j-i == 1 {
			if v, ok := visited[n]; !ok || (allowMultipleTrails && !v) {
				countPathsRecursive(mountain, n, d, allowMultipleTrails, visited, currentCount)
			}
		}
	}

	visited[s] = false
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
