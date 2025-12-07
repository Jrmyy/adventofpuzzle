package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type conditionRecord struct {
	Record      string
	Arrangement []int
}

// Dynamic programing that have the following state:
//   - currPos : the current position within the string
//   - currBlockPos: the current position within the list of blocks
//     (for instance if we have blocks [1,2,3] and currBlockPos = 1 then blocks[currBlockPos] = 2)
//   - currBlockLength: the length of the currentBlock we are visiting
func getArrangementsCount(r conditionRecord, currPos, currBlockPos, currBlockLength int, cache map[string]int) int {
	cacheKey := fmt.Sprintf("%v,%v,%v", currPos, currBlockPos, currBlockLength)
	cacheValue, ok := cache[cacheKey]
	if ok {
		return cacheValue
	}
	// When we are at the end of the string, the string is valid if
	// The current block position is equal to the length of blocks we expect and the current block has a 0 length
	// The current block position is equal to the length of blocks minus 1 and the current block length is equal to the length of the last block
	if currPos == len(r.Record) {
		if (currBlockPos == len(r.Arrangement) && currBlockLength == 0) ||
			(currBlockPos == len(r.Arrangement)-1 && currBlockLength == r.Arrangement[len(r.Arrangement)-1]) {
			return 1
		}
		return 0
	}
	res := 0
	for _, c := range []rune{'.', '#'} {
		if rune(r.Record[currPos]) == c || r.Record[currPos] == '?' {
			if c == '.' && currBlockLength == 0 {
				// If the current char is '?' or '.' and we are not in block of '#' we keep discovering the string without changing anything
				res += getArrangementsCount(r, currPos+1, currBlockPos, 0, cache)
			} else if c == '.' &&
				currBlockLength > 0 &&
				currBlockPos < len(r.Arrangement) &&
				r.Arrangement[currBlockPos] == currBlockLength {
				// If the current char is '?' or '.' and we are visiting a block (meaning we are at the end of a block):
				// - we still have less blocks than what is required and
				// - the current block position we are in is equal to the current block length (meaning the current block we are in have the same amount of '#' we have just visited)
				// We keep discovering the string, but we increment the block index, and we reset the block length
				res += getArrangementsCount(r, currPos+1, currBlockPos+1, 0, cache)
			} else if c == '#' {
				// If the current char is '?' or '#' we keep discovering the string, but we increment the length of the current block, and we stay in the current block
				res += getArrangementsCount(r, currPos+1, currBlockPos, currBlockLength+1, cache)
			}
		}
	}
	cache[cacheKey] = res
	return res
}

func getAllArrangementsCount(ipt []conditionRecord) int {
	s := 0
	for _, r := range ipt {
		s += getArrangementsCount(r, 0, 0, 0, map[string]int{})
	}
	return s
}

func parseInput() []conditionRecord {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([]conditionRecord, len(lines))
	for idx, line := range lines {
		parts := strings.Split(line, " ")
		var arrangement []int
		sum := 0
		for _, i := range strings.Split(parts[1], ",") {
			j := aocutils.MustStringToInt(i)
			arrangement = append(arrangement, j)
			sum += j
		}
		ipt[idx] = conditionRecord{
			Record:      parts[0],
			Arrangement: arrangement,
		}
	}
	return ipt
}

func unfold(ipt []conditionRecord) []conditionRecord {
	records := make([]conditionRecord, len(ipt))
	for i, r := range ipt {
		newParts := make([]int, 0, 5*len(r.Arrangement))
		for j := 0; j < 5; j++ {
			for _, p := range r.Arrangement {
				newParts = append(newParts, p)
			}
		}
		records[i] = conditionRecord{
			Record:      r.Record + "?" + r.Record + "?" + r.Record + "?" + r.Record + "?" + r.Record,
			Arrangement: newParts,
		}
	}
	return records
}

func main() {
	ipt := parseInput()
	fmt.Println(getAllArrangementsCount(ipt))
	ipt = unfold(ipt)
	fmt.Println(getAllArrangementsCount(ipt))
}
