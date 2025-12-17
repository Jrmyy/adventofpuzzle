package main

import (
	"embed"
	"fmt"
	"regexp"

	"adventofcode/2015/09/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var regex = regexp.MustCompile("^(\\w+) to (\\w+) = (\\d+)$")

func parseInput(lines []string) (internal.DistanceRegistry, internal.LocationRegistry) {
	distances := internal.DistanceRegistry{}
	locations := internal.LocationRegistry{}
	for _, line := range lines {
		matches := regex.FindStringSubmatch(line)[1:]
		distance := aocutils.MustStringToInt(matches[2])
		distances.Add(matches[0], matches[1], distance)
		locations.Add(matches[0])
		locations.Add(matches[1])
	}
	return distances, locations
}

func runPartOne(distanceRegistry internal.DistanceRegistry, locationRegistry internal.LocationRegistry) int {
	algorithm := internal.NewShortestPathAlgorithm(distanceRegistry, locationRegistry)
	return algorithm.Run()
}

func runPartTwo(distanceRegistry internal.DistanceRegistry, locationRegistry internal.LocationRegistry) int {
	algorithm := internal.NewLongestPathAlgorithm(distanceRegistry, locationRegistry)
	return algorithm.Run()
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	distanceRegistry, locationRegistry := parseInput(ipt)
	fmt.Println(runPartOne(distanceRegistry, locationRegistry))
	fmt.Println(runPartTwo(distanceRegistry, locationRegistry))
}
