package main

import (
	"embed"
	"fmt"
	"math"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type module struct {
	Outputs       []string
	IsFlipFlop    bool
	IsConjunction bool
	IsOn          bool
	CurrentSignal int
	LatestPulse   map[string]int
}

func getPulses(modules map[string]*module, presses int) int {
	res := map[int]int{
		-1: 0,
		1:  0,
	}
	cycles := map[string]int{}
	for ipt := range modules["kl"].LatestPulse {
		cycles[ipt] = 0
	}
	for i := 1; i <= presses; i++ {
		res[-1]++
		todo := []string{"broadcaster"}
		for len(todo) > 0 {
			mn := todo[0]
			m := modules[mn]
			signal := m.CurrentSignal
			if m.IsConjunction {
				signal = -1
				for _, v := range m.LatestPulse {
					if v == -1 {
						signal = 1
						break
					}
				}
			}
			if signal == 0 {
				panic(fmt.Sprintf("%v has signal to send equal to 0", mn))
			}
			cv, cok := cycles[mn]
			if cok && cv == 0 && signal == 1 {
				cycles[mn] = i
			}
			todo = todo[1:]
			for _, opt := range m.Outputs {
				mm, ok := modules[opt]
				res[signal]++
				if !ok {
					continue
				}
				if mm.IsFlipFlop {
					if signal == -1 {
						if mm.IsOn {
							mm.CurrentSignal = -1
							mm.IsOn = false
						} else {
							mm.CurrentSignal = 1
							mm.IsOn = true
						}
						todo = append(todo, opt)
					}
				} else if mm.IsConjunction {
					mm.LatestPulse[mn] = signal
					todo = append(todo, opt)
				}
			}
		}
		done := true
		for _, v := range cycles {
			if v == 0 {
				done = false
				break
			}
		}
		if done {
			lcms := make([]int, 0, len(cycles))
			for _, v := range cycles {
				lcms = append(lcms, v)
			}
			return aocutils.LcmList(lcms)
		}
	}
	return res[1] * res[-1]
}

func runPartOne(modules map[string]*module) int {
	return getPulses(modules, 1000)
}

func runPartTwo(modules map[string]*module) int {
	// rx comes from conjunction kl. To have only one low pulse, this means that kl must have
	// all inputs last sending a high pulse.
	// We just need to get the LCM of all button presses for each input of conjunction kl to send a high pulse.
	return getPulses(modules, math.MaxInt)
}

func parseInput() map[string]*module {
	modules := map[string]*module{}
	lines := aocutils.MustGetDayInput(inputFile)
	for _, line := range lines {
		parts := strings.Split(line, " -> ")
		ipt := parts[0]
		opts := strings.Split(parts[1], ", ")
		m := &module{Outputs: opts}
		if ipt[0] == '%' {
			ipt = ipt[1:]
			m.IsFlipFlop = true
			m.CurrentSignal = -1
		} else if ipt[0] == '&' {
			ipt = ipt[1:]
			m.IsConjunction = true
			m.LatestPulse = map[string]int{}
		} else {
			m.CurrentSignal = -1
		}
		modules[ipt] = m
	}
	for n, m := range modules {
		for _, opt := range m.Outputs {
			mm, ok := modules[opt]
			if !ok {
				continue
			}
			if mm.IsConjunction {
				mm.LatestPulse[n] = -1
			}
		}
	}
	return modules
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	ipt = parseInput()
	fmt.Println(runPartTwo(ipt))
}
