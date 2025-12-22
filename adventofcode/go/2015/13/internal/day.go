package internal

import (
	"math"
	"regexp"
	"strings"

	"adventofcode/pkg/aocutils"
)

var sentenceRegex = regexp.MustCompile("^(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+).$")

type HappinessRegistry map[aocutils.Pair[string]]int

type SeatingArrangementAlgorithm struct {
	happinessRegistry HappinessRegistry
	guests            aocutils.Set[string]
}

func (algorithm SeatingArrangementAlgorithm) AddNeutralGuest(name string) {
	for guest := range algorithm.guests {
		algorithm.happinessRegistry[aocutils.Pair[string]{First: name, Second: guest}] = 0
		algorithm.happinessRegistry[aocutils.Pair[string]{First: guest, Second: name}] = 0
	}
	algorithm.guests.Add(name)
}

func (algorithm SeatingArrangementAlgorithm) Optimize() int {
	return algorithm.optimize([]string{}, algorithm.guests)
}

func (algorithm SeatingArrangementAlgorithm) cacheKey(currentSeating []string) string {
	return strings.Join(currentSeating, ",")
}

func (algorithm SeatingArrangementAlgorithm) computeTotalHappiness(finalSeating []string) int {
	totalHappiness := 0
	for i := 0; i < len(finalSeating); i++ {
		current := finalSeating[i]
		next := finalSeating[(i+1)%len(finalSeating)]
		totalHappiness += algorithm.happinessRegistry[aocutils.Pair[string]{First: current, Second: next}]
		totalHappiness += algorithm.happinessRegistry[aocutils.Pair[string]{First: next, Second: current}]
	}
	return totalHappiness
}

func (algorithm SeatingArrangementAlgorithm) optimize(currentSeating []string, toBeSeated aocutils.Set[string]) int {
	if len(toBeSeated) == 0 {
		return algorithm.computeTotalHappiness(currentSeating)
	}

	maxHappiness := math.MinInt
	for next := range toBeSeated {
		newToBeSeated := toBeSeated.Clone()
		newToBeSeated.Delete(next)
		maxHappiness = max(
			maxHappiness,
			algorithm.optimize(append(currentSeating, next), newToBeSeated),
		)
	}

	return maxHappiness
}

func NewSeatingArrangementAlgorithm(rawConstraints []string) SeatingArrangementAlgorithm {
	happinessRegistry := HappinessRegistry{}
	guests := aocutils.Set[string]{}
	for _, line := range rawConstraints {
		matches := sentenceRegex.FindStringSubmatch(line)[1:]
		happiness := aocutils.MustStringToInt(matches[2])
		if matches[1] == "lose" {
			happiness *= -1
		}
		guests.Add(matches[0])
		happinessRegistry[aocutils.Pair[string]{First: matches[0], Second: matches[3]}] = happiness
	}
	return SeatingArrangementAlgorithm{
		happinessRegistry: happinessRegistry,
		guests:            guests,
	}
}
