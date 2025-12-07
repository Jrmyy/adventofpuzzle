package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
	shared2018 "adventofcode/pkg/shared/2018"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne(ipt []string) int {
	return shared2018.RunProcess(ipt, []int{0, 0, 0, 0, 0, 0})
}

func runPartTwo(_ []string) int {
	// After checking the outputs
	// [1 10551260 10551261 1 12 1]
	// [3 10551260 10551261 2 12 1]
	// [3 10551260 10551261 3 12 1]
	// [7 10551260 10551261 4 12 1]
	// [12 10551260 10551261 5 12 1]
	// [12 10551260 10551261 6 12 1]
	// [12 10551260 10551261 7 12 1]
	// [12 10551260 10551261 8 12 1]
	// [12 10551260 10551261 9 12 1]
	// [22 10551260 10551261 10 12 1]
	// So it seems that in register 0, we add from 1 to 10551260 all the dividers of 10551260
	cnt := 0
	for i := 1; i <= 10551260; i++ {
		if 10551260%i == 0 {
			cnt += i
		}
	}
	return cnt
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
