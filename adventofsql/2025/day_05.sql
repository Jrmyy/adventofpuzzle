with
  dangerous_days as (
    select
      reading_date
    from temperature_readings
    group by reading_date
    having
      avg(temp_celsius) > 0
  )

select
  count(*)
from dangerous_days
