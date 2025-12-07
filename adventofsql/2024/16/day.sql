with
    timestamps as (
        select
            timestamp as start_time
            , lead(timestamp, 1) over (order by timestamp) as end_time
            , coordinate
        from
            sleigh_locations
    )

    , location_most_seen as (
        select
            coordinate
        from
            timestamps
        where
            end_time is not null
        order by (end_time - start_time) desc
        limit 1
    )

select
    place_name
from
    areas
    inner join location_most_seen on true
where
    ST_Within(location_most_seen.coordinate::geometry, areas.polygon::geometry)
