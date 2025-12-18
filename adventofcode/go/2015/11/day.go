package main

import (
	"embed"
	"fmt"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

const alphabetLength = 26

func isSecure(s string) bool {
	for _, r := range s {
		if r == 'l' || r == 'i' || r == 'o' {
			return false
		}
	}

	hasIncreasedStraight := false
	for idx := 0; idx < len(s)-2; idx++ {
		sub := s[idx : idx+3]
		if sub[1]-sub[0] == 1 && sub[2]-sub[1] == 1 {
			hasIncreasedStraight = true
			break
		}
	}

	hasTwoNonOverlappingPairs := false
	var prevPair string
	var prevIdx int
	for idx := 0; idx < len(s)-1; idx++ {
		sub := s[idx : idx+2]
		if sub[0] == sub[1] {
			if prevPair == "" {
				prevPair = sub
				prevIdx = idx
			} else {
				if idx-prevIdx >= 2 {
					hasTwoNonOverlappingPairs = true
					break
				}
			}
		}
	}

	return hasIncreasedStraight && hasTwoNonOverlappingPairs
}

func toInt(s string) int {
	res := 0
	for i := 0; i < len(s); i++ {
		res += int(s[i]-'a'+1) * aocutils.Pow(alphabetLength, len(s)-1-i)
	}
	return res
}

func toString(i int) string {
	var parts []rune
	currentValue := i
	for currentValue > 0 {
		remainder := (currentValue - 1) % alphabetLength
		parts = append(parts, rune('a'+remainder))
		currentValue = (currentValue - 1) / alphabetLength
	}
	slices.Reverse(parts)
	return string(parts)
}

func findNextPassword(start string) string {
	currentNum := toInt(start) + 1
	for {
		password := toString(currentNum)
		if isSecure(password) {
			return password
		}
		currentNum++
	}
}

func runPartOne(start string) string {
	return findNextPassword(start)
}

func runPartTwo(start string) string {
	next := findNextPassword(start)
	return findNextPassword(next)
}

func main() {
	start := aocutils.MustGetDayInput(inputFile)[0]
	fmt.Println(runPartOne(start))
	fmt.Println(runPartTwo(start))
}
