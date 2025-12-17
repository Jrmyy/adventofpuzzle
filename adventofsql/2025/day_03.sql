select
  locations_visited.location
  , count(locations_visited.visit_date)
from locations_visited
inner join reindeer
  on locations_visited.reindeer_id = reindeer.id
where
  reindeer.name = 'Blitzen'
group by 1
order by 2 desc
limit 1
