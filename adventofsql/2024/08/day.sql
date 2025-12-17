with
  recursive
  staff_tree(managee_id, manager_id, level) as (
    select
      staff.staff_id as managee_id
      , staff.manager_id as manager_id
      , 1 as level
    from staff

    union all

    select
      staff.staff_id as managee_id
      , staff.manager_id as manager_id
      , staff_tree.level + 1
    from staff
    join staff_tree
      on staff.manager_id = staff_tree.managee_id
  )

select
  max(level)
from staff_tree
