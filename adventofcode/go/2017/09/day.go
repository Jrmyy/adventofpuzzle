package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func getGroupScoreAndGarbageCount(group string) (int, int) {
	isNextIgnored, isGarbage, garbageCount, groupScore, lastGroup := false, false, 0, 0, 0
	for _, charUnicode := range group {
		char := string(charUnicode)
		if isNextIgnored {
			isNextIgnored = false
			continue
		}
		if char == "!" {
			isNextIgnored = true
			continue
		}
		if isGarbage {
			if char == ">" {
				isGarbage = false
			} else {
				garbageCount++
			}
		} else {
			if char == "<" {
				isGarbage = true
			} else if char == "{" {
				groupScore += lastGroup + 1
				lastGroup = lastGroup + 1
			} else if char == "}" {
				lastGroup = lastGroup - 1
			}
		}
	}
	return groupScore, garbageCount
}

func runPartOne(ipt string) int {
	groupScore, _ := getGroupScoreAndGarbageCount(ipt)
	return groupScore
}

func runPartTwo(ipt string) int {
	_, garbageCount := getGroupScoreAndGarbageCount(ipt)
	return garbageCount
}

func parseInput() string {
	return aocutils.MustGetDayInput(inputFile)[0]
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
