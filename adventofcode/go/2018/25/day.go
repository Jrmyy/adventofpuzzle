package main

import (
	"embed"
	"fmt"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

type Point4D struct {
	A int
	B int
	C int
	D int
}

func (p Point4D) InRange(o Point4D) bool {
	return aocutils.Abs(p.A-o.A)+aocutils.Abs(p.B-o.B)+aocutils.Abs(p.C-o.C)+aocutils.Abs(p.D-o.D) <= 3
}

//go:embed input.txt
var inputFile embed.FS

func runPartOne(ipt []string) int {
	points := make([]Point4D, len(ipt))
	seen := map[Point4D]bool{}
	for idx, line := range ipt {
		coordinates := strings.Split(strings.TrimSpace(line), ",")
		p := Point4D{
			A: aocutils.MustStringToInt(coordinates[0]),
			B: aocutils.MustStringToInt(coordinates[1]),
			C: aocutils.MustStringToInt(coordinates[2]),
			D: aocutils.MustStringToInt(coordinates[3]),
		}
		points[idx] = p
	}

	var constellations [][]Point4D
	for len(seen) < len(points) {
		var q []Point4D
		var c []Point4D
		for _, p := range points {
			if _, ok := seen[p]; !ok {
				q = []Point4D{p}
				c = []Point4D{p}
				break
			}
		}

		for len(q) > 0 {
			u := q[0]
			seen[u] = true
			q = q[1:]
			for _, p := range points {
				if p != u && u.InRange(p) && !slices.Contains(c, p) {
					q = append(q, p)
					c = append(c, p)
					seen[p] = true
				}
			}
		}

		constellations = append(constellations, c)
	}

	return len(constellations)
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
}
