package internal

import (
	"slices"

	"adventofcode/pkg/aocutils"
)

type Circuits map[aocutils.Point]int
type BoxesPair aocutils.Pair[aocutils.Point]
type Playground struct {
	Pairs      []BoxesPair
	BoxesCount int

	circuits     Circuits
	maxCircuitId int
}

func (playground *Playground) CreateCircuits(maxConnections int) (Circuits, BoxesPair) {
	currentIdx := 0
	for currentIdx < maxConnections {
		pair := playground.Pairs[currentIdx]
		playground.mergePairWithExistingCircuits(pair)
		if playground.isFullyConnected() {
			break
		}
		currentIdx++
	}
	return playground.circuits, playground.Pairs[currentIdx]
}

func (playground *Playground) ResetCircuits() {
	playground.circuits = Circuits{}
	playground.maxCircuitId = 0
}

func (playground *Playground) isFullyConnected() bool {
	if len(playground.circuits) != playground.BoxesCount {
		return false
	}
	circuitsCount := map[int]int{}
	for _, v := range playground.circuits {
		circuitsCount[v]++
	}
	return len(circuitsCount) == 1
}

func (playground *Playground) mergePairWithExistingCircuits(pair BoxesPair) {
	firstCircuitIdx, foundFirst := playground.circuits[pair.First]
	secondCircuitIdx, foundSecond := playground.circuits[pair.Second]
	if foundFirst && foundSecond && firstCircuitIdx == secondCircuitIdx {
		return
	}

	if foundFirst && foundSecond {
		for toMerge, circuitIdx := range playground.circuits {
			if circuitIdx == secondCircuitIdx {
				playground.circuits[toMerge] = firstCircuitIdx
			}
		}
	} else if foundFirst {
		playground.circuits[pair.Second] = firstCircuitIdx
	} else if foundSecond {
		playground.circuits[pair.First] = secondCircuitIdx
	} else {
		playground.circuits[pair.First] = playground.maxCircuitId
		playground.circuits[pair.Second] = playground.maxCircuitId
		playground.maxCircuitId++
	}
}

func NewPlayground(boxes []aocutils.Point) *Playground {
	var pairs []BoxesPair
	for fi, first := range boxes {
		for _, second := range boxes[fi+1:] {
			pairs = append(
				pairs,
				BoxesPair{First: first, Second: second},
			)
		}
	}

	slices.SortFunc(pairs, func(a, b BoxesPair) int {
		diff := a.First.EuclideanDist(a.Second) - b.First.EuclideanDist(b.Second)
		if diff > 0 {
			return 1
		}
		if diff == 0 {
			return 0
		}
		return -1
	})

	return &Playground{
		Pairs:      pairs,
		BoxesCount: len(boxes),

		circuits:     map[aocutils.Point]int{},
		maxCircuitId: 0,
	}
}
