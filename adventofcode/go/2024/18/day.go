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

var memorySize = 70

func runEscape(lines []string, maxBytes int) int {
	fallenBytes := make([]aocutils.Point, maxBytes)
	for idx, line := range lines[:maxBytes] {
		coordinates := strings.Split(strings.TrimSpace(line), ",")
		fallenBytes[idx] = aocutils.Point{
			X: aocutils.MustStringToInt(coordinates[0]),
			Y: aocutils.MustStringToInt(coordinates[1]),
		}
	}

	g := aocutils.Graph[aocutils.Point]{}
	for y := 0; y <= memorySize; y++ {
		for x := 0; x <= memorySize; x++ {
			p := aocutils.Point{X: x, Y: y}
			e := aocutils.Edges[aocutils.Point]{}
			for _, n := range p.Neighbours2D(false) {
				if !slices.Contains(fallenBytes, n) && n.X >= 0 && n.X <= memorySize && n.Y >= 0 && n.Y <= memorySize {
					e[n] = 1
				}
			}
			g[p] = e
		}
	}

	dist, _ := g.Dijkstra(aocutils.Point{X: 0, Y: 0})
	return dist[aocutils.Point{X: 70, Y: 70}]
}

func runPartOne(lines []string) int {
	return runEscape(lines, 1024)
}

func runPartTwo(lines []string) string {
	for i := 1025; i <= len(lines); i++ {
		dist := runEscape(lines, i)
		if dist == math.MaxInt {
			return lines[i-1]
		}
	}
	return ""
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
