with
  distance_diffs as (
    select
      facilities.name
      , (facilities.x - north_pole.x) as diff_x
      , (facilities.y - north_pole.y) as diff_y
    from facilities
    cross join north_pole
  )

select
  name
from distance_diffs
order by diff_x * diff_x + diff_y * diff_y
limit 1
