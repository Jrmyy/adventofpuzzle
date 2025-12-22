select
  child_name
from delivery_schedule
where
  not exists (
    select
      sleigh_manifest.child_id
    from sleigh_manifest
    where
      sleigh_manifest.child_id = delivery_schedule.child_id
  )
