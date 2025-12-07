package main

import (
	"fmt"
	"slices"
)

func runPartOne(ipt int) int {
	l := make([]int, 1, 2017)
	currPos := 0
	l[0] = 0
	for i := 1; i <= 2017; i++ {
		newPos := (currPos+ipt)%len(l) + 1
		if newPos == len(l) {
			l = append(l, i)
		} else {
			l = slices.Insert(l, newPos, i)
		}
		currPos = newPos
	}
	idx := slices.Index(l, 2017)
	return l[(idx+1)%len(l)]
}

func runPartTwo(ipt int) int {
	currPos := 0
	l := 1
	j := -1
	for i := 1; i <= 50_000_000; i++ {
		newPos := (currPos+ipt)%l + 1
		if newPos == 1 {
			j = i
		}
		l++
		currPos = newPos
	}
	return j
}

func main() {
	ipt := 386
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
