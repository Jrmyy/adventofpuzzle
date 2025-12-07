package internal

type Fight struct {
	Attacker *Group
	Defender *Group
}

func (f Fight) Execute() {
	f.Defender.Defends(f.Attacker)
}
