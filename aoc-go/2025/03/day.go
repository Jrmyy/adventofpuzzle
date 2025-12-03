package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode-go/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type batteriesBank []string

func (batteriesBank batteriesBank) FindLargestJoltage(size int) int {
	toFind := size
	parts := make([]string, size)
	lastFoundIdx := -1

	for toFind > 0 {
		localMax := -1
		selectedBattery := ""
		offset := lastFoundIdx + 1

		for idx, battery := range batteriesBank[offset : len(batteriesBank)-toFind+1] {
			if batteryValue := aocutils.MustStringToInt(battery); batteryValue > localMax {
				localMax = batteryValue
				selectedBattery = battery
				lastFoundIdx = offset + idx
			}
		}

		parts[size-toFind] = selectedBattery
		toFind--
	}

	return aocutils.MustStringToInt(strings.Join(parts, ""))
}

func parseInput(lines []string) []batteriesBank {
	banks := make([]batteriesBank, len(lines))
	for idx, line := range lines {
		bank := strings.Split(line, "")
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
		totalJoltage += b.FindLargestJoltage(batteriesToTurnOn)
	}
	return totalJoltage
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	batteriesBanks := parseInput(ipt)
	fmt.Println(runPartOne(batteriesBanks))
	fmt.Println(runPartTwo(batteriesBanks))
}
