with
  enhanced_data as (
    select
      *
      , case
          when season = 'Spring' then 1
          when season = 'Summer' then 2
          when season = 'Fall' then 3
          else 4
        end as season_idx
    from treeharvests
  )
  , full_data as (
    select
      *
      , lag(trees_harvested, 1)
        over (partition by field_name order by harvest_year, season_idx) as nm1_trees_harvested
      , lag(trees_harvested, 2)
        over (partition by field_name order by harvest_year, season_idx) as nm2_trees_harvested
    from enhanced_data
  )
  , result as (
    select
      field_name
      , harvest_year
      , season
      , round((trees_harvested + nm1_trees_harvested + nm2_trees_harvested) * 1.0 / 3,
              2) as three_season_moving_avg
    from full_data
  )

select
  max(three_season_moving_avg)
from result
