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
	maxSignal := int64(0)
	for phaseA := 0; phaseA <= 4; phaseA++ {
		for phaseB := 0; phaseB <= 4; phaseB++ {
			for phaseC := 0; phaseC <= 4; phaseC++ {
				for phaseD := 0; phaseD <= 4; phaseD++ {
					for phaseE := 0; phaseE <= 4; phaseE++ {
						phases := []int{phaseA, phaseB, phaseC, phaseD, phaseE}
						if hasDuplicates(phases) {
							continue
						}
						inputSignal := int64(0)
						for _, phase := range phases {
							program := shared2019.NewIntcodeProgram(parseInput(), []int64{int64(phase), inputSignal})
							err := program.Run(1)
							if err != nil {
								panic(err)
							}
							inputSignal = program.Outputs[0]
						}
						maxSignal = max(maxSignal, inputSignal)
					}
				}
			}
		}
	}
	return maxSignal
}

func runPartTwo() int64 {
	maxSignal := int64(0)
	for phaseA := 5; phaseA <= 9; phaseA++ {
		for phaseB := 5; phaseB <= 9; phaseB++ {
			for phaseC := 5; phaseC <= 9; phaseC++ {
				for phaseD := 5; phaseD <= 9; phaseD++ {
					for phaseE := 5; phaseE <= 9; phaseE++ {
						phases := []int{phaseA, phaseB, phaseC, phaseD, phaseE}
						if hasDuplicates(phases) {
							continue
						}

						programs := make([]*shared2019.IntcodeProgram, 5)
						for i := 0; i < 5; i++ {
							inputs := []int64{int64(phases[i])}
							if i == 0 {
								inputs = append(inputs, 0)
							}
							programs[i] = shared2019.NewIntcodeProgram(parseInput(), inputs)
						}

						programIdx := 0
						for {
							program := programs[programIdx]
							err := program.Run(1)

							if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
								panic(err)
							}

							if programIdx == 0 && len(program.Outputs) == 0 {
								break
							}

							nextProgramIdx := (programIdx + 1) % 5
							programs[nextProgramIdx].AddInputs(program.Outputs)
							program.ClearOutputs()
							programIdx = nextProgramIdx
						}

						lastOutput := programs[0].Inputs[len(programs[0].Inputs)-1]
						maxSignal = max(maxSignal, lastOutput)
					}
				}
			}
		}
	}
	return maxSignal
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

func hasDuplicates(arr []int) bool {
	seen := map[int]bool{}
	for _, val := range arr {
		_, exists := seen[val]
		if exists {
			return true
		}
		seen[val] = true
	}
	return false
}

func main() {
	fmt.Println(runPartOne())
	fmt.Println(runPartTwo())
}
