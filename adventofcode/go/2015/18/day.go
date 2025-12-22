package main

import (
	"embed"
	"log"

	"adventofcode/2015/18/internal"
	"adventofcode/pkg/aocutils"
)

//go:embed input.txt
var inputFile embed.FS

func main() {
	ipt := aocutils.MustGetDayInput(inputFile)
	lightBoard, err := internal.NewLightBoard(ipt)
	if err != nil {
		log.Fatal(err)
	}
	lightBoard.Animate()
}
