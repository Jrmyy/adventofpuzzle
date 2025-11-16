package shared2019

import (
	"fmt"

	"adventofcode-go/pkg/aocutils"
)

type IntcodeProgram struct {
	Inputs  []int64
	Memory  map[int]int64
	Outputs []int64

	inputIdx     int
	position     int
	relativeBase int
}

func (p *IntcodeProgram) Run(maxOutputSize int) error {
	for {
		instruction := p.formatInstruction()
		opCode := p.getOpCode(instruction)

		var err error
		operation, err := operationFactory(opCode)
		if err != nil {
			return err
		}
		if operation == nil {
			break
		}

		p.position, err = operation(p, p.position, instruction)
		if err != nil {
			return err
		}

		if maxOutputSize >= 0 && len(p.Outputs) >= maxOutputSize {
			return nil
		}
	}
	return nil
}

func (p *IntcodeProgram) formatInstruction() string {
	return fmt.Sprintf("%05d", p.Memory[p.position])
}

func (p *IntcodeProgram) getOpCode(instruction string) int {
	return aocutils.MustStringToInt(instruction[len(instruction)-2:])
}

func (p *IntcodeProgram) AddInputs(newInputs []int64) {
	p.Inputs = append(p.Inputs, newInputs...)
}

func (p *IntcodeProgram) ResetInputs(newInputs []int64) {
	p.Inputs = newInputs
	p.inputIdx = 0
}

func (p *IntcodeProgram) ClearOutputs() {
	p.Outputs = []int64{}
}

func NewIntcodeProgram(memory map[int]int64, inputs []int64) *IntcodeProgram {
	return &IntcodeProgram{
		Inputs:  inputs,
		Memory:  memory,
		Outputs: []int64{},

		inputIdx:     0,
		position:     0,
		relativeBase: 0,
	}
}
