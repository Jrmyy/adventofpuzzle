package main

import (
	"embed"
	"fmt"

	"adventofcode/2025/11/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) internal.Network {
	return internal.NewNetwork(lines)
}

func runPartOne(network internal.Network) int {
	return network.CountPaths("you", "out")
}

func runPartTwo(network internal.Network) int {
	fftDacPathsCount := network.CountPaths("svr", "fft") *
		network.CountPaths("fft", "dac") *
		network.CountPaths("dac", "out")

	dacFftPathsCount := network.CountPaths("svr", "dac") *
		network.CountPaths("dac", "fft") *
		network.CountPaths("fft", "out")

	return fftDacPathsCount + dacFftPathsCount
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	network := parseInput(ipt)
	fmt.Println(runPartOne(network))
	fmt.Println(runPartTwo(network))
}
