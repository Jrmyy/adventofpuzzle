package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
	"adventofcode/pkg/shared/2017"
)

//go:embed input.txt
var inputFile embed.FS

func generateSequence(x, y int) []int {
	l := make([]int, y-x+1)
	for i := x; i <= y; i++ {
		l[i-x] = i
	}
	return l
}

func runPartOne(instructions []int) int {
	return shared2017.RunKnotHashRounds(instructions, 1)
}

func runPartTwo(instructions string) string {
	return shared2017.KnotHash(instructions)
}

func parseInputP1(line string) []int {
	figures := strings.Split(line, ",")
	ipt := make([]int, len(figures))
	for idx, fig := range figures {
		ipt[idx] = aocutils.MustStringToInt(fig)
	}
	return ipt
}

func main() {
	line := aocutils.MustGetDayInput(inputFile)[0]
	instructionsP1 := parseInputP1(line)
	fmt.Println(runPartOne(instructionsP1))
	fmt.Println(runPartTwo(line))
}
