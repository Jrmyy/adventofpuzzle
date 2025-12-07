package main

import (
	"embed"
	"fmt"
	"math"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type Node struct {
	Point     aocutils.Point
	Direction aocutils.Point
}

var directions = []aocutils.Point{
	{X: 0, Y: -1}, {X: 0, Y: 1}, {X: 1, Y: 0}, {X: -1, Y: 0},
}

func getGraph(ipt []string) (aocutils.Graph[Node], Node, aocutils.Point) {
	var points []aocutils.Point
	var start Node
	var endPosition aocutils.Point
	for y, line := range ipt {
		for x, c := range line {
			p := aocutils.Point{X: x, Y: y}
			if c != '#' {
				points = append(points, p)
			}
			if c == 'S' {
				start = Node{Point: p, Direction: aocutils.Point{X: 1, Y: 0}}
			}
			if c == 'E' {
				endPosition = p
			}
		}
	}

	g := aocutils.Graph[Node]{}
	for _, p := range points {
		for _, d := range directions {
			node := Node{Point: p, Direction: d}
			e := aocutils.Edges[Node]{}
			for _, dd := range directions {
				if d != dd {
					e[Node{Point: p, Direction: dd}] = 1000
				}
			}
			neighbour := node.Point.Add(d)
			if slices.Contains(points, neighbour) {
				e[Node{Point: neighbour, Direction: d}] = 1
			}
			g[node] = e
		}
	}

	return g, start, endPosition
}

func runPartOne(ipt []string) int {
	g, start, endPosition := getGraph(ipt)
	dist, _ := g.Dijkstra(start)
	m := math.MaxInt
	for n, d := range dist {
		if n.Point == endPosition {
			m = min(m, d)
		}
	}
	return m
}

func runPartTwo(ipt []string) int {
	g, start, endPosition := getGraph(ipt)
	dist, prev := g.Dijkstra(start)

	m := math.MaxInt
	var toVisit []Node
	for n, d := range dist {
		if n.Point == endPosition {
			if d < m {
				m = d
				toVisit = []Node{n}
			} else if d == m {
				toVisit = append(toVisit, n)
			}
		}
	}

	criticalPoints := map[aocutils.Point]bool{}
	for len(toVisit) > 0 {
		n := toVisit[0]
		criticalPoints[n.Point] = true
		toVisit = toVisit[1:]
		toVisit = append(toVisit, prev[n]...)
	}

	return len(criticalPoints)
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
