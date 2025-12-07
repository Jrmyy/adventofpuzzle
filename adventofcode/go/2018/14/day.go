package main

import (
	"fmt"
	"strconv"
	"strings"

	"adventofcode/pkg/aocutils"
)

func runPartOne(ipt int) string {
	recipes := make([]int, 0, ipt+10)
	recipes = append(recipes, []int{3, 7}...)
	fi, si := 0, 1
	for len(recipes) < ipt+10 {
		s := recipes[fi] + recipes[si]
		parts := strings.Split(fmt.Sprintf("%v", s), "")
		for _, p := range parts {
			recipes = append(recipes, aocutils.MustStringToInt(p))
		}
		fi = (1 + fi + recipes[fi]) % len(recipes)
		si = (1 + si + recipes[si]) % len(recipes)
	}
	subRecipes := recipes[ipt : ipt+10]
	s := ""
	for _, r := range subRecipes {
		s += fmt.Sprintf("%v", r)
	}
	return s
}

func runPartTwo(ipt int) int {
	seq := fmt.Sprintf("%v", ipt)
	recipes := []byte{'3', '7'}
	fi, si := 0, 1
	i := 0
	for i < 50_000_000 {
		rfi := int(recipes[fi] - '0')
		rsi := int(recipes[si] - '0')
		score := []byte(strconv.Itoa(rfi + rsi))
		recipes = append(recipes, score...)
		fi = (1 + fi + rfi) % len(recipes)
		si = (1 + si + rsi) % len(recipes)
		i++
	}
	return strings.Index(string(recipes), seq)
}

func main() {
	ipt := 919901
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
