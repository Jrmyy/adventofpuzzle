package internal

import (
	"errors"
	"fmt"
	"strings"

	"adventofcode/pkg/aocutils"
)

func fromRegexWeaknessesOrImmunities(characteristicType string, rawValues string) ([]string, []string, error) {
	switch characteristicType {
	case "":
		return nil, nil, nil
	case "weak":
		return strings.Split(rawValues, ", "), nil, nil
	case "immune":
		return nil, strings.Split(rawValues, ", "), nil
	default:
		return nil, nil, fmt.Errorf("unexpected type %s", characteristicType)
	}
}

func FromRegexMatches(regexMatches []string, groupType string) (*Group, error) {
	firstWeaknesses, firstImmunities, err := fromRegexWeaknessesOrImmunities(regexMatches[3], regexMatches[4])
	if err != nil {
		return nil, err
	}

	secondWeaknesses, secondImmunities, err := fromRegexWeaknessesOrImmunities(regexMatches[5], regexMatches[6])
	if err != nil {
		return nil, err
	}

	if firstWeaknesses != nil && secondWeaknesses != nil {
		return nil, errors.New("reassigning weaknesses")
	}
	if firstImmunities != nil && secondImmunities != nil {
		return nil, errors.New("reassigning immunities")
	}

	weaknesses := map[string]bool{}
	for _, s := range append(firstWeaknesses, secondWeaknesses...) {
		weaknesses[s] = true
	}
	immunities := map[string]bool{}
	for _, s := range append(firstImmunities, secondImmunities...) {
		immunities[s] = true
	}

	g := &Group{
		UnitsCount:    aocutils.MustStringToInt(regexMatches[1]),
		HealthPerUnit: aocutils.MustStringToInt(regexMatches[2]),
		DamagePerUnit: aocutils.MustStringToInt(regexMatches[7]),
		Weaknesses:    weaknesses,
		Immunities:    immunities,
		DamageType:    regexMatches[8],
		Initiative:    aocutils.MustStringToInt(regexMatches[9]),
		GroupType:     groupType,
	}
	return g, g.Validate()
}
