package main

import (
	"embed"
	"fmt"
	"regexp"
	"strings"

	"adventofcode-go/2025/06/internal"
	"adventofcode-go/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var regex = regexp.MustCompile("([\\w+*]+)")

func parseInputPartTwo(lines []string) []internal.MathProblem {
	// Calculate shape of the columns and the lines
	maxLineLength := 0
	var colSizes []int
	for idx, line := range lines[:len(lines)-1] {
		matches := regex.FindAllString(line, -1)
		maxLineLength = max(maxLineLength, len(line))
		if idx == 0 {
			colSizes = make([]int, len(matches))
		}
		for colIdx, m := range matches {
			colSizes[colIdx] = max(colSizes[colIdx], len(m))
		}
	}

	// Gets the raw numbers with their leading or trailing spaces
	var problemsRawNumbers [][]string
	for idx, line := range lines[:len(lines)-1] {
		missingSpaces := maxLineLength - len(line)
		for k := 0; k < missingSpaces; k++ {
			line = line + " "
		}
		rawNumbers := make([]string, len(colSizes))
		i := 0
		for colIdx, colSize := range colSizes {
			rawNumbers[colIdx] = line[i : i+colSize]
			i += colSize + 1
		}
		if idx == 0 {
			problemsRawNumbers = make([][]string, len(rawNumbers))
			for problemIdx := range rawNumbers {
				problemsRawNumbers[problemIdx] = make([]string, len(lines)-1)
			}
		}
		for problemIdx := range rawNumbers {
			problemsRawNumbers[problemIdx][idx] = rawNumbers[problemIdx]
		}
	}

	// Compute the correct numbers
	problemsNumbers := make([][]int, len(problemsRawNumbers))
	for problemIdx, rawNumbers := range problemsRawNumbers {
		numbers := make([]int, len(rawNumbers[0]))
		for j := len(rawNumbers[0]) - 1; j >= 0; j-- {
			finalRawNumber := ""
			for _, rawNumber := range rawNumbers {
				finalRawNumber += rawNumber[j : j+1]
			}
			numbers[j] = aocutils.MustStringToInt(strings.TrimSpace(finalRawNumber))
		}
		problemsNumbers[problemIdx] = numbers
	}

	// Generate the problems
	return internal.MakeProblems(
		problemsNumbers,
		regex.FindAllString(lines[len(lines)-1], -1),
	)
}

func parseInputPartOne(lines []string) []internal.MathProblem {
	var problemsNumbers [][]int
	for idx, line := range lines[:len(lines)-1] {
		matches := regex.FindAllString(line, -1)
		if idx == 0 {
			problemsNumbers = make([][]int, len(matches))
		}
		for problemIdx, rawNumber := range matches {
			problemsNumbers[problemIdx] = append(problemsNumbers[problemIdx], aocutils.MustStringToInt(rawNumber))
		}
	}
	return internal.MakeProblems(
		problemsNumbers,
		regex.FindAllString(lines[len(lines)-1], -1),
	)
}

func runPartOne(lines []string) int {
	return internal.ComputeGrandTotal(parseInputPartTwo(lines))
}

func runPartTwo(lines []string) int {
	return internal.ComputeGrandTotal(parseInputPartOne(lines))
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
