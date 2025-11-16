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
	return program.Memory[0]
}

func runPartTwo() int64 {
	for noun := int64(0); noun <= 99; noun++ {
		for verb := int64(0); verb <= 99; verb++ {
			program := shared2019.NewIntcodeProgram(parseInput(noun, verb), []int64{})
			err := program.Run(-1)
			if err != nil {
				panic(err)
			}
			if program.Memory[0] == 19690720 {
				return 100*noun + verb
			}
		}
	}
	panic("no solution found")
}

func parseInput(noun, verb int64) map[int]int64 {
	line := aocutils.MustGetDayInput(inputFile)[0]
	stringValues := strings.Split(line, ",")
	values := map[int]int64{}
	for idx := range stringValues {
		values[idx] = aocutils.MustStringToInt64(stringValues[idx])
	}
	values[1] = noun
	values[2] = verb
	return values
}

func main() {
	program := shared2019.NewIntcodeProgram(parseInput(12, 2), []int64{})
	fmt.Println(runPartOne(program))

	// Reset program memory for part two
	fmt.Println(runPartTwo())
}
