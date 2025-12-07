with
    productions as (
        select
            production_date
            , toys_produced
            , lag(toys_produced, 1) over (order by production_date) as previous_day_production
        from
            toy_production
        order by production_date desc
    )

    , statistics as (
        select
            production_date
            , toys_produced
            , previous_day_production
            , toys_produced - previous_day_production as production_change
            ,
            100.0 * (toys_produced - previous_day_production) / previous_day_production as production_change_percentage
        from
            productions
    )

select
    production_date
from
    statistics
where
    production_change_percentage is not null
order by production_change_percentage desc
limit 1
