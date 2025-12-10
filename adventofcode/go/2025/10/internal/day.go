package internal

import (
	"slices"
	"strconv"
	"strings"

	"adventofcode/pkg/aocutils"
)

var indicatorMapping = map[rune]string{
	'.': "0",
	'#': "1",
}

type Button []int

func (b Button) ToBitMask(maxLength int) int64 {
	var bitmask int64
	for indicatorIdx := 0; indicatorIdx < maxLength; indicatorIdx++ {
		if slices.Contains(b, indicatorIdx) {
			bitmask |= 1 << (maxLength - indicatorIdx - 1)
		}
	}
	return bitmask
}

type Machine struct {
	startStateRequirement int64
	buttons               []Button
	joltageRequirement    []int

	indicatorsCount int
}

func (machine Machine) getAllUnitaryButtonsMasks() []int64 {
	combinations := make([]int64, len(machine.buttons))
	for idx, b := range machine.buttons {
		combinations[idx] = b.ToBitMask(machine.indicatorsCount)
	}
	return combinations
}

func (machine Machine) GetFewestPressesToStart() int {
	unitaryButtons := machine.getAllUnitaryButtonsMasks()

	visited := aocutils.Set[int64]{}
	queue := aocutils.NewPriorityQueue[int64]()
	queue.AddWithPriority(0, 0)

	for queue.IsNotEmpty() {
		state, fewestPressed := queue.ExtractMinWithPriority()
		if state == machine.startStateRequirement {
			return fewestPressed
		}

		if visited.Has(state) {
			continue
		}
		visited.Add(state)

		for _, unitaryButton := range unitaryButtons {
			queue.AddWithPriority(state^unitaryButton, fewestPressed+1)
		}
	}

	panic("should have found a solution")
}

func getStartState(rawStartStateRequirement string) int64 {
	startStateBinaryRepresentation := ""
	for _, rawStartIndicator := range rawStartStateRequirement {
		startStateBinaryRepresentation += indicatorMapping[rawStartIndicator]
	}

	startStateRequirement, err := strconv.ParseInt(startStateBinaryRepresentation, 2, 64)
	if err != nil {
		panic(err)
	}

	return startStateRequirement
}

func getButtons(rawButtons []string) []Button {
	buttons := make([]Button, len(rawButtons))
	for buttonIdx, rawButton := range rawButtons {
		rawButtonImpactedIndexes := strings.Split(rawButton[1:len(rawButton)-1], ",")
		currentButton := make([]int, len(rawButtonImpactedIndexes))
		for currentButtonIdx, i := range rawButtonImpactedIndexes {
			currentButton[currentButtonIdx] = aocutils.MustStringToInt(i)
		}
		buttons[buttonIdx] = currentButton
	}
	return buttons
}

func getJoltageRequirement(rawJoltageRequirements []string) []int {
	joltageRequirements := make([]int, len(rawJoltageRequirements))
	for rawJoltageIdx, rawJoltage := range rawJoltageRequirements {
		joltageRequirements[rawJoltageIdx] = aocutils.MustStringToInt(rawJoltage)
	}
	return joltageRequirements
}

func NewMachine(line string) Machine {
	bracketsParts := strings.Split(line, "]")
	curlyParts := strings.Split(bracketsParts[1], "{")

	rawStartStateRequirement := bracketsParts[0][1:]
	startStateRequirement := getStartState(rawStartStateRequirement)

	rawButtons := strings.Split(strings.TrimSpace(curlyParts[0]), " ")
	buttons := getButtons(rawButtons)

	rawJoltageRequirements := strings.Split(curlyParts[1][:len(curlyParts[1])-1], ",")
	joltageRequirements := getJoltageRequirement(rawJoltageRequirements)

	return Machine{
		startStateRequirement: startStateRequirement,
		buttons:               buttons,
		joltageRequirement:    joltageRequirements,
		indicatorsCount:       len(rawStartStateRequirement),
	}
}
