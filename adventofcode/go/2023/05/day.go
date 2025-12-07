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

type garden struct {
	seeds  []int
	phases []phase
}

type phaseRange struct {
	destStart   int
	sourceStart int
	length      int
}

type phase struct {
	ranges []phaseRange
}

func runPartOne(ipt garden) int {
	for _, p := range ipt.phases {
		for idx, s := range ipt.seeds {
			for _, r := range p.ranges {
				if s >= r.sourceStart && s < r.sourceStart+r.length {
					ipt.seeds[idx] = r.destStart + (s - r.sourceStart)
					break
				}
			}
		}
	}
	m := ipt.seeds[0]
	for _, s := range ipt.seeds[1:] {
		if s < m {
			m = s
		}
	}
	return m
}

func runPartTwo(ipt garden) int {
	m := 10000000000000
	for len(ipt.seeds) > 0 {
		ms, l := ipt.seeds[0], ipt.seeds[1]
		ipt.seeds = ipt.seeds[2:]
		newSeeds := make([]int, l)
		for s := ms; s < ms+l; s++ {
			newSeeds[s-ms] = s
		}
		for _, p := range ipt.phases {
			for idx, s := range newSeeds {
				for _, r := range p.ranges {
					if s >= r.sourceStart && s < r.sourceStart+r.length {
						newSeeds[idx] = r.destStart + (s - r.sourceStart)
						break
					}
				}
			}
		}
		for _, s := range newSeeds {
			if s < m {
				m = s
			}
		}
	}
	return m
}

func parseInput() garden {
	lines := aocutils.MustGetDayInput(inputFile)
	seedsStr := strings.TrimPrefix(lines[0], "seeds: ")
	var seeds []int
	for _, s := range strings.Split(seedsStr, " ") {
		seeds = append(seeds, aocutils.MustStringToInt(s))
	}
	var phases []phase
	var currPhase phase
	for _, line := range lines[1:] {
		if strings.HasSuffix(line, " map:") {
			currPhase = phase{}
		} else if line == "" {
			if len(currPhase.ranges) > 0 {
				phases = append(phases, currPhase)
			}
		} else if regexp.MustCompile("\\d").MatchString(line) {
			parts := strings.Split(line, " ")
			pr := phaseRange{
				destStart:   aocutils.MustStringToInt(parts[0]),
				sourceStart: aocutils.MustStringToInt(parts[1]),
				length:      aocutils.MustStringToInt(parts[2]),
			}
			currPhase.ranges = append(currPhase.ranges, pr)
		}
	}
	phases = append(phases, currPhase)
	return garden{
		phases: phases,
		seeds:  seeds,
	}
}

func main() {
	ipt1 := parseInput()
	fmt.Println(runPartOne(ipt1))
	ipt2 := parseInput()
	fmt.Println(runPartTwo(ipt2))
}
