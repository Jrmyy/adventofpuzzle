package tui

import (
	"fmt"
)

type ScreenAdapterName string

var (
	ScreenAdapterTCell = ScreenAdapterName("tcell")
)

type ScreenAdapter interface {
	Size() (width, height int)
	SetStringContent(x, y int, content string, style Style)
	SetRuneContent(x, y int, content rune, style Style)
	Show()
	Clear()
	Stop()
	TriggerGracefulTermination()
	LoopUntilTermination()
}

var adapters = map[ScreenAdapterName]func() (ScreenAdapter, error){
	ScreenAdapterTCell: NewTCellAdapter,
}

func NewAdapter(adapterName ScreenAdapterName) (ScreenAdapter, error) {
	adapterFunc, exists := adapters[adapterName]

	if !exists {
		return nil, fmt.Errorf("no adapter %s found", adapterName)
	}

	return adapterFunc()
}
