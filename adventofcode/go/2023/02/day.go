package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type cubeSubset map[string]int
type game []cubeSubset

func runPartOne(games []game) int {
	conditions := map[string]int{
		"red":   12,
		"green": 13,
		"blue":  14,
	}
	s := 0
	for idx, g := range games {
		valid := true
		for _, subset := range g {
			for color, count := range subset {
				condition := conditions[color]
				if condition < count {
					valid = false
				}
			}
		}
		if valid {
			s += idx + 1
		}
	}
	return s
}

func runPartTwo(games []game) int {
	s := 0
	for _, g := range games {
		m := map[string]int{
			"green": 0,
			"red":   0,
			"blue":  0,
		}
		for _, subset := range g {
			for color, count := range subset {
				m[color] = max(m[color], count)
			}
		}
		s += m["green"] * m["blue"] * m["red"]
	}
	return s
}

func parseInput() []game {
	lines := aocutils.MustGetDayInput(inputFile)
	games := make([]game, len(lines))
	for lIdx, line := range lines {
		idAndSubsets := strings.Split(line, ": ")
		subsets := strings.Split(idAndSubsets[1], "; ")
		g := make(game, len(subsets))
		for sIdx, subset := range subsets {
			cubes := strings.Split(subset, ", ")
			parsedSubset := cubeSubset{}
			for _, cube := range cubes {
				colorAndCount := strings.Split(cube, " ")
				parsedSubset[colorAndCount[1]] = aocutils.MustStringToInt(colorAndCount[0])
			}
			g[sIdx] = parsedSubset
		}
		games[lIdx] = g
	}
	return games
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
