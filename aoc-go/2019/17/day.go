package main

import (
	"embed"
	"errors"
	"fmt"
	"strings"

	"adventofcode-go/2019/17/internal"
	"adventofcode-go/pkg/aocutils"
	shared2019 "adventofcode-go/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne() int {
	program := shared2019.NewIntcodeProgram(parseInput(), []int64{})
	space, _, _ := internal.BuildMap(program)
	return internal.CalculateAlignmentParameters(space)
}

func runPartTwo() int64 {
	memory := parseInput()
	memory[0] = 2
	program := shared2019.NewIntcodeProgram(memory, []int64{})

	space, position, direction := internal.BuildMap(program)
	path := internal.BuildPath(space, position, direction)
	fmt.Println("DEBUG -----", path, "----- DEBUG")

	// After analyzing the path, we find the following functions:
	// A: L6 R8 L4 R8 L12
	// B: L12 R10 L4
	// C: L12 L6 L4 L4
	// Main: A B B C B C B C A A
	functions := []string{
		"A,B,B,C,B,C,B,C,A,A\n",
		"L,6,R,8,L,4,R,8,L,12\n",
		"L,12,R,10,L,4\n",
		"L,12,L,6,L,4,L,4\n",
		"n\n",
	}

	for _, function := range functions {
		fmt.Print(function)
		program.ResetInputs(shared2019.ToASCII(function))
		program.ClearOutputs()
		if err := program.Run(-1); err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
			panic(err)
		}

		var line []rune
		for _, output := range program.Outputs {
			if output == 10 {
				fmt.Println(string(line))
				line = []rune{}
			} else {
				line = append(line, rune(output))
			}
		}
	}

	return program.Outputs[len(program.Outputs)-1]
}

func parseInput() map[int]int64 {
	line := aocutils.MustGetDayInput(inputFile)[0]
	stringValues := strings.Split(line, ",")
	values := map[int]int64{}
	for idx := range stringValues {
		values[idx] = aocutils.MustStringToInt64(stringValues[idx])
	}
	return values
}

func main() {
	fmt.Println(runPartOne())
	fmt.Println(runPartTwo())
}
