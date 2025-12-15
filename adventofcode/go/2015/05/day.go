package main

import (
	"embed"
	"fmt"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var vowels = []uint8{'a', 'e', 'i', 'o', 'u'}
var badPatterns = []string{"ab", "cd", "pq", "xy"}

func isNiceP1(s string) bool {
	vowelsCnt := 0
	hasALetterTwiceInARow := false

	for idx := 0; idx < len(s)-1; idx++ {
		pattern := s[idx : idx+2]
		if slices.Contains(badPatterns, pattern) {
			return false
		}
		if slices.Contains(vowels, pattern[0]) {
			vowelsCnt++
		}
		hasALetterTwiceInARow = hasALetterTwiceInARow || pattern[0] == pattern[1]
	}

	if slices.Contains(vowels, s[len(s)-1]) {
		vowelsCnt++
	}
	return vowelsCnt >= 3 && hasALetterTwiceInARow
}

func hasSymmetricPattern(s string) bool {
	for idx := 0; idx < len(s)-2; idx++ {
		pattern := s[idx : idx+3]
		if pattern[0] == pattern[2] {
			return true
		}
	}
	return false
}

func hasNonOverlappingTwiceRepeatingPattern(s string) bool {
	pairs := map[string]int{}
	for idx := 0; idx < len(s)-1; idx++ {
		pattern := s[idx : idx+2]
		prevIdx, exists := pairs[pattern]
		if !exists {
			pairs[pattern] = idx
		} else if idx-prevIdx >= 2 {
			return true
		}
	}
	return false
}

func isNiceP2(s string) bool {
	return hasNonOverlappingTwiceRepeatingPattern(s) && hasSymmetricPattern(s)
}

func getNiceStringsCount(lines []string, isNiceFunc func(s string) bool) int {
	res := 0
	for _, s := range lines {
		if isNiceFunc(s) {
			res++
		}
	}
	return res
}

func runPartOne(lines []string) int {
	return getNiceStringsCount(lines, isNiceP1)
}

func runPartTwo(lines []string) int {
	return getNiceStringsCount(lines, isNiceP2)
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
