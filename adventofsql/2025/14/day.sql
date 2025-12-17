with
  distinct_mischiefs as (
    select
      child_id
      , count(distinct category) as different_mischiefs
    from behaviour_events
    group by 1
  )

  , gifts as (
    select
      children.id
      , case
          when coalesce(distinct_mischiefs.different_mischiefs, 0) = 0 then 'Toy'
          when distinct_mischiefs.different_mischiefs = 1 then 'Book'
          else 'Coal'
        end as gift
    from children
    left join distinct_mischiefs
      on distinct_mischiefs.child_id = children.id
  )

select
  gift
  , count(id)
from gifts
group by 1
order by 2 desc
limit 1
