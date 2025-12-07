with
    interesting_url_query_parameters as (
        select
            url
            , split_part(url, '?', 2) as query_parameters
        from
            web_requests
        where
            url like '%utm_source=advent-of-sql%'
            and regexp_count(url, 'utm_source=') = 1
    )
select
    url
from
    interesting_url_query_parameters
order by
    cardinality(string_to_array(query_parameters, '&')) desc
    , url
limit 1
