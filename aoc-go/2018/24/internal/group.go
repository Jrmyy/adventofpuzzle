package internal

import (
	"errors"
	"maps"
	"slices"
)

type Group struct {
	UnitsCount int

	HealthPerUnit int

	Immunities map[string]bool
	Weaknesses map[string]bool

	DamageType    string
	DamagePerUnit int

	Initiative int

	GroupType string
}

func (g *Group) EffectivePower() int {
	return g.UnitsCount * g.DamagePerUnit
}

func (g *Group) Copy() *Group {
	return &Group{
		UnitsCount:    g.UnitsCount,
		HealthPerUnit: g.HealthPerUnit,
		DamagePerUnit: g.DamagePerUnit,
		Weaknesses:    maps.Clone(g.Weaknesses),
		Immunities:    maps.Clone(g.Immunities),
		DamageType:    g.DamageType,
		Initiative:    g.Initiative,
		GroupType:     g.GroupType,
	}
}

func (g *Group) Validate() error {
	for _, v := range []int{g.UnitsCount, g.HealthPerUnit, g.DamagePerUnit, g.Initiative} {
		if v <= 0 {
			return errors.New("must be >0")
		}
	}
	if g.DamageType == "" {
		return errors.New("no damage type provided")
	}
	return nil
}

func (g *Group) DamageDoneTo(attacked *Group) int {
	// If the attacked Group has an immunity to the damage type, no damage taken
	dmgMultiplier := 1
	if attacked.Immunities[g.DamageType] {
		dmgMultiplier = 0
	}
	if attacked.Weaknesses[g.DamageType] {
		dmgMultiplier = 2
	}
	return dmgMultiplier * g.EffectivePower()
}

func (g *Group) ListTargetByPriority(possibleTargets []*Group) []*Group {
	orderedTargets := make([]*Group, 0, len(possibleTargets))
	for _, defender := range possibleTargets {
		if g.DamageDoneTo(defender) > 0 {
			orderedTargets = append(orderedTargets, defender)
		}
	}

	slices.SortFunc(
		orderedTargets,
		func(a, b *Group) int {
			diffDamage := g.DamageDoneTo(b) - g.DamageDoneTo(a)
			if diffDamage != 0 {
				return diffDamage
			}

			diffEffectivePower := b.EffectivePower() - a.EffectivePower()
			if diffEffectivePower != 0 {
				return diffEffectivePower
			}

			return b.Initiative - a.Initiative
		},
	)
	return orderedTargets
}

func (g *Group) Defends(attacker *Group) {
	if attacker.UnitsCount <= 0 {
		return
	}
	killedUnits := min(attacker.DamageDoneTo(g)/g.HealthPerUnit, g.UnitsCount)
	g.UnitsCount -= killedUnits
}
