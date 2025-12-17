package internal

import (
	"log"
	"regexp"
	"strings"

	"adventofcode/pkg/aocutils"
)

var digitsRegex = regexp.MustCompile("^(\\d+)$")

type Attribute struct {
	IntValue  uint16
	WireValue string
}

func (attribute Attribute) IsApplicable(signals map[string]uint16) bool {
	_, ok := signals[attribute.WireValue]
	return attribute.WireValue == "" || ok
}

func (attribute Attribute) Get(signals map[string]uint16) uint16 {
	if attribute.WireValue != "" {
		return signals[attribute.WireValue]
	}
	return attribute.IntValue
}

func newAttribute(rawValue string) Attribute {
	if digitsRegex.MatchString(rawValue) {
		return Attribute{IntValue: uint16(aocutils.MustStringToInt(rawValue))}
	}
	return Attribute{WireValue: rawValue}
}

type MonoConnection struct {
	First Attribute
}

func (connection MonoConnection) IsApplicable(signals map[string]uint16) bool {
	return connection.First.IsApplicable(signals)
}

func newMonoConnection(rawValue string) MonoConnection {
	return MonoConnection{First: newAttribute(rawValue)}
}

type DualConnection struct {
	First  Attribute
	Second Attribute
}

func (connection DualConnection) IsApplicable(signals map[string]uint16) bool {
	return connection.First.IsApplicable(signals) && connection.Second.IsApplicable(signals)
}

func newDualConnection(firstRawValue, secondRawValue string) DualConnection {
	return DualConnection{First: newAttribute(firstRawValue), Second: newAttribute(secondRawValue)}
}

type Connection interface {
	Connect(signals map[string]uint16) uint16
	IsApplicable(signals map[string]uint16) bool
}

type Instruction struct {
	OutputWire string
	Connection Connection
}

func (instruction Instruction) IsApplicable(signals map[string]uint16) bool {
	return instruction.Connection.IsApplicable(signals)
}

func (instruction Instruction) Apply(signals map[string]uint16) {
	_, ok := signals[instruction.OutputWire]
	if ok {
		return
	}
	signals[instruction.OutputWire] = instruction.Connection.Connect(signals)
}

type SetConnection struct {
	MonoConnection
}

func (connection SetConnection) Connect(signals map[string]uint16) uint16 {
	return connection.First.Get(signals)
}

type NotConnection struct {
	MonoConnection
}

func (connection NotConnection) Connect(signals map[string]uint16) uint16 {
	return ^connection.First.Get(signals)
}

type AndConnection struct {
	DualConnection
}

func (connection AndConnection) Connect(signals map[string]uint16) uint16 {
	return connection.First.Get(signals) & connection.Second.Get(signals)
}

type OrConnection struct {
	DualConnection
}

func (connection OrConnection) Connect(signals map[string]uint16) uint16 {
	return connection.First.Get(signals) | connection.Second.Get(signals)
}

type LShiftConnection struct {
	DualConnection
}

func (connection LShiftConnection) Connect(signals map[string]uint16) uint16 {
	return connection.First.Get(signals) << connection.Second.Get(signals)
}

type RShiftConnection struct {
	DualConnection
}

func (connection RShiftConnection) Connect(signals map[string]uint16) uint16 {
	return connection.First.Get(signals) >> connection.Second.Get(signals)
}

func NewInstruction(line string) Instruction {
	parts := strings.Split(line, " -> ")
	operation := parts[0]
	wire := parts[1]

	var connection Connection

	operationParts := strings.Split(operation, " ")
	if len(operationParts) == 1 {
		connection = SetConnection{newMonoConnection(operation)}
	} else if len(operationParts) == 2 {
		connection = NotConnection{newMonoConnection(strings.TrimPrefix(operation, "NOT "))}
	} else if len(operationParts) == 3 {
		operator := operationParts[1]
		dualConnection := newDualConnection(operationParts[0], operationParts[2])
		switch operator {
		case "AND":
			connection = AndConnection{dualConnection}
		case "OR":
			connection = OrConnection{dualConnection}
		case "LSHIFT":
			connection = LShiftConnection{dualConnection}
		case "RSHIFT":
			connection = RShiftConnection{dualConnection}
		default:
			log.Panicf("operator %s not found", operator)
		}
	} else {
		log.Panicf("operation cannot be parsed: %s", operation)
	}

	return Instruction{
		OutputWire: wire,
		Connection: connection,
	}
}
