select
  facilities.name
  , (facilities.x - north_pole.x) * (facilities.x - north_pole.x) +
    (facilities.y - north_pole.y) * (facilities.y - north_pole.y) as distance
from facilities
cross join north_pole
order by 2
limit 1
