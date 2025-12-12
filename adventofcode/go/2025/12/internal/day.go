package internal

type Present [][]rune

type FitState int

const (
	FitEasily FitState = iota
	MayFitWithOptimization
	FitImpossible
)

func (present Present) CountNecessarySpaces() (int, int) {
	totalTilesNeeded, filledTilesNeeded := 0, 0
	for y := range present {
		for x := range present[y] {
			totalTilesNeeded++
			if present[y][x] == '#' {
				filledTilesNeeded++
			}
		}
	}
	return totalTilesNeeded, filledTilesNeeded
}

type Tree struct {
	Presents []int
	Grid     [][]rune
}

func (tree Tree) CanFitAllPresents(presentsShapes []Present) FitState {
	totalAvailableSpace := len(tree.Grid) * len(tree.Grid[0])
	totalNeededSpace, filledNeededSpace := 0, 0
	for idx, cnt := range tree.Presents {
		totalTilesNeeded, filledTilesNeeded := presentsShapes[idx].CountNecessarySpaces()
		totalNeededSpace += cnt * totalTilesNeeded
		filledNeededSpace += cnt * filledTilesNeeded
	}

	// This means we can put each shape, including their free spaces without doing any work
	if totalNeededSpace <= totalAvailableSpace {
		return FitEasily
	}

	// This means that the number of filled spaces is bigger than the size of the grid, so no arrangement is possible
	if filledNeededSpace > totalAvailableSpace {
		return FitImpossible
	}

	// It might work, it might not, but it will need some optimization such as flipping, rotating, and having free
	// spaces of the presents being used to put parts of other presents.
	return MayFitWithOptimization
}
