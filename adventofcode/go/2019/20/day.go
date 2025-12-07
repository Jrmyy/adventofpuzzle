package main

import (
	"embed"
	"fmt"
	"os"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type Node struct {
	aocutils.Point
	Depth int
}

type Portal struct {
	OuterPosition aocutils.Point
	InnerPosition aocutils.Point
}

var maxRecursion = 30

func parseMazeP1(ipt []string) (aocutils.Graph[aocutils.Point], aocutils.Point, aocutils.Point) {
	graph := aocutils.Graph[aocutils.Point]{}
	portalLinks := map[string][]aocutils.Point{}

	for y, line := range ipt {
		for x, c := range line {
			point := aocutils.Point{X: x, Y: y}
			if c >= 'A' && c <= 'Z' {
				for _, neighbour := range point.Neighbours2D(false) {
					if neighbour.Y >= 0 && neighbour.Y < len(ipt) && neighbour.X >= 0 && neighbour.X < len(line) {
						neighbourChar := ipt[neighbour.Y][neighbour.X]
						if neighbourChar >= 'A' && neighbourChar <= 'Z' {
							portalName := fmt.Sprintf("%s%s", string(c), string(neighbourChar))
							portalLinks[portalName] = append(portalLinks[portalName], point, neighbour)
							break
						}
					}
				}
			}
		}
	}

	portals := map[string][]aocutils.Point{}
	reversedPortals := map[aocutils.Point]string{}
	for portalName, portalPoints := range portalLinks {
		connectedPoints := make([]aocutils.Point, 0, len(portalPoints))
		for _, portalPoint := range portalPoints {
			for _, neighbour := range portalPoint.Neighbours2D(false) {
				if neighbour.Y >= 0 && neighbour.Y < len(ipt) && neighbour.X >= 0 && neighbour.X < len(ipt[neighbour.Y]) {
					if ipt[neighbour.Y][neighbour.X] == '.' && !slices.Contains(connectedPoints, portalPoint) {
						connectedPoints = append(connectedPoints, portalPoint)
						reversedPortals[portalPoint] = portalName
						break
					}
				}
			}
		}
		portals[portalName] = connectedPoints
	}

	var points []aocutils.Point
	for y, line := range ipt {
		for x, c := range line {
			point := aocutils.Point{X: x, Y: y}
			if _, ok := reversedPortals[point]; c == '.' || ok {
				points = append(points, point)
			}
		}
	}

	var start, end aocutils.Point

	for _, point := range points {
		edges := aocutils.Edges[aocutils.Point]{}
		defaultWeight := 1
		if _, ok := reversedPortals[point]; ok {
			defaultWeight = 0
		}
		for _, neighbour := range point.Neighbours2D(false) {
			if slices.Contains(points, neighbour) {
				if neighbour == portals["AA"][0] {
					start = point
				}
				if neighbour == portals["ZZ"][0] {
					end = point
				}
				weight := defaultWeight
				if _, ok := reversedPortals[neighbour]; ok {
					weight = 0
				}
				edges[neighbour] = weight
			}
		}
		if portalName, ok := reversedPortals[point]; ok {
			connectedPoints := portals[portalName]
			for _, connectedPoint := range connectedPoints {
				if connectedPoint != point {
					edges[connectedPoint] = 1
				}
			}
		}
		graph[point] = edges
	}

	return graph, start, end
}

func parseMazeP2(ipt []string) (aocutils.Graph[Node], Node, Node) {
	graph := aocutils.Graph[Node]{}
	portalLinks := map[string][]aocutils.Point{}

	for y, line := range ipt {
		for x, c := range line {
			point := aocutils.Point{X: x, Y: y}
			if c >= 'A' && c <= 'Z' {
				for _, neighbour := range point.Neighbours2D(false) {
					if neighbour.Y >= 0 && neighbour.Y < len(ipt) && neighbour.X >= 0 && neighbour.X < len(line) {
						neighbourChar := ipt[neighbour.Y][neighbour.X]
						if neighbourChar >= 'A' && neighbourChar <= 'Z' {
							portalName := fmt.Sprintf("%s%s", string(c), string(neighbourChar))
							portalLinks[portalName] = append(portalLinks[portalName], point, neighbour)
							break
						}
					}
				}
			}
		}
	}

	portals := map[string]Portal{}
	reversedPortals := map[aocutils.Point]string{}
	for portalName, portalPoints := range portalLinks {
		connectedPoints := make([]aocutils.Point, 0, len(portalPoints))
		for _, portalPoint := range portalPoints {
			for _, neighbour := range portalPoint.Neighbours2D(false) {
				if neighbour.Y >= 0 && neighbour.Y < len(ipt) && neighbour.X >= 0 && neighbour.X < len(ipt[neighbour.Y]) {
					if ipt[neighbour.Y][neighbour.X] == '.' && !slices.Contains(connectedPoints, portalPoint) {
						connectedPoints = append(connectedPoints, portalPoint)
						reversedPortals[portalPoint] = portalName
						break
					}
				}
			}
		}

		if len(connectedPoints) == 2 {
			isFirstOuter := connectedPoints[0].Y <= 1 ||
				connectedPoints[0].Y >= len(ipt)-2 ||
				connectedPoints[0].X <= 1 ||
				connectedPoints[0].X >= len(ipt[connectedPoints[0].Y])-2
			if isFirstOuter {
				portals[portalName] = Portal{InnerPosition: connectedPoints[1], OuterPosition: connectedPoints[0]}
			} else {
				portals[portalName] = Portal{InnerPosition: connectedPoints[0], OuterPosition: connectedPoints[1]}
			}
		} else if len(connectedPoints) == 1 {
			portals[portalName] = Portal{InnerPosition: connectedPoints[0], OuterPosition: connectedPoints[0]}
		} else {
			os.Exit(1)
		}
	}

	var points []aocutils.Point
	for y, line := range ipt {
		for x, c := range line {
			point := aocutils.Point{X: x, Y: y}
			if _, ok := reversedPortals[point]; c == '.' || ok {
				points = append(points, point)
			}
		}
	}

	var nodes []Node

	for _, point := range points {
		portalName, isPortal := reversedPortals[point]
		if isPortal {
			if portalName == "AA" || portalName == "ZZ" {
				continue
			} else {
				portal := portals[portalName]
				for depth := 0; depth <= maxRecursion; depth++ {
					nodes = append(nodes, Node{Point: portal.InnerPosition, Depth: depth})
				}
				for depth := 1; depth <= maxRecursion; depth++ {
					nodes = append(nodes, Node{Point: portal.OuterPosition, Depth: depth})
				}
			}
		} else {
			if ipt[point.Y][point.X] != '.' {
				os.Exit(1)
			}
			for depth := 0; depth <= maxRecursion; depth++ {
				nodes = append(nodes, Node{Point: point, Depth: depth})
			}
		}
	}

	var start, end Node

	for _, node := range nodes {
		edges := aocutils.Edges[Node]{}
		if portalName, ok := reversedPortals[node.Point]; ok {
			portal := portals[portalName]
			if portalName == "AA" || portalName == "ZZ" {
				os.Exit(1)
			}
			if node.Point == portal.InnerPosition {
				if node.Depth < maxRecursion {
					edges[Node{Point: portal.OuterPosition, Depth: node.Depth + 1}] = 1
				}
			} else if node.Point == portal.OuterPosition {
				if node.Depth == 0 {
					os.Exit(1)
				}
				edges[Node{Point: portal.InnerPosition, Depth: node.Depth - 1}] = 1
			} else {
				os.Exit(1)
			}
			for _, neighbour := range node.Neighbours2D(false) {
				neighbourNode := Node{Point: neighbour, Depth: node.Depth}
				if slices.Contains(nodes, neighbourNode) {
					edges[neighbourNode] = 0
				}
			}
		} else {
			for _, neighbour := range node.Neighbours2D(false) {
				neighbourNode := Node{Point: neighbour, Depth: node.Depth}
				if neighbour == portals["AA"].InnerPosition && node.Depth == 0 {
					start = node
				} else if neighbour == portals["ZZ"].InnerPosition && node.Depth == 0 {
					end = node
				}
				if slices.Contains(nodes, neighbourNode) {
					weight := 1
					if _, isNeighbourPortal := reversedPortals[neighbour]; isNeighbourPortal {
						weight = 0
					}
					edges[neighbourNode] = weight
				}
			}
		}
		graph[node] = edges
	}

	fmt.Println("creating nodes done")

	return graph, start, end
}

func run[T comparable](lines []string, mazeGeneratorFunc func([]string) (aocutils.Graph[T], T, T)) int {
	graph, start, end := mazeGeneratorFunc(lines)
	fmt.Println("Start: ", start)
	fmt.Println("End :", end)
	fmt.Println("Nodes :", len(graph))
	dist, _ := graph.Dijkstra(start)
	return dist[end]
}

func runPartOne(lines []string) int {
	return run[aocutils.Point](lines, parseMazeP1)
}

func runPartTwo(lines []string) int {
	return run[Node](lines, parseMazeP2)
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
