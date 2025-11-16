package internal

type Operation interface {
	Operate(deck []int) []int
}

type NewDeckOperation struct{}

func (op NewDeckOperation) Operate(deck []int) []int {
	var newDeck []int
	for _, card := range deck {
		newDeck = append([]int{card}, newDeck...)
	}
	return newDeck
}

type CutOperation struct {
	Value int
}

func (op CutOperation) Operate(deck []int) []int {
	var cutIndex int
	if op.Value >= 0 {
		cutIndex = op.Value
	} else {
		cutIndex = len(deck) + op.Value
	}
	return append(deck[cutIndex:], deck[:cutIndex]...)
}

type IncrementOperation struct {
	Value int
}

func (op IncrementOperation) Operate(deck []int) []int {
	incrementedDeck := make([]int, len(deck))
	for i, card := range deck {
		newIdx := (op.Value * i) % len(deck)
		if incrementedDeck[newIdx] != 0 {
			panic("overriding existing card")
		}
		incrementedDeck[newIdx] = card
	}
	return incrementedDeck
}
