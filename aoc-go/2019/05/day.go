package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode-go/pkg/aocutils"
	shared2019 "adventofcode-go/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne(program *shared2019.IntcodeProgram) int64 {
	err := program.Run(-1)
	if err != nil {
		panic(err)
	}
	return program.Outputs[len(program.Outputs)-1]
}

func runPartTwo(program *shared2019.IntcodeProgram) int64 {
	err := program.Run(-1)
	if err != nil {
		panic(err)
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
	program := shared2019.NewIntcodeProgram(parseInput(), []int64{1})
	fmt.Println(runPartOne(program))

	program = shared2019.NewIntcodeProgram(parseInput(), []int64{5})
	fmt.Println(runPartTwo(program))
}
