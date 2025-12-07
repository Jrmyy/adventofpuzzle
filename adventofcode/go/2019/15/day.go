package main

import (
	"embed"
	"fmt"
	"maps"
	"math"
	"strings"

	"adventofcode/pkg/aocutils"
	shared2019 "adventofcode/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

var directions = []aocutils.Point{
	{X: 0, Y: -1}, // north
	{X: 0, Y: 1},  // south
	{X: -1, Y: 0}, // west
	{X: 1, Y: 0},  // east
}

type state struct {
	position aocutils.Point
	memory   map[int]int64
}

func runPartOne() int {
	return getDistancesFromOxygen()[aocutils.Point{X: 0, Y: 0}]
}

func runPartTwo() int {
	maxDistance := 0
	for _, distance := range getDistancesFromOxygen() {
		// ignore unreachable points
		if distance == math.MaxInt {
			continue
		}
		maxDistance = max(maxDistance, distance)
	}
	return maxDistance
}

func parseInput() map[int]int64 {
	line := aocutils.MustGetDayInput(inputFile)[0]
	stringValues := strings.Split(line, ",")
	values := map[int]int64{}
	for idx := range stringValues {
		values[idx] = aocutils.MustStringToInt64(stringValues[idx])
	}
	return values
}

func getDistancesFromOxygen() map[aocutils.Point]int {
	area, destination := discoverArea()
	return computeDistancesFromOxygen(area, destination)
}

func discoverArea() (map[aocutils.Point]int64, aocutils.Point) {
	toVisit := []state{
		{
			position: aocutils.Point{X: 0, Y: 0},
			memory:   parseInput(),
		},
	}
	area := map[aocutils.Point]int64{}
	area[aocutils.Point{X: 0, Y: 0}] = 1
	destination := aocutils.Point{}
	for len(toVisit) > 0 {
		currentState := toVisit[0]
		toVisit = toVisit[1:]
		for dirIdx, dir := range directions {
			currentPoint := currentState.position
			newPosition := currentPoint.Add(dir)
			if _, visited := area[newPosition]; visited {
				continue
			}
			program := shared2019.NewIntcodeProgram(maps.Clone(currentState.memory), []int64{int64(dirIdx + 1)})
			err := program.Run(1)
			if err != nil {
				panic(err)
			}
			area[newPosition] = program.Outputs[0]
			if area[newPosition] == 2 {
				destination = newPosition
			}
			if area[newPosition] != 0 {
				toVisit = append(toVisit, state{
					position: newPosition,
					memory:   program.Memory,
				})
			}
		}
	}
	return area, destination
}

func computeDistancesFromOxygen(area map[aocutils.Point]int64, destination aocutils.Point) map[aocutils.Point]int {
	graph := aocutils.Graph[aocutils.Point]{}
	for point := range area {
		edges := aocutils.Edges[aocutils.Point]{}
		for _, neighbor := range point.Neighbours2D(false) {
			if area[neighbor] != 0 {
				edges[neighbor] = 1
			}
		}
		graph[point] = edges
	}

	distances, _ := graph.Dijkstra(destination)
	return distances
}

func main() {
	fmt.Println(runPartOne())
	fmt.Println(runPartTwo())
}
