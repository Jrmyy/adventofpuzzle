package main

import (
	"embed"
	"fmt"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func serialize(forest map[aocutils.Point]rune) string {
	repr := make([]rune, len(forest))
	for p, c := range forest {
		repr[p.Y*50+p.X] = c
	}
	return string(repr)
}

func simulate(ipt []string, steps int) int {
	forest := map[aocutils.Point]rune{}
	for y, line := range ipt {
		for x, c := range line {
			forest[aocutils.Point{X: x, Y: y}] = c
		}
	}

	seen := map[string]int{}

	loopSize := 0

	for i := 0; i < steps; i++ {
		newForest := map[aocutils.Point]rune{}
		for p, c := range forest {
			possibleNeighbours := p.Neighbours2D(true)
			neighbours := make([]aocutils.Point, 0, len(possibleNeighbours))
			for _, n := range possibleNeighbours {
				if _, ok := forest[n]; ok {
					neighbours = append(neighbours, n)
				}
			}

			switch c {
			case '.':
				trees := 0
				for _, n := range neighbours {
					if forest[n] == '|' {
						trees++
					}
				}
				if trees >= 3 {
					newForest[p] = '|'
				} else {
					newForest[p] = '.'
				}
			case '|':
				lumberyard := 0
				for _, n := range neighbours {
					if forest[n] == '#' {
						lumberyard++
					}
				}
				if lumberyard >= 3 {
					newForest[p] = '#'
				} else {
					newForest[p] = '|'
				}
			case '#':
				hasTree := false
				hasLumberyard := false
				for _, n := range neighbours {
					hasTree = forest[n] == '|' || hasTree
					hasLumberyard = forest[n] == '#' || hasLumberyard
				}
				if hasTree && hasLumberyard {
					newForest[p] = '#'
				} else {
					newForest[p] = '.'
				}
			}
		}

		serialized := serialize(forest)
		if j, ok := seen[serialized]; ok {
			loopSize = i - j
			initial := len(seen) - loopSize
			return simulate(ipt, initial+(steps-initial)%loopSize)
		} else {
			seen[serialized] = i
		}
		forest = newForest
	}

	cntTrees := 0
	cntLumberyard := 0

	for _, c := range forest {
		if c == '|' {
			cntTrees++
		}
		if c == '#' {
			cntLumberyard++
		}
	}

	return cntTrees * cntLumberyard
}

func runPartOne(ipt []string) int {
	return simulate(ipt, 10)
}

func runPartTwo(ipt []string) int {
	return simulate(ipt, 1000000000)
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
