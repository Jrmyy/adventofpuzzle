package main

import (
	"embed"
	"fmt"
	"regexp"
	"strconv"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type digPlan struct {
	Direction string
	Length    int
	Color     string
}

var directions = []string{"R", "D", "L", "U"}

func getCubicMeters(ipt []digPlan) int {
	positions := [][]int{{0, 0}}
	border := 0
	for _, p := range ipt {
		lastPos := positions[len(positions)-1]
		border += p.Length
		switch p.Direction {
		case "D":
			positions = append(positions, []int{lastPos[0], lastPos[1] + p.Length})
		case "U":
			positions = append(positions, []int{lastPos[0], lastPos[1] - p.Length})
		case "L":
			positions = append(positions, []int{lastPos[0] - p.Length, lastPos[1]})
		case "R":
			positions = append(positions, []int{lastPos[0] + p.Length, lastPos[1]})
		}
	}
	positions = positions[:len(positions)-1]

	// We calculate the area using shoelace formula
	area := 0
	for i := range positions {
		curr := positions[i]
		next := positions[(i+1)%len(positions)]
		area += curr[0]*next[1] - curr[1]*next[0]
	}

	area /= 2
	// Now since Pick's theorem states that area = interior + border / 2 - 1 it means that
	// interior + border (solution of the puzzle) = area + border / 2 + 1
	return area + border/2 + 1
}

func runPartOne(ipt []digPlan) int {
	return getCubicMeters(ipt)
}

func runPartTwo(ipt []digPlan) int {
	for i := range ipt {
		l, err := strconv.ParseInt(ipt[i].Color[1:6], 16, 64)
		if err != nil {
			panic(err)
		}
		ipt[i].Length = int(l)
		ipt[i].Direction = directions[aocutils.MustStringToInt(ipt[i].Color[6:])]
	}
	return getCubicMeters(ipt)
}

func parseInput() []digPlan {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([]digPlan, len(lines))
	regex := regexp.MustCompile("([RDLU]) (\\d+) \\((.+)\\)")
	for idx, line := range lines {
		matches := regex.FindStringSubmatch(line)
		ipt[idx] = digPlan{Direction: matches[1], Length: aocutils.MustStringToInt(matches[2]), Color: matches[3]}
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
