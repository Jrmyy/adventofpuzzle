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

func playLookAndSay(start string, iterations int) int {
	current := start
	for i := 0; i < iterations; i++ {
		var nextBuilder strings.Builder

		currentChar := current[0]
		currentCnt := 1
		idx := 1
		for idx < len(current) {
			if current[idx] == currentChar {
				currentCnt++
			} else {
				nextBuilder.WriteString(strconv.Itoa(currentCnt))
				nextBuilder.WriteRune(rune(currentChar))
				currentCnt = 1
				currentChar = current[idx]
			}
			idx++
		}
		nextBuilder.WriteString(strconv.Itoa(currentCnt))
		nextBuilder.WriteRune(rune(currentChar))
		current = nextBuilder.String()
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
