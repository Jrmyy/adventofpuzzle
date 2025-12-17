select
  reindeer.name
from reindeer
left join checkins
  on reindeer.id = checkins.reindeer_id and checkins.checkin_date = '2025-12-01'
where
  checkins.reindeer_id is null
