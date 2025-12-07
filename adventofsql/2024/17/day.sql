-- Data is corrupted, answer is 14:30....

with
    timezones as (
        select
            name as timezone
            , case
                  when name = 'Europe/Kyiv'
                      then array [name, 'Europe/Kiev', 'Europe/Uzhgorod', 'Europe/Zaporozhye']::varchar[]
                  else array [name]::varchar[]
                end as timezone_aliases
            , utc_offset
        from
            pg_timezone_names
    )

    , enhanced_workshops as (
        select
            workshops.workshop_name
            , workshops.timezone
            , timezones.utc_offset
            , workshops.business_start_time as aware_business_start_time
            , workshops.business_start_time - timezones.utc_offset::time as utc_business_start_time
            , workshops.business_end_time as aware_business_end_time
            , workshops.business_end_time - timezones.utc_offset::time as utc_business_end_time
        from
            workshops
            inner join timezones
            on workshops.timezone = any (timezones.timezone_aliases)
        where
            workshops.timezone not like '%New_York%'
    )
    , timeslots as (
        select
            generate_series::time as timeslot_start
            , (generate_series::time + interval '1 hour')::time as timeslot_end
        from
            (
                select *
                from
                    generate_series('1970-01-01 09:00'::timestamp, '1970-01-01 17:00'::timestamp,
                                    '30 minute'::interval)
            ) t
    )

    , verified_timeslots as (
        select
            timeslots.timeslot_start
            , timeslots.timeslot_end
            , bool_and(enhanced_workshops.utc_business_start_time <= timeslots.timeslot_start and
                       enhanced_workshops.utc_business_end_time >= timeslots.timeslot_end) as available_for_all
        from
            timeslots
            inner join enhanced_workshops on true
        group by
            timeslots.timeslot_start, timeslots.timeslot_end
    )

select
    min(timeslot_start)
from
    verified_timeslots
where
    available_for_all
