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

type nodes = map[int][]int

func getPartOfGroup(nodes nodes, groupId int) []int {
	partOfGroup := make([]int, 0, len(nodes))
	for node, children := range nodes {
		if node == groupId {
			partOfGroup = append(partOfGroup, groupId)
		} else {
			if slices.Contains(children, groupId) {
				partOfGroup = append(partOfGroup, node)
			} else {
				var toVisit = children
				visited := make([]int, 0, len(nodes))
				for len(toVisit) > 0 {
					n := toVisit[0]
					visited = append(visited, n)
					toVisit = toVisit[1:]
					cchildren, ok := nodes[n]
					if ok {
						for _, cc := range cchildren {
							if cc == groupId {
								partOfGroup = append(partOfGroup, node)
								break
							}
							if !slices.Contains(toVisit, cc) && !slices.Contains(visited, cc) {
								toVisit = append(toVisit, cc)
							}
						}
					}
				}
			}
		}
	}
	return partOfGroup
}

func runPartOne(ipt nodes) int {
	return len(getPartOfGroup(ipt, 0))
}

func runPartTwo(ipt nodes) int {
	remainingNodes := ipt
	groups := 0
	for len(remainingNodes) > 0 {
		var groupId int
		for k, _ := range remainingNodes {
			groupId = k
			break
		}
		foundNodes := getPartOfGroup(remainingNodes, groupId)
		newRemainingNodes := nodes{}
		for k, v := range remainingNodes {
			if !slices.Contains(foundNodes, k) {
				newChildren := make([]int, 0, len(v))
				for _, c := range v {
					if !slices.Contains(foundNodes, c) {
						newChildren = append(newChildren, c)
					}
				}
				newRemainingNodes[k] = newChildren
			}
		}
		remainingNodes = newRemainingNodes
		groups++
	}
	return groups
}

func parseInput() nodes {
	lines := aocutils.MustGetDayInput(inputFile)
	var ipt = nodes{}
	for _, line := range lines {
		parts := strings.Split(line, " <-> ")
		rawTo := strings.Split(parts[1], ", ")
		to := make([]int, len(rawTo))
		for rIdx, r := range rawTo {
			to[rIdx] = aocutils.MustStringToInt(r)
		}
		ipt[aocutils.MustStringToInt(parts[0])] = to
	}
	return ipt
}

func main() {
	ipt := parseInput()
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
