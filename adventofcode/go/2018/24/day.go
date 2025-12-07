package main

import (
	"embed"
	"fmt"
	"regexp"
	"strings"

	"adventofcode/2018/24/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) *internal.Simulation {
	regex := regexp.MustCompile("^(\\d+) units each with (\\d+) hit points (?:\\((weak|immune) to ([a-z, ]+)?(?:; )?(?:(weak|immune) to ([a-z, ]+)?)?\\) )?with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)$")
	groups := [2]internal.Army{}
	groupIdx := -1
	groupType := ""
	for _, line := range lines {
		switch strings.TrimSpace(line) {
		case "Immune System:":
			groupIdx = 0
			groupType = strings.TrimSuffix(line, ":")
		case "Infection:":
			groupIdx = 1
			groupType = strings.TrimSuffix(line, ":")
		case "":
			continue
		default:
			matches := regex.FindStringSubmatch(line)
			g, err := internal.FromRegexMatches(matches, groupType)
			if err != nil {
				panic(err)
			}
			groups[groupIdx] = append(groups[groupIdx], g)
		}
	}

	return &internal.Simulation{ImmuneSystem: groups[0], Infection: groups[1]}
}

func runPartOne(simulation *internal.Simulation) int {
	_, res := simulation.Run()
	return res
}

func runPartTwo(initialSimulation *internal.Simulation) int {
	boost := 1
	for {
		simulation := initialSimulation.Duplicate(boost)
		groupType, res := simulation.Run()
		if groupType == "Immune System" {
			return res
		}
		boost++
	}
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	initialSimulation := parseInput(ipt)
	fmt.Println(runPartOne(initialSimulation.Duplicate(0)))
	fmt.Println(runPartTwo(initialSimulation))
}
