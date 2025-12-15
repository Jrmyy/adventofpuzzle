package internal

import (
	"strings"

	"adventofcode/pkg/aocutils"
)

type Present struct {
	Width  int
	Height int
	Length int
}

func (present Present) WrappingPaper() int {
	return present.area() + present.smallestSideArea()
}

func (present Present) Ribbon() int {
	return present.smallestSidePerimeter() + present.volume()
}

func (present Present) area() int {
	return 2*present.Width*present.Length + 2*present.Width*present.Height + 2*present.Height*present.Length
}

func (present Present) smallestSideArea() int {
	return min(present.Width*present.Length, present.Width*present.Height, present.Height*present.Length)
}

func (present Present) smallestSidePerimeter() int {
	return 2 * min(present.Width+present.Length, present.Width+present.Height, present.Height+present.Length)
}

func (present Present) volume() int {
	return present.Height * present.Width * present.Length
}

func NewPresent(line string) Present {
	dimensions := strings.Split(line, "x")
	return Present{
		Width:  aocutils.MustStringToInt(dimensions[0]),
		Height: aocutils.MustStringToInt(dimensions[1]),
		Length: aocutils.MustStringToInt(dimensions[2]),
	}
}
