with
  children_per_region as (
    select
      families.region
      , count(children.child_id) as children_count
    from families
    inner join children
      using (family_id)
    group by families.region
  )

select
  region
from children_per_region
order by children_count desc
limit 1
