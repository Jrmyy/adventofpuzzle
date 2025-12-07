package main

import (
	"embed"
	"fmt"
	"regexp"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type race struct {
	Time     int
	Distance int
}

func runPartOne(ipt []race) int {
	wins := 1
	for _, r := range ipt {
		rWins := 0
		for w := 0; w <= r.Time; w++ {
			if w*(r.Time-w) > r.Distance {
				rWins++
			}
		}
		wins *= rWins
	}
	return wins
}

func runPartTwo(ipt []race) int {
	timeStr, distStr := "", ""
	for _, r := range ipt {
		timeStr += fmt.Sprintf("%v", r.Time)
		distStr += fmt.Sprintf("%v", r.Distance)
	}
	time := aocutils.MustStringToInt(timeStr)
	distance := aocutils.MustStringToInt(distStr)
	wins := 0
	for w := 0; w <= time; w++ {
		if w*(time-w) > distance {
			wins++
		}
	}
	return wins
}

func parseInput() []race {
	lines := aocutils.MustGetDayInput(inputFile)
	times := regexp.MustCompile("(\\d+)").FindAllString(lines[0], -1)
	distances := regexp.MustCompile("(\\d+)").FindAllString(lines[1], -1)
	races := make([]race, len(times))
	for idx, time := range times {
		races[idx] = race{Time: aocutils.MustStringToInt(time), Distance: aocutils.MustStringToInt(distances[idx])}
	}
	return races
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
