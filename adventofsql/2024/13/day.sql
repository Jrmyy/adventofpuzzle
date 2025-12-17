with
  emails as (
    select
      unnest(email_addresses) as email
    from contact_list
  )

select
  split_part(email, '@', 2) as domain
  , count(*)
from emails
group by 1
order by 2 desc
