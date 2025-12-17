with
  needed_presents as (
    select
      present
      , count(child_id) as needed
    from present_assignments
    group by 1
  )

select
  needed_presents.present
  , needed_presents.needed - coalesce(inventory.quantity, 0) as missing
from needed_presents
left join inventory
  using (present)
order by 2 desc
limit 1
