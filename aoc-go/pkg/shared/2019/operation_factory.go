package shared2019

import "fmt"

const opCodeHalt = 99

var registry = map[int]operationRunner{
	1: add,
	2: multiply,
	3: saveInput,
	4: saveOutput,
	5: jumpIfTrue,
	6: jumpIfFalse,
	7: lessThan,
	8: equal,
	9: adjustRelativeBase,
}

func operationFactory(opCode int) (operationRunner, error) {
	if opCode == opCodeHalt {
		return nil, nil
	}

	operation, exists := registry[opCode]

	if !exists {
		return nil, fmt.Errorf("unknown opcode: %d", opCode)
	}

	return operation, nil
}
