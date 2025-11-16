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

func runPartOne(operations []internal.Operation) int {
	deck := createDeck(10007)
	deck = shuffle(deck, operations)
	return findPosition(deck, 2019)
}

func parseInput() []internal.Operation {
	lines := aocutils.MustGetDayInput(inputFile)
	operations := make([]internal.Operation, len(lines))
	for i, line := range lines {
		if strings.HasPrefix(line, "deal into new stack") {
			operations[i] = internal.NewDeckOperation{}
		} else if strings.HasPrefix(line, "cut ") {
			cutValue := aocutils.MustStringToInt(strings.TrimPrefix(line, "cut "))
			operations[i] = internal.CutOperation{Value: cutValue}
		} else if strings.HasPrefix(line, "deal with increment ") {
			incrementValue := aocutils.MustStringToInt(strings.TrimPrefix(line, "deal with increment "))
			operations[i] = internal.IncrementOperation{Value: incrementValue}
		} else {
			panic("cannot parse operation")
		}
	}
	return operations
}

func createDeck(deckSize int) []int {
	deck := make([]int, deckSize)
	for i := 0; i < deckSize; i++ {
		deck[i] = i
	}
	return deck
}

func shuffle(deck []int, operations []internal.Operation) []int {
	for _, op := range operations {
		deck = op.Operate(deck)
	}
	return deck
}

func findPosition(deck []int, searchedCard int) int {
	for idx, card := range deck {
		if card == searchedCard {
			return idx
		}
	}
	panic("not found")
}

func main() {
	operations := parseInput()
	fmt.Println(runPartOne(operations))
}
