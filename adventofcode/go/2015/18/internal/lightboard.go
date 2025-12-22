package internal

import (
	"fmt"
	"maps"
	"time"

	"adventofcode/pkg/aocutils"
	"adventofcode/pkg/tui"
)

type LightBoard struct {
	tui tui.TUI

	// TODO: handle resize and uses data from tui
	screenWidth  int
	screenHeight int

	initialGrid map[aocutils.Point]bool
	grid        map[aocutils.Point]bool

	maxGridX int
	maxGridY int

	screenGridOffsetX int
	screenGridOffsetY int

	resultChannel chan int

	options LightBoardOptions
}

func (lightBoard *LightBoard) drawHorizontalBorders() {
	redStyle := tui.StyleDefault.Foreground(tui.ColorRed)
	whiteStyle := tui.StyleDefault.Foreground(tui.ColorWhite)
	for x := lightBoard.screenGridOffsetX - 1; x <= lightBoard.screenGridOffsetX+lightBoard.maxGridX+1; x++ {
		var topStyle tui.Style
		var bottomStyle tui.Style
		if x%2 == 0 {
			topStyle = redStyle
			bottomStyle = whiteStyle
		} else {
			topStyle = whiteStyle
			bottomStyle = redStyle
		}
		lightBoard.tui.PutStyledRune(
			x,
			lightBoard.screenGridOffsetY-1,
			lightBoard.options.horizontalBorderChar,
			topStyle,
		)
		lightBoard.tui.PutStyledRune(
			x,
			lightBoard.screenGridOffsetY+lightBoard.maxGridY+1,
			lightBoard.options.horizontalBorderChar,
			bottomStyle,
		)
	}
}

func (lightBoard *LightBoard) drawVerticalBorders() {
	redStyle := tui.StyleDefault.Foreground(tui.ColorRed)
	whiteStyle := tui.StyleDefault.Foreground(tui.ColorWhite)
	for y := lightBoard.screenGridOffsetY - 1; y <= lightBoard.screenGridOffsetY+lightBoard.maxGridY+1; y++ {
		var leftStyle tui.Style
		var rightStyle tui.Style
		if y%2 == 0 {
			leftStyle = redStyle
			rightStyle = whiteStyle
		} else {
			leftStyle = whiteStyle
			rightStyle = redStyle
		}
		lightBoard.tui.PutStyledRune(
			lightBoard.screenGridOffsetX-1,
			y,
			lightBoard.options.verticalBorderChar,
			leftStyle,
		)
		lightBoard.tui.PutStyledRune(
			lightBoard.screenGridOffsetX+lightBoard.maxGridX+1,
			y,
			lightBoard.options.verticalBorderChar,
			rightStyle,
		)
	}
}

func (lightBoard *LightBoard) drawCorners() {
	style := tui.StyleDefault.Foreground(tui.ColorWhite)
	for _, x := range []int{
		lightBoard.screenGridOffsetX - 1,
		lightBoard.screenGridOffsetX + lightBoard.maxGridX + 1,
	} {
		for _, y := range []int{
			lightBoard.screenGridOffsetY - 1,
			lightBoard.screenGridOffsetY + lightBoard.maxGridY + 1,
		} {
			lightBoard.tui.PutStyledRune(
				x,
				y,
				lightBoard.options.cornerChar,
				style,
			)
		}
	}
}

func (lightBoard *LightBoard) drawBorders() {
	lightBoard.drawHorizontalBorders()
	lightBoard.drawVerticalBorders()
	lightBoard.drawCorners()
}

func (lightBoard *LightBoard) drawGrid() {
	for y := 0; y <= lightBoard.maxGridY; y++ {
		for x := 0; x <= lightBoard.maxGridX; x++ {
			point := aocutils.Point{X: x, Y: y}
			isOn := lightBoard.grid[point]
			char := ' '
			color := tui.ColorWhite
			if isOn {
				color = tui.ColorGreen
				char = '*'
			}
			lightBoard.tui.PutStyledRune(
				lightBoard.screenGridOffsetX+x,
				lightBoard.screenGridOffsetY+y,
				char,
				tui.StyleDefault.Foreground(color),
			)
		}
	}
}

func (lightBoard *LightBoard) drawBottomMessage() {
	message := fmt.Sprintf(
		"%c %s %c",
		lightBoard.options.messagePrefix,
		"Advent of Code 2015 - day 18",
		lightBoard.options.messageSuffix,
	)
	// Adding emojis add extra spaces not really visible
	// TODO: dynamic handling of prefix and suffix restriction on emoji
	startX := (lightBoard.screenWidth - (len(message) - 4)) / 2
	lightBoard.tui.PutString(startX, lightBoard.screenGridOffsetY+lightBoard.maxGridY+2, message)
}

func (lightBoard *LightBoard) drawFullBoard() {
	lightBoard.drawBorders()
	lightBoard.drawGrid()
	lightBoard.drawBottomMessage()
	lightBoard.tui.Show()
}

func (lightBoard *LightBoard) stop() {
	close(lightBoard.resultChannel)
	for res := range lightBoard.resultChannel {
		fmt.Println(res)
	}
}

func (lightBoard *LightBoard) updateLights() {
	newGrid := map[aocutils.Point]bool{}
	for point, isOn := range lightBoard.grid {
		if lightBoard.options.stuckLights.Has(point) {
			newGrid[point] = isOn
			continue
		}

		countNeighboursOn := 0
		for _, neighbour := range point.Neighbours2D(true) {
			countNeighboursOn += aocutils.Bool2int(lightBoard.grid[neighbour])
		}
		var newIsOn bool
		if isOn {
			newIsOn = countNeighboursOn == 2 || countNeighboursOn == 3
		} else {
			newIsOn = countNeighboursOn == 3
		}
		newGrid[point] = newIsOn
	}
	lightBoard.grid = newGrid
}

func (lightBoard *LightBoard) simulateBothParts() {
	lightBoard.simulate()

	time.Sleep(lightBoard.options.millisecondsBetweenParts * time.Millisecond)
	lightBoard.tui.Clear()

	lightBoard.options = NewLightBoardOptions(WithStuckLights(lightBoard.getCorners()))
	lightBoard.grid = maps.Clone(lightBoard.initialGrid)
	lightBoard.simulate()

	time.Sleep(lightBoard.options.millisecondsBeforeStop * time.Millisecond)
}

func (lightBoard *LightBoard) simulate() {
	lightBoard.drawFullBoard()
	for i := 0; i < lightBoard.options.steps; i++ {
		lightBoard.updateLights()
		lightBoard.drawFullBoard()
		time.Sleep(lightBoard.options.millisecondsBetweenSteps * time.Millisecond)
	}
	lightBoard.calculateLightOnCount()
}

func (lightBoard *LightBoard) calculateLightOnCount() {
	lightOnCount := 0
	for _, isOn := range lightBoard.grid {
		lightOnCount += aocutils.Bool2int(isOn)
	}
	lightBoard.resultChannel <- lightOnCount
	lightBoard.displayLightOnCount(lightOnCount)
}

func (lightBoard *LightBoard) displayLightOnCount(lightOnCount int) {
	prefix := fmt.Sprintf("%c Response is", lightBoard.options.messagePrefix)
	response := fmt.Sprintf("%d", lightOnCount)
	suffix := fmt.Sprintf("%c", lightBoard.options.messageSuffix)

	// TODO: dynamic handling of prefix and suffix restriction on emoji
	totalSize := len(prefix) + 1 - 2 + len(response) + 1 + len(suffix) - 2
	resultOffsetX := lightBoard.screenGridOffsetX + lightBoard.maxGridX/2 - totalSize/2

	lightBoard.tui.PutString(resultOffsetX, lightBoard.screenGridOffsetY-2, prefix)

	style := tui.StyleDefault
	style = style.Underline(tui.UnderlineStyleDotted, tui.ColorRed)
	style = style.Bold(true)
	style = style.Foreground(tui.ColorRed)

	lightBoard.tui.PutStyledString(
		resultOffsetX+len(prefix)+1-2,
		lightBoard.screenGridOffsetY-2,
		response,
		style,
	)

	lightBoard.tui.PutString(
		resultOffsetX+len(prefix)+1+-2+len(response)+1,
		lightBoard.screenGridOffsetY-2,
		suffix,
	)

	lightBoard.tui.Show()
}

func (lightBoard *LightBoard) Animate() {
	lightBoard.tui.Animate(
		lightBoard.simulateBothParts,
		lightBoard.stop,
	)
}

func (lightBoard *LightBoard) getCorners() aocutils.Set[aocutils.Point] {
	corners := aocutils.Set[aocutils.Point]{}
	corners.Add(aocutils.Point{X: 0, Y: 0})
	corners.Add(aocutils.Point{X: lightBoard.maxGridX, Y: 0})
	corners.Add(aocutils.Point{X: 0, Y: lightBoard.maxGridY})
	corners.Add(aocutils.Point{X: lightBoard.maxGridX, Y: lightBoard.maxGridY})
	return corners
}

func NewLightBoard(rawGrid []string) (*LightBoard, error) {
	termUi, err := tui.NewTUI(tui.ScreenAdapterTCell)
	if err != nil {
		return nil, err
	}

	screenWidth, screenHeight := termUi.Size()

	lightBoard := &LightBoard{
		tui:           termUi,
		screenWidth:   screenWidth,
		screenHeight:  screenHeight,
		resultChannel: make(chan int, 2),
	}

	grid, maxGridX, maxGridY := initGrid(rawGrid)
	lightBoard.grid = grid
	lightBoard.initialGrid = maps.Clone(grid)
	lightBoard.maxGridX = maxGridX
	lightBoard.maxGridY = maxGridY

	lightBoard.screenGridOffsetX = max((screenWidth-lightBoard.maxGridX)/2, 0)
	lightBoard.screenGridOffsetY = max((screenHeight-lightBoard.maxGridY)/2, 0)

	lightBoard.options = NewDefaultLightBoardOptions()
	return lightBoard, nil
}
