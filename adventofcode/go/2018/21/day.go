package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
	"adventofcode/pkg/shared/2018"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne(ipt []string) int {
	// Here what is done is that I checked the program until I found where the register 0 is used
	// It was used once and checked for equality with another register.
	// I just took the value of that register and it worked
	return shared2018.RunProcess(ipt, []int{0, 0, 0, 0, 0, 0})
}

func runPartTwo(ipt []string) int {
	// Here what is done is that I checked the program until I found the first value again
	// I assumed there was a cycle
	// Then I just returned the last seen value before seeing the first value again
	return shared2018.RunProcess(ipt, []int{0, 0, 0, 0, 0, 0})
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
