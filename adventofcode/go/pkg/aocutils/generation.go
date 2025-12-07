package aocutils

func GenerateSequence(x, y int) []int {
	l := make([]int, y-x+1)
	for i := x; i <= y; i++ {
		l[i-x] = i
	}
	return l
}
