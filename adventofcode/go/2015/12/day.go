package main

import (
	"embed"
	"encoding/json"
	"errors"
	"fmt"

	"adventofcode/pkg/aocutils"
)

var errDoubleCounting = errors.New("double counting red")

//go:embed input.txt
var inputFile embed.FS

func parseInput(line string) any {
	var res interface{}
	if err := json.Unmarshal([]byte(line), &res); err != nil {
		panic(err)
	}
	return res
}

func countDocumentNumbers(v any, ignoreRed bool) (int, error) {
	switch t := v.(type) {
	case float64:
		return int(t), nil
	case []any:
		cnt := 0
		for _, arrayItem := range t {
			itemCount, _ := countDocumentNumbers(arrayItem, ignoreRed)
			cnt += itemCount
		}
		return cnt, nil
	case map[string]any:
		cnt := 0
		for _, objectItem := range t {
			itemCount, err := countDocumentNumbers(objectItem, ignoreRed)
			if errors.Is(err, errDoubleCounting) {
				return 0, nil
			}
			cnt += itemCount
		}
		return cnt, nil
	case string:
		if t == "red" && ignoreRed {
			return 0, errDoubleCounting
		}
		return 0, nil
	default:
		return 0, nil
	}
}

func countAllDocumentNumbers(jsonStructure any, ignoreRed bool) int {
	res, err := countDocumentNumbers(jsonStructure, ignoreRed)
	if err != nil {
		panic(err)
	}
	return res
}

func runPartOne(jsonStructure any) int {
	return countAllDocumentNumbers(jsonStructure, false)
}

func runPartTwo(jsonStructure any) int {
	return countAllDocumentNumbers(jsonStructure, true)
}

func main() {
	rawJson := aocutils.MustGetDayInput(inputFile)[0]
	jsonStructure := parseInput(rawJson)
	fmt.Println(runPartOne(jsonStructure))
	fmt.Println(runPartTwo(jsonStructure))
}
