package main

import (
	"embed"
	"fmt"
	"strconv"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func runPartOne(ipt []string) int64 {
	registers := map[string]int64{}
	var snd int64 = 0
	i := 0
	for i < len(ipt) {
		inst := ipt[i]
		parts := strings.Split(inst, " ")
		op := parts[0]
		shouldInc := true
		switch op {
		case "snd":
			snd = registers[parts[1]]
		case "rcv":
			x := parts[1]
			xx, ok := registers[x]
			if ok && xx > 0 {
				return snd
			}
		default:
			x, y := parts[1], parts[2]
			yy := getIntOrRegisterValue(registers, y)
			switch op {
			case "set":
				registers[x] = yy
			case "add":
				registers[x] += yy
			case "mul":
				registers[x] *= yy
			case "mod":
				registers[x] %= yy
			default:
				xx := getIntOrRegisterValue(registers, x)
				if xx > 0 {
					i += int(yy)
					shouldInc = false
				}
			}
		}
		if shouldInc {
			i++
		}
	}
	panic("should not happen")
}

func getIntOrRegisterValue(registers map[string]int64, registerKeyOrInt string) int64 {
	yy := int64(0)
	if yyy, err := strconv.ParseInt(registerKeyOrInt, 10, 64); err == nil {
		yy = yyy
	} else {
		yyy, ok := registers[registerKeyOrInt]
		if ok {
			yy = yyy
		}
	}
	return yy
}

func runPartTwo(ipt []string) int {
	allRegisters := []map[string]int64{
		{"p": 0},
		{"p": 1},
	}
	allQueues := [][]int64{
		{},
		{},
	}
	indices := []int{0, 0}
	cnt := 0
	blocked := []bool{false, false}
	pIdx := 0
	for !blocked[0] || !blocked[1] {
		for !blocked[pIdx] {
			pInstIdx := indices[pIdx]
			inst := ipt[pInstIdx]
			parts := strings.Split(inst, " ")
			op := parts[0]
			shouldInc := true
			registers := allRegisters[pIdx]
			switch op {
			case "snd":
				blocked[pIdx] = false
				x := parts[1]
				xx := getIntOrRegisterValue(registers, x)
				allQueues[(pIdx+1)%2] = append(allQueues[(pIdx+1)%2], xx)
				if pIdx == 1 {
					cnt++
				}
			case "rcv":
				if len(allQueues[pIdx]) == 0 {
					oldPIdx, newPIdx := pIdx, (pIdx+1)%2
					blocked[oldPIdx] = true
					blocked[newPIdx] = len(allQueues[newPIdx]) == 0
					pIdx = newPIdx
					shouldInc = false
				} else {
					blocked[pIdx] = false
					registers[parts[1]] = allQueues[pIdx][0]
					allQueues[pIdx] = allQueues[pIdx][1:]
				}
			default:
				blocked[pIdx] = false
				x, y := parts[1], parts[2]
				yy := getIntOrRegisterValue(registers, y)
				switch op {
				case "set":
					registers[x] = yy
				case "add":
					registers[x] += yy
				case "mul":
					registers[x] *= yy
				case "mod":
					registers[x] %= yy
				default:
					xx := getIntOrRegisterValue(registers, x)
					if xx > 0 {
						indices[pIdx] += int(yy)
						shouldInc = false
					}
				}
			}
			if shouldInc {
				indices[pIdx]++
			}
		}
	}
	return cnt
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
