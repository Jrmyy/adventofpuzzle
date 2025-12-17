with
  last_sleigh_location as (
    select
      coordinate
    from sleigh_locations
    order by timestamp desc
    limit 1
  )

select
  place_name
from areas
inner join last_sleigh_location
  on true
where
  st_within(last_sleigh_location.coordinate::geometry, areas.polygon::geometry)
