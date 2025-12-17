with
  distinct_mischiefs as (
    select
      child_id
      , count(distinct category) as different_mischiefs
    from behaviour_events
    group by 1
  )

select
  count(child_id)
from distinct_mischiefs
where
  different_mischiefs > 1
