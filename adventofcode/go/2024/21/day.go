package main

import (
	"embed"
	"fmt"
	"math"
	"reflect"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type Pair[T comparable] struct {
	First  T
	Second T
}

var numericPad = map[string]aocutils.Point{
	"7": {X: 0, Y: 0},
	"8": {X: 1, Y: 0},
	"9": {X: 2, Y: 0},
	"4": {X: 0, Y: 1},
	"5": {X: 1, Y: 1},
	"6": {X: 2, Y: 1},
	"1": {X: 0, Y: 2},
	"2": {X: 1, Y: 2},
	"3": {X: 2, Y: 2},
	"0": {X: 1, Y: 3},
	"A": {X: 2, Y: 3},
}

var directionalPad = map[string]aocutils.Point{
	"^": {X: 1, Y: 0},
	"A": {X: 2, Y: 0},
	"<": {X: 0, Y: 1},
	"v": {X: 1, Y: 1},
	">": {X: 2, Y: 1},
}

var directions = map[aocutils.Point]string{
	aocutils.Point{X: 0, Y: 1}:  "v",
	aocutils.Point{X: 0, Y: -1}: "^",
	aocutils.Point{X: 1, Y: 0}:  ">",
	aocutils.Point{X: -1, Y: 0}: "<",
}

var cache = map[string]int{}

var optimalNumericPaths = generateNumericPadOptimalPaths()
var optimalDirectionalPaths = generateDirectionalPadOptimalPaths()

func generateNumericPadOptimalPaths() map[Pair[string]][]string {
	numGraph := aocutils.Graph[string]{
		"A": aocutils.Edges[string]{
			"0": 1,
			"3": 1,
		},
		"0": aocutils.Edges[string]{
			"2": 1,
			"A": 1,
		},
		"1": aocutils.Edges[string]{
			"2": 1,
			"4": 1,
		},
		"2": aocutils.Edges[string]{
			"1": 1,
			"5": 1,
			"3": 1,
			"0": 1,
		},
		"3": aocutils.Edges[string]{
			"2": 1,
			"A": 1,
			"6": 1,
		},
		"4": aocutils.Edges[string]{
			"1": 1,
			"5": 1,
			"7": 1,
		},
		"5": aocutils.Edges[string]{
			"2": 1,
			"4": 1,
			"6": 1,
			"8": 1,
		},
		"6": aocutils.Edges[string]{
			"3": 1,
			"9": 1,
			"5": 1,
		},
		"7": aocutils.Edges[string]{
			"4": 1,
			"8": 1,
		},
		"8": aocutils.Edges[string]{
			"7": 1,
			"9": 1,
			"5": 1,
		},
		"9": aocutils.Edges[string]{
			"6": 1,
			"8": 1,
		},
	}

	paths := map[Pair[string]][]string{}

	for s := range numGraph {
		_, prevs := numGraph.Dijkstra(s)
		for o := range numGraph {
			if s != o {
				allSubPaths := findAllSubPaths(numericPad, prevs, s, o)
				allSubPathsWithMinEntropy := make([]string, 0, len(allSubPaths))
				for _, subPath := range allSubPaths {
					cntChange := 1
					last := string(subPath[0])
					for _, c := range subPath[1:] {
						sc := string(c)
						if sc != last {
							cntChange++
						}
						last = sc
					}
					if cntChange <= 2 {
						allSubPathsWithMinEntropy = append(allSubPathsWithMinEntropy, subPath)
					}
				}
				paths[Pair[string]{First: s, Second: o}] = allSubPathsWithMinEntropy
			}
		}
	}

	return paths
}

func generateDirectionalPadOptimalPaths() map[Pair[string]][]string {
	dirGraph := aocutils.Graph[string]{
		"A": aocutils.Edges[string]{
			"^": 1,
			">": 1,
		},
		">": aocutils.Edges[string]{
			"v": 1,
			"A": 1,
		},
		"<": aocutils.Edges[string]{
			"v": 1,
		},
		"v": aocutils.Edges[string]{
			"^": 1,
			">": 1,
			"<": 1,
		},
		"^": aocutils.Edges[string]{
			"A": 1,
			"v": 1,
		},
	}

	paths := map[Pair[string]][]string{}

	for s := range dirGraph {
		_, prevs := dirGraph.Dijkstra(s)
		for o := range dirGraph {
			if s != o {
				allSubPaths := findAllSubPaths(directionalPad, prevs, s, o)
				allSubPathsWithMinEntropy := make([]string, 0, len(allSubPaths))
				for _, subPath := range allSubPaths {
					cntChange := 1
					last := string(subPath[0])
					for _, c := range subPath[1:] {
						sc := string(c)
						if sc != last {
							cntChange++
						}
						last = sc
					}
					if cntChange <= 2 {
						allSubPathsWithMinEntropy = append(allSubPathsWithMinEntropy, subPath)
					}
				}
				paths[Pair[string]{First: s, Second: o}] = allSubPathsWithMinEntropy
			}
		}
	}

	return paths
}

func findAllSubPaths(pad map[string]aocutils.Point, prevs map[string][]string, from, to string) []string {
	var allSubPaths []string
	nodePrevs := prevs[to]
	if reflect.DeepEqual(nodePrevs, []string{from}) {
		fromPosition, toPosition := pad[from], pad[to]
		return []string{
			directions[toPosition.Sub(fromPosition)],
		}
	}
	for _, p := range nodePrevs {
		allChildSubPaths := findAllSubPaths(pad, prevs, from, p)
		for _, childSubPath := range allChildSubPaths {
			fP, tP := pad[p], pad[to]
			childSubPath += directions[tP.Sub(fP)]
			allSubPaths = append(allSubPaths, childSubPath)
		}
	}
	return allSubPaths
}

func pathsForSequence(optimalPaths map[Pair[string]][]string, code string) [][]string {
	possibleSequences := [][]string{{""}}
	start := "A"
	for _, c := range code {
		dest := string(c)
		unaryOptimalPaths := optimalPaths[Pair[string]{First: start, Second: dest}]
		if len(unaryOptimalPaths) == 0 {
			for idx := range possibleSequences {
				possibleSequences[idx] = append(possibleSequences[idx], "A")
			}
		} else {
			newOptimalSequencePaths := make([][]string, len(possibleSequences)*len(unaryOptimalPaths))
			for i, unaryOptimalPath := range unaryOptimalPaths {
				for j, existingSequence := range possibleSequences {
					newSequence := make([]string, len(existingSequence)+1)
					for k := range existingSequence {
						newSequence[k] = existingSequence[k]
					}
					newSequence[len(newSequence)-1] = unaryOptimalPath + "A"
					newOptimalSequencePaths[i*len(possibleSequences)+j] = newSequence
				}
			}
			possibleSequences = newOptimalSequencePaths
		}
		start = dest
	}
	return possibleSequences
}

func getShortestSequenceLength(code string, depth int, optimalPaths map[Pair[string]][]string) int {
	repr := fmt.Sprintf("%s||%d", code, depth)
	if v, ok := cache[repr]; ok {
		return v
	}
	sequences := pathsForSequence(optimalPaths, code)
	res := math.MaxInt
	if depth == 0 {
		for _, seq := range sequences {
			seqRes := 0
			for _, part := range seq {
				seqRes += len(part)
			}
			res = min(res, seqRes)
		}
	} else {
		for _, seq := range sequences {
			seqRes := 0
			for _, part := range seq {
				seqRes += getShortestSequenceLength(part, depth-1, optimalDirectionalPaths)
			}
			res = min(res, seqRes)
		}
	}
	cache[repr] = res
	return res
}

func computeComplexity(codes []string, numRobots int) int {
	res := 0
	for _, code := range codes {
		length := getShortestSequenceLength(code, numRobots, optimalNumericPaths)
		seqIntValue := aocutils.MustStringToInt(strings.Replace(code, "A", "", -1))
		res += seqIntValue * length
	}
	return res
}

func main() {
	codes := aocutils.MustGetDayInput(inputFile)
	fmt.Println(computeComplexity(codes, 2))
	fmt.Println(computeComplexity(codes, 25))
}
