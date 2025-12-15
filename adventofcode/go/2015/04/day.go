package main

import (
	"crypto/md5"
	"embed"
	"encoding/hex"
	"fmt"
	"math"
	"strconv"
	"strings"
	"sync"
	"sync/atomic"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func findSmallestPasscode(secretKey string, prefix string, start, step int, minValue *atomic.Int64) {
	i := start
	for {
		code := secretKey + strconv.Itoa(i)
		sum := md5.Sum([]byte(code))
		hash := hex.EncodeToString(sum[:])

		currentMinValue := minValue.Load()
		currentValue := int64(i)

		if currentValue >= currentMinValue {
			return
		}

		if strings.HasPrefix(hash, prefix) {
			minValue.CompareAndSwap(currentMinValue, min(minValue.Load(), currentValue))
			return
		}

		i += step
	}
}

func runPartOne(secretKey string) int64 {
	return findSmallestPasscodeConcurrently(secretKey, 5)
}

func runPartTwo(secretKey string) int64 {
	return findSmallestPasscodeConcurrently(secretKey, 6)
}

func findSmallestPasscodeConcurrently(secretKey string, leadingZeros int) int64 {
	numWorkers := 8
	prefix := strings.Repeat("0", leadingZeros)

	var minValue atomic.Int64
	minValue.Store(math.MaxInt64)

	var wg sync.WaitGroup
	for i := 0; i < numWorkers; i++ {
		wg.Go(func() {
			findSmallestPasscode(secretKey, prefix, i, numWorkers, &minValue)
		})
	}
	wg.Wait()

	return minValue.Load()
}

func main() {
	secretKey := strings.TrimSpace(aocutils.MustGetDayInput(inputFile)[0])
	fmt.Println(runPartOne(secretKey))
	fmt.Println(runPartTwo(secretKey))
}
