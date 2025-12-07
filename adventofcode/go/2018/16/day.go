package main

import (
	"embed"
	"fmt"
	"reflect"
	"regexp"
	"strings"

	"adventofcode/pkg/aocutils"
	"adventofcode/pkg/shared/2018"
)

//go:embed input.txt
var inputFile embed.FS

var re = regexp.MustCompile(`(?m)(\d+)`)

func parseRegisters(rawRegisters string) []int {
	var registers []int
	for _, match := range re.FindAllString(rawRegisters, -1) {
		i := aocutils.MustStringToInt(match)
		registers = append(registers, i)
	}
	return registers
}

func parseOp(rawOp string) []int {
	parts := strings.Split(rawOp, " ")
	op := make([]int, len(parts))
	for idx, value := range parts {
		i := aocutils.MustStringToInt(value)
		op[idx] = i
	}
	return op
}

func runPartOne(lines []string) int {
	idx := 0
	cnt := 0
	for idx < len(lines) {
		rawBefore, rawOp, rawAfter := lines[idx], lines[idx+1], lines[idx+2]
		if rawBefore == "" {
			break
		}
		before := parseRegisters(rawBefore)
		after := parseRegisters(rawAfter)
		op := parseOp(rawOp)

		opMatch := 0
		for _, opFn := range shared2018.Operators {
			opBefore := make([]int, len(before))
			copy(opBefore, before)
			opFn(op[1:], opBefore)
			if reflect.DeepEqual(opBefore, after) {
				opMatch++
			}
		}
		if opMatch >= 3 {
			cnt++
		}

		idx += 4
	}
	return cnt
}

func runPartTwo(lines []string) int {
	idx := 0
	possibleMatches := map[int]map[string]bool{}
	for idx < len(lines) {
		rawBefore, rawOp, rawAfter := lines[idx], lines[idx+1], lines[idx+2]
		if rawBefore == "" {
			break
		}
		before := parseRegisters(rawBefore)
		after := parseRegisters(rawAfter)
		op := parseOp(rawOp)

		for opName, opFn := range shared2018.Operators {
			opBefore := make([]int, len(before))
			copy(opBefore, before)
			opFn(op[1:], opBefore)
			if reflect.DeepEqual(opBefore, after) {
				if matches, ok := possibleMatches[op[0]]; !ok {
					possibleMatches[op[0]] = map[string]bool{
						opName: true,
					}
				} else {
					matches[opName] = true
				}
			}
		}
		idx += 4
	}

	realMatches := map[int]string{}
	for len(realMatches) != len(possibleMatches) {
		var toDelete string
		for opCode, possible := range possibleMatches {
			if len(possible) == 1 {
				for opName := range possible {
					realMatches[opCode] = opName
					toDelete = opName
				}
			}
			break
		}
		for _, possible := range possibleMatches {
			delete(possible, toDelete)
		}
	}

	registers := []int{0, 0, 0, 0}

	for idx < len(lines) {
		line := lines[idx]
		idx++
		if line == "" {
			continue
		}

		op := parseOp(line)
		opFn := shared2018.Operators[realMatches[op[0]]]
		opFn(op[1:], registers)
	}

	return registers[0]
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
