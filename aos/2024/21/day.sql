with
    sales_per_quarter as (
        select
            extract(year from sale_date) as year
            , extract(quarter from sale_date) as quarter
            , sum(amount) as total_sales
        from
            sales
        group by 1, 2
    )
    , sales_with_previous_quarter as (
        select
            year
            , quarter
            , total_sales
            , lag(total_sales) over (order by year, quarter) as previous_total_sales
        from
            sales_per_quarter
        order by
            year
            , quarter
    )
    , sales_with_growth_rate as (
        select
            year
            , quarter
            , total_sales
            , (total_sales - previous_total_sales) * 1.0 / previous_total_sales as growth_rate
        from
            sales_with_previous_quarter
        where
            previous_total_sales is not null
    )
select
    year::varchar || ',' || quarter::varchar as quarter
from
    sales_with_growth_rate
order by growth_rate desc
limit 1
