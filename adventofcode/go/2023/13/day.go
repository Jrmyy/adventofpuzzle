package main

import (
	"embed"
	"fmt"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func reverse(s string) string {
	newS := ""
	for i := len(s) - 1; i >= 0; i-- {
		newS += string(s[i])
	}
	return newS
}

type symmetricalLine struct {
	Direction string
	Number    int
}

var factors = map[string]int{
	"h": 100,
	"v": 1,
}

func getAxis(grid []string) []symmetricalLine {
	var matches []symmetricalLine
	for y := range grid {
		u, d := grid[:y], grid[y:]
		if len(u) > 0 && len(d) > 0 {
			m := min(len(u), len(d))
			u, d = u[y-m:], d[:m]
			isHorizontallySymmetrical := true
			for i := range u {
				if u[i] != d[len(d)-1-i] {
					isHorizontallySymmetrical = false
					break
				}
			}
			if isHorizontallySymmetrical {
				matches = append(matches, symmetricalLine{Direction: "h", Number: y})
			}
		}
	}
	for x := 1; x < len(grid[0]); x++ {
		l := make([]string, 0, len(grid))
		r := make([]string, 0, len(grid))
		m := min(x, len(grid[0])-x)
		mm := max(0, x-m)
		for _, ll := range grid {
			l = append(l, ll[mm:m+mm])
			r = append(r, reverse(ll[x:x+m]))
		}
		if len(l) > 0 && len(r) > 0 {
			isVerticallySymmetrical := true
			for i := range l {
				if l[i] != r[i] {
					isVerticallySymmetrical = false
					break
				}
			}
			if isVerticallySymmetrical {
				matches = append(matches, symmetricalLine{Direction: "v", Number: x})
			}
		}
	}
	return matches
}

func runPartOne(ipt [][]string) int {
	s := 0
	for _, p := range ipt {
		matches := getAxis(p)
		if len(matches) != 1 {
			panic(fmt.Sprintf("should have only one match, have %v", len(matches)))
		}
		s += factors[matches[0].Direction] * matches[0].Number
	}
	return s
}

func runPartTwo(ipt [][]string) int {
	s := 0
	axis := make([]symmetricalLine, len(ipt))
	for idx, p := range ipt {
		matches := getAxis(p)
		if len(matches) != 1 {
			panic(fmt.Sprintf("should have only one match, have %v", len(matches)))
		}
		axis[idx] = matches[0]
	}
	for idx, p := range ipt {
		found := false
		for y := range p {
			for x := range p[0] {
				c := "."
				if p[y][x] == '.' {
					c = "#"
				}
				pp := slices.Clone(p)
				pp[y] = pp[y][:x] + c + pp[y][x+1:]
				matches := getAxis(pp)
				if len(matches) > 0 {
					diffIdx := slices.IndexFunc(
						matches, func(line symmetricalLine) bool {
							return line.Direction != axis[idx].Direction || line.Number != axis[idx].Number
						},
					)
					if diffIdx >= 0 {
						match := matches[diffIdx]
						s += factors[match.Direction] * match.Number
						found = true
						break
					}
				}
			}
			if found {
				break
			}
		}
		if !found {
			panic(fmt.Sprintf("did not found any smudge for pattern %v", idx))
		}
	}
	return s
}

func parseInput() [][]string {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([][]string, 0, len(lines))
	curr := make([]string, 0, len(lines))
	for _, line := range lines {
		if line == "" {
			ipt = append(ipt, curr)
			curr = []string{}
		} else {
			curr = append(curr, line)
		}
	}
	if len(curr) > 0 {
		ipt = append(ipt, curr)
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
