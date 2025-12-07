package shared2017

import (
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

func runKnotHashRounds(l []int, instructions []int, numRounds int) {
	skip, idx := 0, 0
	for r := 0; r < numRounds; r++ {
		for _, ins := range instructions {
			for i := 0; i < ins/2; i++ {
				from := (idx + i) % len(l)
				to := (idx + ins - 1 - i) % len(l)
				l[from], l[to] = l[to], l[from]
			}
			idx += ins + skip
			skip++
		}
	}
}

func KnotHash(original string) string {
	var instructions []int
	for _, unicode := range original {
		instructions = append(instructions, int(unicode))
	}
	instructions = append(instructions, []int{17, 31, 73, 47, 23}...)
	l := aocutils.GenerateSequence(0, 255)
	runKnotHashRounds(l, instructions, 64)
	blocks := make([]string, 16)
	for b := 0; b < 16; b++ {
		lb := l[b*16+1 : (b+1)*16]
		xorOpt := l[b*16]
		for _, el := range lb {
			xorOpt = xorOpt ^ el
		}
		blocks[b] = fmt.Sprintf("%02x", xorOpt)
	}
	return strings.Join(blocks, "")
}

func RunKnotHashRounds(instructions []int, numRounds int) int {
	l := aocutils.GenerateSequence(0, 255)
	skip, idx := 0, 0
	for r := 0; r < numRounds; r++ {
		for _, ins := range instructions {
			for i := 0; i < ins/2; i++ {
				from := (idx + i) % len(l)
				to := (idx + ins - 1 - i) % len(l)
				l[from], l[to] = l[to], l[from]
			}
			idx += ins + skip
			skip++
		}
	}
	return l[0] * l[1]
}
