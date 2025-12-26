select
  children.region
  , sum(gifts.value) as total_gift_value
from gifts
inner join deliveries
  using (child_id)
inner join children
  using (child_id)
where
  deliveries.status = 'delivered'
group by 1
order by 2 desc
limit 1
