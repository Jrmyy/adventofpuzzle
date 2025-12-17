package main

import (
	"embed"
	"fmt"
	"strconv"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne(lines []string) int {
	initialCnt, decodedCnt := 0, 0
	for _, line := range lines {
		initialCnt += len(line)
		unquoted, err := strconv.Unquote(line)
		if err != nil {
			panic(err)
		}
		decodedCnt += len(unquoted)
	}
	return initialCnt - decodedCnt
}

func runPartTwo(lines []string) int {
	initialCnt, encodedCnt := 0, 0
	for _, line := range lines {
		initialCnt += len(line)
		encodedCnt += len(strconv.Quote(line))
	}
	return encodedCnt - initialCnt
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
