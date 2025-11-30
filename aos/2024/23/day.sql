with
    boundaries as (
        select
            min(id) as min_seq
            , max(id) as max_seq
        from
            sequence_table
    )

    , all_values as (
        select
            t.n
        from
            boundaries
            inner join generate_series(boundaries.min_seq, boundaries.max_seq) as t(n) on true
    )

    , missing_values as (
        select
            n
        from
            all_values

        except

        select
            id as n
        from
            sequence_table
    )
    , with_previous_values as (
        select
            n
            , n - lag(n, 1, n) over (order by n) as diff
        from
            missing_values
    )
    , grouped_values as (
        select
            n
            , sum(case when diff = 1 then 0 else 1 end) over (order by n) as group_id
        from
            with_previous_values
    )
select
    string_agg(n::varchar, ',') as missing_numbers
from
    grouped_values
group by group_id
order by group_id
