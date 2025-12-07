package main

import (
	"embed"
	"fmt"
	"math"
	"os"
	"reflect"
	"strconv"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func getComboOperand(operand int, registers []int) int {
	if operand <= 3 {
		return operand
	}
	if operand <= 6 {
		return registers[operand-4]
	}
	if operand == 7 {
		os.Exit(1)
	}
	return -1
}

func runProgram(xa int, program []int) []int {
	registers := []int{xa, 0, 0}
	idx := 0
	var outputs []int
	for idx < len(program) {
		instruction := program[idx]
		operand := program[idx+1]
		switch instruction {
		case 0:
			denominator := getComboOperand(operand, registers)
			registers[0] = int(math.Floor(float64(registers[0]) / math.Pow(2, float64(denominator))))
			idx += 2
		case 1:
			registers[1] = registers[1] ^ operand
			idx += 2
		case 2:
			registers[1] = getComboOperand(operand, registers) % 8
			idx += 2
		case 3:
			if registers[0] == 0 {
				idx += 2
			} else {
				idx = operand
			}
		case 4:
			registers[1] = registers[1] ^ registers[2]
			idx += 2
		case 5:
			opt := getComboOperand(operand, registers) % 8
			outputs = append(outputs, opt)
			idx += 2
		case 6:
			denominator := getComboOperand(operand, registers)
			registers[1] = int(math.Floor(float64(registers[0]) / math.Pow(2, float64(denominator))))
			idx += 2
		case 7:
			denominator := getComboOperand(operand, registers)
			registers[2] = int(math.Floor(float64(registers[0]) / math.Pow(2, float64(denominator))))
			idx += 2
		}
	}
	return outputs
}

func parseInput(ipt []string) (int, []int) {
	ra := aocutils.MustStringToInt(strings.TrimSpace(strings.Split(ipt[0], ":")[1]))
	rawProgram := strings.Split(strings.TrimSpace(strings.Split(ipt[4], ":")[1]), ",")
	program := make([]int, len(rawProgram))
	for idx := range rawProgram {
		program[idx] = aocutils.MustStringToInt(rawProgram[idx])
	}
	return ra, program
}

func runPartOne(ipt []string) string {
	ra, program := parseInput(ipt)
	outputs := runProgram(ra, program)
	strOutput := ""
	for _, opt := range outputs {
		strOutput += strconv.Itoa(opt) + ","
	}
	return strOutput[0 : len(strOutput)-1]
}

func runPartTwo(ipt []string) int {
	// First I needed to find at least one number which contains 16 outputs and the 6 first elements,
	// it is the start of the loop. Then there is pattern for matching number outputs with the first 6 elements.
	// When there is a number (ra) matching the first 6 elements of the program, the next one will be ra+2, and
	// then ra + 4 194 302. Then I try each solution by incrementing by the gap until I found the right one.
	// Since we want to return 16 figures and ra is divided by 8 (rounded to the lower value) I know that the result
	// cannot exceed 8**16.
	mi := 35_000_041_546_651
	ma := int(math.Pow(8, 16))
	_, program := parseInput(ipt)
	ra := mi
	for ra <= ma {
		outputs := runProgram(ra, program)
		if reflect.DeepEqual(outputs, program) {
			return ra
		}
		ra += 2
		outputs = runProgram(ra, program)
		if reflect.DeepEqual(outputs, program) {
			return ra
		}
		ra += 4_194_302
	}
	return -1
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
