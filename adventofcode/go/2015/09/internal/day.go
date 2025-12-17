package internal

import (
	"math"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

type DistanceRegistry map[aocutils.Pair[string]]int

func (registry DistanceRegistry) Add(placeA, placeB string, distance int) {
	registry[aocutils.Pair[string]{First: placeA, Second: placeB}] = distance
	registry[aocutils.Pair[string]{First: placeB, Second: placeA}] = distance
}

type LocationRegistry = aocutils.Set[string]

type PathAlgorithm struct {
	distanceRegistry DistanceRegistry
	locationRegistry LocationRegistry
	cache            map[string]int
	evalFunc         func(currValue, newValue int) int
	evalInitValue    int
}

func (algorithm PathAlgorithm) Run() int {
	return algorithm.runRecursively("", algorithm.locationRegistry.Clone())
}

func (algorithm PathAlgorithm) runRecursively(currentLocation string, toVisit LocationRegistry) int {
	cacheKey := hashKey(currentLocation, toVisit)
	if cached, ok := algorithm.cache[cacheKey]; ok {
		return cached
	}

	if len(toVisit) == 0 {
		return 0
	}

	newToVisit := toVisit.Clone()
	newToVisit.Delete(currentLocation)

	optimizedValue := algorithm.evalInitValue
	for next := range toVisit {
		distance := algorithm.distanceRegistry[aocutils.Pair[string]{First: currentLocation, Second: next}]
		optimizedValue = algorithm.evalFunc(
			optimizedValue,
			distance+algorithm.runRecursively(next, newToVisit),
		)
	}

	algorithm.cache[cacheKey] = optimizedValue
	return optimizedValue
}

func hashKey(currentLocation string, toVisit aocutils.Set[string]) string {
	sortedToVisit := make([]string, 0, len(toVisit))
	for location := range toVisit {
		sortedToVisit = append(sortedToVisit, location)
	}
	slices.Sort(sortedToVisit)
	return currentLocation + "|" + strings.Join(sortedToVisit, ",")
}

func NewShortestPathAlgorithm(distanceRegistry DistanceRegistry, locationRegistry LocationRegistry) PathAlgorithm {
	return PathAlgorithm{
		distanceRegistry: distanceRegistry,
		locationRegistry: locationRegistry,
		cache:            make(map[string]int),
		evalFunc: func(currValue, newValue int) int {
			return min(currValue, newValue)
		},
		evalInitValue: math.MaxInt,
	}
}

func NewLongestPathAlgorithm(distanceRegistry DistanceRegistry, locationRegistry LocationRegistry) PathAlgorithm {
	return PathAlgorithm{
		distanceRegistry: distanceRegistry,
		locationRegistry: locationRegistry,
		cache:            make(map[string]int),
		evalFunc: func(currValue, newValue int) int {
			return max(currValue, newValue)
		},
		evalInitValue: math.MinInt,
	}
}
