package main

import (
	"embed"
	"fmt"
	"os"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type Node struct {
	Position aocutils.Point
	Tool     string
}

var types = []string{"rocky", "wet", "narrow"}
var tools = map[string][]string{
	"rocky":  {"torch", "climbing gear"},
	"wet":    {"climbing gear", "neither"},
	"narrow": {"torch", "neither"},
}

func createCave(ipt []string, extended bool) (map[aocutils.Point]int, aocutils.Point) {
	depth := aocutils.MustStringToInt(strings.TrimPrefix(ipt[0], "depth: "))
	rawTarget := strings.Split(strings.TrimPrefix(ipt[1], "target: "), ",")
	target := aocutils.Point{X: aocutils.MustStringToInt(rawTarget[0]), Y: aocutils.MustStringToInt(rawTarget[1])}
	p0 := aocutils.Point{X: 0, Y: 0}

	erosions := map[aocutils.Point]int{}
	cave := map[aocutils.Point]int{}

	maxY := target.Y
	maxX := target.X
	if extended {
		maxY = maxY + 100
		maxX = maxX + 100
	}

	for y := 0; y <= maxY; y++ {
		for x := 0; x <= maxX; x++ {
			p := aocutils.Point{X: x, Y: y}
			var geologicIndex int
			if p == target || p == p0 {
				geologicIndex = 0
			} else if p.X == 0 {
				geologicIndex = p.Y * 48271
			} else if p.Y == 0 {
				geologicIndex = p.X * 16807
			} else {
				ex, ok := erosions[aocutils.Point{X: x - 1, Y: y}]
				if !ok {
					os.Exit(1)
				}
				ey, ok := erosions[aocutils.Point{X: x, Y: y - 1}]
				if !ok {
					os.Exit(1)
				}
				geologicIndex = ex * ey
			}
			erosion := (geologicIndex + depth) % 20183
			erosions[p] = erosion
			cave[p] = erosion % 3
		}
	}
	return cave, target
}

func runPartOne(ipt []string) int {
	cave, _ := createCave(ipt, false)
	totalRisk := 0
	for _, risk := range cave {
		totalRisk += risk
	}
	return totalRisk
}

func runPartTwo(ipt []string) int {
	cave, target := createCave(ipt, true)
	g := aocutils.Graph[Node]{}
	for p, t := range cave {
		regionType := types[t]
		nodeTools := tools[regionType]
		for idx, srcTool := range nodeTools {
			ns := Node{Position: p, Tool: srcTool}
			g[ns] = aocutils.Edges[Node]{
				Node{Position: p, Tool: nodeTools[(idx+1)%2]}: 7,
			}
			for _, np := range ns.Position.Neighbours2D(false) {
				if nt, ok := cave[np]; ok {
					neighbourRegionType := types[nt]
					if slices.Contains(tools[neighbourRegionType], srcTool) {
						g[ns][Node{Position: np, Tool: srcTool}] = 1
					}
				}
			}
		}
	}

	distances, _ := g.Dijkstra(Node{Position: aocutils.Point{X: 0, Y: 0}, Tool: "torch"})
	return distances[Node{Position: target, Tool: "torch"}]
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
