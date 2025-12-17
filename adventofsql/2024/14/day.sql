with
  receipts as (
    select
      jsonb_array_elements(santarecords.cleaning_receipts) as receipt
    from santarecords
  )

  , green_suits_cleanings as (
    select
      (receipt ->> 'drop_off')::date as drop_off_date
    from receipts
    where
      receipt ->> 'color' = 'green'
      and receipt ->> 'garment' = 'suit'
  )

select
  max(drop_off_date)
from green_suits_cleanings
