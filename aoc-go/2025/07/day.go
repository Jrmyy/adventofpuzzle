package main

import (
	"embed"
	"fmt"

	"adventofcode-go/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) ([][]rune, int) {
	manifold := make([][]rune, len(lines))
	var startX int
	for y, line := range lines {
		layer := make([]rune, len(line))
		for x, c := range line {
			if c == 'S' {
				startX = x
			}
			layer[x] = c
		}
		manifold[y] = layer
	}
	return manifold, startX
}

func countSplits(manifold [][]rune, startX int) int {
	splits := 0
	beams := map[int]bool{startX: true}
	for y := 1; y < len(manifold); y++ {
		spreadBeams := map[int]bool{}
		for beam := range beams {
			if beam < 0 || beam >= len(manifold[0]) {
				continue
			}
			if manifold[y][beam] == '^' {
				// If we reach a splitter, left and right become the new beams, and we found a split
				spreadBeams[beam-1] = true
				spreadBeams[beam+1] = true
				splits++
			} else {
				// If not, we keep going down for the same X
				spreadBeams[beam] = true
			}
		}
		beams = spreadBeams
	}
	return splits
}

func countTimelines(manifold [][]rune, startX int) int {
	timelines := map[int]int{startX: 1}

	for y := 1; y < len(manifold); y++ {
		spreadTimelines := map[int]int{}
		for particle := range timelines {
			if particle < 0 || particle >= len(manifold[0]) {
				continue
			}

			if manifold[y][particle] == '^' {
				// If we reach a splitter, we increase the number of left and right timelines by the current number
				// of timelines, representing a merge of possible timelines from the start to this position
				spreadTimelines[particle-1] += timelines[particle]
				spreadTimelines[particle+1] += timelines[particle]
			} else {
				// Otherwise we report the current number of timelines to the down position
				spreadTimelines[particle] += timelines[particle]
			}
		}
		timelines = spreadTimelines
	}

	totalTimelines := 0
	for _, particleTimelines := range timelines {
		totalTimelines += particleTimelines
	}
	return totalTimelines
}

func runPartOne(manifold [][]rune, startX int) int {
	return countSplits(manifold, startX)
}

func runPartTwo(manifold [][]rune, startX int) int {
	return countTimelines(manifold, startX)
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	manifold, startX := parseInput(ipt)
	fmt.Println(runPartOne(manifold, startX))
	fmt.Println(runPartTwo(manifold, startX))
}
