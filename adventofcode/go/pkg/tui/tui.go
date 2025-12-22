package tui

type TUI struct {
	adapter ScreenAdapter
}

func (tui TUI) PutStyledString(x, y int, content string, style Style) {
	tui.adapter.SetStringContent(x, y, content, style)
}

func (tui TUI) PutString(x, y int, content string) {
	tui.adapter.SetStringContent(x, y, content, StyleDefault)
}

func (tui TUI) PutStyledRune(x, y int, char rune, style Style) {
	tui.adapter.SetRuneContent(x, y, char, style)
}

func (tui TUI) PutRune(x, y int, char rune) {
	tui.adapter.SetRuneContent(x, y, char, StyleDefault)
}

func (tui TUI) Clear() {
	tui.adapter.Clear()
}

func (tui TUI) Show() {
	tui.adapter.Show()
}

func (tui TUI) Size() (int, int) {
	return tui.adapter.Size()
}

func (tui TUI) Animate(simulationFn func(), onStopCallback func()) {
	defer tui.stop(onStopCallback)

	go func() {
		simulationFn()
		tui.adapter.TriggerGracefulTermination()
	}()

	tui.adapter.LoopUntilTermination()
}

func (tui TUI) stop(onStopCallback func()) {
	// You have to catch panics in a defer, clean up, and
	// re-raise them - otherwise your application can
	// die without leaving any diagnostic trace.
	maybePanic := recover()
	tui.adapter.Stop()
	if maybePanic != nil {
		panic(maybePanic)
	}

	onStopCallback()
}

func NewTUI(adapterName ScreenAdapterName) (TUI, error) {
	adapter, err := NewAdapter(adapterName)
	if err != nil {
		return TUI{}, err
	}

	return TUI{adapter: adapter}, nil
}
