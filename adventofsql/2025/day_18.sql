with
  ranked_shipments as (
    select
      shipment_id
      , category_id
      , row_number() over (partition by category_id order by priority desc, shipment_id asc) __rank
    from shipments
    inner join categories
      using (category_id)
    where
      shipments.weight <= categories.max_weight
  )

select
  sum(shipment_id)
from ranked_shipments
where
  __rank = 1
