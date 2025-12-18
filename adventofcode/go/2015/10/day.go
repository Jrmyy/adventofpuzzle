package main

import (
	"embed"
	"fmt"
	"strconv"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func buildNextString(start string) string {
	var nextBuilder strings.Builder
	currentChar := start[0]
	currentCnt := 1
	idx := 1
	for idx < len(start) {
		if start[idx] == currentChar {
			currentCnt++
		} else {
			nextBuilder.WriteString(strconv.Itoa(currentCnt))
			nextBuilder.WriteRune(rune(currentChar))
			currentCnt = 1
			currentChar = start[idx]
		}
		idx++
	}
	nextBuilder.WriteString(strconv.Itoa(currentCnt))
	nextBuilder.WriteRune(rune(currentChar))
	return nextBuilder.String()
}

func playLookAndSay(start string, iterations int) int {
	current := start
	for i := 0; i < iterations; i++ {
		current = buildNextString(current)
	}
	return len(current)
}

func runPartOne(start string) int {
	return playLookAndSay(start, 40)
}

func runPartTwo(start string) int {
	return playLookAndSay(start, 50)
}

func main() {
	start := aocutils.MustGetDayInput(inputFile)[0]
	fmt.Println(runPartOne(start))
	fmt.Println(runPartTwo(start))
}
