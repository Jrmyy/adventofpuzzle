with
  produced_toys as (
    select
      toy_id
      , sum(quantity) as actual_total
    from production_logs
    group by 1
  )

select
  toys.name
from toys
inner join production_summary
  on toys.id = production_summary.toy_id
inner join produced_toys
  using (toy_id)
where
  produced_toys.actual_total != production_summary.expected_total
