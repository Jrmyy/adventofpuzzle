package main

import (
	"embed"
	"errors"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
	shared2019 "adventofcode/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput() *shared2019.IntcodeProgram {
	line := aocutils.MustGetDayInput(inputFile)[0]
	stringValues := strings.Split(line, ",")
	values := map[int]int64{}
	for idx := range stringValues {
		values[idx] = aocutils.MustStringToInt64(stringValues[idx])
	}
	return shared2019.NewIntcodeProgram(values, []int64{})
}

func runPartOne(program *shared2019.IntcodeProgram) {
	err := program.Run(-1)
	if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
		panic(err)
	}
	for _, output := range program.Outputs {
		fmt.Print(string(rune(output)))
	}
	program.ClearOutputs()

	inputsToProvide := []string{
		"west\n",
		"take hypercube\n",
		"west\n",
		"take space law space brochure\n",
		"west\n",
		"north\n",
		"take shell\n",
		"west\n",
		"take mug\n",
		"south\n",
		"take festive hat\n",
		"north\n",
		"east\n",
		"south\n",
		"east\n",
		"east\n",
		"east\n",
		"east\n",
		"north\n",
		"west\n",
		"north\n",
		"take whirled pass\n",
		"west\n",
		"west\n",
		"take astronaut ice cream\n",
		"south\n",
		"inv\n",
		"drop space law space brochure\n",
		"drop mug\n",
		"south\n",
	}

	for _, inputs := range inputsToProvide {
		program.ResetInputs(shared2019.ToASCII(inputs))
		fmt.Println(inputs)
		err = program.Run(-1)
		if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
			panic(err)
		}
		for _, output := range program.Outputs {
			fmt.Print(string(rune(output)))
		}
		program.ClearOutputs()
	}
	return
}

func main() {
	program := parseInput()
	runPartOne(program)
}
