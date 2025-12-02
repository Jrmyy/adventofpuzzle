package main

import (
	"embed"
	"fmt"
	"strconv"
	"strings"

	"adventofcode-go/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type numRange struct {
	Min int
	Max int
}

func parseInput(lines []string) []numRange {
	var ranges []numRange
	for _, r := range strings.Split(lines[0], ",") {
		parts := strings.Split(r, "-")
		ranges = append(
			ranges,
			numRange{
				Min: aocutils.MustStringToInt(parts[0]),
				Max: aocutils.MustStringToInt(parts[1]),
			},
		)
	}
	return ranges
}

func runPartOne(ranges []numRange) int {
	return findInvalidNumbers(ranges, isInvalidP1)
}

func runPartTwo(ranges []numRange) int {
	return findInvalidNumbers(ranges, isInvalidP2)
}

func findInvalidNumbers(ranges []numRange, invalidFunc func(i int) bool) int {
	res := 0
	for _, r := range ranges {
		for i := r.Min; i <= r.Max; i++ {
			if invalidFunc(i) {
				res += i
			}
		}
	}
	return res
}

func isInvalidP1(i int) bool {
	s := strconv.Itoa(i)
	if len(s)%2 != 0 {
		return false
	}

	return s[len(s)/2:] == s[:len(s)/2]
}

func isInvalidP2(i int) bool {
	str := strconv.Itoa(i)
	for chunkLength := 1; chunkLength <= len(str)/2; chunkLength++ {
		// If we cannot split the string into identical length chunks, we ignore
		if len(str)%chunkLength != 0 {
			continue
		}

		// We build the string chunks
		chunks := map[string]bool{}
		for chunkIdx := 0; chunkIdx < len(str)/chunkLength; chunkIdx++ {
			chunk := str[chunkIdx*chunkLength : (chunkIdx+1)*chunkLength]
			chunks[chunk] = true
		}

		// If we have only one different chunk, that means that we found a number formed by only one repeating pattern
		// Therefore it is invalid
		if len(chunks) == 1 {
			return true
		}
	}
	return false
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	ranges := parseInput(ipt)
	fmt.Println(runPartOne(ranges))
	fmt.Println(runPartTwo(ranges))
}
