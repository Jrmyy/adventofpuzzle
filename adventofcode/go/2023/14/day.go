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

func moveNorth(ipt [][]byte) [][]byte {
	for y, l := range ipt {
		for x, v := range l {
			if v == 'O' {
				yy := y - 1
				for true {
					if yy >= 0 && yy < len(ipt) && ipt[yy][x] == '.' {
						yy = yy - 1
					} else {
						break
					}
				}
				ipt[y][x] = '.'
				ipt[yy+1][x] = 'O'
			}
		}
	}
	return ipt
}

func moveSouth(ipt [][]byte) [][]byte {
	for y := len(ipt) - 1; y >= 0; y-- {
		for x, v := range ipt[y] {
			if v == 'O' {
				yy := y + 1
				for true {
					if yy >= 0 && yy < len(ipt) && ipt[yy][x] == '.' {
						yy = yy + 1
					} else {
						break
					}
				}
				ipt[y][x] = '.'
				ipt[yy-1][x] = 'O'
			}
		}
	}
	return ipt
}

func moveWest(ipt [][]byte) [][]byte {
	for y, l := range ipt {
		for x, v := range l {
			if v == 'O' {
				xx := x - 1
				for true {
					if xx >= 0 && xx < len(ipt[0]) && ipt[y][xx] == '.' {
						xx = xx - 1
					} else {
						break
					}
				}
				ipt[y][x] = '.'
				ipt[y][xx+1] = 'O'
			}
		}
	}
	return ipt
}

func moveEast(ipt [][]byte) [][]byte {
	for y, l := range ipt {
		for x := len(ipt[0]) - 1; x >= 0; x-- {
			if l[x] == 'O' {
				xx := x + 1
				for true {
					if xx >= 0 && xx < len(ipt[0]) && ipt[y][xx] == '.' {
						xx = xx + 1
					} else {
						break
					}
				}
				ipt[y][x] = '.'
				ipt[y][xx-1] = 'O'
			}
		}
	}
	return ipt
}

func runPartOne(ipt [][]byte) int {
	ipt = moveNorth(ipt)
	s := 0
	for idx, l := range ipt {
		s += strings.Count(string(l), "O") * (len(ipt) - idx)
	}
	return s
}

func repr(ipt [][]byte) string {
	s := ""
	for y := range ipt {
		s += string(ipt[y]) + ","
	}
	return strings.TrimSuffix(s, ",")
}

func runPartTwo(ipt [][]byte) int {
	cache := []string{repr(ipt)}
	cycleLength := 0
	after := 0
	for i := 1; i <= 1_000_000_000; i++ {
		ipt = moveNorth(ipt)
		ipt = moveWest(ipt)
		ipt = moveSouth(ipt)
		ipt = moveEast(ipt)
		r := repr(ipt)
		if slices.Contains(cache, r) {
			cycleLength = i - slices.Index(cache, r)
			after = i
			break
		}
		cache = append(cache, r)
	}
	todo := 1_000_000_000 - after
	toloop := todo - todo/cycleLength*cycleLength
	for i := 1; i <= toloop; i++ {
		ipt = moveNorth(ipt)
		ipt = moveWest(ipt)
		ipt = moveSouth(ipt)
		ipt = moveEast(ipt)
	}
	s := 0
	for idx, l := range ipt {
		s += strings.Count(string(l), "O") * (len(ipt) - idx)
	}
	return s
}

func parseInput() [][]byte {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([][]byte, 0, len(lines))
	for _, line := range lines {
		ipt = append(ipt, []byte(line))
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	ipt = parseInput()
	fmt.Println(runPartTwo(ipt))
}
