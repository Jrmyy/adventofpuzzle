package main

import (
	"embed"
	"fmt"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseGraph(ipt []string) (aocutils.Graph[aocutils.Point], aocutils.Point) {
	var points []aocutils.Point
	var start aocutils.Point
	for y, line := range ipt {
		for x, c := range line {
			p := aocutils.Point{X: x, Y: y}
			if c != '#' {
				points = append(points, p)
			}
			if c == 'S' {
				start = p
			}
		}
	}

	g := aocutils.Graph[aocutils.Point]{}
	for _, p := range points {
		e := aocutils.Edges[aocutils.Point]{}
		for _, n := range p.Neighbours2D(false) {
			if slices.Contains(points, n) {
				e[n] = 1
			}
		}
		g[p] = e
	}

	return g, start
}

func getCheatsCount(ipt []string, allowedCheatDuration int) int {
	g, s := parseGraph(ipt)
	dist, _ := g.Dijkstra(s)
	cnt := 0
	for p, d := range dist {
		for pp, dd := range dist {
			if p != pp {
				cheatDist := p.Dist(pp)
				realDist := d - dd
				if cheatDist <= allowedCheatDuration && realDist-cheatDist >= 100 {
					cnt++
				}
			}
		}
	}
	return cnt
}

func runPartOne(ipt []string) int {
	return getCheatsCount(ipt, 2)
}

func runPartTwo(ipt []string) int {
	return getCheatsCount(ipt, 20)
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
