with
    counts_per_gift as (
        select
            gift_id
            , count(*) as amount
        from
            gift_requests
        group by gift_id
    )

    , data as (
        select
            gifts.gift_name
            , counts_per_gift.amount
            , round((percent_rank() over (order by counts_per_gift.amount))::numeric, 2) as overall_rank
        from
            counts_per_gift
            inner join gifts
            on gifts.gift_id = counts_per_gift.gift_id

    )

    , max_data as (
        select
            max(overall_rank) as max_overall_rank
        from
            data
    )

select
    data.gift_name
    , data.overall_rank
from
    data
    inner join max_data on true
where
    data.overall_rank != max_data.max_overall_rank
order by data.overall_rank desc, data.gift_name
limit 1
