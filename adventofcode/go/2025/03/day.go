package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type batteriesBank []int

func (batteriesBank batteriesBank) FindLargestJoltage(size, offsetIdx int) int {
	if size == 0 {
		return 0
	}

	largestJoltage, largestJoltageIdx := batteriesBank.findLocalLargestJoltage(size, offsetIdx)
	return largestJoltage*aocutils.Pow(10, size-1) + batteriesBank.FindLargestJoltage(size-1, largestJoltageIdx+1)
}

func (batteriesBank batteriesBank) findLocalLargestJoltage(size, offsetIdx int) (int, int) {
	localLargestJoltage := -1
	localLargestJoltageIdx := -1
	for idx, battery := range batteriesBank[offsetIdx : len(batteriesBank)-size+1] {
		if battery > localLargestJoltage {
			localLargestJoltage = battery
			localLargestJoltageIdx = offsetIdx + idx
		}
	}

	return localLargestJoltage, localLargestJoltageIdx
}

func parseInput(lines []string) []batteriesBank {
	banks := make([]batteriesBank, len(lines))
	for idx, line := range lines {
		rawBank := strings.Split(line, "")
		bank := make([]int, len(rawBank))
		for lIdx, s := range rawBank {
			bank[lIdx] = aocutils.MustStringToInt(s)
		}
		banks[idx] = bank
	}
	return banks
}

func runPartOne(batteriesBanks []batteriesBank) int {
	return findTotalJoltage(batteriesBanks, 2)
}

func runPartTwo(batteriesBanks []batteriesBank) int {
	return findTotalJoltage(batteriesBanks, 12)
}

func findTotalJoltage(batteriesBanks []batteriesBank, batteriesToTurnOn int) int {
	totalJoltage := 0
	for _, b := range batteriesBanks {
		totalJoltage += b.FindLargestJoltage(batteriesToTurnOn, 0)
	}
	return totalJoltage
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	batteriesBanks := parseInput(ipt)
	fmt.Println(runPartOne(batteriesBanks))
	fmt.Println(runPartTwo(batteriesBanks))
}
