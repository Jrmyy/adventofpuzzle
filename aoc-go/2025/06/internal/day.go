package internal

type mathOperator func(a, b int) int

type MathProblem struct {
	Numbers []int

	operator  mathOperator
	initValue int
}

func (m MathProblem) Calculate() int {
	res := m.initValue
	for _, number := range m.Numbers {
		res = m.operator(res, number)
	}
	return res
}

func newAdditionProblem(numbers []int) MathProblem {
	return MathProblem{
		Numbers: numbers,
		operator: func(a, b int) int {
			return a + b
		},
		initValue: 0,
	}
}

func newMultiplyProblem(numbers []int) MathProblem {
	return MathProblem{
		Numbers: numbers,
		operator: func(a, b int) int {
			return a * b
		},
		initValue: 1,
	}
}

func MakeProblems(numbers [][]int, operators []string) []MathProblem {
	if len(numbers) != len(operators) {
		panic("cannot produce problems")
	}
	problems := make([]MathProblem, len(numbers))
	for problemIdx, problemNumbers := range numbers {
		if operators[problemIdx] == "+" {
			problems[problemIdx] = newAdditionProblem(problemNumbers)
		} else {
			problems[problemIdx] = newMultiplyProblem(problemNumbers)
		}
	}
	return problems
}

func ComputeGrandTotal(problems []MathProblem) int {
	res := 0
	for _, problem := range problems {
		res += problem.Calculate()
	}
	return res
}
