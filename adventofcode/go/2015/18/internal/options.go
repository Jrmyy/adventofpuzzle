package internal

import (
	"time"

	"adventofcode/pkg/aocutils"
)

type Option func(*LightBoardOptions)

func NewDefaultLightBoardOptions() LightBoardOptions {
	return LightBoardOptions{
		millisecondsBetweenSteps: 100,
		stuckLights:              aocutils.Set[aocutils.Point]{},
		steps:                    100,
		verticalBorderChar:       '|',
		horizontalBorderChar:     '-',
		cornerChar:               '🎄',
		messagePrefix:            '🎁',
		messageSuffix:            '🎅',
		millisecondsBeforeStop:   3000,
		millisecondsBetweenParts: 3000,
	}
}

type LightBoardOptions struct {
	millisecondsBetweenSteps time.Duration
	stuckLights              aocutils.Set[aocutils.Point]
	steps                    int
	verticalBorderChar       rune
	horizontalBorderChar     rune
	cornerChar               rune
	messagePrefix            rune
	messageSuffix            rune
	millisecondsBeforeStop   time.Duration
	millisecondsBetweenParts time.Duration
}

// NewLightBoardOptions creates a LightBoardOptions instance with default values,
// applying any provided options to override specific fields.
func NewLightBoardOptions(opts ...Option) LightBoardOptions {
	options := NewDefaultLightBoardOptions()
	for _, opt := range opts {
		opt(&options)
	}
	return options
}

func WithMillisecondsBetweenSteps(ms time.Duration) Option {
	return func(o *LightBoardOptions) {
		o.millisecondsBetweenSteps = ms
	}
}

func WithStuckLights(stuckLights aocutils.Set[aocutils.Point]) Option {
	return func(o *LightBoardOptions) {
		o.stuckLights = stuckLights
	}
}

func WithSteps(steps int) Option {
	return func(o *LightBoardOptions) {
		o.steps = steps
	}
}

func WithVerticalBorderChar(char rune) Option {
	return func(o *LightBoardOptions) {
		o.verticalBorderChar = char
	}
}

func WithHorizontalBorderChar(char rune) Option {
	return func(o *LightBoardOptions) {
		o.horizontalBorderChar = char
	}
}

func WithCornerChar(char rune) Option {
	return func(o *LightBoardOptions) {
		o.cornerChar = char
	}
}

func WithMessagePrefix(char rune) Option {
	return func(o *LightBoardOptions) {
		o.messagePrefix = char
	}
}

func WithMessageSuffix(char rune) Option {
	return func(o *LightBoardOptions) {
		o.messageSuffix = char
	}
}

func WithMillisecondsBeforeStop(ms time.Duration) Option {
	return func(o *LightBoardOptions) {
		o.millisecondsBeforeStop = ms
	}
}

func WithMillisecondsBeforeParts(ms time.Duration) Option {
	return func(o *LightBoardOptions) {
		o.millisecondsBetweenParts = ms
	}
}
