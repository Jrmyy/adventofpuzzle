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

func hash(s string) int {
	h := 0
	for _, c := range s {
		h += int(c)
		h *= 17
		h %= 256
	}
	return h
}

func runPartOne(ipt []string) int {
	s := 0
	for _, p := range ipt {
		s += hash(p)
	}
	return s
}

func runPartTwo(ipt []string) int {
	boxes := [256][]string{}
	for _, p := range ipt {
		if strings.HasSuffix(p, "-") {
			lens := strings.TrimSuffix(p, "-")
			idx := hash(lens)
			indexOf := slices.IndexFunc(
				boxes[idx], func(s string) bool {
					return strings.Split(s, " ")[0] == lens
				},
			)
			if indexOf >= 0 {
				boxes[idx] = slices.Delete(boxes[idx], indexOf, indexOf+1)
			}
		} else {
			num := aocutils.MustStringToInt(string(p[len(p)-1]))
			lens := p[:len(p)-2]
			idx := hash(lens)
			r := fmt.Sprintf("%v %v", lens, num)
			indexOf := slices.IndexFunc(
				boxes[idx], func(s string) bool {
					return strings.Split(s, " ")[0] == lens
				},
			)
			if indexOf >= 0 {
				boxes[idx][indexOf] = r
			} else {
				boxes[idx] = append(boxes[idx], r)
			}
		}
	}
	s := 0
	for bi, b := range boxes {
		for li, l := range b {
			s += (bi + 1) * (li + 1) * aocutils.MustStringToInt(strings.Split(l, " ")[1])
		}
	}
	return s
}

func parseInput() []string {
	line := aocutils.MustGetDayInput(inputFile)[0]
	return strings.Split(line, ",")
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
