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

func runPartOne(ipt []string) int {
	cnt := 0
	for _, line := range ipt {
		cnt += strings.Count(line, "XMAS")
		cnt += strings.Count(line, "SAMX")
	}

	for i := range ipt[0] {
		line := ""
		for j := range ipt {
			line += string(ipt[j][i])
		}
		cnt += strings.Count(line, "XMAS")
		cnt += strings.Count(line, "SAMX")
	}

	patterns := []string{"XMAS", "SAMX"}
	for y := range ipt[0 : len(ipt)-3] {
		for x := range ipt[y][0 : len(ipt[y])-3] {
			left := ""
			right := ""
			for i := 0; i < 4; i++ {
				left += string(ipt[y+i][x+i])
				right += string(ipt[y+3-i][x+i])
			}
			if slices.Contains(patterns, left) {
				cnt++
			}
			if slices.Contains(patterns, right) {
				cnt++
			}
		}
	}

	return cnt
}

func runPartTwo(ipt []string) int {
	cnt := 0
	patterns := []string{"MAS", "SAM"}
	for y := range ipt[0 : len(ipt)-2] {
		for x := range ipt[y][0 : len(ipt[y])-2] {
			left := ""
			right := ""
			for i := 0; i < 3; i++ {
				left += string(ipt[y+i][x+i])
				right += string(ipt[y+2-i][x+i])
			}
			if slices.Contains(patterns, left) && slices.Contains(patterns, right) {
				cnt++
			}
		}
	}

	return cnt
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
