package main

import (
	"embed"
	"fmt"
	"math"
	"strconv"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func getPrice(i int) int {
	s := strconv.Itoa(i)
	price := aocutils.MustStringToInt(string(s[len(s)-1]))
	return price
}

var operations = []func(i int) int{
	func(i int) int { return i * 64 },
	func(i int) int { return int(math.Floor(float64(i) / 32.0)) },
	func(i int) int { return i * 2048 },
}

func getSecretNumber(secretNumber int) int {
	for _, op := range operations {
		operated := op(secretNumber)
		secretNumber = (operated ^ secretNumber) % 16777216
	}
	return secretNumber
}

func runPartTwo(ipt []string) int {
	var prices [][]int
	var diffPrices [][]int
	for _, line := range ipt {
		secretNumber := aocutils.MustStringToInt(line)
		unitaryPrices := []int{getPrice(secretNumber)}
		var unitaryDiffPrices []int
		for j := 0; j < 2000; j++ {
			secretNumber = getSecretNumber(secretNumber)
			unitaryPrices = append(unitaryPrices, getPrice(secretNumber))
			unitaryDiffPrices = append(unitaryDiffPrices, unitaryPrices[len(unitaryPrices)-1]-unitaryPrices[len(unitaryPrices)-2])
		}
		prices = append(prices, unitaryPrices)
		diffPrices = append(diffPrices, unitaryDiffPrices)
	}

	possibleSequences := map[string]int{}
	for _, diffPrice := range diffPrices {
		for idx := range diffPrice[:len(diffPrice)-3] {
			repr := fmt.Sprintf("%d,%d,%d,%d", diffPrice[idx], diffPrice[idx+1], diffPrice[idx+2], diffPrice[idx+3])
			possibleSequences[repr] -= 1
		}
	}

	pq := aocutils.NewPriorityQueue[string]()
	for seq, occ := range possibleSequences {
		// The res is to be found among the sequence appearing a lot
		pq.AddWithPriority(seq, occ)
	}

	res := 0
	for pq.IsNotEmpty() {
		seqStr := pq.ExtractMin()
		rawSeq := strings.Split(seqStr, ",")
		seq := make([]int, 4)
		for idx := range rawSeq {
			seq[idx] = aocutils.MustStringToInt(rawSeq[idx])
		}
		seqRes := 0
		for buyerIdx, diffPrice := range diffPrices {
			for idx := range diffPrice[:len(diffPrice)-4] {
				eq := true
				for k := range seq {
					if seq[k] != diffPrice[idx+k] {
						eq = false
						break
					}
				}
				if eq {
					seqRes += prices[buyerIdx][idx+4]
					break
				}
			}
		}
		res = max(res, seqRes)
		// fmt.Println(res) to quickly give the solution
	}

	return res
}

func runPartOne(ipt []string) int {
	res := 0
	for _, line := range ipt {
		secretNumber := aocutils.MustStringToInt(line)
		for j := 0; j < 2000; j++ {
			secretNumber = getSecretNumber(secretNumber)
		}
		res += secretNumber
	}
	return res
}

func main() {
	lines := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(lines))
	fmt.Println(runPartTwo(lines))
}
