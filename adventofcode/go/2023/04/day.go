package main

import (
	"embed"
	"fmt"
	"regexp"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne(ipt [][][]int) int {
	sum := 0
	for _, c := range ipt {
		wns, ans := c[0], c[1]
		matches := 0
		for _, an := range ans {
			if slices.Contains(wns, an) {
				if matches == 0 {
					matches += 1
				} else {
					matches *= 2
				}
			}
		}
		sum += matches
	}
	return sum
}

func runPartTwo(ipt [][][]int) int {
	sum := 0
	wins := make([]int, len(ipt))
	for j := range wins {
		wins[j] = 1
	}
	for cc, c := range ipt {
		wns, ans := c[0], c[1]
		matching := 0
		for _, an := range ans {
			if slices.Contains(wns, an) {
				matching++
			}
		}
		if matching > 0 {
			for i := cc + 1; i <= cc+matching; i++ {
				wins[i] += wins[cc]
			}
		}
	}
	for _, w := range wins {
		sum += w
	}
	return sum
}

func parseInput() [][][]int {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([][][]int, 0, len(lines))
	for _, line := range lines {
		re := regexp.MustCompile("Card +\\d+:(( +\\d+)+) \\|(( +\\d+)+)")
		matches := re.FindStringSubmatch(line)
		parts := []string{matches[1], matches[3]}
		l := make([][]int, 2)
		for pp, p := range parts {
			numbersStr := strings.Split(p, " ")
			numbers := make([]int, 0, len(numbersStr))
			for _, ns := range numbersStr {
				if ns != "" {
					numbers = append(numbers, aocutils.MustStringToInt(ns))
				}
			}
			l[pp] = numbers
		}
		ipt = append(ipt, l)
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
