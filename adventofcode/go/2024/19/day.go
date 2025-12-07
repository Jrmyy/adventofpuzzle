package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) ([]string, []string) {
	patterns := strings.Split(strings.TrimSpace(lines[0]), ", ")
	return patterns, lines[2:]
}

func countArrangements(towel string, patterns []string, cache map[string]int) int {
	if towel == "" {
		return 1
	}
	if v, ok := cache[towel]; ok {
		return v
	}
	sum := 0
	for _, p := range patterns {
		if strings.HasPrefix(towel, p) {
			sum += countArrangements(towel[len(p):], patterns, cache)
		}
	}
	cache[towel] = sum
	return sum
}

func runPartOne(lines []string) int {
	patterns, towels := parseInput(lines)
	cnt := 0
	cache := map[string]int{}
	for _, towel := range towels {
		if countArrangements(towel, patterns, cache) > 0 {
			cnt++
		}
	}
	return cnt
}

func runPartTwo(lines []string) int {
	patterns, towels := parseInput(lines)
	cnt := 0
	cache := map[string]int{}
	for _, towel := range towels {
		cnt += countArrangements(towel, patterns, cache)
	}
	return cnt
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
