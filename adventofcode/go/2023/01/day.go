package main

import (
	"embed"
	"fmt"
	"regexp"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var letterDigits = []string{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"}

func findCalibrationValues(lines []string, allowStringDigits bool) int {
	s := 0
	numbers := aocutils.GenerateSequence(1, 9)
	for _, line := range lines {
		miIdx, maIdx, miV, maV := len(line), -1, "", ""
		for _, i := range numbers {
			digit := fmt.Sprintf("%v", i)
			r := regexp.MustCompile(digit)
			indexes := r.FindAllStringIndex(line, -1)
			for _, match := range indexes {
				if match[0] < miIdx {
					miIdx = match[0]
					miV = digit
				}
				if match[0] > maIdx {
					maIdx = match[0]
					maV = digit
				}
			}
		}
		if allowStringDigits {
			for digit, letterDigit := range letterDigits {
				r := regexp.MustCompile(letterDigit)
				strDigit := fmt.Sprintf("%v", digit)
				indexes := r.FindAllStringIndex(line, -1)
				for _, match := range indexes {
					if match[0] < miIdx {
						miIdx = match[0]
						miV = strDigit
					}
					if match[0] > maIdx {
						maIdx = match[0]
						maV = strDigit
					}
				}
			}
		}
		s += aocutils.MustStringToInt(miV + maV)
	}
	return s
}

func runPartOne(ipt []string) int {
	return findCalibrationValues(ipt, false)
}

func runPartTwo(ipt []string) int {
	return findCalibrationValues(ipt, true)
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
