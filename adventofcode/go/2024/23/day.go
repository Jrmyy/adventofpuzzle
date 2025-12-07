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

func findClusters(lines []string, p1 bool) map[string]bool {
	connections := map[string]map[string]bool{}
	for _, line := range lines {
		parts := strings.Split(line, "-")
		for idx, part := range parts {
			if _, ok := connections[part]; !ok {
				connections[part] = map[string]bool{}
			}
			other := parts[(idx+1)%2]
			connections[part][other] = true
		}
	}

	clusters := map[string]bool{}
	for first, others := range connections {
		for second := range others {
			for third := range others {
				if _, ok := connections[second][third]; ok {
					cluster := []string{first, second, third}
					if !p1 {
						for {
							stop := true
							var toCheck []string
							for _, member := range cluster {
								for computer := range connections[member] {
									toCheck = append(toCheck, computer)
								}
							}

							for _, possibleMember := range toCheck {
								isMember := true
								for _, member := range cluster {
									if _, okNew := connections[member][possibleMember]; !okNew {
										isMember = false
										break
									}
								}
								if isMember {
									cluster = append(cluster, possibleMember)
									stop = false
								}
							}

							if stop {
								break
							}
						}
					}
					slices.Sort(cluster)
					clusters[strings.Join(cluster, ",")] = true
				}
			}
		}
	}

	return clusters
}

func runPartTwo(lines []string) string {
	clusters := findClusters(lines, false)
	ma := 0
	res := ""
	for cluster := range clusters {
		if len(cluster) > ma {
			ma = len(cluster)
			res = cluster
		}
	}

	return res
}

func runPartOne(lines []string) int {
	clusters := findClusters(lines, true)
	cnt := 0
	for cluster := range clusters {
		if strings.HasPrefix(cluster, "t") || strings.Contains(cluster, ",t") {
			cnt++
		}
	}
	return cnt
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	fmt.Println(runPartOne(ipt))
	fmt.Println(runPartTwo(ipt))
}
