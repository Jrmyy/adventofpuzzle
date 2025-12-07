package shared2019

type parametersMode int

const (
	parametersModePosition = iota
	parametersModeImmediate
	parametersModeRelative
)

func toParameterMode(mode byte) parametersMode {
	switch mode {
	case '0':
		return parametersModePosition
	case '2':
		return parametersModeRelative
	default:
		return parametersModeImmediate
	}
}

type operationArg struct {
	Value          int64
	ParametersMode parametersMode
}

func (arg operationArg) Resolve(p *IntcodeProgram) int64 {
	switch arg.ParametersMode {
	case parametersModePosition:
		return p.Memory[int(arg.Value)]
	case parametersModeRelative:
		return p.Memory[p.relativeBase+int(arg.Value)]
	default:
		return arg.Value
	}
}

func (arg operationArg) ResolveDestination(p *IntcodeProgram) int {
	switch arg.ParametersMode {
	case parametersModeRelative:
		return p.relativeBase + int(arg.Value)
	default:
		return int(arg.Value)
	}
}

func newArg(value int64, rawParameterMode byte) operationArg {
	return operationArg{
		Value:          value,
		ParametersMode: toParameterMode(rawParameterMode),
	}
}
