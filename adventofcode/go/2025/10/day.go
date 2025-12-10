package main

import (
	"embed"
	"fmt"

	"adventofcode/2025/10/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) []internal.Machine {
	machines := make([]internal.Machine, len(lines))
	for idx, line := range lines {
		machines[idx] = internal.NewMachine(line)
	}

	return machines
}

func runPartOne(machines []internal.Machine) int {
	totalFewest := 0
	for _, m := range machines {
		totalFewest += m.GetFewestPressesToStart()
	}
	return totalFewest
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	machines := parseInput(ipt)
	fmt.Println(runPartOne(machines))
}
