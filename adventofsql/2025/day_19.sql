with
  to_unload as (
    select
      area_id
      , sum(quantity) as total_quantity
    from unloaded_items
    group by 1
  )

select
  storage_areas.name
from storage_areas
inner join to_unload
  using (area_id)
where
  to_unload.total_quantity >= storage_areas.min_quantity
order by storage_areas.name
limit 1
