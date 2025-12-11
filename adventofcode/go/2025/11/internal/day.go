package internal

import (
	"strings"
)

type Network map[string][]string

func (network Network) countPathsWithCache(start, end string, cache map[string]int) int {
	if start == end {
		return 1
	}

	if cachedValue, ok := cache[start]; ok {
		return cachedValue
	}

	count := 0
	for _, dest := range network[start] {
		count += network.countPathsWithCache(dest, end, cache)
	}

	cache[start] = count
	return count
}

func (network Network) CountPaths(start, end string) int {
	return network.countPathsWithCache(start, end, map[string]int{})
}

func NewNetwork(lines []string) Network {
	network := Network{}
	for _, line := range lines {
		parts := strings.Split(line, ":")
		start := parts[0]
		for _, end := range strings.Split(strings.TrimSpace(parts[1]), " ") {
			network[start] = append(network[start], end)
		}
	}
	return network
}
