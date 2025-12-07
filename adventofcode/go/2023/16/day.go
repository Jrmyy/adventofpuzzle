package main

import (
	"embed"
	"fmt"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var directions = map[rune][]int{
	'R': {1, 0},
	'L': {-1, 0},
	'U': {0, -1},
	'D': {0, 1},
}

type beam struct {
	X         int
	Y         int
	Direction rune
}

func (b beam) Repr() string {
	return fmt.Sprintf("%v,%v,%v", b.X, b.Y, b.Direction)
}

func getTilesEnergy(ipt [][]string, start beam) int {
	beams := []beam{start}
	visited := map[string]bool{beams[0].Repr(): true}
	for len(beams) > 0 {
		newBeams := make([]beam, 0, 2*len(beams))
		for _, b := range beams {
			direction := directions[b.Direction]
			b.X += direction[0]
			b.Y += direction[1]
			if b.X < 0 || b.X >= len(ipt[0]) || b.Y < 0 || b.Y >= len(ipt) {
				continue
			}
			r := b.Repr()
			_, ok := visited[r]
			if ok {
				continue
			}
			visited[r] = true
			switch ipt[b.Y][b.X] {
			case "/":
				switch b.Direction {
				case 'R':
					b.Direction = 'U'
				case 'U':
					b.Direction = 'R'
				case 'L':
					b.Direction = 'D'
				default:
					b.Direction = 'L'
				}
				newBeams = append(newBeams, b)
			case "\\":
				switch b.Direction {
				case 'R':
					b.Direction = 'D'
				case 'U':
					b.Direction = 'L'
				case 'L':
					b.Direction = 'U'
				default:
					b.Direction = 'R'
				}
				newBeams = append(newBeams, b)
			case "|":
				if b.Direction == 'U' || b.Direction == 'D' {
					newBeams = append(newBeams, b)
				} else {
					bb := beam{X: b.X, Y: b.Y, Direction: 'U'}
					b.Direction = 'D'
					newBeams = append(newBeams, b, bb)
				}
			case "-":
				if b.Direction == 'L' || b.Direction == 'R' {
					newBeams = append(newBeams, b)
				} else {
					bb := beam{X: b.X, Y: b.Y, Direction: 'L'}
					b.Direction = 'R'
					newBeams = append(newBeams, b, bb)
				}
			default:
				newBeams = append(newBeams, b)
			}
		}
		beams = newBeams
	}
	positions := make([]string, 0, len(visited))
	for r := range visited {
		c := r[:len(r)-2]
		if !slices.Contains(positions, c) {
			positions = append(positions, c)
		}
	}
	return len(positions) - 1
}

func runPartOne(ipt [][]string) int {
	return getTilesEnergy(ipt, beam{X: -1, Y: 0, Direction: 'R'})
}

func runPartTwo(ipt [][]string) int {
	m := 0
	for y := range ipt {
		e := getTilesEnergy(ipt, beam{X: -1, Y: y, Direction: 'R'})
		if e > m {
			m = e
		}
		e = getTilesEnergy(ipt, beam{X: len(ipt[0]), Y: y, Direction: 'L'})
		if e > m {
			m = e
		}
	}
	for x := range ipt[0] {
		e := getTilesEnergy(ipt, beam{X: x, Y: -1, Direction: 'D'})
		if e > m {
			m = e
		}
		e = getTilesEnergy(ipt, beam{X: x, Y: len(ipt), Direction: 'U'})
		if e > m {
			m = e
		}
	}
	return m
}

func parseInput() [][]string {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([][]string, 0, len(lines))
	for _, line := range lines {
		ipt = append(ipt, strings.Split(line, ""))
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
