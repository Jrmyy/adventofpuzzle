select
  group_concat(runes.letter, '') as magic_word
from teleport_sequence
inner join runes
  on runes.id = teleport_sequence.rune_id
order by teleport_sequence.position
