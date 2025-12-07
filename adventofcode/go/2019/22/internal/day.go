package internal

import (
	"adventofcode/pkg/aocutils"
)

type ShuffleProcess struct {
	Shuffles    []Shuffle
	DeckSize    int64
	Repetitions int64
}

func (process ShuffleProcess) GetCardPosition(card int64) int64 {
	mA, mB := process.compose()
	return (aocutils.ModMultiply(mA, card, process.DeckSize) + mB) % process.DeckSize
}

func (process ShuffleProcess) GetCardAtPosition(position int64) int64 {
	mA, mB := process.compose()
	return aocutils.ModMultiply(position-mB, aocutils.ModInv(mA, process.DeckSize), process.DeckSize)
}

func (process ShuffleProcess) compose() (int64, int64) {
	var a, b int64 = 1, 0
	for _, shuffle := range process.Shuffles {
		opA, opB := shuffle.GetLinearCoefficients()
		a = aocutils.ModMultiply(a, opA, process.DeckSize)
		b = (aocutils.ModMultiply(b, opA, process.DeckSize) + opB) % process.DeckSize
	}
	mA := aocutils.ModPow(a, process.Repetitions, process.DeckSize)
	mB := aocutils.ModMultiply(
		aocutils.ModMultiply(b, mA-1, process.DeckSize),
		aocutils.ModInv(a-1, process.DeckSize),
		process.DeckSize,
	)
	return mA, mB
}

type Shuffle interface {
	GetLinearCoefficients() (int64, int64)
}

type DealIntoNewStackShuffle struct{}

func (op DealIntoNewStackShuffle) GetLinearCoefficients() (int64, int64) {
	return -1, -1
}

type CutShuffle struct {
	Value int64
}

func (op CutShuffle) GetLinearCoefficients() (int64, int64) {
	return 1, -op.Value
}

type DealWithIncrementShuffle struct {
	Value int64
}

func (op DealWithIncrementShuffle) GetLinearCoefficients() (int64, int64) {
	return op.Value, 0
}
