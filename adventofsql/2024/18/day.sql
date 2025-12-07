with
    recursive
    recursive_staff(staff_id, staff_name, manager_id, manager_ids) as (
        select
            staff_id
            , staff_name
            , manager_id
            , array []::integer[] as manager_ids
        from
            staff
        where
            manager_id is null

        union all

        select
            staff.staff_id
            , staff.staff_name
            , staff.manager_id
            , recursive_staff.manager_ids || array [staff.manager_id] as manager_ids
        from
            staff
            join recursive_staff on staff.manager_id = recursive_staff.staff_id
    )

    , hierarchical_staff as (
        select
            staff_id
            , staff_name
            , manager_id
            , manager_ids || array [staff_id] as path
            , cardinality(manager_ids) + 1 as level
        from
            recursive_staff
    )

    , peers as (
        select
            staff_id
            , staff_name
            , level
            , count(*) over (partition by manager_id) as peers_same_manager
            , count(*) over (partition by level) as total_peers_same_level
        from
            hierarchical_staff
    )

select *
from
    peers
order by
    total_peers_same_level desc
    , level desc
    , staff_id
limit 1
