package main

import (
	"embed"
	"fmt"
	"regexp"
	"slices"

	"adventofcode-go/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var regex = regexp.MustCompile("^pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)$")

type nanobot struct {
	Center aocutils.Point
	Radius int
}

func (n nanobot) Overlaps(other nanobot) bool {
	return n.Center.Dist(other.Center) <= n.Radius
}

func (n nanobot) Contains(p aocutils.Point) bool {
	return n.Center.Dist(p) <= n.Radius
}

func parseInput(lines []string) []nanobot {
	nanobots := make([]nanobot, len(lines))
	for idx, line := range lines {
		match := regex.FindStringSubmatch(line)
		nanobots[idx] = nanobot{
			Center: aocutils.Point{
				X: aocutils.MustStringToInt(match[1]),
				Y: aocutils.MustStringToInt(match[2]),
				Z: aocutils.MustStringToInt(match[3]),
			},
			Radius: aocutils.MustStringToInt(match[4]),
		}
	}
	return nanobots
}

func runPartOne(nanobots []nanobot) int {
	mRadius := 0
	maxCnt := 0
	for _, n1 := range nanobots {
		cnt := 0
		for _, n2 := range nanobots {
			if n1.Overlaps(n2) {
				cnt++
			}
		}
		if n1.Radius >= mRadius {
			maxCnt = cnt
			mRadius = n1.Radius
		}
	}

	return maxCnt
}

func runPartTwo(nanobots []nanobot) int {
	xMap := map[int]int{}
	for _, bot := range nanobots {
		xMin := bot.Center.X + bot.Center.Y + bot.Center.Z - bot.Radius
		xMax := bot.Center.X + bot.Center.Y + bot.Center.Z + bot.Radius + 1

		xMap[xMin]++
		xMap[xMax]--
	}

	xKeys := make([]int, 0, len(xMap))
	for x := range xMap {
		xKeys = append(xKeys, x)
	}
	slices.Sort(xKeys)

	running := 0
	maV := 0
	maxStart := 0
	for _, x := range xKeys {
		v := xMap[x]
		running += v
		if running > maV {
			maV = running
			maxStart = x
		}
	}

	for _, x := range xKeys {
		if x > maxStart {
			return x - 1
		}
	}

	panic("Not found")
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	nanobots := parseInput(ipt)
	fmt.Println(runPartOne(nanobots))
	fmt.Println(runPartTwo(nanobots))
}
