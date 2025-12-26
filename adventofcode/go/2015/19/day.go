package main

import (
	"embed"
	"fmt"
	"iter"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type transformation struct {
	From string
	To   string
}

func parseInput(lines []string) ([]transformation, string) {
	transformations := make([]transformation, len(lines)-2)

	for idx, rawTransformation := range lines[:len(lines)-2] {
		parts := strings.Split(rawTransformation, " => ")
		transformations[idx] = transformation{From: parts[0], To: parts[1]}
	}

	return transformations, lines[len(lines)-1]
}

func generateTransformations(transformations []transformation, current string) iter.Seq[string] {
	return func(yield func(string) bool) {
		for _, t := range transformations {
			for i := 0; i <= len(current)-len(t.From); i++ {
				sub := current[i : i+len(t.From)]
				if sub == t.From {
					newMolecule := current[0:i] + t.To + current[i+len(t.From):]
					if !yield(newMolecule) {
						return
					}
				}
			}
		}
	}
}

func runPartOne(transformations []transformation, medicine string) int {
	uniqueMolecules := aocutils.Set[string]{}
	for molecule := range generateTransformations(transformations, medicine) {
		uniqueMolecules.Add(molecule)
	}
	return len(uniqueMolecules)
}

func runPartTwo(transformations []transformation, target string) int {
	res := 0

	current := target
	for current != "e" {
		tmp := current
		// Try to apply each transformation in reverse
		for _, t := range transformations {
			if !strings.Contains(current, t.To) {
				continue
			}
			current = strings.Replace(current, t.To, t.From, 1)
			res++
		}

		// If no transformation was applied, reset and reshuffle the transformations to try to find a different path
		if tmp == current {
			current = target
			res = 0
			aocutils.Shuffle(transformations)
		}
	}

	return res
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	transformations, target := parseInput(lines)
	fmt.Println(runPartOne(transformations, target))
	fmt.Println(runPartTwo(transformations, target))
}
