with
  main_categories as (
    select
      id
      , error_code
      , substr(error_code, 0, instr(error_code, '_')) as main_category
    from machine_errors
  )

  , main_cateogries_with_remaining as (
    select
      id
      , error_code
      , main_category
      , substr(error_code, length(main_category) + 2) as remaining
    from main_categories
  )

select
  main_category || '_' || substr(remaining, 0, instr(remaining, '_')) as error_category
  , count(id)
from main_cateogries_with_remaining
group by 1
order by 2 desc
limit 1
