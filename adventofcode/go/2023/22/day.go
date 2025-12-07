package main

import (
	"embed"
	"fmt"
	"maps"
	"regexp"
	"slices"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type brick struct {
	Name string
	Fx   int
	Fy   int
	Fz   int
	Tx   int
	Ty   int
	Tz   int
}

func overlap(a, aa, b, bb int) bool {
	a, aa = min(a, aa), max(a, aa)
	b, bb = min(b, bb), max(b, bb)

	if b >= a && b <= aa {
		return true
	}

	if bb >= a && bb <= aa {
		return true
	}

	if bb >= aa && b <= a {
		return true
	}

	if aa >= bb && a <= b {
		return true
	}

	return false
}

func (b brick) collide(o brick) bool {
	return overlap(b.Fz, b.Tz, o.Fz, o.Tz) && overlap(b.Fx, b.Tx, o.Fx, o.Tx) && overlap(b.Fy, b.Ty, o.Fy, o.Ty)
}

func fall(ipt map[string]brick) map[string]brick {
	bricks := make([]brick, 0, len(ipt))
	for _, v := range ipt {
		bricks = append(bricks, v)
	}
	slices.SortStableFunc(
		bricks, func(a, b brick) int {
			return a.Fz - b.Fz
		},
	)
	for _, b := range bricks {
		bn := b.Name
		for true {
			if b.Fz == 1 || b.Tz == 1 {
				break
			}
			b.Fz--
			b.Tz--
			collision := false
			for on, o := range ipt {
				if bn != on {
					if b.collide(o) {
						collision = true
						break
					}
				}
			}
			if collision {
				b.Fz++
				b.Tz++
				break
			}
		}
		ipt[bn] = b
	}
	return ipt
}

func runPartOne(ipt map[string]brick) int {
	ipt = fall(ipt)
	canBeDisintegrate := 0
	for k := range ipt {
		newIpt := maps.Clone(ipt)
		delete(newIpt, k)
		afterFallNewIpt := maps.Clone(newIpt)
		afterFallNewIpt = fall(afterFallNewIpt)
		if maps.Equal(afterFallNewIpt, newIpt) {
			canBeDisintegrate++
		}
	}
	return canBeDisintegrate
}

func runPartTwo(ipt map[string]brick) int {
	ipt = fall(ipt)
	cnt := 0
	for k := range ipt {
		newIpt := maps.Clone(ipt)
		delete(newIpt, k)
		afterFallNewIpt := maps.Clone(newIpt)
		afterFallNewIpt = fall(afterFallNewIpt)
		if !maps.Equal(afterFallNewIpt, newIpt) {
			for kk := range newIpt {
				if newIpt[kk] != afterFallNewIpt[kk] {
					cnt++
				}
			}
		}
	}
	return cnt
}

func parseInput() map[string]brick {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = map[string]brick{}
	re := regexp.MustCompile("(\\d+)")
	for idx, line := range lines {
		m := re.FindAllString(line, -1)
		n := fmt.Sprintf("%v", idx)
		ipt[n] = brick{
			Name: n,
			Fx:   aocutils.MustStringToInt(m[0]),
			Fy:   aocutils.MustStringToInt(m[1]),
			Fz:   aocutils.MustStringToInt(m[2]),
			Tx:   aocutils.MustStringToInt(m[3]),
			Ty:   aocutils.MustStringToInt(m[4]),
			Tz:   aocutils.MustStringToInt(m[5]),
		}
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	ipt = parseInput()
	fmt.Println(runPartTwo(ipt))
}
