package main

import (
	"embed"
	"fmt"
	"slices"

	"adventofcode/2015/07/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) []internal.Instruction {
	instructions := make([]internal.Instruction, len(lines))
	for idx, line := range lines {
		instructions[idx] = internal.NewInstruction(line)
	}
	return instructions
}

func applyInstructions(instructions []internal.Instruction, signals map[string]uint16) {
	remainingInstructions := slices.Clone(instructions)
	for len(remainingInstructions) > 0 {
		newRemainingInstructions := make([]internal.Instruction, 0, len(remainingInstructions))
		for _, instruction := range remainingInstructions {
			if instruction.IsApplicable(signals) {
				instruction.Apply(signals)
			} else {
				newRemainingInstructions = append(newRemainingInstructions, instruction)
			}
		}
		remainingInstructions = newRemainingInstructions
	}
}

func runPartOne(instructions []internal.Instruction) uint16 {
	signals := map[string]uint16{}
	applyInstructions(instructions, signals)
	return signals["a"]
}

func runPartTwo(instructions []internal.Instruction) uint16 {
	signals := map[string]uint16{}
	applyInstructions(instructions, signals)
	newSignals := map[string]uint16{"b": signals["a"]}
	applyInstructions(instructions, newSignals)
	return newSignals["a"]
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	instructions := parseInput(ipt)
	fmt.Println(runPartOne(instructions))
	fmt.Println(runPartTwo(instructions))
}
