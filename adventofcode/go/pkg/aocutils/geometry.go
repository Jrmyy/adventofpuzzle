package aocutils

import (
	"fmt"
	"math"
	"os"
)

type Point struct {
	X int
	Y int
	Z int
}

func (p Point) Neighbours2D(includeDiag bool) []Point {
	neighbours := []Point{
		{X: p.X - 1, Y: p.Y, Z: p.Z},
		{X: p.X + 1, Y: p.Y, Z: p.Z},
		{X: p.X, Y: p.Y - 1, Z: p.Z},
		{X: p.X, Y: p.Y + 1, Z: p.Z},
	}
	if includeDiag {
		neighbours = append(neighbours, []Point{
			{X: p.X - 1, Y: p.Y - 1, Z: p.Z},
			{X: p.X - 1, Y: p.Y + 1, Z: p.Z},
			{X: p.X + 1, Y: p.Y - 1, Z: p.Z},
			{X: p.X + 1, Y: p.Y + 1, Z: p.Z},
		}...)
	}
	return neighbours
}

func (p Point) Add(o Point) Point {
	return Point{X: p.X + o.X, Y: p.Y + o.Y, Z: p.Z + o.Z}
}

func (p Point) Sub(o Point) Point {
	return Point{X: p.X - o.X, Y: p.Y - o.Y, Z: p.Z - o.Z}
}

func (p Point) ManhattanDist(o Point) int {
	return Abs(p.X-o.X) + Abs(p.Y-o.Y) + Abs(p.Z-o.Z)
}

func (p Point) EuclideanDist(o Point) float64 {
	return math.Sqrt(
		math.Pow(float64(p.X-o.X), 2) + math.Pow(float64(p.Y-o.Y), 2) + math.Pow(float64(p.Z-o.Z), 2),
	)
}

func (p Point) TurnLeft2D() Point {
	return Point{X: -p.Y, Y: p.X, Z: p.Z}
}

func (p Point) TurnRight2D() Point {
	return Point{X: p.Y, Y: -p.X, Z: p.Z}
}

type Edges[T comparable] map[T]int

// Graph structure represents a weighted graph, with a mapping between the vertices and their edges to the
// other vertices. The only condition is that T must be comparable to be a map key.
type Graph[T comparable] map[T]Edges[T]

func (g Graph[T]) Dijkstra(start T) (map[T]int, map[T][]T) {
	dist := map[T]int{}
	prev := map[T][]T{}

	queue := NewPriorityQueue[T]()
	queue.AddWithPriority(start, 0)

	for s, ds := range g {
		dist[s] = math.MaxInt
		for d := range ds {
			dist[d] = math.MaxInt
		}
	}
	dist[start] = 0

	for queue.IsNotEmpty() {
		u := queue.ExtractMin()

		if _, ok := g[u]; !ok {
			fmt.Println(fmt.Sprintf("%v not found", u))
			os.Exit(1)
		}

		for neighbor, weight := range g[u] {
			alt := dist[u] + weight
			if alt < dist[neighbor] {
				dist[neighbor] = alt
				prev[neighbor] = []T{u}
				queue.AddWithPriority(neighbor, alt)
			} else if alt == dist[neighbor] {
				prev[neighbor] = append(prev[neighbor], u)
			}
		}
	}
	return dist, prev
}
