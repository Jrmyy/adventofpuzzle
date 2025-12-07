with
    max_skilled as (
        select
            elf_id
            , primary_skill
            , years_experience
            , row_number() over (partition by primary_skill order by years_experience desc, elf_id) as __rnk
        from
            workshop_elves
    )
    , min_skilled as (
        select
            elf_id
            , primary_skill
            , years_experience
            , row_number() over (partition by primary_skill order by years_experience, elf_id) as __rnk
        from
            workshop_elves
    )

select
    max_skilled.elf_id as max_years_experience_elf_id
    , min_skilled.elf_id as min_years_experience_elf_id
    , max_skilled.primary_skill as shared_skill
from
    max_skilled
    inner join min_skilled using (primary_skill)
where
    max_skilled.__rnk = 1
    and min_skilled.__rnk = 1
