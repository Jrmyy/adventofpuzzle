package main

import (
	"embed"
	"fmt"

	"adventofcode/2015/14/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) internal.ReindeerOlympics {
	return internal.NewRace(lines)
}

func runPartOne(race internal.ReindeerOlympics) int {
	return race.FindWinnerByDistance()
}

func runPartTwo(race internal.ReindeerOlympics) int {
	return race.FindWinnerByPoints()
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	race := parseInput(ipt)
	fmt.Println(runPartOne(race))
	fmt.Println(runPartTwo(race))
}
