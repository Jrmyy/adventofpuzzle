with
    skills as (
        select
            id
            , unnest(string_to_array(skills, ',')) as skill
        from
            elves
    )

select
    count(distinct id) as numofelveswithsql
from
    skills
where
    lower(skill) = 'sql'
