select
  r1.name
from reindeer r1
inner join reindeer r2
  using (flight_style)
where
  r2.name = 'Comet'
  and r1.role = 'reserve'
order by r1.stamina desc
limit 1
