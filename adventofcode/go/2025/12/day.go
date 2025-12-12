package main

import (
	"embed"
	"fmt"
	"regexp"
	"strings"

	"adventofcode/2025/12/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) ([]internal.Present, []internal.Tree) {
	currentIdx := 0
	presentIdx := -1

	var presents []internal.Present
	var trees []internal.Tree

	currentPresent := internal.Present{}

	for currentIdx < len(lines) {
		if m, _ := regexp.MatchString("^\\d:$", lines[currentIdx]); m {
			presentIdx++
		} else if strings.TrimSpace(lines[currentIdx]) == "" {
			presents = append(presents, currentPresent)
			currentPresent = internal.Present{}
		} else if m, _ := regexp.MatchString("^\\d+x\\d+:[\\d ]+$", lines[currentIdx]); m {
			parts := strings.Split(lines[currentIdx], ":")
			dimensions := strings.Split(parts[0], "x")
			var grid [][]rune
			for y := 0; y < aocutils.MustStringToInt(dimensions[1]); y++ {
				var gridLine []rune
				for x := 0; x < aocutils.MustStringToInt(dimensions[0]); x++ {
					gridLine = append(gridLine, '.')
				}
				grid = append(grid, gridLine)
			}
			rawPresentsToFit := strings.Split(strings.TrimSpace(parts[1]), " ")
			presentsToFit := make([]int, len(rawPresentsToFit))
			for idx := range presentsToFit {
				presentsToFit[idx] = aocutils.MustStringToInt(rawPresentsToFit[idx])
			}
			trees = append(trees, internal.Tree{
				Presents: presentsToFit,
				Grid:     grid,
			})
		} else {
			currentPresent = append(currentPresent, []rune(lines[currentIdx]))
		}
		currentIdx++
	}

	return presents, trees
}

func runPartOne(presentsShapes []internal.Present, trees []internal.Tree) int {
	res := 0
	for _, tree := range trees {
		fitState := tree.CanFitAllPresents(presentsShapes)
		if fitState != internal.FitImpossible {
			res++
		}
		if fitState == internal.MayFitWithOptimization {
			fmt.Println("This tree has to be checked")
		}
	}
	return res
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	presentsShapes, trees := parseInput(ipt)
	fmt.Println(runPartOne(presentsShapes, trees))
}
