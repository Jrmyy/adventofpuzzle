package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

type graph struct {
	Vertices map[string]*vertex
}

type vertex struct {
	Name  string
	Edges []*edge
}

type edge struct {
	Condition   string
	Destination *vertex
}

type condition struct {
	Lte int
	Gte int
	Gt  int
	Lt  int
}

func (g graph) GetAllConditionsFromInToA() []map[string]*condition {
	visited := map[string]bool{}
	for k := range g.Vertices {
		visited[k] = false
	}
	var allPaths []string
	g.dfs("in", visited, []string{}, &allPaths)
	conditions := make([]map[string]*condition, len(allPaths))
	for idx, p := range allPaths {
		cm := map[string]*condition{}
		cp := strings.Split(p, " && ")
		for _, cs := range cp {
			variable := cs[:1]
			v, ok := cm[variable]
			if !ok {
				c := &condition{Gte: -1, Lte: -1, Gt: -1, Lt: -1}
				if strings.Contains(cs, "<=") {
					c.Lte = aocutils.MustStringToInt(cs[3:])
				} else if strings.Contains(cs, ">=") {
					c.Gte = aocutils.MustStringToInt(cs[3:])
				} else if strings.Contains(cs, ">") {
					c.Gt = aocutils.MustStringToInt(cs[2:])
				} else {
					c.Lt = aocutils.MustStringToInt(cs[2:])
				}
				cm[variable] = c
			} else {
				if strings.Contains(cs, "<=") {
					lte := aocutils.MustStringToInt(cs[3:])
					if v.Lte != -1 {
						v.Lte = min(v.Lte, lte)
					} else {
						v.Lte = lte
					}
				} else if strings.Contains(cs, ">=") {
					gte := aocutils.MustStringToInt(cs[3:])
					if v.Gte != -1 {
						v.Gte = max(v.Gte, gte)
					} else {
						v.Gte = gte
					}
				} else if strings.Contains(cs, ">") {
					gt := aocutils.MustStringToInt(cs[2:])
					if v.Gt != -1 {
						v.Gt = max(v.Gt, gt)
					} else {
						v.Gt = gt
					}
				} else {
					lt := aocutils.MustStringToInt(cs[2:])
					if v.Lt != -1 {
						v.Lt = min(v.Lt, lt)
					} else {
						v.Lt = lt
					}
				}
			}
		}
		conditions[idx] = cm
	}
	return conditions
}

func (g graph) dfs(source string, visited map[string]bool, currentPath []string, allPaths *[]string) {
	if source == "A" {
		*allPaths = append(*allPaths, strings.Join(currentPath, " && "))
		return
	}
	visited[source] = true
	for _, d := range g.Vertices[source].Edges {
		if !visited[d.Destination.Name] {
			currentPath = append(currentPath, d.Condition)
			g.dfs(d.Destination.Name, visited, currentPath, allPaths)
			currentPath = currentPath[:len(currentPath)-1]
		}
	}
	visited[source] = false
}

func runPartOne(g graph, cases []map[string]int) int {
	conditions := g.GetAllConditionsFromInToA()
	cnt := 0
	for _, ca := range cases {
		isValid := false
		for _, co := range conditions {
			isValid = true
			for variable, value := range ca {
				c, ok := co[variable]
				if ok {
					if !((c.Lte == -1 || value <= c.Lte) &&
						(value >= c.Gte || c.Gte == -1) &&
						(c.Gt == -1 || value > c.Gt) &&
						(c.Lt == -1 || value < c.Lt)) {
						isValid = false
						break
					}
				}
			}
			if isValid {
				for _, v := range ca {
					cnt += v
				}
				break
			}
		}
	}
	return cnt
}

func runPartTwo(g graph) int {
	conditions := g.GetAllConditionsFromInToA()
	cnt := 0
	for _, co := range conditions {
		s := 1
		for _, variable := range []string{"x", "m", "a", "s"} {
			v, ok := co[variable]
			if !ok {
				s *= 4000
			} else {
				mi := 4001
				ma := -1
				il := true
				iu := true
				if v.Lt != -1 {
					mi = v.Lt
					il = false
				}
				if v.Lte != -1 {
					if v.Lte < mi {
						mi = v.Lte
						il = true
					}
				}
				if v.Gt != -1 {
					ma = v.Gt
					iu = false
				}
				if v.Gte != -1 {
					if v.Gte > ma {
						ma = v.Gte
						iu = true
					}
				}
				r := 0
				if mi == 4001 {
					if ma == -1 {
						panic("should not happen")
					}
					r = 4000 - ma + 1
				} else if ma == -1 {
					if mi == 4001 {
						panic("should not happen")
					}
					r = mi
				} else {
					r = mi - ma + 1
				}
				if !il {
					r--
				}
				if !iu {
					r--
				}
				s *= r
			}
		}
		cnt += s
	}
	return cnt
}

func reverseCondition(conditionsPart []string) string {
	r := make([]string, len(conditionsPart))
	for i, c := range conditionsPart {
		if strings.Contains(c, "<") {
			r[i] = strings.ReplaceAll(c, "<", ">=")
		} else {
			r[i] = strings.ReplaceAll(c, ">", "<=")
		}
	}
	return strings.Join(r, " && ")
}

func parseInput() (graph, []map[string]int) {
	lines := aocutils.MustGetDayInput(inputFile)
	g := graph{Vertices: map[string]*vertex{}}
	i := 0
	for idx, line := range lines {
		if line == "" {
			i = idx
			break
		}
		parts := strings.Split(line, "{")
		workflowName := parts[0]
		v, vok := g.Vertices[workflowName]
		if !vok {
			vv := &vertex{Name: workflowName}
			g.Vertices[workflowName] = vv
			v = vv
		}
		rawRules := strings.TrimSuffix(parts[1], "}")
		rules := strings.Split(rawRules, ",")
		var c []string
		for _, r := range rules[:len(rules)-1] {
			p := strings.Split(r, ":")
			child := p[1]
			cc := p[0]
			_, ok := g.Vertices[child]
			if !ok {
				g.Vertices[child] = &vertex{Name: child}
			}
			rc := reverseCondition(c)
			if len(c) > 0 {
				rc += " && "
			}
			v.Edges = append(
				v.Edges,
				&edge{Destination: g.Vertices[child], Condition: rc + cc},
			)
			c = append(c, cc)
		}
		child := rules[len(rules)-1]
		_, ok := g.Vertices[child]
		if !ok {
			g.Vertices[child] = &vertex{Name: child}
		}
		v.Edges = append(
			v.Edges,
			&edge{Destination: g.Vertices[child], Condition: reverseCondition(c)},
		)
	}

	var cases []map[string]int
	for j := i + 1; j < len(lines); j++ {
		c := map[string]int{}
		line := strings.TrimPrefix(strings.TrimSuffix(lines[j], "}"), "{")
		for _, p := range strings.Split(line, ",") {
			vp := strings.Split(p, "=")
			c[vp[0]] = aocutils.MustStringToInt(vp[1])
		}
		cases = append(cases, c)
	}

	return g, cases
}

func main() {
	g, cases := parseInput()
	fmt.Println(runPartOne(g, cases))
	fmt.Println(runPartTwo(g))
}
