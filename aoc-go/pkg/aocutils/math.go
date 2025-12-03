package aocutils

func GcdList(l []int) int {
	g := l[1]
	for _, i := range l[1:] {
		g = Gcd(g, i)
	}
	return g
}

func Gcd(a, b int) int {
	for a != b {
		if a > b {
			a -= b
		} else {
			b -= a
		}
	}
	return a
}

func Lcm(a, b int) int {
	return a * b / Gcd(a, b)
}

func LcmList(l []int) int {
	lcm := l[0]
	for _, i := range l[1:] {
		lcm = Lcm(lcm, i)
	}
	return lcm
}

func Abs(i int) int {
	if i < 0 {
		return -i
	}
	return i
}

func ModPow(base, exponent, mod int64) int64 {
	var result int64 = 1
	base = (base + mod) % mod
	exponent = (exponent + mod) % mod
	for exponent > 0 {
		if exponent%2 == 1 {
			result = ModMultiply(result, base, mod)
		}
		exponent /= 2
		base = ModMultiply(base, base, mod)
	}
	return result
}

func ModMultiply(a, b, mod int64) int64 {
	var result int64 = 0
	a = (a + mod) % mod
	b = (b + mod) % mod
	for a != 0 {
		if a%2 == 1 {
			result = (result + b) % mod
		}
		a /= 2
		b = (b * 2) % mod
	}
	return (result + mod) % mod
}

func ModInv(a, n int64) int64 {
	return ModPow(a, n-2, n)
}
