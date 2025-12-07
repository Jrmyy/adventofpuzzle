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

func runPartOne(instructions []string, network map[string][]string) int {
	cnt, idx, curr := 0, 0, "AAA"
	for curr != "ZZZ" {
		inst := instructions[idx]
		if inst == "R" {
			curr = network[curr][1]
		} else {
			curr = network[curr][0]
		}
		idx = (idx + 1) % len(instructions)
		cnt++
	}
	return cnt
}

func runPartTwo(instructions []string, network map[string][]string) int {
	currs := make([]string, 0, len(network))
	for k := range network {
		if strings.HasSuffix(k, "A") {
			currs = append(currs, k)
		}
	}

	minToReach := make([]int, len(currs))
	for cIdx, curr := range currs {
		cnt, idx := 0, 0
		for !strings.HasSuffix(curr, "Z") {
			inst := instructions[idx]
			nIdx := 0
			if inst == "R" {
				nIdx = 1
			}
			curr = network[curr][nIdx]
			idx = (idx + 1) % len(instructions)
			cnt++
		}
		minToReach[cIdx] = cnt
	}

	return aocutils.LcmList(minToReach)
}

func parseInput() ([]string, map[string][]string) {
	lines := aocutils.MustGetDayInput(inputFile)
	instructions := strings.Split(lines[0], "")
	re := regexp.MustCompile(`(?m)(\w{3}) = \((\w{3}), (\w{3})\)`)
	network := map[string][]string{}
	for _, line := range lines[2:] {
		matches := re.FindStringSubmatch(line)
		network[matches[1]] = []string{matches[2], matches[3]}
	}
	return instructions, network
}

func main() {
	instructions, network := parseInput()
	fmt.Println(runPartOne(instructions, network))
	fmt.Println(runPartTwo(instructions, network))
}
