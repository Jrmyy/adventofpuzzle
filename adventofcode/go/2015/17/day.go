package main

import (
	"embed"
	"fmt"
	"math"
	"math/bits"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) []int {
	containers := make([]int, len(lines))
	for idx, line := range lines {
		containers[idx] = aocutils.MustStringToInt(line)
	}
	slices.SortFunc(containers, func(a, b int) int {
		return a - b
	})
	return containers
}

func countUsedContainers(mask int) int {
	return bits.OnesCount(uint(mask))
}

func findCombinations(toFit int, containers []int) (int, map[int]int) {
	type state struct{ toFit, containersMask int }

	totalCombinations := 0
	queue := []state{
		{
			toFit:          toFit,
			containersMask: 0,
		},
	}
	usedContainersDistribution := map[int]int{}
	visited := aocutils.Set[int]{}
	for len(queue) > 0 {
		currentState := queue[0]
		queue = queue[1:]

		if _, seen := visited[currentState.containersMask]; seen || currentState.toFit < 0 {
			continue
		}

		visited.Add(currentState.containersMask)

		if currentState.toFit == 0 {
			totalCombinations++
			usedContainersDistribution[countUsedContainers(currentState.containersMask)] += 1
		}

		for idx, container := range containers {
			// Check if the container is already used in the current mask
			if currentState.containersMask&(1<<idx) != 0 {
				continue
			}

			// Skip if the container exceeds the remaining target
			if container > currentState.toFit {
				break
			}

			queue = append(
				queue,
				state{
					toFit:          currentState.toFit - container,
					containersMask: currentState.containersMask | (1 << idx),
				},
			)
		}
	}
	return totalCombinations, usedContainersDistribution
}

func runPartOne(containers []int) int {
	totalCombinations, _ := findCombinations(150, containers)
	return totalCombinations
}

func runPartTwo(containers []int) int {
	_, usedContainersDistribution := findCombinations(150, containers)
	minUsedContainers := math.MaxInt
	minUsedCombinations := 0
	for usedContainers, combinationsCount := range usedContainersDistribution {
		if usedContainers < minUsedContainers {
			minUsedContainers = usedContainers
			minUsedCombinations = combinationsCount
		}
	}
	return minUsedCombinations
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	containers := parseInput(ipt)
	fmt.Println(runPartOne(containers))
	fmt.Println(runPartTwo(containers))
}
