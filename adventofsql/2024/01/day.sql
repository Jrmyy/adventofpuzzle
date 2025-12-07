with
    wishes_data as (
        select
            child_id
            , wishes ->> 'first_choice' as primary_wish
            , wishes ->> 'second_choice' as backup_wish
            , wishes -> 'colors' ->> 0 as favorite_color
            , json_array_length(wishes -> 'colors') as color_count
        from
            wish_lists
    )
    , toy_data AS (
        select
            toy_name
            , case
                  when difficulty_to_make = 1 then 'Simple Gift'
                  when difficulty_to_make = 2 then 'Moderate Gift'
                  else 'Complex Gift'
                end as gift_complexity
            , case
                  when category = 'outdoor' then 'Outside Workshop'
                  when category = 'educational' then 'Learning Workshop'
                  else 'General Workshop'
                end as workshop_assignment
        from
            toy_catalogue
    )

select
    children.name
    , wishes_data.primary_wish
    , wishes_data.backup_wish
    , wishes_data.favorite_color
    , wishes_data.color_count
    , toy_data.gift_complexity
    , toy_data.workshop_assignment
from
    children
    inner join wishes_data
    on wishes_data.child_id = children.child_id
    inner join toy_data
    on wishes_data.primary_wish = toy_data.toy_name
order by children.name
limit 5
