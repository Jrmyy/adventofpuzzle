package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var directions = map[string]aocutils.Point{
	"W": {X: -1, Y: 0},
	"E": {X: 1, Y: 0},
	"N": {X: 0, Y: -1},
	"S": {X: 0, Y: 1},
}

func runPartOne(r string) int {
	start := aocutils.Point{X: 0, Y: 0}
	graph := aocutils.Graph[aocutils.Point]{}
	explore(r, start, graph)
	destinations := map[aocutils.Point]bool{}
	for s, ds := range graph {
		destinations[s] = true
		for d := range ds {
			destinations[d] = true
		}
	}
	pathSize := 0
	dist, _ := graph.Dijkstra(start)
	for _, n := range dist {
		if n > pathSize {
			pathSize = n
		}
	}
	return pathSize
}

func runPartTwo(r string) int {
	start := aocutils.Point{X: 0, Y: 0}
	graph := aocutils.Graph[aocutils.Point]{}
	explore(r, start, graph)
	destinations := map[aocutils.Point]bool{}
	for s, ds := range graph {
		destinations[s] = true
		for d := range ds {
			destinations[d] = true
		}
	}
	numRooms := 0
	dist, _ := graph.Dijkstra(start)
	for _, n := range dist {
		if n >= 1000 {
			numRooms++
		}
	}
	return numRooms
}

func explore(r string, start aocutils.Point, graph aocutils.Graph[aocutils.Point]) {
	curr := start
	i := 0
	for i < len(r) {
		c := string(r[i])
		if c == "(" {
			opened := 1
			j := i + 1
			for opened > 0 {
				if r[j] == '(' {
					opened++
				} else if r[j] == ')' {
					opened--
				}
				j++
			}
			explore(r[i+1:j-1], curr, graph)
			i = j
		} else if c == "|" {
			curr = start
			i++
		} else {
			next := curr.Add(directions[c])
			if _, ok := graph[curr]; !ok {
				graph[curr] = aocutils.Edges[aocutils.Point]{}
			}
			graph[curr][next] = 1
			curr = next
			i++
		}
	}
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	r := ipt[0]
	cleanR := r[1 : len(r)-1]
	fmt.Println(runPartOne(cleanR))
	fmt.Println(runPartTwo(cleanR))
}
