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

func runDance(programs []string, instructions []string) []string {
	for _, instruction := range instructions {
		switch instruction[0] {
		case 'x':
			parts := strings.Split(instruction[1:], "/")
			a, b := aocutils.MustStringToInt(parts[0]), aocutils.MustStringToInt(parts[1])
			programs[a], programs[b] = programs[b], programs[a]
		case 'p':
			parts := strings.Split(instruction[1:], "/")
			a, b := slices.Index(programs, parts[0]), slices.Index(programs, parts[1])
			programs[a], programs[b] = programs[b], programs[a]
		case 's':
			spinSize := aocutils.MustStringToInt(instruction[1:])
			newPrograms := make([]string, 16)
			for idx, p := range programs {
				newPrograms[(idx+spinSize)%len(programs)] = p
			}
			programs = newPrograms
		default:
			panic("Do not know this command")
		}

	}
	return programs
}

func runPartOne(ipt []string) string {
	programs := make([]string, 0, 16)
	for c := 'a'; c <= 'p'; c++ {
		programs = append(programs, fmt.Sprintf("%c", c))
	}
	programs = runDance(programs, ipt)
	return strings.Join(programs, "")
}

func runPartTwo(ipt []string) string {
	programs := make([]string, 0, 16)
	for c := 'a'; c <= 'p'; c++ {
		programs = append(programs, fmt.Sprintf("%c", c))
	}
	initialPrograms := "abcdefghijklmnop"
	cache := []string{initialPrograms}
	for true {
		programs = runDance(programs, ipt)
		s := strings.Join(programs, "")
		if s == initialPrograms {
			break
		}
		cache = append(cache, s)
	}
	return cache[1_000_000_000%len(cache)]
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
