package aocutils

import "math/rand"

type Pair[T comparable] struct {
	First  T
	Second T
}

func Shuffle[T any](l []T) {
	for i := range l {
		j := rand.Intn(i + 1)
		l[i], l[j] = l[j], l[i]
	}
}
