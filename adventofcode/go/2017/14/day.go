package main

import (
	"fmt"

	shared2017 "adventofcode/pkg/shared/2017"
)

var hexMapping = map[rune][]int{
	'0': {0, 0, 0, 0},
	'1': {0, 0, 0, 1},
	'2': {0, 0, 1, 0},
	'3': {0, 0, 1, 1},
	'4': {0, 1, 0, 0},
	'5': {0, 1, 0, 1},
	'6': {0, 1, 1, 0},
	'7': {0, 1, 1, 1},
	'8': {1, 0, 0, 0},
	'9': {1, 0, 0, 1},
	'a': {1, 0, 1, 0},
	'b': {1, 0, 1, 1},
	'c': {1, 1, 0, 0},
	'd': {1, 1, 0, 1},
	'e': {1, 1, 1, 0},
	'f': {1, 1, 1, 1},
}

func floodFill(disk [][]int, x, y int) {
	if y < 0 || y >= len(disk) || x < 0 || x >= len(disk[y]) || disk[y][x] == 0 {
		return
	}
	disk[y][x] = 0
	floodFill(disk, x+1, y)
	floodFill(disk, x-1, y)
	floodFill(disk, x, y+1)
	floodFill(disk, x, y-1)
}

func buildGrid(ipt string) [][]int {
	var disk = make([][]int, 128)
	for y := 0; y < 128; y++ {
		line := fmt.Sprintf("%v-%v", ipt, y)
		hash := shared2017.KnotHash(line)
		disk[y] = make([]int, 128)
		for x, c := range hash {
			repr, ok := hexMapping[c]
			if !ok {
				panic("Should not happen")
			}
			for xx, n := range repr {
				disk[y][x*4+xx] = n
			}
		}
	}
	return disk
}

func runPartOne(ipt string) int {
	disk := buildGrid(ipt)
	cnt := 0
	for _, l := range disk {
		for _, el := range l {
			if el == 1 {
				cnt++
			}
		}
	}
	return cnt
}

func runPartTwo(ipt string) int {
	disk := buildGrid(ipt)
	groups := 0
	for y := range disk {
		for x := range disk[y] {
			if disk[y][x] == 1 {
				floodFill(disk, x, y)
				groups++
			}
		}
	}
	return groups
}

func main() {
	ipt := "hfdlxzhv"
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
