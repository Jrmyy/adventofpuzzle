package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func scanner(height, time int) int {
	offset := time % (2 * (height - 1))
	if offset > height-1 {
		return 2*(height-1) - offset
	}
	return offset
}

func runPartOne(ipt map[int]int) int {
	caughtSum := 0
	for layerPos, layerHeight := range ipt {
		if scanner(layerHeight, layerPos) == 0 {
			caughtSum += layerPos * layerHeight
		}
	}
	return caughtSum
}

func runPartTwo(ipt map[int]int) int {
	delay, keep := 0, true
	for keep {
		delay++
		keep = false
		for layerPos, layerHeight := range ipt {
			if scanner(layerHeight, layerPos+delay) == 0 {
				keep = true
				break
			}
		}
	}
	return delay
}

func parseInput() map[int]int {
	lines := aocutils.MustGetDayInput(inputFile)
	layers := map[int]int{}
	for _, line := range lines {
		parts := strings.Split(line, ": ")
		layerIdx, layerLen := aocutils.MustStringToInt(parts[0]), aocutils.MustStringToInt(parts[1])
		layers[layerIdx] = layerLen
	}
	return layers
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
