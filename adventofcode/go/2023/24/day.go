package main

import (
	"embed"
	"fmt"
	"math"
	"regexp"

	"adventofcode/pkg/aocutils"
)

type hailstone struct {
	X  float64
	Y  float64
	Z  float64
	Vx float64
	Vy float64
	Vz float64
}

//go:embed input.txt
var inputFile embed.FS

var epsilon = 1e-11

func nearlyEqual(a, b, epsilon float64) bool {
	if a == b {
		return true
	}

	diff := math.Abs(a - b)

	if a == 0.0 || b == 0.0 || diff < math.SmallestNonzeroFloat64 {
		return diff < epsilon*math.SmallestNonzeroFloat64
	}

	return diff/(math.Abs(a)+math.Abs(b)) < epsilon
}

func runPartOne(ipt []hailstone) int {
	cnt, mi, ma := 0, 200000000000000.0, 400000000000000.0
	// To know where the 2 hailstones are going to cross paths we have to solve
	// xa + ta * vxa = xb + tb * vxb
	// ya + ta * vya = yb + tb * vyb
	// This 2 equations can determine the formula for ta and tb
	// ta = (xb - xa + tb * vxb) / vxa
	// tb = (vxa * (yb - ya) + vya * (xa - xb)) / (vya * vxb - vyb * vxa)
	// Once we have ta and tb we just need to get xi and yi and check if it is between mi and ma
	for ai, a := range ipt {
		for _, b := range ipt[ai+1:] {
			tbd := a.Vy*b.Vx - a.Vx*b.Vy

			// Means parallel
			if nearlyEqual(tbd, 0, epsilon) {
				continue
			}

			tb := (a.Vx*(b.Y-a.Y) + a.Vy*(a.X-b.X)) / tbd
			ta := (b.X - a.X + tb*b.Vx) / a.Vx
			xia := a.X + ta*a.Vx
			xib := b.X + tb*b.Vx
			yia := a.Y + ta*a.Vy
			yib := b.Y + tb*b.Vy

			// If ta or tb is negative, this means we are in the past
			if ta < 0 || tb < 0 {
				continue
			}

			// If xia !~= xib or yia !~= yib, this means we have an error in the calculation
			if !nearlyEqual(xia, xib, epsilon) || !nearlyEqual(yia, yib, epsilon) {
				panic("should not happen")
			}

			if xia >= mi && xia <= ma && yia >= mi && yia <= ma {
				cnt++
			}
		}
	}
	return cnt
}

func parseInput() []hailstone {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([]hailstone, len(lines))
	re := regexp.MustCompile("(-*\\d+)+")
	for idx, line := range lines {
		m := re.FindAllString(line, -1)
		ipt[idx] = hailstone{
			X:  aocutils.MustStringToFloat64(m[0]),
			Y:  aocutils.MustStringToFloat64(m[1]),
			Z:  aocutils.MustStringToFloat64(m[2]),
			Vx: aocutils.MustStringToFloat64(m[3]),
			Vy: aocutils.MustStringToFloat64(m[4]),
			Vz: aocutils.MustStringToFloat64(m[5]),
		}
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	// For part 2, see day.py
}
