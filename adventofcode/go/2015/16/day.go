package main

import (
	"embed"
	"fmt"
	"regexp"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var expectedAntSueAttributes = map[string]int{
	"children":    3,
	"cats":        7,
	"samoyeds":    2,
	"pomeranians": 3,
	"akitas":      0,
	"vizslas":     0,
	"goldfish":    5,
	"trees":       3,
	"cars":        2,
	"perfumes":    1,
}

var regex = regexp.MustCompile("^Sue \\d+: (.*)$")

func equalityOperator(actual, expected int) bool {
	return actual == expected
}

func parseInput(lines []string) []map[string]int {
	ants := make([]map[string]int, len(lines))
	for idx, line := range lines {
		inlineAttributes := regex.FindStringSubmatch(line)[1]
		rawAttributeParts := strings.Split(inlineAttributes, ", ")
		ant := map[string]int{}
		for _, rawAttributePart := range rawAttributeParts {
			attributeParts := strings.Split(rawAttributePart, ": ")
			ant[attributeParts[0]] = aocutils.MustStringToInt(attributeParts[1])
		}
		ants[idx] = ant
	}
	return ants
}

func matchesAllAttributes(ant, reference map[string]int, specificComparators map[string]func(int, int) bool) bool {
	for name, value := range ant {
		comparator, exists := specificComparators[name]
		if !exists {
			comparator = equalityOperator
		}
		if !comparator(value, reference[name]) {
			return false
		}
	}
	return true
}

func findAntSue(ants []map[string]int, specificComparators map[string]func(int, int) bool) int {
	for idx, ant := range ants {
		if matchesAllAttributes(ant, expectedAntSueAttributes, specificComparators) {
			return idx + 1
		}
	}
	panic("cannot find Ant Sue")
}

func runPartOne(ants []map[string]int) int {
	return findAntSue(ants, map[string]func(int, int) bool{})
}

func runPartTwo(ants []map[string]int) int {
	return findAntSue(
		ants,
		map[string]func(int, int) bool{
			"trees":       func(a, b int) bool { return b < a },
			"cats":        func(a, b int) bool { return b < a },
			"pomeranians": func(a, b int) bool { return b > a },
			"goldfish":    func(a, b int) bool { return b > a },
		},
	)
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	ants := parseInput(ipt)
	fmt.Println(runPartOne(ants))
	fmt.Println(runPartTwo(ants))
}
