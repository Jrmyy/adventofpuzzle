package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func innerP1(ipt [][]string, x int, y int, ds string) int {
	xx := x - len(ds)
	isAdjacentToSymbol := false
	for xxx := max(xx-1, 0); xxx <= min(x, len(ipt[0])-1); xxx++ {
		for yy := max(y-1, 0); yy <= min(y+1, len(ipt)-1); yy++ {
			if ipt[yy][xxx] != "." && (ipt[yy][xxx] < "0" || ipt[yy][xxx] > "9") {
				isAdjacentToSymbol = true
			}
		}
	}
	if isAdjacentToSymbol {
		return aocutils.MustStringToInt(ds)
	}
	return 0
}

func runPartOne(ipt [][]string) int {
	sum := 0
	isCurrentNumber := false
	for y, line := range ipt {
		ds := ""
		for x, c := range line {
			if c >= "0" && c <= "9" {
				if !isCurrentNumber {
					isCurrentNumber = true
				}
				ds += c
			} else {
				if ds != "" {
					sum += innerP1(ipt, x, y, ds)
					isCurrentNumber = false
					ds = ""
				}
			}
		}
		if ds != "" {
			sum += innerP1(ipt, len(ipt[y]), y, ds)

		}
	}
	return sum
}

func innerP2(ipt [][]string, x, y int, ds string, gears map[string][]int) {
	xx := x - len(ds)
	for xxx := max(xx-1, 0); xxx <= min(x, len(ipt[0])-1); xxx++ {
		for yy := max(y-1, 0); yy <= min(y+1, len(ipt)-1); yy++ {
			if ipt[yy][xxx] == "*" {
				key := fmt.Sprintf("%v-%v", yy, xxx)
				_, ok := gears[key]
				if !ok {
					gears[key] = make([]int, 0, 2)
				}
				gears[key] = append(gears[key], aocutils.MustStringToInt(ds))
			}
		}
	}
}

func runPartTwo(ipt [][]string) int {
	sum := 0
	isCurrentNumber := false
	gears := map[string][]int{}
	for y, line := range ipt {
		ds := ""
		for x, c := range line {
			if c >= "0" && c <= "9" {
				if !isCurrentNumber {
					isCurrentNumber = true
				}
				ds += c
			} else {
				if ds != "" {
					innerP2(ipt, x, y, ds, gears)
					isCurrentNumber = false
					ds = ""
				}
			}
		}
		if ds != "" {
			innerP2(ipt, len(ipt[y]), y, ds, gears)
			isCurrentNumber = false
			ds = ""
		}
	}
	for _, v := range gears {
		if len(v) == 2 {
			sum += v[0] * v[1]
		}
	}
	return sum
}

func parseInput() [][]string {
	lines := aocutils.MustGetDayInput(inputFile)
	ipt := make([][]string, len(lines))
	for i, l := range lines {
		ipt[i] = strings.Split(l, "")
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
