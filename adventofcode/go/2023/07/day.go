package main

import (
	"embed"
	"fmt"
	"slices"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

var cardsOrder = []string{
	"2",
	"3",
	"4",
	"5",
	"6",
	"7",
	"8",
	"9",
	"T",
	"J",
	"Q",
	"K",
	"A",
}

type hand struct {
	Bid           int
	Cards         string
	OriginalCards string
}

func getCardsType(c string) int {
	p := strings.Split(c, "")
	r := map[string]int{}
	for _, cc := range p {
		_, ok := r[cc]
		if !ok {
			r[cc] = 0
		}
		r[cc]++
	}

	switch len(r) {
	case 5:
		return 0
	case 4:
		return 1
	case 3:
		m := 0
		for _, v := range r {
			m = max(m, v)
		}
		if m == 2 {
			return 2
		}
		return 3
	case 2:
		m := 0
		for _, v := range r {
			m = max(m, v)
		}
		if m == 3 {
			return 4
		}
		return 5
	default:
		return 6
	}
}

func isCardBetter(fc string, sc string) bool {
	i := 0
	for i < 5 {
		ffc := fmt.Sprintf("%v", string(fc[i]))
		fsc := fmt.Sprintf("%v", string(sc[i]))

		f, s := slices.Index(cardsOrder, ffc), slices.Index(cardsOrder, fsc)

		if f == s {
			i++
		} else {
			return f > s
		}
	}
	panic("Should not happen")
}

func isBetter(fc hand, sc hand) bool {
	fct := getCardsType(fc.Cards)
	sct := getCardsType(sc.Cards)
	return fct > sct || (fct == sct && isCardBetter(fc.OriginalCards, sc.OriginalCards))
}

func getBetterCardInList(cs []string) string {
	var champ string
	for idx, c := range cs {
		isChamp := true
		for iidx, cc := range cs {
			if idx != iidx {
				if !isCardBetter(c, cc) {
					isChamp = false
				}
			}
		}
		if isChamp {
			champ = c
		}
	}
	if champ == "" {
		panic("Should not happen")
	}
	return champ
}

func transformJokerCard(s string) string {
	j := strings.Count(s, "J")
	other := map[string]int{}
	for _, c := range strings.Split(s, "") {
		if c != "J" {
			other[c]++
		}
	}
	switch j {
	case 0:
		return s
	case 5:
		return "AAAAA"
	default:
		candidates := make([]string, 0, len(other))
		m := 0
		for k, v := range other {
			if v > m {
				candidates = []string{k}
				m = v
			} else if v == m {
				candidates = append(candidates, k)
			}
		}
		bc := getBetterCardInList(candidates)
		return strings.Replace(s, "J", bc, -1)
	}
}

func getWinnings(ipt []hand) int {
	ranks := make([]int, len(ipt))
	for idx, h := range ipt {
		rIdx := 0
		for iidx, hh := range ipt {
			if idx != iidx {
				if isBetter(h, hh) {
					rIdx++
				}
			}
		}
		if ranks[rIdx] != 0 {
			panic("Should not happen")
		}
		ranks[rIdx] = h.Bid
	}
	t := 0
	for idx, b := range ranks {
		t += (idx + 1) * b
	}
	return t
}

func runPartOne(ipt []hand) int {
	return getWinnings(ipt)
}

func runPartTwo(ipt []hand) int {
	for idx := range ipt {
		ipt[idx] = hand{
			Bid:           ipt[idx].Bid,
			Cards:         transformJokerCard(ipt[idx].Cards),
			OriginalCards: ipt[idx].OriginalCards,
		}
	}
	cardsOrder = slices.Delete(cardsOrder, 9, 10)
	cardsOrder = append([]string{"J"}, cardsOrder...)
	return getWinnings(ipt)
}

func parseInput() []hand {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = make([]hand, len(lines))
	for idx, line := range lines {
		parts := strings.Split(line, " ")
		ipt[idx] = hand{
			Bid:           aocutils.MustStringToInt(parts[1]),
			Cards:         parts[0],
			OriginalCards: parts[0],
		}
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
