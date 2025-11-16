package main

import (
	"embed"
	"errors"
	"fmt"
	"slices"
	"strings"

	"adventofcode-go/pkg/aocutils"
	shared2019 "adventofcode-go/pkg/shared/2019"
)

//go:embed input.txt
var inputFile embed.FS

const (
	networkSize = 50
	idleSignal  = -1
)

func runPartOne() int64 {
	computers, packets := setUpNetwork()
	for {
		for i := 0; i < networkSize; i++ {
			program := computers[i]
			err := program.Run(3)
			if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
				panic(err)
			}
			if len(program.Outputs) != 3 && len(program.Outputs) != 0 {
				panic("weird amount of outputs")
			}
			if len(program.Outputs) == 3 {
				if program.Outputs[0] == 255 {
					return program.Outputs[2]
				}
				packets[program.Outputs[0]] = append(
					packets[program.Outputs[0]], program.Outputs[1:]...,
				)
			}
		}

		for i := 0; i < networkSize; i++ {
			program := computers[i]
			program.ClearOutputs()
			if len(packets[i]) > 0 {
				program.AddInputs(packets[i])
			} else {
				program.AddInputs([]int64{-1})
			}
			packets[i] = []int64{}
		}
	}
}

func runPartTwo() int64 {
	computers, packets := setUpNetwork()
	nat := []int64{idleSignal, idleSignal}
	lastNat := slices.Clone(nat)
	isIdleCnt := 0

	for {
		for i := 0; i < networkSize; i++ {
			program := computers[i]
			err := program.Run(3)
			if err != nil && !errors.Is(err, shared2019.ErrProgramNeedsInput) {
				panic(err)
			}
			if len(program.Outputs) != 3 && len(program.Outputs) != 0 {
				panic("weird amount of outputs")
			}
			if len(program.Outputs) == 3 {
				if program.Outputs[0] == 255 {
					nat = program.Outputs[1:]
				} else {
					packets[program.Outputs[0]] = append(
						packets[program.Outputs[0]], program.Outputs[1], program.Outputs[2],
					)
				}
			}
		}

		isIdle := true
		for i := 0; i < networkSize; i++ {
			program := computers[i]
			// computer is idle if empty incoming packet queue and is trying to receive packets (err program input
			// needed) without sending packets (outputs is empty)
			isIdle = isIdle && len(program.Outputs) == 0 && len(packets[i]) == 0
			program.ClearOutputs()
			if len(packets[i]) > 0 {
				program.AddInputs(packets[i])
			} else if i > 0 {
				program.AddInputs([]int64{idleSignal})
			}
			packets[i] = []int64{}
		}

		if isIdle {
			isIdleCnt++
		} else {
			isIdleCnt = 0
		}

		if isIdleCnt > 1 {
			if lastNat[1] == nat[1] {
				return nat[1]
			}
			computers[0].AddInputs(nat)
			lastNat = nat
		} else {
			computers[0].AddInputs([]int64{idleSignal})
		}
	}
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

func setUpNetwork() ([]*shared2019.IntcodeProgram, [][]int64) {
	computers := make([]*shared2019.IntcodeProgram, networkSize)
	packets := make([][]int64, networkSize)
	for i := 0; i < networkSize; i++ {
		computers[i] = shared2019.NewIntcodeProgram(parseInput(), []int64{int64(i)})
	}
	return computers, packets
}

func main() {
	fmt.Println(runPartOne())
	fmt.Println(runPartTwo())
}
