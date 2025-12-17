with
  unioned as (
    select
      *
    from letters_a

    union all
    select
      *
    from letters_b
  )

  , parsed as (
    select
      chr(value) as letter
    from unioned
    order by id
  )

select
  string_agg(letter, '')
from parsed
where
  letter ~ '[a-zA-Z!"''(),-.;:? ]'
