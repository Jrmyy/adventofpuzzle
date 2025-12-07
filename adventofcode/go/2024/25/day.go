package main

import (
	"embed"
	"fmt"
	"os"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func getElement(lines []string, i int) []int {
	element := make([]int, 5)
	for x := 0; x <= 4; x++ {
		s := ""
		for y := 1; y <= 5; y++ {
			s += string(lines[i+y][x])
		}
		element[x] = strings.Count(s, "#")
	}
	return element
}

func runPartOne(lines []string) int {
	var keys, locks [][]int
	i := 0
	for i < len(lines) {
		line := lines[i]
		if line == "" {
			i++
			continue
		} else if strings.Count(line, "#") == len(line) {
			// lock
			lock := getElement(lines, i)
			locks = append(locks, lock)
			i += 7
		} else if strings.Count(line, ".") == len(line) {
			// key
			key := getElement(lines, i)
			keys = append(keys, key)
			i += 7
		} else {
			os.Exit(1)
		}
	}

	cnt := 0
	for _, lock := range locks {
		for _, key := range keys {
			fit := true
			for j := range lock {
				if lock[j]+key[j] > 5 {
					fit = false
					break
				}
			}
			if fit {
				cnt++
			}
		}
	}

	return cnt
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
}
