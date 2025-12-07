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

func runMaze(maze [][]string) (string, int) {
	x, y, cnt := 0, 0, 0
	direction := []int{0, 1}
	letters := ""
	for xx, c := range maze[0] {
		if c == "|" {
			x = xx
			break
		}
	}
	toCatch := 0
	for yy := range maze {
		for xx := range maze[yy] {
			if !slices.Contains([]string{"|", "+", "-", " "}, maze[yy][xx]) {
				toCatch++
			}
		}
	}
	for len(letters) < toCatch {
		xx, yy := x+direction[0], y+direction[1]
		v := maze[yy][xx]
		if !slices.Contains([]string{"|", "+", "-", " "}, v) {
			letters = letters + v
		}
		if v == "+" {
			adjacents := [][]int{
				{xx + 1, yy},
				{xx - 1, yy},
				{xx, yy + 1},
				{xx, yy - 1},
			}
			for _, adj := range adjacents {
				if adj[0] != x && adj[1] != y &&
					adj[1] >= 0 && adj[1] < len(maze) &&
					adj[0] >= 0 && adj[0] < len(maze[adj[1]]) &&
					maze[adj[1]][adj[0]] != " " {
					direction = []int{adj[0] - xx, adj[1] - yy}
				}
			}
		}
		x, y = xx, yy
		cnt++
	}
	return letters, cnt + 1
}

func runPartOne(maze [][]string) string {
	letters, _ := runMaze(maze)
	return letters
}

func runPartTwo(maze [][]string) int {
	_, cnt := runMaze(maze)
	return cnt
}

func parseInput() [][]string {
	lines := aocutils.MustGetDayInput(inputFile)
	var maze = make([][]string, len(lines))
	for idx, line := range lines {
		maze[idx] = strings.Split(line, "")
	}
	return maze
}

func main() {
	maze := parseInput()
	fmt.Println(runPartOne(maze))
	fmt.Println(runPartTwo(maze))
}
