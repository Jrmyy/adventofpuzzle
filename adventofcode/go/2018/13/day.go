package main

import (
	"embed"
	"fmt"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

var LeftV = []int{-1, 0}
var RightV = []int{1, 0}
var TopV = []int{0, -1}
var BottomV = []int{0, 1}

type cart struct {
	P       []int
	V       []int
	Turn    string
	Removed bool
}
type cartAndTrack struct {
	Carts []cart
	Track [][]string
}

//go:embed input.txt
var inputFile embed.FS

func applyMove(ipt cartAndTrack, c cart) cart {
	switch ipt.Track[c.P[1]][c.P[0]] {
	case "/":
		if slices.Equal(c.V, LeftV) {
			c.V = BottomV
		} else if slices.Equal(c.V, TopV) {
			c.V = RightV
		} else if slices.Equal(c.V, BottomV) {
			c.V = LeftV
		} else {
			c.V = TopV
		}
	case "\\":
		if slices.Equal(c.V, LeftV) {
			c.V = TopV
		} else if slices.Equal(c.V, TopV) {
			c.V = LeftV
		} else if slices.Equal(c.V, BottomV) {
			c.V = RightV
		} else {
			c.V = BottomV
		}
	case "+":
		switch c.Turn {
		case "L":
			if slices.Equal(c.V, LeftV) {
				c.V = BottomV
			} else if slices.Equal(c.V, TopV) {
				c.V = LeftV
			} else if slices.Equal(c.V, BottomV) {
				c.V = RightV
			} else {
				c.V = TopV
			}
			c.Turn = "S"
		case "S":
			c.Turn = "R"
		default:
			if slices.Equal(c.V, LeftV) {
				c.V = TopV
			} else if slices.Equal(c.V, TopV) {
				c.V = RightV
			} else if slices.Equal(c.V, BottomV) {
				c.V = LeftV
			} else {
				c.V = BottomV
			}
			c.Turn = "L"
		}
	}
	return c
}

func runPartOne(ipt cartAndTrack) string {
	for true {
		for ci, c := range ipt.Carts {
			c.P = []int{c.P[0] + c.V[0], c.P[1] + c.V[1]}
			for _, cc := range ipt.Carts {
				if slices.Equal(cc.P, c.P) {
					return fmt.Sprintf("%v,%v", cc.P[0], cc.P[1])
				}
			}
			ipt.Carts[ci] = applyMove(ipt, c)
		}
	}
	panic("Should not happen")
}

func runPartTwo(ipt cartAndTrack) string {
	for len(ipt.Carts) > 1 {
		for ci, c := range ipt.Carts {
			if !c.Removed {
				c.P = []int{c.P[0] + c.V[0], c.P[1] + c.V[1]}
				for cci, cc := range ipt.Carts {
					if slices.Equal(cc.P, c.P) && !cc.Removed {
						ipt.Carts[cci].Removed = true
						c.Removed = true
						break
					}
				}
				if !c.Removed {
					c = applyMove(ipt, c)
				}
				ipt.Carts[ci] = c
			}
		}
		newCarts := make([]cart, 0, len(ipt.Carts))
		for _, c := range ipt.Carts {
			if !c.Removed {
				newCarts = append(newCarts, c)
			}
		}
		ipt.Carts = newCarts
	}
	return fmt.Sprintf("%v,%v", ipt.Carts[0].P[0], ipt.Carts[0].P[1])
}

func parseInput() cartAndTrack {
	lines := aocutils.MustGetDayInput(inputFile)
	track := make([][]string, len(lines))
	var carts []cart
	for idx, line := range lines {
		split := strings.Split(line, "")
		trackLine := make([]string, len(split))
		for ci, c := range split {
			switch c {
			case ">":
				trackLine[ci] = "-"
				carts = append(
					carts, cart{
						P:    []int{ci, idx},
						V:    []int{1, 0},
						Turn: "L",
					},
				)
			case "<":
				trackLine[ci] = "-"
				carts = append(
					carts, cart{
						P:    []int{ci, idx},
						V:    []int{-1, 0},
						Turn: "L",
					},
				)
			case "^":
				trackLine[ci] = "|"
				carts = append(
					carts, cart{
						P:    []int{ci, idx},
						V:    []int{0, -1},
						Turn: "L",
					},
				)
			case "v":
				trackLine[ci] = "|"
				carts = append(
					carts, cart{
						P:    []int{ci, idx},
						V:    []int{0, 1},
						Turn: "L",
					},
				)
			default:
				trackLine[ci] = c
			}
		}
		track[idx] = trackLine
	}
	return cartAndTrack{
		Track: track,
		Carts: carts,
	}
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	ipt = parseInput()
	fmt.Println(runPartTwo(ipt))
}
