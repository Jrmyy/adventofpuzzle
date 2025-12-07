package main

import (
	"embed"
	"fmt"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var directions = []aocutils.Point{
	{X: 0, Y: -1},
	{X: 0, Y: 1},
	{X: -1, Y: 0},
	{X: 1, Y: 0},
}

func getZones(ipt []string) [][]aocutils.Point {
	garden := parseInput(ipt)
	seen := map[aocutils.Point]bool{}
	toCheck := []aocutils.Point{
		{X: 0, Y: 0},
	}
	var zones [][]aocutils.Point
	zone := []aocutils.Point{
		{X: 0, Y: 0},
	}
	r := garden[toCheck[0]]
	for len(seen) != len(garden) {
		p := toCheck[0]
		toCheck = toCheck[1:]
		seen[p] = true
		if r == '.' {
			r = garden[p]
		}
		neighbours := p.Neighbours2D(false)
		for _, n := range neighbours {
			if _, ok := seen[n]; garden[n] == r && !slices.Contains(toCheck, n) && !ok {
				zone = append(zone, n)
				toCheck = append(toCheck, n)
			}
		}
		if len(toCheck) == 0 {
			zones = append(zones, zone)
			for np := range garden {
				if _, ok := seen[np]; !ok {
					toCheck = []aocutils.Point{np}
					zone = []aocutils.Point{np}
					break
				}
			}
			if len(toCheck) == 0 {
				break
			}
			r = garden[toCheck[0]]
		}
	}
	return zones
}

func runPartOne(ipt []string) int {
	zones := getZones(ipt)
	price := 0
	for _, z := range zones {
		perimeter := 0
		for _, p := range z {
			for _, n := range p.Neighbours2D(false) {
				if !slices.Contains(z, n) {
					perimeter++
				}
			}
		}
		price += perimeter * len(z)
	}
	return price
}

func runPartTwo(ipt []string) int {
	zones := getZones(ipt)
	price := 0
	for _, z := range zones {
		// The number of sides is equal to the number of corners
		corners := 0
		for _, p := range z {
			for _, d := range directions {
				p1 := p.Add(d)
				orthogonalD := aocutils.Point{X: -d.Y, Y: d.X}
				p2 := p.Add(orthogonalD)
				// Exterior corners are defined by the neighbors in 2 orthogonal directions not being in the zone
				if !slices.Contains(z, p1) && !slices.Contains(z, p2) {
					corners++
				}
				p3 := p.Add(d.Add(orthogonalD))
				// Interior corners are defined by the neighbors in 2 orthogonal directions being in the zone and
				//the neighbor in diagonal not being in the zone
				if slices.Contains(z, p1) && slices.Contains(z, p2) && !slices.Contains(z, p3) {
					corners++
				}
			}
		}
		price += len(z) * corners
	}
	return price
}

func parseInput(ipt []string) map[aocutils.Point]rune {
	garden := map[aocutils.Point]rune{}
	for y := range ipt {
		for x, c := range ipt[y] {
			garden[aocutils.Point{X: x, Y: y}] = c
		}
	}
	return garden
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
