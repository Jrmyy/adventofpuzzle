package shared2018

import (
	"strings"

	"adventofcode/pkg/aocutils"
)

var bools = map[bool]int{
	true:  1,
	false: 0,
}

func addr(op, registers []int) {
	registers[op[2]] = registers[op[0]] + registers[op[1]]
}

func addi(op, registers []int) {
	registers[op[2]] = registers[op[0]] + op[1]
}

func mulr(op, registers []int) {
	registers[op[2]] = registers[op[0]] * registers[op[1]]
}

func muli(op, registers []int) {
	registers[op[2]] = registers[op[0]] * op[1]
}

func banr(op, registers []int) {
	registers[op[2]] = registers[op[0]] & registers[op[1]]
}

func bani(op, registers []int) {
	registers[op[2]] = registers[op[0]] & op[1]
}

func borr(op, registers []int) {
	registers[op[2]] = registers[op[0]] | registers[op[1]]
}

func bori(op, registers []int) {
	registers[op[2]] = registers[op[0]] | op[1]
}

func setr(op, registers []int) {
	registers[op[2]] = registers[op[0]]
}

func seti(op, registers []int) {
	registers[op[2]] = op[0]
}

func gtir(op, registers []int) {
	a := op[0]
	b := registers[op[1]]
	registers[op[2]] = bools[a > b]
}

func gtri(op, registers []int) {
	a := registers[op[0]]
	b := op[1]
	registers[op[2]] = bools[a > b]
}

func gtrr(op, registers []int) {
	a := registers[op[0]]
	b := registers[op[1]]
	registers[op[2]] = bools[a > b]
}

func eqir(op, registers []int) {
	a := op[0]
	b := registers[op[1]]
	registers[op[2]] = bools[a == b]
}

func eqri(op, registers []int) {
	a := registers[op[0]]
	b := op[1]
	registers[op[2]] = bools[a == b]
}

func eqrr(op, registers []int) {
	a := registers[op[0]]
	b := registers[op[1]]
	registers[op[2]] = bools[a == b]
}

var Operators = map[string]func([]int, []int){
	"addr": addr,
	"addi": addi,
	"mulr": mulr,
	"muli": muli,
	"banr": banr,
	"bani": bani,
	"borr": borr,
	"bori": bori,
	"setr": setr,
	"seti": seti,
	"gtir": gtir,
	"gtri": gtri,
	"gtrr": gtrr,
	"eqir": eqir,
	"eqri": eqri,
	"eqrr": eqrr,
}

func RunProcess(ipt []string, registers []int) int {
	instructionPointer := aocutils.MustStringToInt(strings.TrimPrefix(ipt[0], "#ip "))
	instructions := ipt[1:]
	for registers[instructionPointer] < len(instructions) {
		pos := registers[instructionPointer]
		instruction := instructions[pos]
		parts := strings.Split(instruction, " ")
		opName := parts[0]
		opArgs := make([]int, len(parts)-1)
		for idx, i := range parts[1:] {
			opArgs[idx] = aocutils.MustStringToInt(i)
		}
		opFn := Operators[opName]
		opFn(opArgs, registers)
		registers[instructionPointer]++
	}
	return registers[0]
}
