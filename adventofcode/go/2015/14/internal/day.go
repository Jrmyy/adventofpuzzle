package internal

import (
	"regexp"
	"slices"

	"adventofcode/pkg/aocutils"
)

var descriptionRegex = regexp.MustCompile("^(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.$")

type ReindeerState int

type RaceState struct {
	Distance int
	Points   int
}

const (
	reindeerStateFlying = iota
	reindeerStateResting
)

type ReindeerOlympics struct {
	Contestants []*Reindeer
	Duration    int
}

type Reindeer struct {
	Name                string
	KilometersPerSecond int
	FlyingPeriod        int
	RestingPeriod       int

	currentTimer int
	currentState ReindeerState
}

func (reindeer *Reindeer) Prepare() {
	reindeer.currentTimer = reindeer.FlyingPeriod
	reindeer.currentState = reindeerStateFlying
}

func (reindeer *Reindeer) SimulateSecond() int {
	var distance int

	if reindeer.currentState == reindeerStateFlying {
		distance = reindeer.KilometersPerSecond
	}
	reindeer.currentTimer--

	if reindeer.currentTimer <= 0 {
		if reindeer.currentState == reindeerStateFlying {
			reindeer.currentTimer = reindeer.RestingPeriod
			reindeer.currentState = reindeerStateResting
		} else {
			reindeer.currentTimer = reindeer.FlyingPeriod
			reindeer.currentState = reindeerStateFlying
		}
	}

	return distance
}

func (race ReindeerOlympics) FindWinnerByDistance() int {
	return race.findWinner(func(state *RaceState) int {
		return state.Distance
	})
}

func (race ReindeerOlympics) FindWinnerByPoints() int {
	return race.findWinner(func(state *RaceState) int {
		return state.Points
	})
}

func (race ReindeerOlympics) prepare() []*RaceState {
	for _, reindeer := range race.Contestants {
		reindeer.Prepare()
	}

	statuses := make([]*RaceState, len(race.Contestants))
	for idx := range race.Contestants {
		statuses[idx] = &RaceState{Points: 0, Distance: 0}
	}

	return statuses
}

func (race ReindeerOlympics) findWinner(getRankingDimensionFunc func(state *RaceState) int) int {
	statuses := race.prepare()

	for i := 0; i < race.Duration; i++ {
		maxDistance := 0
		leaderIdx := -1
		for idx, reindeer := range race.Contestants {
			statuses[idx].Distance += reindeer.SimulateSecond()
			if statuses[idx].Distance > maxDistance {
				maxDistance = statuses[idx].Distance
				leaderIdx = idx
			}
		}
		statuses[leaderIdx].Points++
	}

	slices.SortFunc(statuses, func(a, b *RaceState) int {
		return getRankingDimensionFunc(b) - getRankingDimensionFunc(a)
	})
	return getRankingDimensionFunc(statuses[0])
}

func NewRace(contestantsDescriptions []string) ReindeerOlympics {
	contestants := make([]*Reindeer, len(contestantsDescriptions))
	for idx, description := range contestantsDescriptions {
		matches := descriptionRegex.FindStringSubmatch(description)[1:]
		contestants[idx] = &Reindeer{
			Name:                matches[0],
			KilometersPerSecond: aocutils.MustStringToInt(matches[1]),
			FlyingPeriod:        aocutils.MustStringToInt(matches[2]),
			RestingPeriod:       aocutils.MustStringToInt(matches[3]),
		}
	}
	return ReindeerOlympics{Contestants: contestants, Duration: 2503}
}
