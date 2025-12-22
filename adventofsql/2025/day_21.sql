with
  all_regions_scores as (
    select
      *
    from air_traffic
    union all
    select
      *
    from aurora_readings
    union all
    select
      *
    from weather_stations
  )

select
  region
from all_regions_scores
group by 1
order by sum(score) desc
limit 1
