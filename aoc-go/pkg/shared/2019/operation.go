package shared2019

type operationRunner func(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error)

func add(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	first := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	second := newArg(p.Memory[currentPosition+2], currentInstruction[1])
	destination := newArg(p.Memory[currentPosition+3], currentInstruction[0])
	p.Memory[destination.ResolveDestination(p)] = first.Resolve(p) + second.Resolve(p)
	return currentPosition + 4, nil
}

func multiply(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	first := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	second := newArg(p.Memory[currentPosition+2], currentInstruction[1])
	destination := newArg(p.Memory[currentPosition+3], currentInstruction[0])
	p.Memory[destination.ResolveDestination(p)] = first.Resolve(p) * second.Resolve(p)
	return currentPosition + 4, nil
}

func saveInput(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	destination := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	if p.inputIdx >= len(p.Inputs) {
		return currentPosition, ErrProgramNeedsInput
	}
	p.Memory[destination.ResolveDestination(p)] = p.Inputs[p.inputIdx]
	p.inputIdx++
	return currentPosition + 2, nil
}

func saveOutput(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	output := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	p.Outputs = append(p.Outputs, output.Resolve(p))
	return currentPosition + 2, nil
}

func jumpIfTrue(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	first := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	second := newArg(p.Memory[currentPosition+2], currentInstruction[1])
	if first.Resolve(p) != 0 {
		return int(second.Resolve(p)), nil
	}
	return currentPosition + 3, nil
}

func jumpIfFalse(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	first := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	second := newArg(p.Memory[currentPosition+2], currentInstruction[1])
	if first.Resolve(p) == 0 {
		return int(second.Resolve(p)), nil
	}
	return currentPosition + 3, nil
}

func lessThan(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	first := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	second := newArg(p.Memory[currentPosition+2], currentInstruction[1])
	destination := newArg(p.Memory[currentPosition+3], currentInstruction[0]).ResolveDestination(p)
	if first.Resolve(p) < second.Resolve(p) {
		p.Memory[destination] = 1
	} else {
		p.Memory[destination] = 0
	}
	return currentPosition + 4, nil
}

func equal(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	first := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	second := newArg(p.Memory[currentPosition+2], currentInstruction[1])
	destination := newArg(p.Memory[currentPosition+3], currentInstruction[0]).ResolveDestination(p)
	if first.Resolve(p) == second.Resolve(p) {
		p.Memory[destination] = 1
	} else {
		p.Memory[destination] = 0
	}
	return currentPosition + 4, nil
}

func adjustRelativeBase(p *IntcodeProgram, currentPosition int, currentInstruction string) (int, error) {
	adjustment := newArg(p.Memory[currentPosition+1], currentInstruction[2])
	p.relativeBase += int(adjustment.Resolve(p))
	return currentPosition + 2, nil
}
