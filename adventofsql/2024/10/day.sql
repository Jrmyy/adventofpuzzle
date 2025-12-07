with
    stats as (
        select
            drink_name
            , date
            , sum(quantity) as consumed
        from
            drinks
        group by 1, 2
    )

    , matching_conditions as (
        select
            date
        from
            stats
        where
            (
                drink_name = 'Hot Cocoa'
                    and consumed = 38
                )
            or (
                drink_name = 'Peppermint Schnapps'
                    and consumed = 298
                )
            or (
                drink_name = 'Eggnog'
                    and consumed = 198
                )
    )

    , count_matching_conditions as (
        select
            date
            , count(*) as matches
        from
            matching_conditions
        group by date
    )

select
    date
from
    count_matching_conditions
where
    matches = 3
