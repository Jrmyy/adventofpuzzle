package main

import (
	"embed"
	"fmt"
	"os"
	"slices"
	"strconv"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type Operation struct {
	First  string
	Second string
	Op     string
}

func parseWires(lines []string) (map[string]int, map[string]Operation) {
	wires := map[string]int{}
	operations := map[string]Operation{}
	for _, line := range lines {
		if line == "" {
			continue
		}
		if strings.Contains(line, ":") {
			parts := strings.Split(line, ": ")
			wires[parts[0]] = aocutils.MustStringToInt(parts[1])
		} else {
			parts := strings.Split(line, " -> ")
			res := parts[1]
			rawOperation := strings.Split(parts[0], " ")
			operations[res] = Operation{First: rawOperation[0], Second: rawOperation[2], Op: rawOperation[1]}
		}
	}
	return wires, operations
}

func runPartOne(lines []string) int64 {
	wires, operations := parseWires(lines)
	mZ := 0

	for {
		done := true
		for res, op := range operations {
			vFirst, okFirst := wires[op.First]
			vSecond, okSecond := wires[op.Second]
			_, okRes := wires[res]
			if okFirst && okSecond && !okRes {
				if strings.HasPrefix(res, "z") {
					mZ = max(mZ, aocutils.MustStringToInt(strings.TrimPrefix(res, "z")))
				}
				done = false
				switch op.Op {
				case "AND":
					wires[res] = vFirst & vSecond
				case "OR":
					wires[res] = vFirst | vSecond
				case "XOR":
					wires[res] = vFirst ^ vSecond
				default:
					os.Exit(1)
				}
			}
		}
		if done {
			break
		}
	}

	binaryRepresentation := ""
	for z := mZ; z >= 0; z-- {
		wire := wires[fmt.Sprintf("z%02d", z)]
		binaryRepresentation += strconv.Itoa(wire)
	}

	i, _ := strconv.ParseInt(binaryRepresentation, 2, 64)
	return i
}

// Basically, we have here a ripple-carry adder https://en.wikipedia.org/wiki/Adder_(electronics)#Ripple-carry_adder
// A ripple-carry adder is a succession of full adders in which the carry from the last step in injected into the next
// one. The formula is then:
// Zn = Cn-1 XOR (Xn XOR Yn)
// Cn = (Xn AND Yn) OR (Cn-1 AND (Xn XOR Yn))
//
// So the following rules applies
// 1. Every operation on z must be a XOR (except for z45 because the output is the final carry so it must be a OR)
// 2. XOR operation should only apply on xn, yn and zn
// 3. AND operators must be chained with an OR (this is for the carry operation)
// 4. XOR operations must only be linked be used in AND (for carry operation) or XOR (for sum operation)
func runPartTwo(lines []string) string {
	_, operations := parseWires(lines)
	wrongWires := make([]string, 0, 8)
	inOutChar := []string{"x", "y", "z"}
	for res, op := range operations {
		if res == "z45" {
			continue
		}

		if res[0] == 'z' && op.Op != "XOR" {
			if !slices.Contains(wrongWires, res) {
				wrongWires = append(wrongWires, res)
			}
		}

		if op.Op == "XOR" &&
			!slices.Contains(inOutChar, string(op.First[0])) &&
			!slices.Contains(inOutChar, string(op.Second[0])) &&
			!slices.Contains(inOutChar, string(res[0])) {
			if !slices.Contains(wrongWires, res) {
				wrongWires = append(wrongWires, res)
			}
		}

		// We filter out the x00 because the first adder can be either a full adder or a half adder with the formula
		// being: S (z00) = A (x00) XOR B (y00) which is our case and C (c00) = A (x00) AND B (y00), which is also our
		// case
		if op.Op == "AND" && op.First != "x00" && op.Second != "x00" {
			for _, parentOp := range operations {
				if (parentOp.First == res || parentOp.Second == res) && parentOp.Op != "OR" {
					if !slices.Contains(wrongWires, res) {
						wrongWires = append(wrongWires, res)
					}
				}
			}
		}

		if op.Op == "XOR" {
			for _, parentOp := range operations {
				if (parentOp.First == res || parentOp.Second == res) && parentOp.Op == "OR" {
					if !slices.Contains(wrongWires, res) {
						wrongWires = append(wrongWires, res)
					}
				}
			}
		}
	}

	slices.Sort(wrongWires)
	return strings.Join(wrongWires, ",")
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
