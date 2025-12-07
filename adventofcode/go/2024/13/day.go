package main

import (
	"embed"
	"fmt"
	"math"
	"regexp"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var buttonRegex = regexp.MustCompile("X\\+(\\d+), Y\\+(\\d+)")
var prizeRegex = regexp.MustCompile("X=(\\d+), Y=(\\d+)")

func playArcade(lines []string, prizeAdd int) int {
	i := 0
	cnt := 0
	for i < len(lines) {
		rawButtonA := buttonRegex.FindAllStringSubmatch(lines[i], -1)
		buttonA := aocutils.Point{
			X: aocutils.MustStringToInt(rawButtonA[0][1]),
			Y: aocutils.MustStringToInt(rawButtonA[0][2]),
		}

		rawButtonB := buttonRegex.FindAllStringSubmatch(lines[i+1], -1)
		buttonB := aocutils.Point{
			X: aocutils.MustStringToInt(rawButtonB[0][1]),
			Y: aocutils.MustStringToInt(rawButtonB[0][2]),
		}

		rawPrize := prizeRegex.FindAllStringSubmatch(lines[i+2], -1)
		prize := aocutils.Point{
			X: aocutils.MustStringToInt(rawPrize[0][1]) + prizeAdd,
			Y: aocutils.MustStringToInt(rawPrize[0][2]) + prizeAdd,
		}

		ta := float64(buttonB.Y*prize.X-buttonB.X*prize.Y) / float64(buttonB.Y*buttonA.X-buttonB.X*buttonA.Y)
		tb := (float64(prize.Y) - float64(buttonA.Y)*ta) / float64(buttonB.Y)

		if math.Floor(ta) == ta && math.Floor(tb) == tb {
			cnt += 3*int(ta) + int(tb)
		}

		i += 4
	}

	return cnt
}

func runPartOne(lines []string) int {
	return playArcade(lines, 0)
}

func runPartTwo(lines []string) int {
	return playArcade(lines, 10000000000000)
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
