package tui

type UnderlineStyle uint8

const (
	UnderlineStyleNone = UnderlineStyle(iota)
	UnderlineStyleSolid
	UnderlineStyleDouble
	UnderlineStyleCurly
	UnderlineStyleDotted
	UnderlineStyleDashed
)

type Style struct {
	backgroundColor Color
	foregroundColor Color
	ulStyle         UnderlineStyle
	ulColor         Color
	isBold          bool
}

var StyleDefault Style

func (style Style) Foreground(color Color) Style {
	newStyle := style
	newStyle.foregroundColor = color
	return newStyle
}

func (style Style) Background(color Color) Style {
	newStyle := style
	newStyle.backgroundColor = color
	return newStyle
}

func (style Style) Underline(ulStyle UnderlineStyle, ulColor Color) Style {
	newStyle := style
	newStyle.ulStyle = ulStyle
	newStyle.ulColor = ulColor
	return newStyle
}

func (style Style) Bold(isBold bool) Style {
	newStyle := style
	newStyle.isBold = isBold
	return newStyle
}
