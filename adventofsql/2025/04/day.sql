select
  group_concat(substr(clearing_messages.word, 1, 1), '')
from clearing_messages
inner join reindeer
  on reindeer.id = clearing_messages.reindeer_id
where
  reindeer.name = 'Blitzen'
order by clearing_messages.word_position
