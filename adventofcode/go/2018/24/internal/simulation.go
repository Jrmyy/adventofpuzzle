package internal

import "slices"

type Simulation struct {
	ImmuneSystem Army
	Infection    Army
}

func (s *Simulation) Duplicate(immuneSystemBoost int) *Simulation {
	immuneSystem := make([]*Group, len(s.ImmuneSystem))
	for idx, g := range s.ImmuneSystem {
		ng := g.Copy()
		ng.DamagePerUnit += immuneSystemBoost
		immuneSystem[idx] = ng
	}

	infection := make([]*Group, len(s.Infection))
	for idx, g := range s.Infection {
		infection[idx] = g.Copy()
	}

	return &Simulation{Infection: infection, ImmuneSystem: immuneSystem}
}

func (s *Simulation) Run() (string, int) {
	for {
		s.reorderArmies()
		if s.isOver() {
			break
		}

		if s.isDraw() {
			return "", 0
		}

		fights := s.findFights()
		for _, fight := range fights {
			fight.Execute()
		}
	}

	return s.calculateWinner()
}

func (s *Simulation) reorderArmies() {
	s.ImmuneSystem = s.reOrderArmy(s.ImmuneSystem)
	s.Infection = s.reOrderArmy(s.Infection)
}

func (s *Simulation) isDraw() bool {
	return s.checkOpponentImmunity(s.Infection, s.ImmuneSystem) && s.checkOpponentImmunity(s.ImmuneSystem, s.Infection)
}

func (s *Simulation) checkOpponentImmunity(attackingArmy, defendingArmy Army) bool {
	for _, attacker := range attackingArmy {
		for _, defender := range defendingArmy {
			if _, ok := defender.Immunities[attacker.DamageType]; !ok {
				return false
			}
		}
	}

	return true
}

func (s *Simulation) isOver() bool {
	return len(s.Infection) == 0 || len(s.ImmuneSystem) == 0
}

func (s *Simulation) calculateWinner() (string, int) {
	winningArmy := append(s.Infection, s.ImmuneSystem...)
	totalUnitsCount := 0
	for _, group := range winningArmy {
		totalUnitsCount += group.UnitsCount
	}
	return winningArmy[0].GroupType, totalUnitsCount
}

func (s *Simulation) findFights() []Fight {
	infectionAttacks := s.assignTargets(s.Infection, s.ImmuneSystem)
	immuneSystemAttacks := s.assignTargets(s.ImmuneSystem, s.Infection)

	sortedFights := make([]Fight, 0, len(infectionAttacks)+len(immuneSystemAttacks))

	for defender, attacker := range infectionAttacks {
		sortedFights = append(sortedFights, Fight{Attacker: attacker, Defender: defender})
	}
	for defender, attacker := range immuneSystemAttacks {
		sortedFights = append(sortedFights, Fight{Attacker: attacker, Defender: defender})
	}

	slices.SortFunc(sortedFights, func(a, b Fight) int {
		return b.Attacker.Initiative - a.Attacker.Initiative
	})
	return sortedFights
}

func (s *Simulation) assignTargets(attackingArmy, defendingArmy Army) map[*Group]*Group {
	fights := map[*Group]*Group{}

	for _, attacker := range attackingArmy {
		targets := attacker.ListTargetByPriority(defendingArmy)
		for _, targeted := range targets {
			if _, alreadyTargeted := fights[targeted]; !alreadyTargeted {
				fights[targeted] = attacker
				break
			}
		}
	}

	return fights
}

func (s *Simulation) reOrderArmy(army Army) Army {
	army = slices.DeleteFunc(army, func(g *Group) bool {
		return g.UnitsCount <= 0
	})
	slices.SortFunc(
		army,
		func(a, b *Group) int {
			diff := b.EffectivePower() - a.EffectivePower()
			if diff != 0 {
				return diff
			}

			return b.Initiative - a.Initiative
		},
	)
	return army
}
