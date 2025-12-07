package main

import (
	"fmt"
	"strconv"
)

var factors = []int64{16807, 48271}

const divider = 2147483647

func runPartOne(ipt []int64) int {
	matches := 0
	cache := map[string]bool{}
	for i := 0; i < 40_000_000; i++ {
		cacheKey := fmt.Sprintf("%v-%v", ipt[0], ipt[1])
		match, ok := cache[cacheKey]
		if ok && match {
			matches++
		}
		ipt = []int64{
			(ipt[0] * factors[0]) % divider,
			(ipt[1] * factors[1]) % divider,
		}
		bases := []string{
			fmt.Sprintf("%016v", strconv.FormatInt(ipt[0], 2)),
			fmt.Sprintf("%016v", strconv.FormatInt(ipt[1], 2)),
		}
		cache[cacheKey] = bases[0][len(bases[0])-16:] == bases[1][len(bases[1])-16:]
		if cache[cacheKey] {
			matches++
		}
	}
	return matches
}

func runPartTwo(ipt []int64) int {
	var aStash []int64
	var bStash []int64
	seen, matches := 0, 0
	for seen < 5_000_000 {
		ipt = []int64{
			(ipt[0] * factors[0]) % divider,
			(ipt[1] * factors[1]) % divider,
		}
		if ipt[0]%4 == 0 {
			aStash = append(aStash, ipt[0])
		}
		if ipt[1]%8 == 0 {
			bStash = append(bStash, ipt[1])
		}
		if len(aStash) > 0 && len(bStash) > 0 {
			seen++
			a := aStash[0]
			aStash = aStash[1:]
			b := bStash[0]
			bStash = bStash[1:]
			bases := []string{
				fmt.Sprintf("%016v", strconv.FormatInt(a, 2)),
				fmt.Sprintf("%016v", strconv.FormatInt(b, 2)),
			}
			if bases[0][len(bases[0])-16:] == bases[1][len(bases[1])-16:] {
				matches++
			}
		}
	}
	return matches
}

func main() {
	ipt := []int64{618, 814}
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
