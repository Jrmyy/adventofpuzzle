package tui

import "github.com/gdamore/tcell/v3"

type TCellAdapter struct {
	tcell.Screen
}

func (adapter TCellAdapter) SetStringContent(x, y int, content string, style Style) {
	adapter.Screen.PutStrStyled(x, y, content, adapter.toTCellStyle(style))
}

func (adapter TCellAdapter) SetRuneContent(x, y int, content rune, style Style) {
	adapter.Screen.SetContent(x, y, content, nil, adapter.toTCellStyle(style))
}

func (adapter TCellAdapter) Stop() {
	adapter.Screen.Fini()
}

func (adapter TCellAdapter) TriggerGracefulTermination() {
	adapter.Screen.EventQ() <- tcell.NewEventInterrupt("graceful termination")
}

func (adapter TCellAdapter) LoopUntilTermination() {
	for {
		event := <-adapter.Screen.EventQ()
		switch screenEvent := event.(type) {
		case *tcell.EventKey:
			if screenEvent.Key() == tcell.KeyEscape || screenEvent.Key() == tcell.KeyCtrlC {
				return
			}
		case *tcell.EventInterrupt:
			return
		}
	}
}

func (adapter TCellAdapter) toTCellStyle(style Style) tcell.Style {
	tCellStyle := tcell.StyleDefault
	tCellStyle = tCellStyle.Foreground(adapter.toTCellColor(style.foregroundColor))
	tCellStyle = tCellStyle.Background(adapter.toTCellColor(style.backgroundColor))
	tCellStyle = tCellStyle.Underline(tcell.UnderlineStyle(style.ulStyle), adapter.toTCellColor(style.ulColor))
	tCellStyle = tCellStyle.Bold(style.isBold)
	return tCellStyle
}

func (adapter TCellAdapter) toTCellColor(color Color) tcell.Color {
	return tcell.GetColor(string(color))
}

func NewTCellAdapter() (ScreenAdapter, error) {
	screen, err := tcell.NewScreen()
	if err != nil {
		return nil, err
	}
	if err = screen.Init(); err != nil {
		return nil, err
	}

	screen.Clear()
	return TCellAdapter{Screen: screen}, nil
}
