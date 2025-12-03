package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode-go/2019/22/internal"
	"adventofcode-go/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne(shuffles []internal.Shuffle) int64 {
	process := internal.ShuffleProcess{
		Shuffles:    shuffles,
		DeckSize:    10_007,
		Repetitions: 1,
	}
	return process.GetCardPosition(2019)
}

func runPartTwo(shuffles []internal.Shuffle) int64 {
	process := internal.ShuffleProcess{
		Shuffles:    shuffles,
		DeckSize:    119_315_717_514_047,
		Repetitions: 101_741_582_076_661,
	}
	return process.GetCardAtPosition(2020)
}

func parseInput() []internal.Shuffle {
	lines := aocutils.MustGetDayInput(inputFile)
	operations := make([]internal.Shuffle, len(lines))
	for i, line := range lines {
		if strings.HasPrefix(line, "deal into new stack") {
			operations[i] = internal.DealIntoNewStackShuffle{}
		} else if strings.HasPrefix(line, "cut ") {
			cutValue := aocutils.MustStringToInt(strings.TrimPrefix(line, "cut "))
			operations[i] = internal.CutShuffle{Value: int64(cutValue)}
		} else if strings.HasPrefix(line, "deal with increment ") {
			incrementValue := aocutils.MustStringToInt(strings.TrimPrefix(line, "deal with increment "))
			operations[i] = internal.DealWithIncrementShuffle{Value: int64(incrementValue)}
		} else {
			panic("cannot parse operation")
		}
	}
	return operations
}

func main() {
	shuffles := parseInput()
	fmt.Println(runPartOne(shuffles))
	fmt.Println(runPartTwo(shuffles))
}
