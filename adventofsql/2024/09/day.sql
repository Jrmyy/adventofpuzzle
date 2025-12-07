with
    avg_per_type_and_reindeer as (
        select
            reindeers.reindeer_id
            , reindeers.reindeer_name
            , training_sessions.exercise_name
            , avg(training_sessions.speed_record) as average_speed
        from
            reindeers
            inner join training_sessions on reindeers.reindeer_id = training_sessions.reindeer_id
        where
            reindeers.reindeer_name != 'Rudolph'
        group by
            reindeers.reindeer_id
            , reindeers.reindeer_name
            , training_sessions.exercise_name
    )

    , max_per_reeinder as (
        select
            reindeer_id
            , reindeer_name
            , max(average_speed) as max_speed
        from
            avg_per_type_and_reindeer
        group by
            reindeer_id
            , reindeer_name
    )

select
    reindeer_name as name
    , round(max_speed, 2) as highest_average_score
from
    max_per_reeinder
order by max_speed desc
limit 3
