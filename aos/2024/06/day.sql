with
    average as (
        select
            avg(gifts.price) as price
        from
            gifts
    )

select
    children.name
from
    children
    inner join gifts on children.child_id = gifts.child_id
    inner join average on true
where
    gifts.price > average.price
order by gifts.price
limit 1
