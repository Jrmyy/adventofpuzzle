package main

import (
	"embed"
	"errors"
	"fmt"
	"strings"

	"adventofcode-go/pkg/aocutils"
	shared2019 "adventofcode-go/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne() int64 {
	// Jump if there is a hole at 2 or 3 and ground at 4 to land on
	// If we cannot early jump, we jump if there is a hole at 1
	return findHullDamage([]string{
		"NOT B J",
		"NOT C T",
		"OR T J",
		"AND D J",
		"NOT A T",
		"OR T J",
		"WALK",
	})
}

func runPartTwo() int64 {
	return findHullDamage([]string{
		"NOT B J",
		"NOT C T",
		"OR T J",
		"AND D J",
		"AND H J",
		"NOT A T",
		"OR T J",
		"RUN",
	})
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

func findHullDamage(instructions []string) int64 {
	program := shared2019.NewIntcodeProgram(parseInput(), []int64{})
	err := program.Run(-1)
	if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
		panic(err)
	}
	for _, output := range program.Outputs {
		fmt.Print(string(rune(output)))
	}

	instruction := strings.Join(instructions, "\n") + "\n"
	fmt.Print(instruction)
	program.ResetInputs(shared2019.ToASCII(instruction))
	program.ClearOutputs()
	err = program.Run(-1)
	if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
		panic(err)
	}

	for i := 0; i < len(program.Outputs)-1; i++ {
		fmt.Print(string(rune(program.Outputs[i])))
	}
	return program.Outputs[len(program.Outputs)-1]
}

func main() {
	fmt.Println(runPartOne())
	fmt.Print("\n\n")
	fmt.Println(runPartTwo())
}
