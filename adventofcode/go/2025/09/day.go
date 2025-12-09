package main

import (
	"embed"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func parseInput(lines []string) []aocutils.Point {
	tiles := make([]aocutils.Point, len(lines))
	for idx, line := range lines {
		parts := strings.Split(line, ",")
		tiles[idx] = aocutils.Point{
			X: aocutils.MustStringToInt(parts[0]),
			Y: aocutils.MustStringToInt(parts[1]),
		}
	}
	return tiles
}

func area(firstCorner, secondCorner aocutils.Point) int {
	minX, maxX, minY, maxY := firstCorner.MinMax(secondCorner)
	return (maxX - minX + 1) * (maxY - minY + 1)
}

// A rectangle is outside the polygon if we can find an edge the rectangle crosses (meaning a part of the
// rectangle is outside the polygon)
func overlaps(firstRectangleCorner, secondRectangleCorner, firstVertex, secondVertex aocutils.Point) bool {
	minRectangleX, maxRectangleX, minRectangleY, maxRectangleY := firstRectangleCorner.MinMax(secondRectangleCorner)
	minEdgeX, maxEdgeX, minEdgeY, maxEdgeY := firstVertex.MinMax(secondVertex)

	// Check if crossed vertically
	if firstVertex.X == secondVertex.X {
		return minRectangleY < maxEdgeY && maxRectangleY > minEdgeY &&
			firstVertex.X > minRectangleX && firstVertex.X < maxRectangleX
	}

	// Check if crossed horizontally
	if firstVertex.Y == secondVertex.Y {
		return minRectangleX < maxEdgeX && maxRectangleX > minEdgeX &&
			firstVertex.Y > minRectangleY && firstVertex.Y < maxRectangleY
	}

	panic("diagonal")
}

func runPartOne(redTiles []aocutils.Point) int {
	res := 0
	for idx, first := range redTiles {
		for _, second := range redTiles[idx+1:] {
			res = max(res, area(first, second))
		}
	}
	return res
}

func runPartTwo(redTiles []aocutils.Point) int {
	res := 0
	for idx, first := range redTiles {
		for _, second := range redTiles[idx+1:] {
			rectangleArea := area(first, second)
			if rectangleArea < res {
				continue
			}

			insidePolygon := true
			for k := 0; k < len(redTiles); k++ {
				tile := redTiles[k]
				nextTile := redTiles[(k+1)%len(redTiles)]
				if overlaps(first, second, tile, nextTile) {
					insidePolygon = false
					break
				}
			}

			if insidePolygon {
				res = rectangleArea
			}
		}
	}
	return res
}

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	redTiles := parseInput(ipt)
	fmt.Println(runPartOne(redTiles))
	fmt.Println(runPartTwo(redTiles))
}
