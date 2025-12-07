package shared2019

func ToASCII(input string) []int64 {
	var ascii []int64
	for _, r := range input {
		ascii = append(ascii, int64(r))
	}
	return ascii
}
